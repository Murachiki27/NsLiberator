package game.client.map;

import game.common.StingNames;

public enum BackgroundType {
	EMPTY("",""),
	FOREST("forest.png", "forest.png"),
	GOBLINS_CAVE("goblins_cave.png", "goblins_cave.png"),
	KING_GOBLIN_ROOM("goblins_cave_boss.png", "goblins_cave_boss.png"),
	DESERT("desert.png", "desert_n.png"),
	MINE_SHAFT("mine_shaft.png", "mine_shaft.png"),
	
	DATA_SELECT("data_select.png", "data_select.png"),
	
	STAFF_ROOM("staff_room.png", "staff_room.png")
	
	;
	
	private String normalImageDir;
	private String nightImageDir;
	
	private BackgroundType(String normalImageDir, String nightImageDir) {
		this.normalImageDir = StingNames.backgrounds_path + normalImageDir;
		this.nightImageDir = StingNames.backgrounds_path + nightImageDir;
	}
	
	public String getNormalImageDir() {
		return this.normalImageDir;
	}
	
	public String getNightImageDir() {
		return this.nightImageDir;
	}
}
