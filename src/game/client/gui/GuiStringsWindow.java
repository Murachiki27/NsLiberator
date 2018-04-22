package game.client.gui;

import org.newdawn.slick.Graphics;

import game.client.image.ImageUtils;

public class GuiStringsWindow extends AbstractStrings {
	
	public GuiStringsWindow(float x, float y, int u, int v, int width, int height, boolean isDrawCursor) {
		super(x + 50.0F, y + 20.0F, isDrawCursor);
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.TALK_WINDOW).getSubImage(u, v, width, height);
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		g.drawImage(this.guiImage, this.posX, this.posY);
		super.draw(g);
	}
}
