package game.client.chara.ai;

import game.client.chara.BattleEnemy;
import game.client.chara.skill.Skill;
import game.client.chara.skill.SkillInfo;

public class EnemyAIHealthAtSkill extends EnemyAIBase {
	
	private int skillIndex;
	
	private int healthMin;
	private int healthMax;
	
	private boolean isForceMode;
	private boolean processForce;
	private boolean isReset;
	
	private Skill[] ownerSkills;
	
	/**
	 * 指定範囲内の体力値になった時に実行されるAI。AIが実行されている時は、
	 * 指定スキルを登録した順に実行する。
	 * @param ownerEnemy 対象の敵
	 * @param healthMin 規定範囲の最小値
	 * @param healthMax 規定範囲の最大値
	 * @param switchForceMode 範囲内に入ったら、強制的に継続実行するか
	 * @param setReset 再度、AIが実行された時に進んでたスキル順をリセットするか
	 * @param skillInfo 
	 */
	public EnemyAIHealthAtSkill(BattleEnemy ownerEnemy, int healthMin, int healthMax, boolean switchForceMode, boolean setReset, SkillInfo... skillInfo) {
		this.skillIndex = -1;
		this.ownerEnemy = ownerEnemy;
		this.healthMin = healthMin;
		this.healthMax = healthMax;
		this.isForceMode = switchForceMode;
		this.isReset = setReset;
		this.ownerSkills = new Skill[skillInfo.length];
		for (int i = 0; i < skillInfo.length; i++) {
			this.ownerSkills[i] = skillInfo[i].getSkill();
		}
	}
	
	@Override
	public boolean startAI() {
		if (this.processForce || (this.ownerEnemy.getHealth() >= this.healthMin && this.ownerEnemy.getHealth() <= this.healthMax)) {
			this.hasReset = false;
			this.proceedSkillIndex();
			if (this.isForceMode && !this.processForce) {
				this.processForce = true;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void resetAI() {
		if (this.isReset) {
			this.skillIndex = -1;
			this.hasReset = true;
		}
	}
	
	@Override
	public int setSkillIndex() {
		return this.ownerEnemy.skillList.indexOf(this.ownerSkills[this.skillIndex]);
	}
	
	private void proceedSkillIndex() {
		this.skillIndex++;
		if (this.skillIndex >= this.ownerSkills.length) {
			this.skillIndex = 0;
		}
	}
	
	public String toStringData() {
		return "EnemyAIHealthAtSkill:" + this.healthMin + ":" + this.healthMax + ":" + this.hashCode();
	}
}
