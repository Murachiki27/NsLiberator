package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.client.image.ImageUtils;

/**
 * 流れアニメーション無しのメッセージを作る基礎クラス
 * 
 * @author Murachiki27
 *
 */
public abstract class AbstractStrings extends TMGuiScreen  {
	// 字体の幅と高さ
	private static final int FONT_WIDTH = 16;
	//private static final int FONT_HEIGHT = 20;
	// 会話分の時のスペース
	//private static final float USERNAME_SPACE = 200.0F;
	// メッセージテキスト
	private String text;
	
	private Image cursorImage;
	
	private boolean isDrawCursor;
	
	public AbstractStrings(float x, float y, boolean isDrawCursor) {
		this.posX = x;
		this.posY = y;
		this.isDrawCursor = isDrawCursor;
		this.cursorImage = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32, 0, 16, 16);
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(Color.white);
		g.drawString(this.text, this.posX, this.posY);
		if (this.isDrawCursor) {
			g.drawImage(this.cursorImage, this.posX + (this.text.length() * FONT_WIDTH) + 15.0F, this.posY + 16.0F);
		}
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
