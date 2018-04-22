package game.client.item;

import java.util.LinkedList;
import java.util.List;

public class TMItems {
	public final int MAX_ITEM = 64;
	public int rate;
	
	private static final List<Item> itemList = new LinkedList<Item>();
	public static final int herb = 0;
	public static final int redherb = 1;
	public static final int herb_juice = 2;
	public static final int redherb_juice = 3;
	public static final int key = 4;
	
	public TMItems() {
	}
	
	public boolean loadItem() {
		registerItem(herb, new ItemFood(5,0).setName("ハーブ"));
		registerItem(redherb, new ItemFood(5,0).setName("赤ハーブ"));
		registerItem(herb_juice, new ItemFood(5,0).setName("ハーブジュース"));
		registerItem(redherb_juice, new ItemFood(100,50).setName("赤ハーブジュース"));
		registerItem(key, new ItemKey("door_key").setName("あやしいカギ"));
		return true;
	}
	
	private void registerItem(int itemId, Item item) {
		itemList.add(itemId, item);
		rate++;
	}
	
	public static Item getItemById(int itemId) {
		return itemList.get(itemId);
	}
	
	public static Item getItemByTag(String itemTag) {
		for (Item item : itemList) {
			if (item.getItemTag().equals(itemTag)) {
				return item;
			}
		}
		return null;
	}
}
