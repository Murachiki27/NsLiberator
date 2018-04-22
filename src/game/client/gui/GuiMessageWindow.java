package game.client.gui;

import org.newdawn.slick.Graphics;

import game.client.image.ImageUtils;

public class GuiMessageWindow extends AbstractMessage {
	
	private float imageX;
	private float imageY;
	
	public GuiMessageWindow(float x, float y, float textX, float textY, int u, int v, int width, int height, int maxLine, int maxCharaInLine) {
		super(x + textX, y + textY, maxLine, maxCharaInLine);
		this.imageX = x;
		this.imageY = y;
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.TALK_WINDOW).getSubImage(u, v, width, height);
	}
	
	public GuiMessageWindow(float x, float y, float textX, float textY, int u, int v, int width, int height) {
		this(x, y, textX, textY, u, v, width, height, 3, 20);
	}
	
	public GuiMessageWindow(float x, float y, int u, int v, int width, int height) {
		this(x, y, 30.0F, 50.0F, u, v, width, height);
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		
		g.drawImage(this.guiImage, this.imageX, this.imageY);
		super.draw(g);
	}
}
