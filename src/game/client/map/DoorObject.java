package game.client.map;

import game.client.map.chara.TMPlayer;
import game.screen.ScreenMapState;

public class DoorObject extends MapTileObject {
	private static final long serialVersionUID = 1L;
	
	private int dustMapId;
	private int dustX;
	private int dustY;
	private EnumDirection dustDir;

	public DoorObject(String tag, int mapId, int x, int y) {
		super(tag, x, y);
		this.dustMapId = mapId;
	}
	
	@Override
	public void accessPlayer(TMPlayer playerChara) {
		super.accessPlayer(playerChara);
		if (!this.hasNeedKeyObject()) {
			ScreenMapState.moveMap(this.dustMapId, this.dustX, this.dustY, this.dustDir);
		} else {
			ScreenMapState.setMessageGui(null, "そのドアはカギがかかっています。\n" + this.getKeyItemName() + "があれば開けられそうだ・・・");
		}
		this.setCloseAction();
	}
	
	@Override
	public boolean isHitTile() {
		return true;
	}
	
	public void setPosition(int x, int y, EnumDirection dir) {
		this.dustX = x;
		this.dustY = y;
		this.dustDir = dir;
	}
	
	public int getDustMapId() {
		return this.dustMapId;
	}
	
	public int getDustX() {
		return this.dustX;
	}
	
	public int getDustY() {
		return this.dustY;
	}
	
	public EnumDirection getDir() {
		return this.dustDir;
	}
	
	@Override
	public MapTile getTile() {
		return MapTileUtils.door1;
	}
}
