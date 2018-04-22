package game.client.chara;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Image;

import game.client.chara.skill.Skill;
import game.client.chara.skill.SkillInfo;
import game.client.map.chara.EnumCharacterType;

public class CharaStatus implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	// キャラクターの名前(絶対変更不可)
	private String name;
	// キャラクターのレベル
	private int level;
	// キャラクターの現在のHP
	protected int health;
	// キャラクターの最大HP(LvUP以外変更不可)
	protected int maxHealth;
	// キャラクターの現在のSP
	protected int skill;
	// キャラクターの最大SP(LvUP以外変更不可)
	protected int maxSkill;
	/** キャラクターが死んでいるか */
	private boolean isDead = false;
	
	/** キャラクターが扱えるスキルのリスト(非アクティブなスキルも含まれる) */
	public List<Skill> skillList = new LinkedList<Skill>();
	
	// キャラクターが持つダメージ量(LvUP以外変更不可)
	private int damage;
	// キャラクターの装備によるダメージ量[MAX:40](装備ステータス以外変更不可)
	private int armorDamage;
	// キャラクターが持つ防御量(LvUP以外変更不可)
	private int hardness;
	// キャラクターの装備による防御量[MAX:40](装備ステータス以外変更不可)
	private int armorHardness;
	// キャラクターの回避率(LvUP以外変更不可)
	private int avoidance;
	// キャラクターの装備による回避率[MAX:40](装備ステータス以外変更不可)
	private int armorAvoid;
	// スキルなどのプラス効果用変数(ターン終了時、必ずリセット)
	private int damage2 = 0;
	private int hardness2 = 0;
	private int avoidance2 = 0;
	// キャラクターが持つダメージが敵の防御力に対して貫通するダメージ割合[MAX:40](装備ステータス以外変更不可)
	private int penetrate;
	// レベルアップの速さ、レベルアップによるステータスの上がり具合(変更不可)
	private int difference;
	/** キャラクターが持つ素早さ(レベルアップで変動) */
	private int quickness;
	/** 防具が持つ素早さ */
	private int armorQuickness;
	// キャラクターが持つ制限時間(装備ステータス以外変更不可)
	private int time;
	// キャラクターが持つ次のレベルまでの経験値量
	private int nextExp = 0;
	// キャラクターが持つそのレベルの最大経験値
	public int maxExp;
	// キャラクターが持つトータル経験値量
	private int totalExp = 0;
	//(10+10)-(5-(1.00-0.10))
	// マップ上のイメージタイプ
	private EnumCharacterType charaType;
	// マップ上のイメージID
	private int imageId;
	
	protected Image charaImage;
	
	
	public CharaStatus(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, int quickness, int imageId, EnumCharacterType charaType) {
		this.name = name;
		this.level = 1;
		this.health = this.maxHealth = health;
		this.skill = this.maxSkill = skillPoint;
		this.damage = damage;
		this.hardness = defence;
		this.avoidance = avoid;
		this.difference = difference;
		this.quickness = quickness;
		this.imageId = imageId;
		this.charaType = charaType;
	}
	
	public CharaStatus(CharacterStatusInfo statusInfo) {
		this(statusInfo.getName(), statusInfo.getHealth(), statusInfo.getSkillPoint(), statusInfo.getDamage(), statusInfo.getDefence(),
				statusInfo.getAvoid(), statusInfo.getDifference(), 0,statusInfo.getImageId(), statusInfo.getCharacterType());
	}
	
	/**
	 * 標準ステータス
	 * @param name
	 */
	public CharaStatus(String name) {
		this(name, 20, 8, 15, 5, 3, 50, 0, 0, EnumCharacterType.CHARACTER);
	}
	
	public void nullData() {
		this.charaImage = null;
	}
	
	public void initData() {
		
	}
	
	public void addSkill(SkillInfo info) {
		this.skillList.add(info.getSkill());
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean levelUp() {
		return addLevel(1);
	}
	
	public boolean addLevel(int level) {
		this.level += level;
		if (this.level < 1) {
			this.level = 1;
		}
		if (this.level > 99) {
			this.level = 99;
		}
		int i1 = this.maxHealth;
		int i2 = this.maxSkill;
		int i3 = this.damage;
		int i4 = this.hardness;
		int i5 = this.avoidance;
		
		this.health = this.maxHealth = 5 * this.level + 15 + (i1 * (this.difference / 10) - i1 * (this.difference / 12));
		this.skill = this.maxSkill = 5 * this.level + 5 - this.level * 2 + (i2 * (this.difference / 10) - i2 * (this.difference / 12));
		
		this.damage = 5 * this.level - (this.level * 2) + (i3 * (this.difference / 10) - i3 * (this.difference / 12));
		this.hardness = 3 * this.level - this.level / 2 - this.level + (i4 * (this.difference / 10) - i4 * (this.difference / 12));
		this.avoidance = this.level / 5 + (i5 * (this.difference / 10) - i5 * (this.difference / 12));
		
		this.nextExp = 0;
		this.maxExp += 10 * this.level * this.level + (this.level * this.level - this.level / 2);
		if (this.getLevelToSkill()) {
			return true;
		}
		return false;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void damage(int damage) {
		this.health -= damage;
		this.setHealth(this.health);
	}
	
	public void heal(int amount) {
		this.health += amount;
		this.setHealth(this.health);
	}
	
	public void setHealth(int health) {
		this.health = health;
		if (this.isDead && this.health > 0) {
			this.isDead = false;
		}
		
		if (this.health > this.maxHealth) {
			this.health = this.maxHealth;
		} else if (this.health < 0) {
			this.health = 0;
			this.isDead = true;
		}
	}
	public int getHealth() {
		return this.health;
	}
	public int getMaxHealth() {
		return this.maxHealth;
	}
	public void setSkill(int skill) {
		this.skill += skill;
		if (this.skill > this.maxSkill) {
			this.skill = this.maxHealth;
		} else if (this.skill < 0) {
			this.skill = 0;
		}
	}
	public int getSkill() {
		return this.skill;
	}
	public int getMaxSkill() {
		return this.maxSkill;
	}
	public void setDead() {
		this.health = 0;
		this.isDead = true;
	}
	public boolean isDead() {
		return this.isDead || this.health == 0;
	}
	
	public int getDamage() {
		return this.damage;
	}
	public void setArmorDamage(int armorDamage) {
		this.armorDamage = armorDamage;
		if (this.armorDamage > 40) {
			this.armorDamage = 40;
		}
	}
	public int getArmorDamage() {
		return this.armorDamage;
	}
	public int getTotalDamage() {
		return this.damage + this.damage2 + this.armorDamage;
	}
	
	public int getHardness() {
		return this.hardness;
	}
	public void setArmorHardness(int armorHardness) {
		this.armorHardness = armorHardness;
		if (this.armorHardness > 40) {
			this.armorHardness = 40;
		}
	}
	public int getArmorHardness() {
		return this.armorHardness;
	}
	public int getTotalHardness() {
		return this.hardness + this.hardness2 + this.armorHardness;
	}
	
	public void setPenetrate(int penetrate) {
		this.penetrate = penetrate;
	}
	public int getPenetrate() {
		return this.penetrate;
	}
	
	public int getAvoidancePoint() {
		return this.avoidance;
	}
	public void setArmorAvoid(int armorAvoid) {
		this.armorAvoid = armorAvoid;
		if (this.armorAvoid > 40) {
			this.armorAvoid = 40;
		}
	}
	public int getArmorAvoid() {
		return this.armorAvoid;
	}
	
	public int getTotalAvoid() {
		return this.avoidance + this.avoidance2 + this.armorAvoid;
	}
	
	public int getQuickness() {
		return this.quickness;
	}
	
	public void setArmorQuickness(int amount) {
		this.armorQuickness = amount;
	}
	
	public int getArmorQuickness() {
		return this.armorQuickness;
	}
	
	public int getTotalQuickness() {
		return this.quickness + this.armorQuickness;
	}
	
	public void setStatusPlus(int plusDamage, int plusHardness) {
		this.damage2 = plusDamage;
		this.hardness2 = plusHardness;
	}
	public void resetStatusPlus() {
		this.damage2 = 0;
		this.hardness2 = 0;
		this.avoidance2 = 0;
	}
	public int getPlusDamage() {
		return this.damage2;
	}
	public int getPlusHardness() {
		return this.hardness2;
	}
	public int getPlusAvoid() {
		return this.avoidance2;
	}
	
	public boolean hasEffect() {
		return this.damage2 != 0 || this.hardness2 != 0 || this.avoidance2 != 0;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	public int getTime() {
		return this.time;
	}
	/**
	 * 経験値を加算してレベルが上がるとtrueで返す
	 * @param exp 経験値の量
	 * @return レベルが上がったかどうか
	 */
	public boolean addExp(int exp) {
		this.totalExp += exp;
		int nextExp1 = this.nextExp + exp;
		if (nextExp1 >= this.maxExp) {
			int excessExp = nextExp1 - this.maxExp;
			do {
				levelUp();
				this.nextExp += excessExp;
				excessExp -= this.maxExp;
			} while (this.maxExp <= this.nextExp);
			return true;
		} else {
			this.nextExp += exp;
			return false;
		}
	}
	public int getNextExp() {
		return this.maxExp - this.nextExp;
	}
	public int getTotalExp() {
		return this.totalExp;
	}
	protected boolean getLevelToSkill() {
		for (Skill s : this.skillList) {
			if (this.level >= s.getTermsLevel()) {
				if (!s.active) {
					s.active = true; // TODO スキル解放時の言葉がない
				}
			}
		}
		return false;
	}
	
	public EnumCharacterType getCharacterType() {
		return this.charaType;
	}
	
	public int getImageId() {
		return this.imageId;
	}
	
	/* -------------------- DEBUG START ------------------------- */
	
	/**
	 * スキルが解放されている数を返します。
	 * @return
	 */
	public int getActiveSkills() {
		int stack = 0;
		for (Skill skill : this.skillList) {
			if (skill.active) {
				stack++;
			}
		}
		return stack;
	}
	
	/* -------------------- DEBUG END ------------------------- */
}
