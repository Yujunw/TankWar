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
	//�����ػ�ʱ�Զ�����paint(Graphics g)����t
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
	
	//ˢ��Ƶ��̫�죬paint������������ɣ�����ʹ��˫����DoubleBuffer
	//offScreenImage����Ļ���ͼ��gOffScreen����Ļ�ڵ�ͼ��
	public void update(Graphics g) {
		if (offScreenImage == null) {
			//createImage,����һ������˫����ġ�������Ļ����Ƶ�ͼ�񡣷���һ����Ļ��ɻ��Ƶ�ͼ�񣬿�����˫����
			offScreenImage = this.createImage(800, 600);
		}
		//getGraphics,���������Ʊ���ͼ��off-screen image��ʹ�õ�ͼ�������ġ��˷�����������ͼ����á�
		//���ػ��Ʊ���ͼ���ͼ�������ġ�
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.CYAN);
		gOffScreen.fillRect(0, 0, 800, 600);
		gOffScreen.setColor(c);
		paint(gOffScreen);
	/*
 	drawImage
	public abstract boolean drawImage(Image img, int x, int y, ImageObserver observer)
	����ָ��ͼ���е�ǰ���õ�ͼ��ͼ������Ͻ�λ�ڸ�ͼ������������ռ�� (x, y)��ͼ���е�͸�����ز�Ӱ��ô��Ѵ��ڵ����ء� 
	�˷������κ�����¶����̷��أ�������ͼ����δ�������أ����һ�û����Ե�ǰ����豸��ɶ�����ת���������Ҳ����ˡ� 
	���ͼ���Ѿ��������أ����������ز��ٷ������ģ��� drawImage ���� true������drawImage ���� false��
	�������Ÿ����ͼ����û��ߵ��˻��ƶ�����һ֡��ʱ�򣬼���ͼ��Ľ��̽�ָ֪ͨ����ͼ��۲��ߡ� 
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
		
		//��Щ��������Ҫ����
		setTitle("Tank War");
		setVisible(true);
		setBounds(200, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setBackground(Color.CYAN);
		//���ָ���Ĵ��ڼ��������ô������������գ��������е�windowClosing����
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
		//�������������������������Ϊ����Ӧ�� repaint�ĵ��ã�AWT���� update���������Լٶ�δ��������� 
				repaint();//repaint()�Զ�����paint(Graphics g)����		
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


