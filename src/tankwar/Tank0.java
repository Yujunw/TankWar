package tankwar_6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank0 {
	
	public static final int TANK_WIDTH = 35;
	public static final int TANK_HEIGHT = 35;
	private static final int X_SPEED = 10;
	private static final int Y_SPEED = 10;
	private int x,y;	
	boolean BL = false,BR = false,BU = false,BD = false;
	enum Direction {L,R,U,D,LU,LD,RU,RD,STOP};
	public Direction dir = Direction.STOP;
	TankWar tw;
	private Direction ptDir = Direction.D;	
	private boolean good;
	private int life = 100;
	private int oldX,oldY;
	private BloodBar bb = new BloodBar();
	BloodCube0 bc = new BloodCube0();
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}

	//让所有Tank共享随机数，一个随机数就可以让所有Tank运动
	private static Random r = new Random();
	private int step = r.nextInt(10)+3;
	private boolean live = true;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank0(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}

	//当调用TankWar类时，必须在构造方法中加入this
	public Tank0(int x, int y,boolean good, TankWar tw,Direction dir) {
		this(x,y,good);
		this.tw = tw;
		this.dir = dir;	
	}

	public  void draw(Graphics g){
		if (!live) {
			tw.tanks.remove(this);
			return;
		}
		if(good) bb.draw(g);
		Color c = g.getColor();
		if(good) {
			g.setColor(Color.ORANGE);
		}
		else {
			g.setColor(Color.MAGENTA);
		}
		g.fillOval(x, y, TANK_WIDTH, TANK_HEIGHT);
		g.setColor(c);	
		
		switch (ptDir) {
		case L:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x, y+TANK_HEIGHT/2);
			break;
		case R:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x+TANK_WIDTH, y+TANK_HEIGHT/2);
			break;
		case U:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x+TANK_WIDTH/2, y);
			break;
		case D:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x+TANK_WIDTH/2, y+TANK_HEIGHT);
			break;
		case LU:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x, y);
			break;
		case LD:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x, y+TANK_HEIGHT);
			break;
		case RU:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x+TANK_WIDTH, y);
			break;
		case RD:
			g.drawLine(x+TANK_WIDTH/2, y+TANK_HEIGHT/2, x+TANK_WIDTH, y+TANK_HEIGHT);
			break;
		default:
			break;
		}
		move();
		
	}
	
	void move(){
		this.oldX = x;
		this.oldY = y;
		switch (dir) {
		case L:
			x += -X_SPEED;
			break;
		case R:
			x += X_SPEED;
			break;
		case U:
			y += -Y_SPEED;
			break;
		case D:
			y += Y_SPEED;
			break;
		case LU:
			x += -X_SPEED;
			y += -Y_SPEED;
			break;
		case LD:
			x += -X_SPEED;
			y += Y_SPEED;
			break;
		case RU:
			x += X_SPEED;
			y += -Y_SPEED;
			break;
		case RD:
			x += X_SPEED;
			y += Y_SPEED;
			break;
		case STOP:
			break;
		default:
			break;
		}
		
		if (this.dir!=Direction.STOP) {
			this.ptDir = this.dir;
		}
		
		if (x<=0) x = 0;
		if(x>=TankWar.WINDOW_WIDTH-TANK_WIDTH) x = TankWar.WINDOW_WIDTH-TANK_WIDTH;
		if(y<=25) y = 25;
		if(y>TankWar.WINDOW_HEIGHT-TANK_HEIGHT) y = TankWar.WINDOW_HEIGHT-TANK_HEIGHT;
	
		if (!good) {
			//将枚举类Direction中的方向封装成一个数组
			Direction[] dirs = Direction.values();
			if (step == 0) {
				int rn = r.nextInt(dirs.length);	
				dir = dirs[rn];
				step = r.nextInt(10)+3;
			}
			step -- ;		
			
			if(r.nextInt(40)>36) this.fire();
		}
		
	}
	
	public void stay(){
		this.x = oldX;
		this.y = oldY;
	}
	
	public void colliedWithWall(Wall0 w){
		if (this.getRect().intersects(w.getRect())) {
			stay();
		}
	}
	
	public void colliedWithTank(List<Tank0> tanks){
		for (int i = 0; i < tanks.size(); i++) {
			Tank0 t = tanks.get(i);
			//需要判断坦克自己与自己碰撞
			if (this!=t) {
				if (this.getRect().intersects(t.getRect())) {
					stay();
				}
			}		
			
		}
		
	}
	
	//按键只改变方向，并不能运动，这样运动会更均匀，不卡顿
	public void keyPressed(KeyEvent e){
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_F2:
			if (!live) {
				this.live = true;
				this.life = 100;
			}		
			break;
		case KeyEvent.VK_LEFT:
			BL = true;
			break;
		case KeyEvent.VK_RIGHT:
			BR = true;
			break;
		case KeyEvent.VK_UP:
			BU = true;
			break;
		case KeyEvent.VK_DOWN:
			BD = true;
			break;
		
		default:
			break;			
		}		
		locateDirection();
	}
	
	public Missile0 fire() {
		if(!live) return null;
		int x = this.x+TANK_WIDTH/2-Missile0.M_WIDTH/2;
		int y = this.y+TANK_HEIGHT/2-Missile0.M_HEIGHT/2;
		Missile0 m = new Missile0(x,y,ptDir,good,this.tw);
		tw.missiles.add(m);
		return m;
		
	}

	private Missile0 fire(Direction dir) {
		if(!live) return null;
		int x = this.x+TANK_WIDTH/2-Missile0.M_WIDTH/2;
		int y = this.y+TANK_HEIGHT/2-Missile0.M_HEIGHT/2;
		Missile0 m = new Missile0(x,y,dir,good,this.tw);
		tw.missiles.add(m);
		return m;
	}
	public void superFire(){
		if(!live) return;
		Direction[] dir = Direction.values();
		for (int i = 0; i < 8; i++) {
			fire(dir[i]);
		}
		
	}
	
	

	void locateDirection(){
		if (BL&&!BR&&!BU&&!BD) {
			dir = Direction.L;
		}
		else if (!BL&&BR&&!BU&&!BD) {
			dir = Direction.R;
		}
		else if (!BL&&!BR&&BU&&!BD) {
			dir = Direction.U;
		}
		else if (!BL&&!BR&&!BU&&BD) {
			dir = Direction.D;
		}
		else if (BL&&!BR&&BU&&!BD) {
			dir = Direction.LU;
		}	
		else if (BL&&!BR&&!BU&&BD) {
			dir = Direction.LD;
		}
		else if (!BL&&BR&&BU&&!BD) {
			dir = Direction.RU;
		}
		else if (!BL&&BR&&!BU&&BD) {
			dir = Direction.RD;
		}
		else if (!BL&&!BR&&!BU&&!BD) {
			dir = Direction.STOP;
		}		
	}

	public void KeyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
		case KeyEvent.VK_LEFT:
			BL = false;
			break;
		case KeyEvent.VK_RIGHT:
			BR = false;
			break;
		case KeyEvent.VK_UP:
			BU = false;
			break;
		case KeyEvent.VK_DOWN:
			BD = false;
			break;
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_Q:
			superFire();
		default:
			break;			
		}
		locateDirection();
	}

	public Rectangle getRect(){
		
		return new Rectangle(x, y, TANK_WIDTH, TANK_HEIGHT);
	}
	
	public void eat(BloodCube0 bc){
		if (this.getRect().intersects(bc.getRect())&&bc.isLive()) {
			this.life = 100;
			bc.setLive(false);
		}
	}
	
	private class BloodBar{
		
		public void draw(Graphics g){
			
			int WIDTH = TANK_WIDTH*life/100;
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-15,WIDTH , 10);
			g.fillRect(x, y-15, WIDTH, 10);
			g.setColor(c);
		}
	}
}















