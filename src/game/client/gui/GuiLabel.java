package game.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import game.common.data.BasicData;


/**
 * 文字列を描写する定義を作るクラス<p>
 * 注意；ここでいう文字列は <b>英数字のみで日本語は対象外とする。</b>
 * 
 * @author Murachiki27
 *
 */
public class GuiLabel extends TMGui {
	/*
	// 最大表示時間
	private int animation;
	// 描写するダメージ数
	private int damage;
	// アニメーションする時間
	private float duration;
	// アニメーションタイマー
	private float timer;
	// アニメーションタイム(リセット用)
	private float maxTime;
	// 表示中ならtrue、それ以外はfalse
	private boolean isVisible = false;
	// 最大表示時間を超えたらtrue、それ以外はfalse
	private boolean isEnded = false;
	*/
	//private Font font;
	
	protected float textPosX;
	protected float textPosY;
	
	private TrueTypeFont tfont;
	
	private String label;
	
	public GuiLabel(float x, float y) {
		this.posX = this.textPosX = x;
		this.posY = this.textPosY = y;
		this.tfont = new TrueTypeFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 48), false);
		//SpriteSheet sheet = new SpriteSheet("font/sprite.png", 48, 48);
		//this.font = new SpriteSheetFont(sheet, '0');
	}
	
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setFont(tfont);
		if (this.label != null) {
			g.drawString(this.label, this.textPosX, this.textPosY);
		}
		g.setFont(BasicData.defaultUfont);
	}
	
	public float getCenterX() {
		return (this.posX + this.tfont.getWidth(this.label)) / 2;
	}
	
	public float getTextPosX() {
		return this.textPosX;
	}
	
	public float getTextPosY() {
		return this.textPosY;
	}
	
	public float getWidth() {
		return this.tfont.getWidth(this.label);
	}
	
	public float getHeight() {
		return this.tfont.getHeight(this.label);
	}
	
	/*
	public void startDraw() {
		this.isVisible = true;
	}
	
	public boolean isEnded() {
		return this.isEnded;
	}
	
	public void reset() {
		this.animation = 0;
		this.timer = this.maxTime;
		this.isVisible = false;
		this.isEnded = false;
	}
	*/
}
