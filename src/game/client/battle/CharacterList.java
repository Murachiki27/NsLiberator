package game.client.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.client.chara.CharaStatus;
import game.client.chara.skill.Skill;
import game.utils.TMRandom;

/**
 * 
 * バトル時のキャラクター配列順序を持ったキャラクターリスト<p>
 * 
 * 
 * @author Murachiki27
 *
 * @param <C> CharaStatusを継承したもの
 * @see game.client.chara.CharaStatus
 */
public class CharacterList<C extends CharaStatus> {
	
	protected int currentCharaIndex;
	
	protected Random rand;
	
	private List<C> list = new ArrayList<>();
	
	public CharacterList() {
		this.rand = new TMRandom();
	}
	
	/**
	 * 
	 * リストにキャラクターを追加します。<p>
	 * 
	 * これは、キャラクターの素早さに影響されるもので、<br>
	 * リストにいるキャラクターより素早さが上だった場合は、<br>
	 * そのキャラクターの手前に追加する。<br>
	 * 
	 * @param chara
	 */
	public void add(C chara) {
		this.addForQuickness(chara);
	}
	
	public void clear() {
		this.list.clear();
	}
	
	public void change(int index, C chara) {
		this.list.set(index, chara);
	}
	
	public void damage(int index, int amount) {
		this.list.get(index).damage(amount);
	}
	
	public void heal(int index, int amount) {
		this.list.get(index).heal(amount);
	}
	
	/**
	 * 
	 * 現在行動中のキャラクターを次のキャラクターへ交代します。<p>
	 * 
	 * もし、次の交代キャラクターが戦闘不能な場合は、その次へと繰り返します。<br>
	 * もし、交代キャラクターがその後に存在しない場合にはfalseで返されます。
	 * 
	 * @return 次の交代キャラクターが存在するか
	 */
	public boolean next() {
		if (!this.list.isEmpty()) {
			do {
				this.currentCharaIndex++;
				if (this.currentCharaIndex >= this.list.size()) {
					this.setFirstIndex();
					return false;
				}
			} while (this.list.get(this.currentCharaIndex).isDead());
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 指定したキャラクター(リスト内)のHPを変更します。<p>
	 * 
	 * もし、回避率が一定の時に確率で動作した場合、falseが返される
	 * 
	 * @param index
	 * @param otherStatus
	 * @param skill
	 * @param plusAmount
	 * @return
	 */
	public boolean set(int index, CharaStatus attackOwner, Skill ownerSkill, int plusAmount) {
		int skillAmount = ownerSkill.getSkillAmount(); // スキルによる効果量
		int totalAmount = 0; // 自分が受ける量
		float avoid = this.getStatus(index).getTotalAvoid();
		
		switch (ownerSkill.getType()) {
		case ATTACK_ALL:
		case ATTACK_ONE:
		case MAGIC_ATC_ALL:
		case MAGIC_ATC_ONE:
			int defDamage = attackOwner.getDamage(); // 相手が持つ基礎ダメージ
			int armDamage = 0; // 相手の装備しているアーマーが持つダメージ
			int defence = this.getStatus(index).getHardness(); // キャラクターが持つ防御量
			float penetrate = this.getStatus(index).getPenetrate() * 0.01F; // キャラクターがダメージ貫通する割合(100で割った値)
			totalAmount = (int) ((defDamage + skillAmount + armDamage + plusAmount) - (defence - (1.0F - penetrate)));
			if (totalAmount < 1) totalAmount = 1;
			
			if ((avoid / 100) > this.rand.nextFloat()) {
				return false;
			}
			this.getStatus(index).damage(totalAmount);
			break;
		case HEAL_ALL:
		case HEAL_ONE:
			totalAmount = skillAmount + plusAmount;
			this.getStatus(index).heal(totalAmount);
			break;
		default:
			break;
		}
		return true;
	}
	
	@Deprecated
	public void damageAll(CharaStatus otherStatus, Skill skill, int plusAmount) {
		for (int i = 0; i < this.list.size(); i++) {
			if (!this.list.get(i).isDead()) {
				this.set(i, otherStatus, skill, plusAmount);
			}
		}
	}
	
	public void healAll(int amount) {
		for (C chara : this.list) {
			chara.heal(amount);
		}
	}
	
	public void setFirstIndex() {
		this.currentCharaIndex = this.getFirstAliveIndex();
	}
	
	public int getFirstAliveIndex() {
		for (int i = 0; i < this.list.size(); i++) {
			if (!this.list.get(i).isDead()) {
				return i;
			}
		}
		return -1;
	}
	
	public int getCurrentCharaIndex() {
		return this.currentCharaIndex;
	}
	
	/**
	 * 
	 * 現在行動中のキャラクターが他のキャラクターの素早さと比較して行動できるかを返します。<p>
	 * 
	 * パラメーター:キャラクターの素早さと現在のリストキャラの素早さを比較し、<br>
	 * リストキャラの方が上だった場合はtrue、<br>
	 * 同じだった場合は、ランダムな結果、<br>
	 * それ以外は、falseを返します。
	 * 
	 * @param otherStatus
	 * @return
	 */
	public boolean canActionQuickness(CharaStatus otherStatus) {
		int quickness = this.list.get(this.currentCharaIndex).getTotalQuickness();
		if (quickness > otherStatus.getTotalQuickness()) {
			return true;
		} else if (quickness == otherStatus.getTotalQuickness()) {
			return this.rand.nextBoolean();
		} else {
			return false;
		}
	}
	
	
	public C getStatus() {
		return this.getStatus(this.currentCharaIndex);
	}
	
	public C getStatus(int index) {
		return this.list.get(index);
	}
	
	public Skill getSkill(int index) {
		return this.list.get(this.currentCharaIndex).skillList.get(index);
	}
	
	private void addForQuickness(C chara) {
		if (this.list.isEmpty()) {
			this.list.add(chara);
		} else {
			int index = this.list.size();
			for (int i = 0; i < this.list.size(); i++) {
				int quickness = this.list.get(i).getTotalQuickness();
				if (quickness < chara.getTotalQuickness()) {
					index = i;
					break;
				}
			}
			this.list.add(index, chara);
		}
	}
	
}
