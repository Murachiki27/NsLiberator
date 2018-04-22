package game.client.chara;

import game.client.chara.skill.SkillInfo;

public class BattlePlayer extends BattleChara {
	private static final long serialVersionUID = 1L;

	public BattlePlayer(String name) {
		super(name);
		this.addSkill(SkillInfo.DEFAULT_LANG);
		this.addSkill(SkillInfo.TACTICAL_FLAME);
		this.addSkill(SkillInfo.BURNING_ARROW);
		this.addSkill(SkillInfo.BALDIN_HEAT);
		this.addSkill(SkillInfo.HELL_MAGIC);
		this.getLevelToSkill();
		this.addLevel(50);
	}
}
