package game.client.item;

public class ItemFood extends Item {
	private static final long serialVersionUID = 1L;

	public ItemFood(int healHP, int healSP) {
		super("tm:item_food");
		this.setHealHP(healHP);
		this.setHealSP(healSP);
	}
}
