package tankwar_6;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Wall0 {
	int x,y,w,h;

	public Wall0(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void draw(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.GRAY);
		g.fillRect(x, y, w, h);
		g.setColor(c);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, w, h);
		
	}
}
