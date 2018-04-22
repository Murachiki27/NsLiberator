package game.client.chara;

import java.util.List;

import org.newdawn.slick.Image;

import game.client.chara.ai.EnemyAIBase;
import game.client.image.ImageUtils;
import game.client.map.chara.EnumCharacterType;

public class BattleStatusObject extends BattleEnemy {
	private static final long serialVersionUID = 1L;
	
	public BattleStatusObject(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, int quickness, int imageId, EnumCharacterType charaType) {
		super(name, health, skillPoint, damage, defence, avoid, difference, quickness, imageId, charaType);
	}

	@Override
	public void initAITask(List<EnemyAIBase> list) {
	}

	@Override
	public Image setImage() {
		return null;
	}
	
	public void setLevel(int level) {
		if (level > 0) {
			this.addLevel(level - 1);
		}
	}
	
	public void setTag(String tag) {
		this.setCommandTag(tag);
	}
	
	public void setExp(int exp) {
		this.exp = exp;
	}
	
	public void setImagePath(String path) {
		this.charaImage = ImageUtils.getDebugBattleImage(path);
	}
}
