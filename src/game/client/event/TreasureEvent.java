package game.client.event;

import game.client.item.Item;
import game.client.item.TMItems;

public class TreasureEvent extends Event {
	private static final long serialVersionUID = 1L;
	// 宝箱に入っているアイテム
    private int itemId;
    private int stack;
    /**
     * 宝箱によるアイテムトレジャーの追加
     * @param x X座標
     * @param y Y座標
     * @param itemId 手に入るアイテム
     * @param stack アイテムの個数
     */
    public TreasureEvent(int x, int y, int itemId, int stack) {
        // 宝箱のチップ番号は12で必ずぶつかる
        super(x, y, 15, true);
        this.itemId = itemId;
        this.stack = stack;
    }
    /**
     * ハンディトレジャーの登録
     * @param x X座標
     * @param y Y座標
     * @param itemId 手に入るアイテム
     */
    public TreasureEvent(int x, int y, int itemId) {
        super(x, y, 25, false);
        this.itemId = itemId;
        this.stack = 1;
    }
    /**
     * アイテム名を返す
     */
    public String getItemName() {
        Item item = TMItems.getItemById(itemId);
        return item.getName();
    }
    /**
     * アイテムの番号を返す
     */
    public int getItemId() {
    	return itemId;
    }
    /**
     * アイテムの数を返す
     */
    public int getItemStack() {
    	return stack;
    }
    /**
     * イベントを文字列に変換（デバッグ用）
     */
    public String toString() {
        return "TREASURE:" + super.toString() + ":" + getItemName();
    }
}