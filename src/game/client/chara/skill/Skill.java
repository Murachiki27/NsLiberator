package game.client.chara.skill;

import java.io.Serializable;

/**
 * キャラクターが扱うスキルの基礎定義クラス
 * 
 * @author Murachiki27
 *
 */
public class Skill implements Serializable  {
	private static final long serialVersionUID = 1L;
	// スキルが使えるかどうか
	public boolean active = false;
	// スキルの名前
	private String name;
	// スキルのタイプ
	private EnumSkillType skillType;
	// スキルのレベル条件
	private int termsLevel = 0;
	// スキルで消費するSP
	private int expenseSP;
	// スキルによる効果量
	private int skillAmount;
	// スキルの説明
	private String description;
	// キャラクターが強制的にターゲット指定する番号 -1ならランダム指定
	private int forcedSelect = -1;
	
	
	/**
	 * 
	 * @param name スキル名
	 * @param skillType スキルのタイプ
	 * @param amount 効果の量
	 */
	public Skill(String name,EnumSkillType skillType, int amount) {
		this(name, skillType, 0, amount, 0, null);
	}
	
	/**
	 * スキルを生成
	 * @param name スキル名
	 * @param termsLevel 必要なレベル
	 * @param amount 効果の量
	 * @param description スキルの説明
	 */
	public Skill(String name,int termsLevel, int amount, String description) {
		this(name, EnumSkillType.ATTACK_ONE, termsLevel, amount, 0, description);
	}
	
	/**
	 * スキルを生成
	 * @param name スキル名
	 * @param skillType スキルのタイプ
	 * @param termsLevel 必要なレベル
	 * @param amount 効果の量
	 * @param expenseSP 必要なSPの量
	 * @param description スキルの説明
	 */
	public Skill(String name, EnumSkillType skillType,int termsLevel, int amount, int expenseSP, String description) {
		this.name = name;
		this.skillType = skillType;
		this.termsLevel = termsLevel;
		this.skillAmount = amount;
		this.expenseSP = expenseSP;
		this.description = description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public EnumSkillType getType() {
		return this.skillType;
	}
	
	public int getTermsLevel() {
		return this.termsLevel;
	}
	
	public int getExpenseSP() {
		return this.expenseSP;
	}
	
	public int getSkillAmount() {
		return this.skillAmount;
	}
	
	public String getDescription() {
		return this.description + "\n消費SP : " + this.expenseSP;
	}
	
	public int getTargetId() {
		return forcedSelect;
	}
}
