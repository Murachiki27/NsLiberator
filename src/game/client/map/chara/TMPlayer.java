package game.client.map.chara;

import org.newdawn.slick.Graphics;

import game.client.map.AbstractMap;
import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.MapTileObject;
import game.client.map.TreasureObject;

public class TMPlayer extends TMCharacter {
	private static final long serialVersionUID = 1L;
	
	private boolean isKeepMoving;
	
	public TMPlayer(String name, AbstractMap map) {
		super("#player", name, EnumCharacterType.CHARACTER, map);
		this.setImage(0);
	}
	
	@Override
	public void renderCharacter(Graphics g, float offsetX, float offsetY) {
		super.renderCharacter(g, offsetX, offsetY);
	}
	
	@Override
	public void updateCharacter(int delta) {
		if (this.isKeepMoving) {
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
	
	public TMCharacter talkWith() {
		int nextX = 0;
		int nextY = 0;
		
		switch (direction) {
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
		
		TMCharacter chara = this.mapData.checkCharacter(nextX, nextY);
		if (chara != null && !chara.isMoving()) {
			switch (direction) {
			case LEFT:
				chara.setDirection(EnumDirection.RIGHT);
				break;
			case RIGHT:
				chara.setDirection(EnumDirection.LEFT);
				break;
			case UP:
				chara.setDirection(EnumDirection.DOWN);
				break;
			case DOWN:
				chara.setDirection(EnumDirection.UP);
				break;
			default:
				break;
			}
		}
		return chara;
	}
	
	public TreasureObject searchTreasure() {
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
		MapTileObject treasure = this.mapData.checkTileObject(nextX, nextY);
		if (treasure instanceof TreasureObject) {
			return (TreasureObject) treasure;
		}
		return null;
	}
	
	public DoorObject getDoor() {
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
		MapTileObject treasure = this.mapData.checkTileObject(nextX, nextY);
		if (treasure instanceof DoorObject) {
			return (DoorObject) treasure;
		}
		return null;
	}
	
	
	public MapTileObject getNextToHitObject() {
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
		MapTileObject value = this.mapData.checkTileObject(nextX, nextY);
		if (value != null && value.isHitTile()) {
			return value;
		}
		return null;
	}
	
	public MapTileObject getNextToObject() {
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
		return this.mapData.checkTileObject(nextX, nextY);
	}
	
    /**
     * 目の前にドアがあるか調べる
     * @return 目の前にあるDoorEventオブジェクト
     */
	/*
    public DoorEvent open() {
        int nextX = 0;
        int nextY = 0;
        // キャラクターの向いている方向の1歩となりの座標
        switch (direction) {
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
        // その方向にドアがあるか調べる
        MapTileObject event = this.mapData.checkTileObject(nextX, nextY);
        if (event instanceof DoorEvent) {
            return (DoorEvent)event;
        }
        return null;
    }
    
    public TalkEvent checkTalk() {
        int nextX = 0;
        int nextY = 0;
        // キャラクターの向いている方向の1歩となりの座標
        switch (direction) {
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
        // その方向にドアがあるか調べる
        MapTileObject event = this.mapData.checkTileObject(nextX, nextY);
        if (event instanceof TalkEvent) {
            return (TalkEvent)event;
        }
        return null;
    }
    */
	
    @Override
    public void setMoving() {
    	this.setMoving(1, this.defaultSpeed);
    }
    
    @Override
    public void setMoving(int moveTiles, float speed) {
    	super.setMoving(moveTiles, speed);
    	if (!this.isKeepMoving) {
    		this.aniTick = 1;
    	}
    	this.isKeepMoving = true;
    }
    
    public void setKeepMoving(boolean isKeepMoving) {
    	this.isKeepMoving = isKeepMoving;
    }
    
    /*
    public StoryEvent getStoryEvent() {
    	return event;
    }
    
    public void setStory(StoryEvent event) {
    	this.event = event;
    }
    
    public StoryFile getStoryfile() {
    	return story;
    }
    
    public void setStoryfile(StoryFile storyfile) {
    	story = storyfile;
    }
    
    public void removeAll() {
    	story = null;
    	event = null;
    }
    */
}
