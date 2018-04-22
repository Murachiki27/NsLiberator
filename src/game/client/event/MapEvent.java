package game.client.event;

public class MapEvent extends Event {
	private static final long serialVersionUID = 1L;
	// 移動先のマップ番号
    public int destMapNo;
    // プレイヤーの向き
    public int dir;
    // 移動先のX座標
    public int destX;
    // 移動先のY座標
    public int destY;
    
    public MapEvent(int x, int y, int chipNo, int destMapNo, int dir, int destX, int destY) {
        // 上に乗ると移動するようにしたいのでぶつからない（false）に設定
        super(x, y, chipNo, false);
        this.destMapNo = destMapNo;
        this.dir = dir;
        this.destX = destX;
        this.destY = destY;
    }
    
    public String toString() {
        return "MAP:" + super.toString() + ":" + destMapNo + ":" + dir + ":" + destX + ":" + destY;
    }
}
