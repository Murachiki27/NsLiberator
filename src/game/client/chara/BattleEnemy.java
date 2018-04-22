package game.client.chara;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.client.chara.ai.EnemyAIBase;
import game.client.chara.enemy.EnemyInfo;
import game.client.chara.enemy.IObjectType;
import game.client.map.chara.EnumCharacterType;

public abstract class BattleEnemy extends EnemyStatus {
	private static final long serialVersionUID = 1L;
	
	private List<EnemyAIBase> task = new ArrayList<EnemyAIBase>();
	private IObjectType objType;
	private String commandName;
	
	private float scale = 1.0F;
	
	private float spawnPercent = 0.5F;
	
	private String message;
	
	public BattleEnemy(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, int quickness, int imageId, EnumCharacterType charaType) {
		super(name, health, skillPoint, damage, defence, avoid, difference, quickness, imageId, charaType);
		this.initAITask(this.task);
		this.charaImage = this.setImage();
	}
	
	
	public BattleEnemy(EnemyInfo info, IObjectType obj) {
		super(info.getStatusInfo());
		this.objType = obj;
		this.commandName = info.getCommandName();
		this.exp = info.getEXP();
		this.addLevel(info.getLevel());
		this.initAITask(this.task);
		this.charaImage = this.setImage();
	}
	
	public BattleEnemy(EnemyInfo info) {
		this(info, null);
	}
	
	@Override
	public void nullData() {
		super.nullData();
	}
	
	@Override
	public void initData() {
		this.charaImage = this.setImage();
	}
	
	public abstract void initAITask(List<EnemyAIBase> list);
	
	public abstract Image setImage();
	
	public EnemyAIBase hasExecAI() {
		EnemyAIBase taskAI = null;
		if (this.task != null) {
			boolean flag = false;
			for (EnemyAIBase ai : this.task) {
				if (!flag && ai.startAI()) {
					taskAI = ai;
					flag = true;
				} else {
					if (!ai.alreadyReset()) {
						ai.resetAI();
					}
				}
			}
		}
		return taskAI;
	}
	
	public void setImageScale(float scale) {
		this.scale = scale;
	}
	
	@Override
	public void onUpdate() {
		EnemyAIBase aiTask = this.hasExecAI();
		if (aiTask != null) {
			this.updateAITask(aiTask);
		} else {
			super.onUpdate();
		}
	}
	
	protected void updateAITask(EnemyAIBase aiTask) {
		int index = aiTask.setSkillIndex();
		if (index < 0) {
			index = 0;
		}
		this.currentSkill = this.skillList.get(index);
	}
	
	
	public boolean beforeEventUpdate(int turn) {
		return true;
	}
	
	public boolean afterEventUpdate(int turn) {
		return true;
	}
	
	
	
	public void drawChara(float px, float py, Graphics g) {
		if (charaImage != null) {
			charaImage.draw(px, 240 - charaImage.getHeight() * this.scale / 2, (int) (charaImage.getWidth() * this.scale), (int) (charaImage.getHeight() * this.scale));
		}
	}
	
	public int getWidth() {
		return this.charaImage.getWidth();
	}
	public int getHeight() {
		return this.charaImage.getHeight();
	}
	
	public float getScale() {
		return this.scale;
	}
	
	protected IObjectType getObjectType() {
		return this.objType;
	}
	
	protected void setCommandTag(String tag) {
		this.commandName = tag;
	}
	
	public String getCommandTag() {
		return this.commandName;
	}
	
	public Image getImage() {
		return this.charaImage;
	}
	
	protected void setSpawnPercent(float spawnPercent) {
		this.spawnPercent = spawnPercent;
	}
	
	public float getSpawnPercent() {
		return this.spawnPercent;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
