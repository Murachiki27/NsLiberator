package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import game.client.image.ImageUtils;
import game.common.data.BasicData;

public class GuiMenuStatus extends AbstarctCharaStatus {
	
	private Image charaImage;
	
	private int imageId;
	
	public GuiMenuStatus(int index) {
		super(BasicData.getPlayerData().getCharaToIndex(index));
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.MENU_STATUS).getSubImage(0, 0, 336, 96);
		this.imageId = BasicData.getPlayerData().getCharaToIndex(index).getImageId();
		this.charaImage = ImageUtils.getMapCharaImage(BasicData.getPlayerData().getCharaToIndex(index).getCharacterType());
		float blank = 10.67F;
		this.posX = 288.0F;
		this.posY =  index * 96.0F + 32.0F + blank * index;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		
		if (this.life <= 0) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.black);
		}
		g.drawImage(this.guiImage, this.posX, this.posY);
		
		g.setColor(Color.white);
		float dx = this.posX;
		float dy = this.posY;
		int valMaxlife = String.valueOf(this.maxlife).length();
		int valMaxskill = String.valueOf(this.maxskill).length();
        int cx = (this.imageId % 8) * (32 * 3);
        int cy = (this.imageId / 8) * (32 * 4);
        int dir = 3;
        g.drawImage(this.charaImage, dx + 32, dy + 32, dx + 64, dy + 64, cx, cy + dir * 32, cx + 32, cy + dir * 32 + 32);
		
		g.drawString(this.name, dx + 64, dy + 7);
		g.drawString("Lv. " + this.lv, dx + 210, dy + 7);
		g.drawString("HP : " + String.format("%"+ valMaxlife +"s", this.life) + " / " + this.maxlife, dx + 80, dy + 32);
		g.drawString("MP : " + String.format("%"+ valMaxskill +"s", this.skill) + " / " + this.maxskill, dx + 210, dy + 32);
	}
	
	@Override
	public void update() {
		super.update();
	}
}