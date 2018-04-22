package game.client.chara;

import game.client.map.chara.EnumCharacterType;

/**
 * キャラクターステータスの情報を作る<p>
 * ※ステータスの基本はLv.1が基準
 * 
 * @author Murachiki27
 *
 */
public enum CharacterStatusInfo {
	ASTEER("アステール", 20, 10,12, 2, 3, 42, EnumCharacterType.CHARACTER, 1),
	EAT("イート", 18, 9, 13, 8, 1, 47, EnumCharacterType.CHARACTER, 2),
	DENITZ("デニッツ", 23, 6, 17, 4, 2, 56, EnumCharacterType.CHARACTER, 3),
	
	GOBLIN("ゴブリン", 20, 7, 2, 1, 0),
	GOBLIN_WAR("ゴブリンウォーリア", 18, 8, 1, 1, 1),
	GOBLIN_MAGIC("ゴブリンメイジ", 21, 8, 1, 1, 2),
	RABBIT("ホーンラビット", 15, 8, 1, 1, 3),
	WOLF("ウルフ", 23, 8, 1, 1, 4),
	SLIME("スライム", 12, 8, 1, 1, 5),
	COBOLT("コボルト", 22, 8, 1, 1, 6),
	DWARF("ドワーフ", 25, 8, 1, 1, 7),
	SNOWMAN("スノーマン", 20, 8, 1, 1, 8),
	YETI("イエティ", 22, 8, 1, 1, 9),
	ICE_GOLEM("アイスゴーレム", 25, 8, 1, 1, 10),
	SKELETON("スケルトン", 27, 8, 1, 1, 11),
	GARGOYLE("ガーゴイル", 18, 8, 1, 1, 12),
	LIZARDMAN("リザードマン", 23, 8, 1, 1, 13),
	REDCAP("レッドキャップ", 22, 8, 1, 1, 14),
	CENTAUR("ケンタウロス", 20, 8, 1, 1, 15),
	
	GOBLIN_KING("キングゴブリン", 28, 8, 1, 1, 50, EnumCharacterType.BOSS, 0),
	GOLEM("ゴーレム", 35, 8, 1, 1, 50, EnumCharacterType.BOSS, 1),
	IFRITO("イフリート", 31, 8, 1, 1, 50, EnumCharacterType.BOSS, 2),
	YETI_KING("キングイエティ", 33, 8, 1, 1, 50, EnumCharacterType.BOSS, 3),
	
	;
	
	private final String name;
	/** 体力(平均：20) */
	private final int health;
	/** スキルポイント(平均：8) */
	private final int skillPoint;
	/** ダメージ(平均：15) */
	private final int damage;
	/** 防御力(平均：5) */
	private final int defence;
	/** 素早さ(平均：3) */
	private final int avoid;
	/** LvUpによる差(基本値：50) */
	private final int difference;
	
	private final EnumCharacterType charaType;
	
	private final int imageId;
	
	private CharacterStatusInfo(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, EnumCharacterType charaType, int imageId) {
		this.name = name;
		this.health = health;
		this.skillPoint = skillPoint;
		this.damage = damage;
		this.defence = defence;
		this.avoid = avoid;
		this.difference = difference;
		this.charaType = charaType;
		this.imageId = imageId;
	}
	
	private CharacterStatusInfo(String name, int health, int damage, int defence, int avoid, int difference, EnumCharacterType charaType, int imageId) {
		this(name, health, 0, damage, defence, avoid, difference, charaType, imageId);
	}
	
	private CharacterStatusInfo(String name, int health, int damage, int defence, int avoid, int imageId) {
		this(name, health, 0, damage, defence, avoid, 50, EnumCharacterType.ENEMY, imageId);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public int getSkillPoint() {
		return this.skillPoint;
	}
	
	public int getDamage() {
		return this.damage;
	}
	
	public int getDefence() {
		return this.defence;
	}
	
	public int getAvoid() {
		return this.avoid;
	}
	
	public int getDifference() {
		return this.difference;
	}
	
	public EnumCharacterType getCharacterType() {
		return this.charaType;
	}
	
	public int getImageId() {
		return this.imageId;
	}
}
