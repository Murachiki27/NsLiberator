package game.client.map.data;

import game.client.chara.enemy.EnemyGoblin;
import game.client.chara.enemy.EnumGoblinType;
import game.client.item.TMItems;
import game.client.map.BackgroundType;
import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.MapTile;
import game.client.map.MapTileUtils;
import game.client.map.TMBasicMapBase;
import game.client.map.TreasureObject;
import game.client.map.chara.EnumCharacterType;
import game.client.map.chara.TMNpc;
import game.client.map.event.TMEventBase;
import game.client.map.event.component.EventBattle;
import game.client.map.event.component.EventChangeDrawMap;
import game.client.map.event.component.EventScrollMap;
import game.client.map.event.component.EventTalk;

public class MapSample  extends TMBasicMapBase {
	private static final long serialVersionUID = 1L;
	
	@game.api.TMSample
	public MapSample(int mapId, String mapFile) {
		super(mapId, mapFile);
		
		/** マップ名 */
		this.setMapName("サンプル");
		/** 戦闘時の背景 */
		this.setBackgroundType(BackgroundType.FOREST);
		
		DoorObject sampleDoor = new DoorObject("sample_door", 0, 2, 6);
		/** 移動先の方向設定 */
		sampleDoor.setPosition(6, 6, EnumDirection.RIGHT);
		
		TreasureObject sampleTreasure = new TreasureObject("sample_tresure", 2, 5);
		sampleTreasure.setItem(TMItems.getItemById(0), 12);
		
		TMNpc sampleNpc = new TMNpc("sample_npc", "sample", EnumCharacterType.CHARACTER, this);
		sampleNpc.setImage(1);
		sampleNpc.setPosition(5, 10);
		sampleNpc.setMovearound(true);
		
		this.addTileObject(sampleDoor);
		this.addTileObject(sampleTreasure);
		
		this.addTileObject(new SampleEvent("sample_event", 5, 5));
		
		this.addCharacter(sampleNpc);
		
		this.addDayEnemy(new EnemyGoblin(EnumGoblinType.NORMAL));
		this.addDayEnemy(new EnemyGoblin(EnumGoblinType.WAR));
		
		this.addNightEnemy(new EnemyGoblin(EnumGoblinType.MAGIC));
	}
	
	
	private class SampleEvent extends TMEventBase {
		private static final long serialVersionUID = 1L;

		@game.api.TMSample
		public SampleEvent(String tag, int x, int y) {
			super(tag, x, y);
			/** 開始のフェードインとフェードアウト */
			this.setStartFade(500, 500);
			/** マスクをかけるときのフェードの値 */
			this.setMaskScreen(500);
			/** 終わりのフェードインとフェードアウト */
			this.setEndFade(500, 500);
			
			/** イベントのプロセス */
			this.add(new EventChangeDrawMap(0, 11, 8));
			this.add(new EventTalk("テスト", "一応テストで表示しているけど、うまくいってるかな？"));
			this.add(new EventScrollMap(0, 5, 2.0F).setOtherMapScroll());
			this.add(new EventScrollMap(5, -5, 2.0F).setOtherMapScroll());
			this.add(new EventScrollMap(5, 0, 2.0F).setOtherMapScroll());
			this.add(new EventScrollMap(-5, 3, 2.0F).setOtherMapScroll());
			this.add(new EventChangeDrawMap(EventChangeDrawMap.CLOSE_MAP, 0, 0));
			this.add(new EventTalk("テスト", "テスト\\n改ページ\\fプレイヤー名:\\p"));
			this.add(new EventBattle("rabbit", "rabbit"));
			this.add(new EventTalk("テスト", "おわり。"));
		}
		
		@game.api.TMSample
		@Override
		public MapTile getTile() {
			return MapTileUtils.ore_coal;
		}
		
	}
}
