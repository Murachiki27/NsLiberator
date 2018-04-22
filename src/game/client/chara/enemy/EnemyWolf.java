package game.client.chara.enemy;

import java.util.List;

import org.newdawn.slick.Image;

import game.client.chara.BattleEnemy;
import game.client.chara.ai.EnemyAIBase;
import game.client.chara.skill.SkillInfo;
import game.client.image.BattleImageInfo;
import game.client.image.ImageUtils;

public class EnemyWolf extends BattleEnemy {
	private static final long serialVersionUID = 1L;

	public EnemyWolf() {
		super(EnemyInfo.WOLF);
		this.setImageScale(2.0F);
		this.addSkill(SkillInfo.HIT);
	}

	@Override
	public void initAITask(List<EnemyAIBase> list) {
	}

	@Override
	public Image setImage() {
		return ImageUtils.getBattleCharaImage(BattleImageInfo.WOLF);
	}
}
