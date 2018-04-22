package game.client.map.chara;

import game.client.map.EnumDirection;
import game.client.map.chara.profession.TMProfessionBase;
import game.client.map.event.TMEventBase;

public class CharacterObject {
	
	public boolean hasForcedTag;
	public String characterTag = "";
	
	public String name = "";
	
	public String texture;
	
	public int posX;
	public int posY;
	
	public float speed = 4.0F;
	
	public EnumDirection direction = EnumDirection.DOWN;
	
	public EnumCharacterType characterType = EnumCharacterType.CHARACTER;
	
	public boolean hasHome;
	public int homeX;
	public int homeY;
	public int homeDistance;
	
	public String talkMessage = "";
	
	public boolean canMovearound = false;
	
	public TMEventBase charaEvent;
	
	public TMProfessionBase charaProfession;
	
	public boolean isRegister() {
		return this.texture != null;
	}
}
