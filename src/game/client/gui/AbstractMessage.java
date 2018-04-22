package game.client.gui;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.client.image.ImageUtils;
import game.common.data.BasicData;

/**
 * 流れメッセージを作る基礎クラス
 * 
 * @author Murachiki27
 * 
 */
public abstract class AbstractMessage extends TMGuiScreen {
	// 字体の幅と高さ
	private static final int FONT_WIDTH = 16;
	private static final int FONT_HEIGHT = 20;
	// 会話分の時のスペース
	private static final float USERNAME_SPACE = 96.0F;
	
	// 行間の幅
	private int lineHeight;
	// 1行に入る文字数
	private final int maxCharLine;
	// 1ページに入る文字数
	private final int maxCharPage;
	
	// メッセージ (最大:2560文字)
	private char[] text = new char[2560];
	
	private Image cursorImage;
	// 会話主
	private String talkOwner;
	
	// 最大ページ
	private int maxPage;
	// 現在のページ
	private int curPage;
	// 現在のカーソル位置
	private int curPos;
	// 次のページへ進めるか
	private boolean nextPage;
	// テキストを流すタイマータスク
	private Timer timer;
	private TimerTask task;
	
	public AbstractMessage(float x, float y, int maxLines, int maxCharLine) {
		this.posX = x;
		this.posY = y;
		this.lineHeight = 8;
		this.maxCharLine = maxCharLine;
		this.maxCharPage = maxLines * maxCharLine;
		this.cursorImage = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32, 0, 16, 16);
		this.timer = new Timer();
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		float space = 0;
		if (this.talkOwner != null && !this.talkOwner.equals("")) {
			for (int i = 0; i < this.talkOwner.length(); i++) {
				char c = this.talkOwner.charAt(i);
				float dx = this.posX + (FONT_WIDTH / 2 - this.getFontWidth(c) / 2) + FONT_WIDTH * (i % 10);
				float dy = this.posY + (this.lineHeight + FONT_HEIGHT * (i / this.maxCharLine));
				g.drawString(String.valueOf(c), dx, dy);
			}
			space = USERNAME_SPACE;
		}
		for (int i = 0; i < this.curPos; i++) {
			char c = this.text[this.curPage * this.maxCharPage + i];
			float dx = this.posX + space + (FONT_WIDTH / 2 - this.getFontWidth(c) / 2) + FONT_WIDTH * (i % this.maxCharLine);
			float dy = this.posY + (this.lineHeight + FONT_HEIGHT * (i / this.maxCharLine));
			g.drawString(String.valueOf(c), dx, dy);
		}
		if (this.nextPage) {
			float dx = this.posX + space + (this.maxCharLine / 2) * FONT_WIDTH - 8;
			float dy = this.posY + (this.lineHeight + FONT_HEIGHT) * 3;
			g.drawImage(this.cursorImage, dx, dy);
		}
	}
	
	public void setMessage(String talkOwner, String message) {
		this.talkOwner = talkOwner;
		this.curPos = 0;
		this.curPage = 0;
		this.nextPage = false;
		for (int i = 0; i < this.text.length; i++) {
			this.text[i] = '　';
		}
		int p = 0;
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			if (c == '\\') {
				i++;
				if (message.charAt(i) == 'n') {
					p += this.maxCharLine;
					p = (p / this.maxCharLine) * this.maxCharLine;
				} else if (message.charAt(i) == 'f') {
					p += this.maxCharPage;
					p = (p / this.maxCharPage) * this.maxCharPage;
				} else if (message.charAt(i) == 'p') {
					String name = BasicData.getStatsData().getUserName();
					for (int j = 0; j < name.length(); j++) {
						char nc = name.charAt(j);
						this.text[p++] = nc;
					}
				}
			} else {
				this.text[p++] = c;
			}
		}
		this.maxPage = p / this.maxCharPage;
		this.task = new DrawingMessageTask();
		this.timer.schedule(task, 0L, 20L);
	}
	
	public boolean nextMessage() {
		if (this.curPage == this.maxPage && this.nextPage) {
			this.task.cancel();
			this.task = null;
			return true;
		}
		if (this.nextPage && this.curPage < this.maxPage) {
			this.curPage++;
			this.curPos = 0;
			this.nextPage = false;
		}
		
		return false;
	}
	
	
	private int getFontWidth(char c) {
		return BasicData.defaultUfont.getWidth(String.valueOf(c));
	}
	
	public boolean isAllVisble() {
		return this.curPage == this.maxPage && this.nextPage;
	}
	
	
	private class DrawingMessageTask extends TimerTask {
		@Override
		public void run() {
			if (!nextPage) {
				char c = text[curPage * maxCharPage + curPos];
				if (c == '　') {
					curPos += maxCharLine;
					curPos = (curPos / maxCharLine) * maxCharLine;
				} else {
					curPos++;
				}
				
				if (curPos >= maxCharPage) {
					nextPage = true;
				}
			}
		}
		
	}
}
