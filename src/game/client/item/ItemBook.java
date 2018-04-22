package game.client.item;

public class ItemBook extends Item {
	private static final long serialVersionUID = 1L;

	public ItemBook() {
		super("tm:item_book");
		this.maxStackSize = 5;
	}
}
