package game.client.chara;

import game.client.map.chara.EnumCharacterType;
import game.common.inventory.slot.TMSlot;

public class BattleChara extends CharaStatus {
	private static final long serialVersionUID = 1L;
	
	private TMSlot handWeapon;
	private TMSlot outerWearWeapon;
	private TMSlot underWearWeapon;
	
	public BattleChara(String name, int health, int skillPoint, int damage, int defence, int avoid, int difference, int quickness,
			int imageId, EnumCharacterType charaType) {
		super(name, health, skillPoint, damage, defence, avoid, difference, quickness, imageId, charaType);
		
		this.handWeapon = new TMSlot();
		this.outerWearWeapon = new TMSlot();
		this.underWearWeapon = new TMSlot();
	}
	
	public BattleChara(String name) {
		this(name, 20, 8, 15, 5, 3, 50, 2, 0, EnumCharacterType.CHARACTER);
	}
	
	public void setHandWeapon(TMSlot otherSlot) {
		this.handWeapon.changeItemInSlot(otherSlot);
	}
	
	public void setOuterWearWeapon(TMSlot otherSlot) {
		this.outerWearWeapon.changeItemInSlot(otherSlot);
	}
	
	public void setUnderWearWeapon(TMSlot otherSlot) {
		this.underWearWeapon.changeItemInSlot(otherSlot);
	}
}