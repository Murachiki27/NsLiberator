package game.client.chara;

import java.util.Random;

import game.client.chara.skill.Skill;
import game.client.map.chara.EnumCharacterType;
import game.common.data.BasicData;
import game.utils.TMRandom;

public class EnemyStatus extends CharaStatus {
	private static final long serialVersionUID = 1L;
	// 現在のスキル番号
	private int skillNo = 0;
	
	protected int exp = 0;
	
	protected int startPos;
	protected int endPos;
	
	protected Random rand;
	// ターゲットのキャラクター番号
	protected int targetIndex;
	
	protected Skill currentSkill;
	
	public EnemyStatus(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, int quickness, int imageId, EnumCharacterType charaType) {
		super(name, health, skillPoint, damage, defence, avoid, difference, quickness, imageId, charaType);
	}
	
	public EnemyStatus(CharacterStatusInfo statusInfo) {
		super(statusInfo);
		this.rand = new TMRandom();
	}
	
	public void onUpdate() {
		this.updateSkill();
	}
	
	protected void updateSkill() {
		if (this.skillList.get(this.skillNo) != null) {
			this.currentSkill = this.skillList.get(this.skillNo);
		} else {
			this.currentSkill = null;
		}
	}
	
	public void updateSkillNo() {
		this.skillNo++;
		if (this.skillNo >= this.skillList.size()) {
			this.skillNo = 0;
		}
	}
	
	public void setTargetToRandom() {
		int size = BasicData.getPlayerData().getMemberSize();
		int index = this.rand.nextInt(size);
		this.targetIndex = index;
	}
	
	public void setTarget(int index) {
		this.targetIndex = index;
	}
	
	public int getTarget() {
		return this.targetIndex;
	}
	
	public int getExp() {
		return this.exp;
	}
	
	public Skill getUseSkill() {
		return this.currentSkill;
	}
	
	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
}
