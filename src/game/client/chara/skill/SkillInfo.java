package game.client.chara.skill;

public enum SkillInfo {
	DEFAULT_LANG("ことばのまほう", EnumSkillType.ATTACK_ONE, 0, 3, 0, ""),
	TACTICAL_FLAME("タクティカルフレイム", EnumSkillType.MAGIC_ATC_ONE, 15, 4, 2, ""),
	BURNING_ARROW("バーニングアロー", EnumSkillType.MAGIC_ATC_ALL, 32, 6, 5, ""),
	BALDIN_HEAT("バルディン・ヒート", EnumSkillType.MAGIC_ATC_ONE, 51, 8, 7, ""),
	HELL_MAGIC("ヘル・マジック", EnumSkillType.MAGIC_ATC_ALL, 68, 10, 9, ""),
	
	REFRESH("リフレッシュ", EnumSkillType.HEAL_ONE, 15, 20, 2, ""),
	SPACE_HALL("スペースホール", EnumSkillType.MAGIC_ATC_ONE, 29, 6, 4, ""),
	SHOOT_STAR("シューティングスター", EnumSkillType.MAGIC_ATC_ALL, 48, 7, 6, ""),
	ASTEROID_IMPACT("アステロイドインパクト", EnumSkillType.MAGIC_ATC_ALL, 64, 9, 7, ""),
	
	MAGICAL_HAND("マジカルハンド", EnumSkillType.MAGIC_ATC_ONE, 15, 4, 2, ""),
	QUICK_ASSIST("クイックアシスト", EnumSkillType.HEAL_ONE, 15, 4, 2, ""),
	HEAL_ASSIST("ヒーリングアシスト", EnumSkillType.HEAL_ONE, 15, 4, 2, ""),
	ALL_SUPPORT("オールサポート", EnumSkillType.HEAL_ALL, 15, 4, 2, ""),
	
	HIT("なぐる", EnumSkillType.ATTACK_ONE, 2),
	KICK("キック", EnumSkillType.ATTACK_ONE, 2),
	
	;
	
	private final String name;
	private final EnumSkillType skillType;
	private final int termsLevel;
	private final int amount;
	private final int expenseSP;
	private final String description;
	
	private final Skill skill;
	
	private SkillInfo(String name, EnumSkillType skillType, int termsLevel, int amount, int expenseSP, String description) {
		this.name = name;
		this.skillType = skillType;
		this.termsLevel = termsLevel;
		this.amount = amount;
		this.expenseSP = expenseSP;
		this.description = description;
		
		this.skill = new Skill(this.name, this.skillType, this.termsLevel, this.amount, this.expenseSP, this.description);
	}
	
	private SkillInfo(String name, EnumSkillType skillType, int amount) {
		this(name, skillType, 0, amount, 0, "");
	}
	
	public Skill getSkill() {
		return this.skill;
	}
	
	public static Skill getSkill(String name) {
		for (SkillInfo info : SkillInfo.values()) {
			if (info.toString().equals(name)) {
				return info.getSkill();
			}
		}
		return null;
	}
}
