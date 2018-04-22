package game.client.gui;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import game.common.data.BasicData;

public class GuiInputWindow {
	private int maxCharacter = 128;
	
	private GuiWindow window;
	
	private boolean active;
	private boolean output;
	
	private String text = "";
	private int cursorPos;
	private String descript = "";
	
	private Input input;
	
	public GuiInputWindow(Input input) {
		this(new GuiWindow(64, 320, 520, 128), input);
	}
	
	public GuiInputWindow(GuiWindow window, Input input) {
		this.window = window;
		this.input = input;
	}
	
	public void renderGui(Graphics g) {
		if (!active) return;
		window.draw(g);
		g.setColor(Color.darkGray);
		g.fillRect(window.getX() + 64, window.getY() + 48, window.getWidth() - 128, 32);
		g.setColor(Color.white);
		int cpos = BasicData.defaultUfont.getWidth(text.substring(0, cursorPos));
		g.drawString(text, window.getX() + 66, window.getY() + 50);
		g.drawString("_", window.getX() + 66 + cpos + 2, window.getY() + 50);
	}
	
	public void keyPressed(int key, char c) {
		if (key != -1) {
			if ((key == 47) && ((this.input.isKeyDown(29)) || (this.input.isKeyDown(157)))) {
				String text = Sys.getClipboard();
				if (text != null) {
					doPaste(text);
				}
				return;
			}
			if ((this.input.isKeyDown(29)) || (this.input.isKeyDown(157))) {
				return;
			}
			if ((this.input.isKeyDown(56)) || (this.input.isKeyDown(184))) {
				return;
			}
		}
		if (key == 28) {
			active = false;
			output = true;
		} else if (key == 14) {
			if ((cursorPos > 0) && (text.length() > 0)) {
				if (cursorPos < text.length()) {
					text = (text.substring(0, cursorPos - 1) + text.substring(cursorPos));
				} else {
					text = text.substring(0, cursorPos - 1);
				}
				cursorPos -= 1;
			}
		} else if ((c > '。') && text.length() < maxCharacter) {
			if (cursorPos < text.length()) {
				text = (text.substring(0, cursorPos) + c + text.substring(cursorPos));
			} else {
				text = (text.substring(0, cursorPos) + c);
			}
			cursorPos += 1;
		} else if ((c < '') && (c > '\037') && text.length() < maxCharacter) {
			if (cursorPos < text.length()) {
				text = (text.substring(0, cursorPos) + c + text.substring(cursorPos));
			} else {
				text = (text.substring(0, cursorPos) + c);
			}
			cursorPos += 1;
		}
	}
	
	public void powerOn() {
		active = true;
	}
	public boolean isActive() {
		return active;
	}
	public boolean output() {
		return output;
	}
	public void reset() {
		active = false;
		output = false;
		text = "";
		cursorPos = 0;
		descript = "";
	}
	public void setDecript(String descript) {
		this.descript = descript;
	}
	private void doPaste(String text) {
		for (int i = 0; i < text.length(); i++) {
			keyPressed(-1, text.charAt(i));
		}
	}
	public String getText() {
		return text;
	}
	public String getDescript() {
		return descript;
	}
}
