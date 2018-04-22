package game.client.gui;

import game.client.chara.BattleChara;

public abstract class AbstarctCharaStatus extends TMGuiScreen {
	
	private BattleChara charaOwner;
	// キャラクターネーム
	protected String name;
	// 体力値
	protected int life;
	protected int maxlife;
	// レベル
	protected int lv;
	// スキルポイント
	protected int skill;
	protected int maxskill;
	
	public AbstarctCharaStatus(BattleChara chara) {
		this.charaOwner = chara;
		this.name = chara.getName();
		this.lv = chara.getLevel();
		this.life = chara.getHealth();
		this.maxlife = chara.getMaxHealth();
		this.skill = chara.getSkill();
		this.maxskill = chara.getMaxSkill();
	}
	
	public void update() {
		this.lv = this.charaOwner.getLevel();
		this.life = this.charaOwner.getHealth();
		this.maxlife = this.charaOwner.getMaxHealth();
		this.skill = this.charaOwner.getSkill();
		this.maxskill = this.charaOwner.getMaxSkill();
	}
}
