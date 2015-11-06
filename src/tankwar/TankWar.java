package tankwar_6;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankWar extends Frame{

	public static final long serialVersionUID = 1L;
	public static final int WINDOW_WIDTH = 800;
	public static final int WINDOW_HEIGHT = 600;
	private Tank0 tg = new Tank0(50, 50,true,this,Tank0.Direction.STOP);
	Image offScreenImage = null;
	List<Missile0> missiles = new ArrayList<Missile0>();
	List<Explode0> explodes = new ArrayList<Explode0>();
	List<Tank0> tanks = new ArrayList<Tank0>();
	Wall0 w1 = new Wall0(100, 400,500,40);
	Wall0 w2 = new Wall0(200,180,40,300);
	BloodCube0 bc = new BloodCube0();
	//窗口重画时自动调用paint(Graphics g)方法t
	public void paint(Graphics g) {	
		
		if (tanks.size()<=0) {
			for (int i = 0; i < 5; i++) {
				tanks.add(new Tank0(50+70*(i+1), 50, false, this,Tank0.Direction.D));
			}
		}
		g.drawString("Missile : "+missiles.size(), 10, 50);
		g.drawString("Explode : "+explodes.size(), 10, 70);
		g.drawString("Tank : "+tanks.size(), 10, 90);
		g.drawString("Life : "+tg.getLife(), 10, 110);
		for (int i = 0; i < missiles.size(); i++) {
			Missile0 m = missiles.get(i);
			m.draw(g);
			m.hitTanks(tanks);
			m.hitTank(tg);
			m.hitWalls(w1);
			m.hitWalls(w2);
		}
		
		for (int i = 0; i < explodes.size(); i++) {
			Explode0 e = explodes.get(i);
			e.draw(g);
		}
		
		for (int i = 0; i < tanks.size(); i++) {
			 Tank0 tb = tanks.get(i);
			 tb.draw(g);
			 tb.colliedWithWall(w1);
			 tb.colliedWithWall(w2);
			 tb.colliedWithTank(tanks);
		}
		
		tg.draw(g);	
		tg.eat(bc);
		w1.draw(g);
		w2.draw(g);
		bc.draw(g);
		
	}
	
	//刷新频率太快，paint方法来不及完成，所以使用双缓冲DoubleBuffer
	//offScreenImage是屏幕外的图像，gOffScreen是屏幕内的图像
	public void update(Graphics g) {
		if (offScreenImage == null) {
			//createImage,创建一幅用于双缓冲的、可在屏幕外绘制的图像。返回一幅屏幕外可绘制的图像，可用于双缓冲
			offScreenImage = this.createImage(800, 600);
		}
		//getGraphics,创建供绘制闭屏图像（off-screen image）使用的图形上下文。此方法仅供闭屏图像调用。
		//返回绘制闭屏图像的图形上下文。
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.CYAN);
		gOffScreen.fillRect(0, 0, 800, 600);
		gOffScreen.setColor(c);
		paint(gOffScreen);
	/*
 	drawImage
	public abstract boolean drawImage(Image img, int x, int y, ImageObserver observer)
	绘制指定图像中当前可用的图像。图像的左上角位于该图形上下文坐标空间的 (x, y)。图像中的透明像素不影响该处已存在的像素。 
	此方法在任何情况下都立刻返回，甚至在图像尚未完整加载，并且还没有针对当前输出设备完成抖动和转换的情况下也是如此。 
	如果图像已经完整加载，并且其像素不再发生更改，则 drawImage 返回 true。否则，drawImage 返回 false，
	并且随着更多的图像可用或者到了绘制动画另一帧的时候，加载图像的进程将通知指定的图像观察者。 
    */
		g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	
	public static void main(String[] args) {
		TankWar tw = new TankWar();
		tw.launchFrame();
		
	}

	public void launchFrame() {
		
		for (int i = 0; i < 10; i++) {
			tanks.add(new Tank0(50+60*(i+1), 50, false, this,Tank0.Direction.D));
		}
		
		//这些方法都需要调用
		setTitle("Tank War");
		setVisible(true);
		setBounds(200, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setBackground(Color.CYAN);
		//添加指定的窗口监听器，用窗口适配器接收，覆盖其中的windowClosing方法
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		new Thread(new PaintThread()).start();
//		PaintThread pt = new PaintThread();
//		Thread t = new Thread(pt);
//		t.start();
		
		addKeyListener(new KeyMonitor());
	}
	
	private class PaintThread implements Runnable{
		public void run() {
			while (true) {
		//如果此组件不是轻量级组件，则为了响应对 repaint的调用，AWT调用 update方法。可以假定未清除背景。 
				repaint();//repaint()自动调用paint(Graphics g)方法		
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private class KeyMonitor extends KeyAdapter{
		public void keyReleased(KeyEvent e) {
			tg.KeyReleased(e);
		}
		public void keyPressed(KeyEvent e) {
			tg.keyPressed(e);
		}

	}
		
}


