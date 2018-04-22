package game.client.map.data;

import game.api.MapObject;
import game.client.map.MapTileObject;
import game.client.map.TMParallaxMapBase;
import game.client.map.chara.TMCharacter;

public class TMAnonyParalMap extends TMParallaxMapBase {
	private static final long serialVersionUID = 1L;
	private final MapObject mapObj;
	
	public TMAnonyParalMap(int mapId, MapObject mapObject) {
		super(mapId, mapObject);
		this.mapObj = mapObject;
	}
	
	public TMAnonyParalMap subMap(int mapId) {
		TMAnonyParalMap newMap = new TMAnonyParalMap(mapId, this.mapObj);
		newMap.setMapName(this.getMapName());
		for (TMCharacter chara : this.getCharacters()) {
			newMap.addCharacter(chara);
		}
		for (MapTileObject object : this.getObjects()) {
			newMap.addTileObject(object);
		}
		return newMap;
	}
}
