package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FieldMapGui {
	
	private Image imgClear;
	
	private String questTitle;
	private boolean isQuestClear;
	
	public FieldMapGui() {
		try {
			imgClear = new Image("img/icons/clear.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void drawQuestTitle(Graphics g) {
		g.setColor(Color.white);
		g.drawString("クエスト : " + questTitle, 20, 20);
		if (isQuestClear) {
			g.drawImage(imgClear, 20, 2);
		}
	}
	
	public void setQuestTitle(String title) {
		this.questTitle = title;
	}
	
	public void setQuestClear(boolean isClear) {
		this.isQuestClear = isClear;
	}
}
