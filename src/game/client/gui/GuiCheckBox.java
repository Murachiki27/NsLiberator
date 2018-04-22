package game.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import game.client.image.ImageUtils;

public class GuiCheckBox extends TMGui {
	private Image checkIcon;
	private float scale;
	private boolean checkState;
	
	public GuiCheckBox(float x, float y, float scale, boolean initState) {
		this.checkIcon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(0, 0, 32, 16).getScaledCopy(scale);
		this.posX = x;
		this.posY = y;
		this.scale = scale;
		this.checkState = initState;
	}
	
	public GuiCheckBox(float x, float y, boolean initState) {
		this(x, y, 1.0F, initState);
	}
	
	@Override
	public void draw(Graphics g) {
		float u = this.getStateToNumber(this.checkState) * (16 * this.scale);
		g.drawImage(this.checkIcon, this.posX, this.posY, this.posX + (16.0F * this.scale), this.posY + (16.0F * this.scale), u, 0, u + (16 * this.scale), 16 * this.scale);
	}
	
	private int getStateToNumber(boolean state) {
		if (state) {
			return 1;
		}
		return 0;
	}
	
	public boolean getState() {
		return this.checkState;
	}
	
	public void doneCheck() {
		if (this.checkState) {
			this.checkState = false;
		} else {
			this.checkState = true;
		}
	}
}
