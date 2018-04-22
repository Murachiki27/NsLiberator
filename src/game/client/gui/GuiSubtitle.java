package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import game.TypingMaster;
import game.common.data.BasicData;

public class GuiSubtitle extends TMGuiScreen {
	private String[] text = new String[32];
	private int textNo;
	private int textMaxNo;
	private Rectangle rect;
	private float textAlpha;
	private float fadeAlpha;
	private int fadeTime;
	public float timer;
	private boolean isEnd = false;
	private boolean isPageEnd = false;
	private boolean seenChange = false;
	
	public GuiSubtitle(int fadeTime) {
		this.fadeTime = fadeTime;
		this.rect = new Rectangle(0, 0, TypingMaster.WIDTH, TypingMaster.HEIGHT);
	}
	
	public void update(int delta) {
		if (this.isEnd) {
			if (this.fadeAlpha > 0.0F && this.textAlpha > 0.0F) {
				this.fadeAlpha -= delta * (1.0F / this.fadeTime);
				this.textAlpha -= delta * (1.0F / 500.0F);
			} else {
				this.isEnd = false;
				this.closeGui();
			}
		} else if (this.isVisible()) {
			if (this.fadeAlpha < 1.0F) {
				this.fadeAlpha += delta * (1.0F / this.fadeTime);
			} else {
				if (!this.isPageEnd && !this.seenChange) {
					this.timer += delta * 0.001;
					if (this.timer >= 3) {
						this.isPageEnd = true;
					}
				}
				
				if (this.seenChange) {
					if (this.textAlpha > 0.0F) {
						this.textAlpha -= delta * (1.0F / 500.0F);
					} else {
						this.seenChange = false;
						this.textNo++;
					}
				} else {
					if (this.textAlpha < 1.0F) {
						this.textAlpha += delta * (1.0F / 500.0F);
					}
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(new Color(0, 0, 0, this.fadeAlpha));
		g.fillRect(this.rect.getX(), this.rect.getY(), this.rect.getWidth(), this.rect.getHeight());
		
		g.setColor(new Color(1,1,1,this.textAlpha));
		if (this.text[textNo] != null) {
			g.drawString(this.text[textNo], getWidth(this.text[textNo]), 220);
		}
	}
	
	public void setTitle(String[] text) {
		this.textNo = 0;
		this.textMaxNo = text.length - 1;
		this.timer = 0;
		for (int i = 0; i < this.text.length; i++) {
			this.text[i] = "";
		}
		this.text = text;
	}
	
	public boolean checkNextText() {
		if (this.textNo == this.textMaxNo && this.isPageEnd) {
			this.isEnd = true;
			return true;
		} else if (this.isPageEnd) {
			this.timer = 0;
			this.isPageEnd = false;
			this.seenChange = true;
		}
		return false;
	}
	
	public void start() {
		this.showGui();
	}
	
	@Override
	public boolean isVisible() {
		return super.isVisible();
	}
	
	private float getWidth(String str) {
		return 320 - (BasicData.defaultUfont.getWidth(str) / 2);
	}
}
