package game.client.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import game.common.input.EnumKeyInput;
import game.common.network.UpdateChecker;
import game.common.settings.Options;
import game.sound.SoundListener;
import game.utils.TMLog;

public class GuiUpdateWindow extends TMGuiScreen {
	private GuiWindow window;
	private GuiMenuBox menu;
	private GuiCheckBox check;
	private Desktop desktop;
	
	public GuiUpdateWindow() {
		this.window = new GuiWindow(64.0F, 32.0F, 512, 416);
		this.menu = new GuiMenuBox(144.0F, 400.0F, 0.0F, 5.0F, 0, 0, false);
		this.menu.addMenuPackStrings(GuiMenuPack.DEFAULT);
		this.menu.addMenuStrings("今後表示しない");
		this.menu.setMultiMode(3, 100);
		
		this.check = new GuiCheckBox(492.5F, 390.0F, 2.0F, false);
		this.desktop = Desktop.getDesktop();
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		this.window.draw(g);
		this.menu.draw(g);
		this.check.draw(g);
		if (UpdateChecker.updImage != null) {
			g.drawImage(UpdateChecker.updImage, 320 - getCentX(), 192 - getCentY());
		} else {
			g.drawString("画像無し", 288, 198);
		}
		g.drawString("最新版 " + UpdateChecker.updVer + " が公開されています。", 128, 64);
		g.drawString("URLを開きますか? ", 250, 320);
		g.setColor(Color.yellow);
		g.drawString("「表示しない」は設定から変更出来ます。", 160, 360);
		g.setColor(Color.white);
	}
	
	@Override
	public void showGui() {
		super.showGui();
		this.window.showGui();
		this.menu.showGui();
	}
	
	@Override
	public void closeGui() {
		super.closeGui();
		this.window.closeGui();
		this.menu.closeGui();
	}
	
	@Override
	public void keyPressed(int key, char c, EnumKeyInput input) {
		super.keyPressed(key, c, input);
		this.menu.keyPressed(key, c, input);
		switch (input) {
		case KEY_DONE:
			SoundListener.play(1);
			int state = menu.get();
			switch (state) {
			case 0:
				this.closeGui();
				Options.UPDATE_NOTICE = !this.check.getState();
				Options.saveConfig();
				try {
					this.desktop.browse(new URI(UpdateChecker.updUrl));
				} catch (IOException | URISyntaxException e) {
					TMLog.println(e.getMessage());
				}
				break;
			case 1:
				this.closeGui();
				Options.UPDATE_NOTICE = !this.check.getState();
				Options.saveConfig();
				break;
			case 2:
				this.check.doneCheck();
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private float getCentX() {
		return UpdateChecker.updImage.getWidth() / 2;
	}
	private float getCentY() {
		return UpdateChecker.updImage.getHeight() / 2;
	}
}
