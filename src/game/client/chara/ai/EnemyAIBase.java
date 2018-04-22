package game.client.chara.ai;

import game.client.chara.BattleEnemy;

/**
 * 敵の行動パターン処理(AI)の基礎定義クラス<p>
 * 
 * AIではスキルに関する細かな設定(ターゲットなど)は
 * 行わず、具体的にどのタイミングでスキルを使うかについて
 * 定義するものとする。
 * 
 * @author Murachiki27
 *
 */
public abstract class EnemyAIBase {
	
	protected BattleEnemy ownerEnemy;
	
	protected boolean hasReset;
	
	public AIBaseType type;
	
	/**
	 * 敵がAIを開始するかどうか
	 * @return
	 */
	public abstract boolean startAI();
	
	/**
	 * 行動をリセットする
	 */
	public abstract void resetAI();
	
	/*
	 * スキルリストからスキルをセットするための番号を返す
	 */
	public abstract int setSkillIndex();
	
	public boolean alreadyReset() {
		return this.hasReset;
	}
}
