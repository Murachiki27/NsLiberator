package game.client.image;


/**
 * 敵の画像(バトル画面上)を登録する<p>
 * 登録名は、画像の名前で登録する
 * 
 * @author Murachiki27
 *
 */
public enum BattleImageInfo {
	GOBLIN("goblin"),
	GOBLIN_WAR("goblin_wr"),
	GOBLIN_MAGIC("goblin_magic"),
	RABBIT("rabbit"),
	WOLF("wolf"),
	SLIME("slime"),
	COBOLT("kobolt"),
	DWARF("dwarf"),
	SNOWMAN("snowman"),
	YETI("yeti"),
	ICE_GOLEM("dwarf"),
	SKELETON("skeleton"),
	GARGOYLE("gargoyle"),
	LIZARDMAN("lizardman"),
	REDCAP("dwarf"),
	CENTAUR("dwarf"),
	
	GOBLIN_KING("goblin_king"),
	GOLEM("golem"),
	IFRITO("ifrit"),
	YETI_KING("goblin_king"),
	;
	
	private final String filePath;
	
	private BattleImageInfo(String filePath) {
		this.filePath = filePath;
	}
	
	public String getPath() {
		return this.filePath;
	}
}
