package game.client.gui;

import org.newdawn.slick.Graphics;

public class GuiAnimationLabel extends GuiLabel {
	// 最大アニメーション時間
	private float maxAnimationTime;
	// アニメーション
	private float animationTimer;
	// 表示タイマー
	private float drawTimer;
	// 表示タイム(リセット用)
	private float maxDrawTime;
	
	private float shakeWidth = 1.2F;
	// 現在使っているか
	// reset()をしないと、永遠に使われ続ける。
	private boolean isUsing;
	
	private LabelAnimationType animationType;
	
	private float seconds = 1.0F;
	
	private float resetPosX;
	private float resetPosY;
	
	public GuiAnimationLabel(float x, float y, float maxDrawTime) {
		super(x, y);
		this.resetPosX = x;
		this.resetPosY = y;
		this.maxDrawTime = maxDrawTime;
	}
	
	public void setAnimationTime(float animationTime) {
		this.maxAnimationTime = animationTime;
	}
	
	public void setShakeWidth(float shakeWidth) {
		this.shakeWidth = shakeWidth;
	}
	
	public void setAnimationType(LabelAnimationType animationType) {
		this.animationType = animationType;
	}
	
	/**
	 * アニメーション速度の設定<p>
	 * 秒単位で入力
	 * @param seconds 秒単位
	 */
	public void setSpeedSec(float seconds) {
		this.seconds = seconds * 1000.0F;
	}
	
	public void setPosition(float x, float y) {
		if (!this.isUsing) {
			this.posX = this.textPosX = this.resetPosX = x;
			this.posY = this.textPosY = this.resetPosY = y;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (this.drawTimer > 0.0F) {
			this.drawTimer -= 1;
			if (this.animationTimer > 0.0F) {
				this.animationTimer -= (1.0F / this.seconds);
			}
			switch (this.animationType) {
			case EXPANSION:
				break;
			case SHAKE:
				this.textPosY = this.posY + (float) Math.cos(this.animationTimer / 10.0F) * this.animationTimer * this.shakeWidth;
				break;
			case VIBRATION:
				break;
			default:
				break;
			}
			//g.drawString("timer:" + this.drawTimer + ", aniTimer:" + this.animationTimer + ",isUse:" + this.isUsing, 150, 50);
			super.draw(g);
		}
	}
	
	public void start() {
		if (!this.isUsing) {
			this.drawTimer = this.maxDrawTime;
			this.animationTimer = this.maxAnimationTime;
			this.isUsing = true;
		}
	}
	
	public void reset() {
		this.animationTimer = this.maxAnimationTime;
		this.drawTimer = this.maxDrawTime;
		this.isUsing = false;
		this.posX = this.textPosX = this.resetPosX;
		this.posY = this.textPosY = this.resetPosY;
	}
	
	public boolean isVisible() {
		return this.drawTimer > 0.0F;
	}
	
	public boolean isUsing() {
		return this.isUsing;
	}
}
