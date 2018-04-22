package game.client.map.data;

import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.TMBasicMapBase;
import game.client.map.chara.EnumCharacterType;
import game.client.map.chara.TMNpc;
import game.client.map.chara.profession.ProfessionSave;
import game.client.map.event.EventDebug1;
import game.client.map.event.TMEventBase;
import game.client.map.event.component.EventDebug;

public class MapDebugRoom extends TMBasicMapBase {
	private static final long serialVersionUID = 1L;
	
	public MapDebugRoom(int mapId, String mapFile) {
		super(mapId, mapFile);
		this.setMapName("Test");
		
		DoorObject door = new DoorObject("door", 0, 2, 6);
		door.setPosition(6, 6, EnumDirection.RIGHT);
		
		TMNpc test = new TMNpc("#test", "アステール", EnumCharacterType.CHARACTER, this);
		test.setImage(1);
		test.setHomePosition(7, 5, 2);
		test.setMovearound(true);
		test.setProfession(new ProfessionSave());
		test.setMessage("やっと、セーブ機能ができたわ・・・\\nセーブしてみる?");
		
		this.addTileObject(door);
		this.addTileObject(new EventDebug1("debug_1", 6, 7));
		this.addTileObject(new EventNullCheck("null_check", 6, 9));
		
		this.addCharacter(test);
	}
	
	@Override
	public boolean isExistMonster() {
		return false;
	}
	
	public class EventNullCheck extends TMEventBase {
		private static final long serialVersionUID = 1L;
		
		public EventNullCheck(String tag, int x, int y) {
			super(tag, x, y);
			this.setStartFade(100, 100);
			this.setEndFade(100, 100);
			this.setMaskScreen(100);
			
			this.add(new EventDebug(EventDebug.DebugType.NULL_CHECK));
		}
	}
}
