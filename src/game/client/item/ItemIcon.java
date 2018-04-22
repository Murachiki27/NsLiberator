package game.client.item;

import java.util.ArrayList;
import java.util.List;

import game.common.image.AnimationSheet;

public class ItemIcon {
	private List<AnimationSheet> itemIcon = new ArrayList<AnimationSheet>();
	
	public ItemIcon() {
	}
	public void draw(int no, int x, int y) {
		draw(no, x, y, 32, 32, 1);
	}
	public void draw(int no, int x, int y, int width, int height, int scale) {
		itemIcon.get(no).draw(x, y, width * scale, height * scale);
	}
	public int getWidth(int index) {
		return itemIcon.get(index).getWidth();
	}
	public int getHeight(int index) {
		return itemIcon.get(index).getHeight();
	}
	public int getSize()  {
		return itemIcon.size();
	}
}
