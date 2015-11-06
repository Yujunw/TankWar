package tankwar_6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class BloodCube0 {
	int x,y,w,h;
	TankWar tw;
	int[][] pos = {{345,234},{350,250},{360,260},{355,265},{358,366},{365,366},{358,357}};
	int step = 0; 
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public BloodCube0(){
		x = pos[0][0];
		y = pos[0][1];
		w = h = 15;
	}
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.red);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		move();
	}

	private void move() {
		step++;
		if (step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
		
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
		
	}
	
}













