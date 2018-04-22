package game.client.item;

import java.io.Serializable;

import game.client.map.chara.TMPlayer;

public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	// アイテムID
	protected static int itemID;
	
	private final String itemTag;
	
	// アイテムの名前
	private String itemName;
	// アイテムのアイコン
	
	// アイテムのスタック数
	private int stackSize;
	// アイテムの最大スタック数
	protected int maxStackSize = 99;
	
	// アイテムの最大ダメージ数
	private int maxdamage;
	// アイテムの最大防御力
	private int maxhardness;
	// アイテムのHP回復量
	private int healHP;
	// アイテムのSP回復量
	private int healSP;
	
	public Item(String itemTag) {
		this.itemTag = itemTag;
	}
	
	public Item setName(String name) {
		this.itemName = name;
		return this;
	}
	
	public Item setMaxDamage(int damage) {
		this.maxdamage = damage;
		return this;
	}
	
	public Item setMaxHardness(int hardness) {
		this.maxhardness = hardness;
		return this;
	}
	
	public Item setHealHP(int heal) {
		this.healHP = heal;
		return this;
	}
	
	public Item setHealSP(int heal) {
		this.healSP = heal;
		return this;
	}
	
	public Item copy() {
		Item newItem = new Item(this.itemTag).setName(this.itemName);
		newItem.maxStackSize = this.maxStackSize;
		if (this.maxdamage > 0) newItem.setMaxDamage(this.maxdamage);
		if (this.maxhardness > 0) newItem.setMaxHardness(this.maxhardness);
		if (this.healHP > 0) newItem.setHealHP(this.healHP);
		if (this.healSP > 0) newItem.setHealSP(this.healSP);
		return newItem;
	}
	
	public String getItemTag() {
		return this.itemTag;
	}
	
	public String getName() {
		return this.itemName;
	}
	
	public boolean addStackInItem(int value) {
		int i = this.stackSize;
		if (value <= 0) {
			return false;
		} else {
			if ((value + i) <= maxStackSize) {
				do {
					++this.stackSize;
				} while (this.stackSize < (value + i) && stackSize < maxStackSize);
				return true;
			}
			return false;
		}
	}
	
	public boolean removeStackInItem(int value) {
		int i = this.stackSize;
		if (value <= 0) {
			return false;
		} else {
			if (stackSize > 0) {
				do {
					--this.stackSize;
				} while (this.stackSize > (i - value) && stackSize > 0);
			}
			return true;
		}
	}
	
	public boolean onItemUse(TMPlayer player) {
		return false;
	}
	
	public int getStackSize() {
		return this.stackSize;
	}
	
	
	public int getMaxStackSize() {
		return this.maxStackSize;
	}
	
	public int getDamage() {
		return maxdamage;
	}
	public int getHardness() {
		return maxhardness;
	}
	public int getHealHp() {
		return healHP;
	}
	public int getHealSp() {
		return healSP;
	}
}
