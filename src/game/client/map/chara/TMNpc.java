package game.client.map.chara;

import org.newdawn.slick.Graphics;

import game.client.map.AbstractMap;
import game.client.map.EnumDirection;
import game.client.map.chara.profession.TMProfessionBase;
import game.client.map.event.TMEventBase;

public class TMNpc extends TMCharacter {
	private static final long serialVersionUID = 1L;
	
	private String talkMessage = "";
	
	private boolean canMovearound;
	private double probMove = 0.01D;
	
	private boolean hasHomepos = false;
	private int homeX;
	private int homeY;
	private int homeDistance;
	
	private boolean hasCustomTag;
	
	private int accessCount;
	
	public boolean eventPause = false;
	
	private TMEventBase charaEvent;
	private TMProfessionBase charaProfession;
	
	
	public TMNpc(String characterTag, String name, EnumCharacterType charaType, AbstractMap map) {
		super(characterTag, name, charaType, map);
	}
	
	public TMNpc(CharacterObject charaObj, AbstractMap map) {
		super(charaObj.characterTag, charaObj.name, charaObj.characterType, map);
		if (charaObj.hasHome) {
			this.setHomePosition(charaObj.homeX, charaObj.homeY, charaObj.homeDistance);
		}
		this.hasCustomTag = charaObj.hasForcedTag;
		this.setPosition(charaObj.posX, charaObj.posY);
		this.setImage(charaObj.texture);
		this.setMessage(charaObj.talkMessage);
		this.setMovearound(charaObj.canMovearound);
		if (charaObj.charaEvent != null) {
			this.setEvent(charaObj.charaEvent);
		}
		this.setProfession(charaObj.charaProfession);
	}
	
	@Override
	public void renderCharacter(Graphics g, float offsetX, float offsetY) {
		super.renderCharacter(g, offsetX, offsetY);
	}
	
	@Override
	public void updateCharacter(int delta) {
		super.updateCharacter(delta);
	}
	
	public void moveUpdate() {
		if (!this.eventPause) {
			if (this.canMovearound) {
				if (this.isMoving()) {
					this.moveCharacter();
				} else if (this.mapData.rand.nextDouble() < this.probMove) {
					int dirId = this.mapData.rand.nextInt(4);
					this.setDirection(EnumDirection.getDirection(dirId));
					if (this.hasHomepos) {
						this.moveHomedDistance();
					} else {
						this.setMoving();
					}
				}
			}
		}
	}
	
	private void moveHomedDistance() {
		int firstX = this.homeX - this.homeDistance;
		int firstY = this.homeY - this.homeDistance;
		int lastX = this.homeX + this.homeDistance;
		int lastY = this.homeY + this.homeDistance;
		
		int nextX = 0;
		int nextY = 0;
		switch (this.direction) {
		case LEFT:
			nextX = this.posX - 1;
			nextY = this.posY;
			break;
		case RIGHT:
			nextX = this.posX + 1;
			nextY = this.posY;
			break;
		case UP:
			nextX = this.posX;
			nextY = this.posY - 1;
			break;
		case DOWN:
			nextX = this.posX;
			nextY = this.posY + 1;
			break;
		default:
			break;
		}
		
		if (nextX > firstX && nextX < lastX && nextY > firstY && nextY > lastY) {
			this.setMoving();
		}
	}
	
	
    @Override
    public void setMoving(int moveTiles, float speed) {
    	super.setMoving(moveTiles, speed);
    	this.aniTick = 1;
    }
	
	public void setMovearound(boolean canMovearound) {
		this.canMovearound = canMovearound;
	}
	
	public boolean canMovearound() {
		return this.canMovearound;
	}
	
	public void setHomePosition(int homeX, int homeY, int distance) {
		this.setPosition(homeX, homeY);
		this.hasHomepos = true;
		this.homeX = homeX;
		this.homeY = homeY;
		this.homeDistance = distance;
	}
	
	public void accessPlayer(TMPlayer palyer) {
		this.accessCount++;
	}
	
	public int getAccessCount() {
		return this.accessCount;
	}
	
	public void setMessage(String message) {
		this.talkMessage = message;
	}
	
	public String getMessage() {
		return this.talkMessage;
	}
	
	/**
	 * イベントをキャラクターにセットします。<p>
	 * 
	 * セットした場合は、そのキャラクターに話しかけた時、<br>
	 * メッセージが表示されずに、イベントが実行される。
	 * 
	 * @param event
	 */
	public void setEvent(TMEventBase event) {
		this.charaEvent = event;
		this.charaEvent.setByChara(this);
	}
	
	/**
	 * キャラクターに職業をセットします。<p>
	 * 
	 * セットした場合は、そのキャラクターに話しかけた時、<br>
	 * メッセージが表示された後に、職業内容が実行される。
	 * 
	 * @param profession
	 */
	public void setProfession(TMProfessionBase profession) {
		this.charaProfession = profession;
	}
	
	public void removeEvent() {
		this.charaEvent = null;
	}
	
	public TMEventBase getEvent() {
		return this.charaEvent;
	}
	
	public TMProfessionBase getProfession() {
		return this.charaProfession;
	}
	
	/* -------------------- DEBUG START ------------------------- */
	
	public boolean hasCustomTag() {
		return this.hasCustomTag;
	}
	
	public void setCustomTag() {
		this.hasCustomTag = true;
	}
	
	/* -------------------- DEBUG END ------------------------- */
}
