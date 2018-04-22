package game.client.map;

import game.TypingMaster;
import game.common.input.EnumKeyInput;
import game.common.input.InputKeyState;

public class CameraPos {
	
	private int centerX;
	private int centerY;
	
	private InputKeyState inputState;
	
	private AbstractMap castMap;
	
	private float posX;
	private float posY;
	
	private float amountX;
	private float amountY;
	
	private float moveSpeed = 4.0F;
	
	private float offsetX;
	private float offsetY;
	
	private int mapWidth;
	private int mapHeight;
	
	public CameraPos(AbstractMap mapBase, int width, int height) {
		this.castMap = mapBase;
		this.mapWidth = width;
		this.mapHeight = height;
		this.inputState = new InputKeyState();
		this.centerX = TypingMaster.WIDTH / 2;
		this.centerY = TypingMaster.HEIGHT / 2;
		this.posX = centerX;
		this.posY = centerY;
	}
	
	public CameraPos(AbstractMap mapBase) {
		this(mapBase, mapBase.getWidth(), mapBase.getHeight());
	}
	
	public void updateCamera(int delta) {
		if (this.castMap != null) {
			if (this.inputState.isPressKeyLeft()) {
				this.move(EnumDirection.LEFT);
			}
			if (this.inputState.isPressKeyRight()) {
				this.move(EnumDirection.RIGHT);
			}
			if (this.inputState.isPressKeyUp()) {
				this.move(EnumDirection.UP);
			}
			if (this.inputState.isPressKeyDown()) {
				this.move(EnumDirection.DOWN);
			}
		}
	}
	
	private void move(EnumDirection dir) {
		switch (dir) {
		case LEFT:
			this.posX -= this.moveSpeed;
			if (this.posX < this.centerX) {
				this.posX = this.centerX;
			} else {
				this.amountX -= this.moveSpeed;
				if (this.amountX < 0) {
					this.amountX = 0;
				}
			}
			break;
		case RIGHT:
			this.posX += this.moveSpeed;
			if (this.posX > this.mapWidth - this.centerX) {
				this.posX = this.mapWidth - this.centerX;
			} else {
				this.amountX += this.moveSpeed;
				if (this.amountX > this.mapWidth) {
					this.amountX = this.mapWidth;
				}
			}
			break;
		case UP:
			this.posY -= this.moveSpeed;
			if (this.posY < this.centerY) {
				this.posY = this.centerY;
			} else {
				this.amountY -= this.moveSpeed;
				if (this.amountY < 0) {
					this.amountY = 0;
				}
			}
			break;
		case DOWN:
			this.posY += this.moveSpeed;
			if (this.posY > this.mapHeight - this.centerY) {
				this.posY = this.mapHeight - this.centerY;
			} else {
				this.amountY += this.moveSpeed;
				if (this.amountY > this.mapHeight) {
					this.amountY = this.mapHeight;
				}
			}
			break;
		default:
			break;
		}
		
		this.offsetX = this.centerX - this.posX;
		this.offsetY = this.centerY - this.posY;
		this.offsetX = Math.min(offsetX, 0);
		this.offsetX = Math.max(offsetX, TypingMaster.WIDTH - this.mapWidth);
		this.offsetY = Math.min(offsetY, 0);
		this.offsetY = Math.max(offsetY, TypingMaster.HEIGHT - this.mapHeight);
	}
	
	public void keyPressed(EnumKeyInput input) {
		this.inputState.keyPressed(input);
		switch (input) {
		case KEY_DONE:
			break;
		case KEY_CANCEL:
			break;
		case KEY_MENU:
			break;
		default:
			break;
		}
	}
	
	public void keyReleased(EnumKeyInput input) {
		this.inputState.keyReleased(input);
		switch (input) {
		case KEY_DONE:
			break;
		case KEY_CANCEL:
			break;
		case KEY_MENU:
			break;
		default:
			break;
		}
	}
	
	public void keyReset() {
		this.inputState.resetKey();
	}
	
	public float getOffsetX() {
		return this.offsetX;
	}
	
	public float getOffsetY() {
		return this.offsetY;
	}
	
	public float getAmountX() {
		return this.amountX;
	}
	
	public float getAmountY() {
		return this.amountY;
	}
}
