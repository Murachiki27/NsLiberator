package game.client.map.data;

import game.api.MapObject;
import game.client.map.MapTileObject;
import game.client.map.TMBasicMapBase;
import game.client.map.chara.TMCharacter;

public class TMAnonyBasicMap extends TMBasicMapBase {
	private static final long serialVersionUID = 1L;
	private final MapObject mapObj;
	
	public TMAnonyBasicMap(int mapId, MapObject mapObject) {
		super(mapId, mapObject);
		this.mapObj = mapObject;
	}
	
	public TMAnonyBasicMap subMap(int mapId) {
		TMAnonyBasicMap newMap = new TMAnonyBasicMap(mapId, this.mapObj);
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
