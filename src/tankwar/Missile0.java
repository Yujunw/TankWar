package tankwar_6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile0 {

	public static final int M_WIDTH = 10;
	public static final int M_HEIGHT = 10;
	private static final int M_SPEED = 20;
	private int x,y;
	private Tank0.Direction dir;
	private boolean mLive = true;
	private boolean good;
	TankWar tw;
	public void setmLive(boolean mLive) {
		this.mLive = mLive;
	}
	public boolean ismLive() {
		return mLive;
	}
	public Missile0(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//当调用TankWar类时，必须在构造方法中加入this
	public Missile0(int x, int y, Tank0.Direction dir,boolean good,TankWar tw) {
		this(x,y);
		this.dir = dir;
		this.good = good;
		this.tw = tw;
	}
	
	public void draw(Graphics g){
		if (!mLive)  {
			tw.missiles.remove(this);
			return;	
		}
		Color c = g.getColor();
		if(good) g.setColor(Color.darkGray);
		else g.setColor(Color.WHITE);
		g.fillOval(x,y , M_WIDTH, M_HEIGHT);
		g.setColor(c);
		move();
	}

	private void move() {
		
		switch (dir) {
		case L:
			x -= M_SPEED;
			break;
		case R:
			x += M_SPEED;
			break;
		case U:
			y -= M_SPEED;
			break;
		case D:
			y += M_SPEED;
			break;
		case LU:
			x += -M_SPEED;
			y += -M_SPEED;
			break;
		case LD:
			x += -M_SPEED;
			y += M_SPEED;
			break;
		case RU:
			x += M_SPEED;
			y += -M_SPEED;
			break;
		case RD:
			x += M_SPEED;
			y += M_SPEED;
			break;
		default:
			break;
		}
		
		if (x>TankWar.WINDOW_WIDTH||y>TankWar.WINDOW_HEIGHT||x<0||y<0) {
			mLive = false;
			tw.missiles.remove(this);
			
		}
	}
	
	public boolean hitTank(Tank0 t){
		if (this.getRect().intersects(t.getRect())&&t.isLive()&&this.good!=t.isGood()) {
			if (t.isGood()) {
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0) t.setLive(false);
			}
			
		else t.setLive(false);
			this.mLive  = false;
			Explode0 e = new Explode0(x, y, tw);
			tw.explodes.add(e);
			return true;
		}
		
		return false;
	}
	
	public boolean hitTanks(List<Tank0> tanks) {
		for (int i = 0; i < tanks.size(); i++) {
			if(hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
	
	public boolean hitWalls(Wall0 w){
		if (this.getRect().intersects(w.getRect())) {
			this.mLive = false;
			return true;
		}
		return false;
		
	}

	public Rectangle getRect(){
		
		return new Rectangle(x, y, M_WIDTH, M_HEIGHT);
		
	}
	
}
















