package game.client.chara.enemy;

import java.util.List;

import org.newdawn.slick.Image;

import game.client.chara.BattleEnemy;
import game.client.chara.ai.EnemyAIBase;
import game.client.chara.skill.SkillInfo;
import game.client.image.BattleImageInfo;
import game.client.image.ImageUtils;

public class EnemyGoblin extends BattleEnemy {
	private static final long serialVersionUID = 1L;
	
	public EnemyGoblin(EnumGoblinType gType) {
		super(EnemyInfo.GOBLIN, gType);
		this.setImageScale(2.0F);
		this.addSkill(SkillInfo.HIT);
		this.addSkill(SkillInfo.KICK);
	}
	
	@Override
	public void initAITask(List<EnemyAIBase> list) {
	}

	@Override
	public Image setImage() {
		switch ((EnumGoblinType)this.getObjectType()) {
		case NORMAL:
			return ImageUtils.getBattleCharaImage(BattleImageInfo.GOBLIN);
		case WAR:
			return ImageUtils.getBattleCharaImage(BattleImageInfo.GOBLIN_WAR);
		case MAGIC:
			return ImageUtils.getBattleCharaImage(BattleImageInfo.GOBLIN_MAGIC);
		default:
			return ImageUtils.getBattleCharaImage(BattleImageInfo.GOBLIN);
		}
	}
}
