package game.client.chara.enemy;

import game.client.chara.CharacterStatusInfo;

/**
 * 一般的な敵の情報を扱う列挙型
 * @author Murachiki27
 *
 */
public enum EnemyInfo {
	GOBLIN(CharacterStatusInfo.GOBLIN, "goblin", 1, 2),
	RABBIT(CharacterStatusInfo.RABBIT ,"rabbit", 2, 3),
	WOLF(CharacterStatusInfo.WOLF, "wolf", 5, 5),
	SLIME(CharacterStatusInfo.SLIME, "slime", 10, 10),
	COBOLT(CharacterStatusInfo.COBOLT, "cobolt", 10, 12),
	DWARF(CharacterStatusInfo.DWARF, "dwarf", 10, 14),
	
	
	
	GOBLIN_KING(CharacterStatusInfo.GOBLIN_KING, "boss_goblin", 8, 20)
	
	;
	
	private final CharacterStatusInfo chara;
	private final String commandName;
	private final int level;
	private final int exp;
	
	private EnemyInfo(CharacterStatusInfo chara, String commandName, int level, int exp) {
		this.chara = chara;
		this.commandName = commandName;
		this.level = level;
		this.exp = exp;
	}
	
	public CharacterStatusInfo getStatusInfo() {
		return this.chara;
	}
	
	public String getCommandName() {
		return this.commandName;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public int getEXP() {
		return this.exp;
	}
}
