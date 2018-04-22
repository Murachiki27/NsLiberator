package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import game.client.chara.BattleChara;
import game.client.image.ImageUtils;

public class GuiBattleStatus extends AbstarctCharaStatus {
	
	private float plusY;
	
	public GuiBattleStatus(BattleChara chara, int index) {
		super(chara);
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.BATTLE_STATUS).getSubImage(0, 0, 160, 160);
		this.posX = index * 160;
		this.posY = 342;
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		if (this.life <= 0) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.black);
		}
		g.drawImage(this.guiImage, this.posX, this.posY + this.plusY);
		// 文字描写
		g.setColor(Color.white);
		float dx = this.posX + 80.0F;
		float dy = this.posY + this.plusY;
		
		g.drawString(this.name, dx - (this.name.length() * 16 / 2), dy + 10);
		g.drawString("Lv. " + this.lv, dx - 60, dy + 10 * 4);
		g.drawString("H P : " + this.life + " / " + this.maxlife, dx - 60, dy + 10 * 6);
		g.drawString("S P : " + this.skill + " / " + this.maxskill, dx - 60, dy + 10 * 8);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	public void setPopup(float height) {
		this.plusY = height;
	}
	
	/*
	public String getName() {
		return this.name;
	}
	
	public int getNamelength() {
		return this.name.length();
	}
	
	public void setLife(int life) {
		this.life += life;
	}
	public int getLife() {
		return life;
	}
	
	public float getY() {
		return this.posY;
	}
	*/
}
