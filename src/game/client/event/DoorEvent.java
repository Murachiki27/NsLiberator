package game.client.event;

public class DoorEvent extends Event {
	private static final long serialVersionUID = 1L;

	/**
     * @param x X座標
     * @param y Y座標
     */
    public DoorEvent(int x, int y) {
        // とびらのチップ番号は18でぶつかる
        super(x, y, 18, true);
    }
    
    /**
     * イベントを文字列に変換（デバッグ用）
     */
    public String toString() {
        return "DOOR:" + super.toString();
    }
}
