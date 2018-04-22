package game.client.gui;

import org.newdawn.slick.Graphics;

public class GuiMenuBox extends AbstractMenuBox {
	private GuiWindow window;
	private boolean windowVisible;
	
	private float menuStartX;
	private float menuStartY;
	
	public GuiMenuBox(float x, float y , float menuStartX, float menuStartY, int width, int height, boolean windowVisible) {
		super(x + menuStartX, y + menuStartY);
		this.menuStartX = menuStartX;
		this.menuStartY = menuStartY;
		this.windowVisible = windowVisible;
		if (windowVisible) {
			this.window = new GuiWindow(x, y, width, height);
			this.window.showGui();
		}
	}
	
	public GuiMenuBox(float x, float y, int width, int height, boolean windowVisible) {
		this(x, y, 35.0F, 20.0F, width, height, windowVisible);
	}
	
	public GuiMenuBox(float x, float y, int width, int height) {
		this(x, y, width, height, true);
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		
		if (this.windowVisible) {
			this.window.draw(g);
		}
		super.draw(g);
	}
	
	public void setWindow(float x, float y, int width, int height) {
		this.posX = x + this.menuStartX;
		this.posY = y + this.menuStartY;
		if (this.windowVisible) {
			this.window = new GuiWindow(x, y, width, height);
			this.window.showGui();
		}
	}
}
