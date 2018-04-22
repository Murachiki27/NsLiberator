package game.client.chara.skill;

public enum EnumSkillType {
	ATTACK_ONE,
	ATTACK_ALL,
	MAGIC_ATC_ONE,
	MAGIC_ATC_ALL,
	EFFECT_ONE,
	EFFECT_ALL,
	MINUS_EFFECT_ONE,
	MINUS_EFFECT_ALL,
	HEAL_ONE,
	HEAL_ALL,
	REV_ONE,
	REV_ALL,
	;
	
	public static boolean getAllActionType(EnumSkillType type) {
		return type == ATTACK_ALL || type == MAGIC_ATC_ALL || type == EFFECT_ALL || type == MINUS_EFFECT_ALL || type == HEAL_ALL || type == REV_ALL;
	}
	
	public static boolean getSupportType(EnumSkillType type) {
		return type == EFFECT_ONE || type == EFFECT_ALL || type == HEAL_ONE || type == HEAL_ALL || type == REV_ONE || type == REV_ALL;
	}
	
	public static EnumSkillType getType(String name) {
		for (EnumSkillType type : EnumSkillType.values()) {
			if (type.toString().equals(name)) {
				return type;
			}
		}
		return EnumSkillType.ATTACK_ONE;
	}
}
