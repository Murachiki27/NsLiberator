package game.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.common.input.EnumKeyInput;

public abstract class TMGuiScreen extends TMGui {
	
    protected Image guiImage;
	
	protected int width;
	protected int height;
    
	private boolean visible;
	
	public TMGuiScreen() {
	}
	
	@Override
	public void draw(Graphics g) {
	}
	
	public void keyPressed(int key, char c, EnumKeyInput input) {
		if (!this.visible) return;
	}
	
	public void keyReleased(int key, char c, EnumKeyInput input) {
		
	}
	
	public void showGui() {
		this.visible = true;
	}
	
	public void closeGui() {
		this.visible = false;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public float getCenterX() {
		return (this.posX + this.width) / 2;
	}
	
	public float getCenterY() {
		return (this.posY + this.height) / 2;
	}
}
