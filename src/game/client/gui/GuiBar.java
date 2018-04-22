package game.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.list.GuiImageList;

public class GuiBar extends TMGuiScreen {
	private Image bar;
	private Image barFrame;
	
	private float imgposX;
	private float maxPosX;
	
	public GuiBar(float x, float y, float scale, int barTypeNo) {
		this(x, y, scale, barTypeNo, true);
	}
	
	public GuiBar(float x, float y, float scale, int barTypeNo, boolean frame) {
		this.posX = x;
		this.posY = y;
		this.bar = GuiImageList.guiIcons.getSubImage(0, 80+32*barTypeNo, 160, 16).getScaledCopy(scale);
		if (frame) {
			this.barFrame = GuiImageList.guiIcons.getSubImage(0, 64+32*barTypeNo, 160, 16).getScaledCopy(scale);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (this.barFrame != null) {
			g.drawImage(this.barFrame, this.posX, this.posY);
		}
		g.drawImage(this.bar, this.posX, this.posY, this.posX + (this.bar.getWidth() - this.imgposX), this.posY + this.bar.getHeight(), 0, 0, this.bar.getWidth(), this.bar.getHeight());
	}
	
	public void update(float deltaPos) {
		this.imgposX = this.maxPosX * deltaPos;
	}
	
	public void setMaxPosition(float maxPosX) {
		this.maxPosX = this.bar.getWidth() / maxPosX;
	}
}
