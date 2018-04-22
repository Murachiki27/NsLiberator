package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import game.TypingMaster;
import game.common.data.PlayerStatsData;

public class GuiLoadSprite extends TMGuiScreen {
	
	private GuiWindow window;
	
	private PlayerStatsData statsData;
	
	public boolean isExist;
	
	public GuiLoadSprite(int index, PlayerStatsData statsData) {
		this.window = new GuiWindow(64, index * 160, 512, 160);
		this.window.showGui();
		this.statsData = statsData;
		if (this.statsData != null) {
			this.isExist = true;
		}
	}
	
	@Override
	public void draw(Graphics g) {
		if (this.statsData != null) {
			if (!this.statsData.getLastVersion().equals(TypingMaster.VERSION)) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.white);
			}
		} else {
			g.setColor(Color.red);
		}
		this.window.draw(g);
		g.setColor(Color.white);
		if (this.statsData != null) {
			g.drawString("冒険者 :" + this.statsData.getUserName(), this.window.getX() + 32.0F, this.window.getY() + 32.0F);
			g.drawString("バージョン :" + this.statsData.getLastVersion(), this.window.getX() + 50.0F, this.window.getY() + 64.0F);
			g.drawString("プレイ時間 :" + this.statsData.getPlayTimeToString(), this.window.getX() + 50.0F, this.window.getY() + 96.0F);
			g.drawString("マップ :" + this.statsData.lastMapName, this.window.getX() + 320.0F, this.window.getY() + 64.0F);
			g.drawString("難易度 :" + this.statsData.getDifficulty().getDifficultyName(), this.window.getX() + 320.0F, this.window.getY() + 96.0F);
		} else {
			g.drawString("セーブデータがありません。", this.window.getX() + 50.0F, this.window.getY() + 64.0F);
		}
	}
}
