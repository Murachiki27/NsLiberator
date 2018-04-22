package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class MultiUserGui {
	private Rectangle gui;
	private Rectangle inGui;
	
	private String name;
	//private float typingSpeed;
	
	public MultiUserGui(int playerNo, String name) {
		gui = new Rectangle(64, 64 + 208 * playerNo, 512, 144);
		inGui = new Rectangle(gui.getX() + 2, gui.getY() + 2, gui.getWidth() - 4, gui.getHeight() - 4);
		this.name = name;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(gui.getX(), gui.getY(), gui.getWidth(), gui.getHeight());
		g.setColor(Color.black);
		g.fillRect(inGui.getX(), inGui.getY(), inGui.getWidth(), inGui.getHeight());
		g.setColor(Color.white);
		if (name != null) {
			g.drawString(name, inGui.getX() + 64, inGui.getY() + 64);
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
}
