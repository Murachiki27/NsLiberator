package game.client.map.data;

import org.newdawn.slick.geom.Rectangle;

import game.client.chara.enemy.EnemyGoblin;
import game.client.chara.enemy.EnumGoblinType;
import game.client.item.ItemKey;
import game.client.item.TMItems;
import game.client.map.BackgroundType;
import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.MapTile;
import game.client.map.MapTileUtils;
import game.client.map.TMParallaxMapBase;
import game.client.map.TreasureObject;
import game.client.map.chara.EnumCharacterType;
import game.client.map.chara.TMNpc;
import game.client.map.event.EventForest;
import game.client.map.event.TMEventBase;
import game.client.map.event.component.EventComponent;
import game.client.map.event.component.EventMenuSelecter;
import game.common.data.BasicData;
import game.screen.ScreenMapState;

public class MapForest extends TMParallaxMapBase {
	private static final long serialVersionUID = 1L;

	public MapForest(int mapId, String mapFile) {
		super(mapId, mapFile);
		this.setMapName("最初の森");
		this.setBackgroundType(BackgroundType.FOREST);
		
		TreasureObject treasure = new TreasureObject("iron", 2, 5);
		treasure.setItem(TMItems.getItemById(0), 12);
		treasure.setLockObject((ItemKey)TMItems.getItemById(TMItems.key));
		
		DoorObject door = new DoorObject("door", 1, 2, 6);
		door.setPosition(6, 6, EnumDirection.RIGHT);
		
		DoorObject door2 = new DoorObject("door2", 5, 2, 12);
		door2.setPosition(6, 6, EnumDirection.RIGHT);
		DoorObject door3 = new DoorObject("door3", 6, 2, 13);
		door3.setPosition(6, 6, EnumDirection.RIGHT);
		
		EventForest event = new EventForest("event", 2, 4);
		
		TMNpc test = new TMNpc("#test", "desy", EnumCharacterType.CHARACTER, this);
		test.setImage(1);
		test.setPosition(5, 10);
		test.setMovearound(true);
		test.setEvent(event);
		
		this.addTileObject(treasure);
		this.addTileObject(door);
		this.addTileObject(door2);
		this.addTileObject(door3);
		this.addTileObject(event);
		
		this.addTileObject(new TransitionObject("#debug_trans", 2, 8));
		
		this.addCharacter(test);
		
		this.addDayEnemy(new EnemyGoblin(EnumGoblinType.NORMAL));
		this.addDayEnemy(new EnemyGoblin(EnumGoblinType.WAR));
		this.addDayEnemy(new EnemyGoblin(EnumGoblinType.MAGIC));
		
		this.addNightEnemy(new EnemyGoblin(EnumGoblinType.NORMAL));
		this.addNightEnemy(new EnemyGoblin(EnumGoblinType.WAR));
		this.addNightEnemy(new EnemyGoblin(EnumGoblinType.MAGIC));
	}
	
	public class TransitionObject extends TMEventBase {
		private static final long serialVersionUID = 1L;
		
		private EventMenuSelecter selecter;
		
		public TransitionObject(String tag, int x, int y) {
			super(tag, x, y);
			String[] menu = BasicData.getMapData().getRegisterMapTags();
			this.selecter = new EventMenuSelecter(new Rectangle(260.0F, 150.0F, 160, 112), menu);
			
			this.add(this.selecter);
		}
		
		@Override
		public boolean isHitTile() {
			return true;
		}
		
		@Override
		public MapTile getTile() {
			return MapTileUtils.flower_red;
		}
		
		@Override
		public void actionFinished(EventComponent comp) {
			if (comp == this.selecter) {
				if (this.selecter.getIndex() != -2) {
					String mapIdStr = BasicData.getMapData().getMapToTag(this.selecter.getString()).getMapID();
					int mapId = Integer.valueOf(mapIdStr);
					ScreenMapState.moveMap(mapId, this.getPosX() - 1, this.getPosY(), EnumDirection.RIGHT);
				}
			}
		}
	}
}
