package game.client.gui;

public class GuiEnemySkillWindow extends GuiStringsWindow {
	private float timer = 0;
	
	public GuiEnemySkillWindow() {
		super(128.0F, 16.0F, 0, 160, 384, 64, false);    
	}
	
	/**
	 * メッセージを先に進める
	 * @return メッセージが終了したらtrueを返す
	 */
	public boolean update(int delta) {
		if (this.isVisible()) {
			// 現在ページが最後のページだったらメッセージを終了する
			this.timer += delta * (1.0F / 1500.0F);
			if (this.timer > 1.0F) {
				this.closeGui();
				return true;
			}
		}
		return false;
	}
	
	public void reset() {
		this.timer = 0;
	}
}
