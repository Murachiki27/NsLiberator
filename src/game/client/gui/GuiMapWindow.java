package game.client.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import game.TypingMaster;
import game.client.image.ImageUtils;
import game.common.input.EnumKeyInput;

public class GuiMapWindow extends TMGuiScreen {
	private static final int ICON = 16;
	private static final float MAP_SCALE = 1.5F;
	
	private static final int MAP_W = (int) ((TypingMaster.WIDTH * MAP_SCALE) - TypingMaster.WIDTH);
	private static final int MAP_H = (int) ((TypingMaster.HEIGHT * MAP_SCALE) - TypingMaster.HEIGHT);
	
	private static final int[] ICON_W = new int[] {11, 14, 7, 3, 18, 21, 22, 21, 33, 35};
	private static final int[] ICON_H = new int[] {26, 14, 10, 20, 5, 12, 15, 17, 9, 8};
	private static final int[] ICONS = new int[] {0, 1, 0, 4, 0, 0, 3, 2, 0, 2};
	
	private int x;
	private int y;
	
	//private int playerX;
	//private int playerY;
	
	private Image[] mapIcon;
	
	private int keyStateX = 0;
	private int keyStateY = 0;
	
	public GuiMapWindow() {
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.MAP_WINDOW).getScaledCopy(MAP_SCALE);
		this.mapIcon = new Image[ICONS.length];
		for (int i = 0; i < mapIcon.length; i++) {
			this.mapIcon[i] = ImageUtils.getGuiImage(GuiImageInfo.MAP_ICON).getScaledCopy(MAP_SCALE);
		}
	}
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		this.guiImage.draw(x, y);
		for (int i = 0; i < mapIcon.length; i++) {
			float size = ICON * MAP_SCALE;
			float dx = x + (ICON_W[i] * size);
			float dy = y + (ICON_H[i] * size);
			float ix = 0;//(ICONS[i] % 2) * size;
			float iy = (ICONS[i] % 5) * size;
			//g.drawImage(mapIcon[i], x + (ICON_W[i] * ICON), y + (ICON_H[i] * ICON), ix, iy, ix + ICON * MAP_SCALE, iy + ICON * MAP_SCALE);
			this.mapIcon[i].draw(dx, dy, dx + size * MAP_SCALE, dy + size * MAP_SCALE, ix, iy, ix + size, iy + size);
		}
	}
	
	public void updateGui() {
		if (this.keyStateX != 0) {
			this.moveX(this.keyStateX);
		}
		if (this.keyStateY != 0) {
			this.moveY(this.keyStateY);
		}
	}
	
	@Override
	public void keyPressed(int key, char c, EnumKeyInput input) {
		super.keyPressed(key, c, input);
		switch (input) {
		case KEY_DOWN:
			this.keyStateY = -1;
			break;
		case KEY_LEFT:
			this.keyStateX = 1;
			break;
		case KEY_RIGHT:
			this.keyStateX = -1;
			break;
		case KEY_UP:
			this.keyStateY = 1;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void keyReleased(int key, char c, EnumKeyInput input) {
		switch (input) {
		case KEY_LEFT:
		case KEY_RIGHT:
			this.keyStateX = 0;
			break;
		case KEY_DOWN:
		case KEY_UP:
			this.keyStateY = 0;
			break;
		default:
			break;
		}
	}
	
	private void moveX(int dirX) {
		if (dirX == 1) {
			this.x += 2;
			if (this.x > 0) {
				this.x = 0;
			}
		}
		if (dirX == -1) {
			this.x -= 2;
			if (this.x < -MAP_W) {
				this.x = -MAP_W;
			}
		}
	}
	
	private void moveY(int dirY) {
		if (dirY == 1) {
			this.y += 2;
			if (this.y > 0) {
				this.y = 0;
			}
		}
		if (dirY == -1) {
			this.y -= 2;
			if (this.y < -MAP_H) {
				this.y = -MAP_H;
			}
		}
	}
}
