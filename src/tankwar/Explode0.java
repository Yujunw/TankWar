package tankwar_6;

import java.awt.Color;
import java.awt.Graphics;

public class Explode0 {
	
	int x,y;
	private boolean live = true;
	int[] diameter = {3,5,8,13,20,30,43,33,25,10,5,2};
	int step = 0;
	TankWar tw;
	
	public Explode0(int x, int y,TankWar tw) {
		this.x = x;
		this.y = y;
		this.tw = tw;
	}
	
	public void draw(Graphics g){
		if (!live) {
			tw.explodes.remove(this);
			return;
		}
		
		if (step == diameter.length) {
			live = false;
			step = 0;
			return;
		}
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillOval(x, y, diameter[step], diameter[step]);
		g.setColor(c);
		
		step++;
	}
}







