package game.client.battle;

import game.client.chara.BattleEnemy;
import game.client.chara.skill.Skill;

public class EnemyList<C extends BattleEnemy> extends CharacterList<C> {
	
	
	public boolean firstEventUpdate(int turn) {
		return this.getStatus().beforeEventUpdate(turn);
	}
	
	public void update() {
		this.getStatus().onUpdate();
	}
	
	public boolean endEventUpdate(int turn) {
		return this.getStatus().afterEventUpdate(turn);
	}
	
	public Skill getSkill() {
		return this.getStatus().getUseSkill();
	}
}
