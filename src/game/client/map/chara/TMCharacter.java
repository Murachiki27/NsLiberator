package game.client.map.chara;

import java.io.Serializable;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.client.image.ImageUtils;
import game.client.map.AbstractMap;
import game.client.map.EnumDirection;

public abstract class TMCharacter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int TILE_SIZE = 32;
	
	private String characterTag;
	
	private String name;
	
	protected int posX;
	protected int posY;
	
	private float pixelX;
	private float pixelY;
	
	protected EnumDirection direction;
	
	protected float defaultSpeed = 4.0F;
	protected float speed;
	
	protected int maxMoveTiles;
	protected int moveTiles;
	
	private float movingLength;
	
	private boolean isMoving;
	
	private boolean isBackwards;
	
	private EnumCharacterType imageType;
	
	private int imageId;
	private String imagePath;
	private Image characterImage;
	
	protected float timer;
	protected int aniTick;
	protected int[] animationFrame;
	
	protected AbstractMap mapData;
	
	public TMCharacter(String characterTag, String name, EnumCharacterType charaType, AbstractMap map) {
		this.characterTag = characterTag;
		this.name = name;
		this.mapData = map;
		this.imageType = charaType;
		this.animationFrame = new int[] { 0, 1, 0, 2};
		this.direction = EnumDirection.DOWN;
		this.speed = this.defaultSpeed;
	}
	
	public void nullData() {
		this.characterImage = null;
	}
	
	public void initData() {
		if (this.imagePath != null) {
			this.setImage(this.imagePath);
		} else {
			this.setImage(this.imageId);
		}
	}
	
	public void renderCharacter(Graphics g, float offsetX, float offsetY) {
		if (this.characterImage != null) {
			float dx = this.pixelX + offsetX;
			float dy = this.pixelY + offsetY;
			int dir = this.direction.getDirId();
			int frame = this.animationFrame[this.aniTick];
			g.drawImage(this.characterImage, dx, dy, dx + TILE_SIZE, dy + TILE_SIZE, frame * TILE_SIZE, dir * TILE_SIZE, frame * TILE_SIZE + TILE_SIZE, dir * TILE_SIZE + TILE_SIZE);
		}
	}
		
	public void updateCharacter(int delta) {
		if (this.moveTiles < this.maxMoveTiles) {
			this.timer += delta * (0.001F * (this.speed));
			if (this.timer >= 1.0F) {
				this.timer = 0.0F;
				this.aniTick += 1;
				if (this.aniTick >= this.animationFrame.length) {
					this.aniTick = 0;
				}
			}
		} else {
			this.timer = 0.0F;
			this.aniTick = 0;
		}
	}
	
	public boolean moveCharacter() {
		if (this.moveTiles < this.maxMoveTiles) {
			if (this.isBackwards) {
				switch (this.direction) {
				case LEFT:
					if (this.moveRight()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case RIGHT:
					if (this.moveLeft()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case UP:
					if (this.moveDown()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case DOWN:
					if (this.moveUp()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				default:
					break;
				}
			} else {
				switch (this.direction) {
				case LEFT:
					if (this.moveLeft()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case RIGHT:
					if (this.moveRight()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case UP:
					if (this.moveUp()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				case DOWN:
					if (this.moveDown()) {
						this.moveTiles += 1;
						if (this.moveTiles >= this.maxMoveTiles) {
							this.speed = this.defaultSpeed;
							this.isMoving = false;
						} else {
							this.movingLength = 0.0F;
						}
						return true;
					}
					break;
				default:
					break;
				}
			}
		}
		return false;
	}
	
	public boolean moveLeft() {
		int nextX = this.posX - 1;
		int nextY = this.posY;
		if (nextX < 0) nextX = 0;
		
		if (!this.mapData.isHit(nextX, nextY)) {
			this.pixelX -= this.speed;
			if (this.pixelX < 0.0F) this.pixelX = 0.0F;
			
			this.movingLength += this.speed;
			
			if (this.movingLength >= TILE_SIZE) {
				this.posX -= 1;
				this.pixelX = this.posX * TILE_SIZE;
				
				return true;
			}
		} else {
			this.isMoving = false;
			this.moveTiles = this.maxMoveTiles;
			this.pixelX = this.posX * TILE_SIZE;
			this.pixelY = this.posY * TILE_SIZE;
		}
		return false;
	}
	
	public boolean moveRight() {
		int nextX = this.posX + 1;
		int nextY = this.posY;
		if (nextX > this.mapData.getCol() - 1) nextX = this.mapData.getCol() - 1;
		
		if (!this.mapData.isHit(nextX, nextY)) {
			this.pixelX += this.speed;
			if (this.pixelX > this.mapData.getWidth() - TILE_SIZE) this.pixelX = this.mapData.getWidth() - TILE_SIZE;
			
			this.movingLength += this.speed;
			
			if (this.movingLength >= TILE_SIZE) {
				this.posX += 1;
				this.pixelX = this.posX * TILE_SIZE;
				
				return true;
			}
		} else {
			this.isMoving = false;
			this.moveTiles = this.maxMoveTiles;
			this.pixelX = this.posX * TILE_SIZE;
			this.pixelY = this.posY * TILE_SIZE;
		}
		return false;
	}
	
	public boolean moveUp() {
		int nextX = this.posX;
		int nextY = this.posY - 1;
		if (nextY < 0) nextY = 0;
		
		if (!this.mapData.isHit(nextX, nextY)) {
			this.pixelY -= this.speed;
			if (this.pixelY < 0.0F) this.pixelY = 0.0F;
			
			this.movingLength += this.speed;
			
			if (this.movingLength >= TILE_SIZE) {
				this.posY -= 1;
				this.pixelY = this.posY * TILE_SIZE;
				
				return true;
			}
		} else {
			this.isMoving = false;
			this.moveTiles = this.maxMoveTiles;
			this.pixelX = this.posX * TILE_SIZE;
			this.pixelY = this.posY * TILE_SIZE;
		}
		return false;
	}
	
	public boolean moveDown() {
		int nextX = this.posX;
		int nextY = this.posY + 1;
		if (nextY > this.mapData.getRow() - 1) nextY = this.mapData.getRow() - 1;
		
		if (!this.mapData.isHit(nextX, nextY)) {
			this.pixelY += this.speed;
			if (this.pixelY > this.mapData.getHeight() - TILE_SIZE) this.pixelY = this.mapData.getHeight() - TILE_SIZE;
			
			this.movingLength += this.speed;
			
			if (this.movingLength >= TILE_SIZE) {
				this.posY += 1;
				this.pixelY = this.posY * TILE_SIZE;
				
				return true;
			}
		} else {
			this.isMoving = false;
			this.moveTiles = this.maxMoveTiles;
			this.pixelX = this.posX * TILE_SIZE;
			this.pixelY = this.posY * TILE_SIZE;
		}
		return false;
	}
	
	public void setImage(int charaImageNo) {
		this.imageId = charaImageNo;
		int u = (charaImageNo % 8) * (TILE_SIZE * 3);
		int v = (charaImageNo / 8) * (TILE_SIZE * 4);
		this.characterImage = ImageUtils.getMapCharaImage(this.imageType).getSubImage(u, v, TILE_SIZE * 3, TILE_SIZE * 4);
	}
	
	/* -------------------- DEBUG START ------------------------- */
	
	public void setImage(String imagePath) {
		this.imagePath = imagePath;
		this.characterImage = ImageUtils.getDebugImage(imagePath);
	}
	
	/* -------------------- DEBUG END ------------------------- */
	
	public void setMoving() {
		this.setMoving(1, this.defaultSpeed);
	}
	
	public void setMoving(int moveTiles, float speed) {
		int newTile;
		if (moveTiles < 0) {
			this.isBackwards = true;
			newTile = 0 - moveTiles;
		} else {
			newTile = moveTiles;
		}
		
		if (speed < 0.0F) speed = 0.0F;
		
		this.maxMoveTiles = newTile;
		this.moveTiles = 0;
		this.speed = speed;
		this.isMoving = true;
		this.movingLength = 0.0F;
	}
	
	public void setPosition(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
		this.pixelX = TILE_SIZE * posX;
		this.pixelY = TILE_SIZE * posY;
	}
	
	public void setDirection(EnumDirection dir) {
		if (dir != null) {
			this.direction = dir;
		}
	}
	
	public void setDefaultSpeed(float speed) {
		if (speed >= 0.0F) {
			this.defaultSpeed = this.speed = speed;
		}
	}
	
	public void setAnimationFrame(int[] frame) {
		this.animationFrame = frame;
	}
	
	public void setMap(AbstractMap mapObj) {
		this.mapData = mapObj;
	}
	
	public String getTag() {
		return this.characterTag;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPosX() {
		return this.posX;
	}
	
	public int getPosY() {
		return this.posY;
	}
	
	public float getPixelX() {
		return this.pixelX;
	}
	
	public float getPixelY() {
		return this.pixelY;
	}
	
	public EnumDirection getDirection() {
		return this.direction;
	}
	
	public EnumCharacterType getCharacterType() {
		return this.imageType;
	}
	
	public Image getImage() {
		return this.characterImage;
	}
	
	public AbstractMap getMapObj() {
		return this.mapData;
	}
	
	public int getImageId() {
		return this.imageId;
	}
	
	public String getImagePath() {
		return this.imagePath;
	}
	
	public boolean isMoving() {
		return this.isMoving;
	}
	
	public boolean isMoveFinished() {
		return this.moveTiles >= this.maxMoveTiles;
	}
}
