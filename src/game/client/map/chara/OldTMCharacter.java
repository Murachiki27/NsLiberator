package game.client.map.chara;

import java.io.Serializable;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.client.map.AbstractMap;
import game.client.map.Common;



/**
 * 
 * マップ上のキャラクタークラス
 * 
 * @deprecated ver.2.1よりキャラクター機能を新しくしたため、廃止されました。
 * @see game.client.map.chara.TMCharacter
 * @author Murachiki27
 *
 */
@Deprecated
public class OldTMCharacter implements Serializable, Common {
	private static final long serialVersionUID = 1L;

	// 移動スピード
    private int speed = 4;

    // 移動確率
    public static final double PROB_MOVE = 0.02;

    // イメージ
    private static Image charaImage;
    // キャラクターのID(マップ内で被らないように配置する)
    private int charaId;
    // キャラクター番号
    private int charaNo;
    // キャラクターの名前
    private String name;

    // 座標
    protected int x, y;   // 単位：マス
    protected int px, py; // 単位：ピクセル

    // 向いている方向（LEFT,RIGHT,UP,DOWNのどれか）
    protected int direction;
    // アニメーションカウンタ
    public float timer;
    private int[] animation = new int[] {0, 1, 0, 2};
    public int count;
    
    // イベント専用キャラクターならTRUE
    private boolean isEventChara = false;
	// オブジェクトの種類
	public String objectType;
    
    //  移動中（スクロール中）か
	protected boolean isMoving;
    // 向いてる方向と反対に進むか(後ずさり用)
	protected boolean isBackingDir = false;
    
    private boolean isKeepMove;
    //  移動中の場合の移動ピクセル数
    private int movingLength;
    // 移動するマスの数
    private int moveTiles = 1;
    // 強制歩行させたときのみtrueで返す
    private boolean forcedMoving;

    // 移動方法
    private int moveType;
    // はなすメッセージ
    private String message;
    
    // マップへの参照
    protected AbstractMap map;
    
    public OldTMCharacter(int x, int y, int charaId,int charaNo, String name, String objectType, int direction, AbstractMap map) {
    	this(x, y, charaId, charaNo, name, direction, 0, map);
    	this.isEventChara = true;
    	this.objectType = objectType;
    }

    public OldTMCharacter(int x, int y, int charaId, int charaNo, String name, int direction, int moveType, AbstractMap map) {
        this.x = x;
        this.y = y;

        px = x * CHIPSIZE;
        py = y * CHIPSIZE;
        
        this.charaId = charaId;
        this.charaNo = charaNo;
        this.name = name;
        this.direction = direction;
        this.count = 0;
        this.moveType = moveType;
        this.map = map;

        // 初回の呼び出しのみイメージをロード
        if (charaImage == null) {
            loadImage();
        }
    }

    public void draw(Graphics g, float offsetX, float offsetY) {
        int cx = (charaNo % 8) * (CHIPSIZE * 3);
        int cy = (charaNo / 8) * (CHIPSIZE * 4);
        // countとdirectionの値に応じて表示する画像を切り替える
        g.drawImage(charaImage, px + offsetX, py + offsetY, px + offsetX + CHIPSIZE, py + offsetY + CHIPSIZE, cx + animation[count] * CHIPSIZE, cy + direction * CHIPSIZE, cx + CHIPSIZE + animation[count] * CHIPSIZE, cy + direction * CHIPSIZE + CHIPSIZE);
    }
    
    public void update(int delta) {
    	if (isKeepMove) {
        	timer += delta * (0.001 * speed);
        	if (timer >= 1.0F) {
        		timer = 0;
        		count += 1;
        		if (count >= animation.length) {
        			count = 0;
        		}
        	}
    	} else {
    		timer = 0;
			count = 0;
    	}
    }

    /**
     * 移動処理。 
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    public boolean move() {
    	if (isBackingDir) {
            switch (direction) {
            case LEFT:
                if (moveRight()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case RIGHT:
                if (moveLeft()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case UP:
                if (moveDown()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case DOWN:
                if (moveUp()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            default:
            	return true;
            }
    	} else {
            switch (direction) {
            case LEFT:
                if (moveLeft()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case RIGHT:
                if (moveRight()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case UP:
                if (moveUp()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            case DOWN:
                if (moveDown()) {
                	if (!forcedMoving) {
                    	reset();
                	}
                    // 移動が完了した
                    return true;
                }
                break;
            default:
            	return true;
            }
    	}
        // 移動が完了していない
        return false;
    }

    /**
     * 左へ移動する。
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    private boolean moveLeft() {
        // 1マス先の座標
        int nextX = x - 1 * moveTiles;
        int nextY = y;
        if (nextX < 0) {
            nextX = 0;
        }
        // その場所に障害物がなければ移動を開始
        if (!map.isHit(nextX, nextY)) {
            // SPEEDピクセル分移動
            px -= speed;
            if (px < 0) {
                px = 0;
            }
            // 移動距離を加算
            movingLength += speed;
            // 移動が1マス分を超えていたら
            if (movingLength >= CHIPSIZE * moveTiles) {
                // 移動する
                x -= (1 * moveTiles);
                px = x * CHIPSIZE;
                // 移動が完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            // 元の位置に戻す
            px = x * CHIPSIZE;
            py = y * CHIPSIZE;
        }
        
        return false;
    }

    /**
     * 右へ移動する。
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    private boolean moveRight() {
        // 1マス先の座標
        int nextX = x + 1 * moveTiles;
        int nextY = y;
        if (nextX > map.getCol() - 1) {
            nextX = map.getCol() - 1;
        }
        // その場所に障害物がなければ移動を開始
        if (!map.isHit(nextX, nextY)) {
            // SPEEDピクセル分移動
            px += speed;
            if (px > map.getWidth() - CHIPSIZE) {
                px = map.getWidth() - CHIPSIZE;
            }
            // 移動距離を加算
            movingLength += speed;
            // 移動が1マス分を超えていたら
            if (movingLength >= CHIPSIZE * moveTiles) {
                // 移動する
                x += (1 * moveTiles);
                px = x * CHIPSIZE;
                // 移動が完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            px = x * CHIPSIZE;
            py = y * CHIPSIZE;
        }
        
        return false;
    }

    /**
     * 上へ移動する。
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    private boolean moveUp() {
        // 1マス先の座標
        int nextX = x;
        int nextY = y - 1;
        if (nextY < 0) {
            nextY = 0;
        }
        // その場所に障害物がなければ移動を開始
        if (!map.isHit(nextX, nextY)) {
            // SPEEDピクセル分移動
            py -= speed;
            if (py < 0) py = 0;
            // 移動距離を加算
            movingLength += speed;
            // 移動が1マス分を超えていたら
            if (movingLength >= CHIPSIZE * moveTiles) {
                // 移動する
                y -= (1 * moveTiles);
                py = y * CHIPSIZE;
                // 移動が完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            px = x * CHIPSIZE;
            py = y * CHIPSIZE;
        }
        
        return false;
    }

    /**
     * 下へ移動する。
     * @return 1マス移動が完了したらtrueを返す。移動中はfalseを返す。
     */
    private boolean moveDown() {
        // 1マス先の座標
        int nextX = x;
        int nextY = y + 1;
        if (nextY > map.getRow() - 1) {
            nextY = map.getRow() - 1;
        }
        // その場所に障害物がなければ移動を開始
        if (!map.isHit(nextX, nextY)) {
            // SPEEDピクセル分移動
            py += speed;
            if (py > map.getHeight() - CHIPSIZE) {
                py = map.getHeight() - CHIPSIZE;
            }
            // 移動距離を加算
            movingLength += speed;
            // 移動が1マス分を超えていたら
            if (movingLength >= CHIPSIZE * moveTiles) {
                // 移動する
                y += (1 * moveTiles);
                py = y * CHIPSIZE;
                // 移動が完了
                isMoving = false;
                return true;
            }
        } else {
            isMoving = false;
            px = x * CHIPSIZE;
            py = y * CHIPSIZE;
        }
        
        return false;
    }
    
    private void loadImage() {
        try {
			charaImage = new Image("img/chara1.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }
    public static Image getCharaImage() {
    	return charaImage;
    }
    
    public int getSpeed() {
    	return speed;
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getPx() {
        return px;
    }
    
    public int getPy() {
        return py;
    }
    
    public void setSpeed(int speed) {
    	this.speed = speed;
    }
    
    public void setPosition(int x, int y) {
    	this.x = x;
    	this.y = y;
    }
    
    public int getCharaId() {
    	return this.charaId;
    }
    
    public int getCharaNo() {
    	return charaNo;
    }
    
    public void setCharaNo(int charaNo) {
    	this.charaNo = charaNo;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public int getDirection() {
        return direction;
    }

    public void setDirection(int dir) {
        direction = dir;
    }
    
    public boolean getTalkEventChara() {
    	return this.isEventChara;
    }
    
    public String getObjectType() {
    	return this.objectType;
    }
    
    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean flag) {
        isMoving = flag;
    	if (flag && !isKeepMove) {
    		count = 1;
    	}
    	isKeepMove = flag;
        // 移動距離を初期化
        movingLength = 0;
    }
    
    public void setMovingforPostion(boolean flag, int tiles, int speedable) {
        isMoving = flag;
    	if (flag && !isKeepMove) {
    		count = 1;
    	}
    	isKeepMove = flag;
        moveTiles = tiles;
        setSpeed(speedable);
        // 移動距離を初期化
        movingLength = 0;
        forcedMoving = true;
    }
    public void setEndKeepMoving() {
    	isKeepMove = false;
    }
    
    public void setBackingDir(boolean isBackingDir) {
    	this.isBackingDir = isBackingDir;
    }
    
    public boolean getEndPostion() {
    	return (movingLength >= CHIPSIZE * moveTiles && forcedMoving) || moveTiles == 0;
    }
    
    public int getMovingLength() {
    	return movingLength;
    }
    public void setMovingLength(int length) {
    	movingLength = length;
    }
    
    public void reset() {
    	if (forcedMoving) {
        	forcedMoving = false;
            moveTiles = 1;
            speed = 4;
            isBackingDir = false;
    	}
    }

    /**
     * キャラクターのメッセージを返す
     * @return メッセージ
     */
    public String getMessage() {
        return message;
    }

    /**
     * キャラクターのメッセージを返す
     * @param message メッセージ
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    public AbstractMap getMap() {
    	return this.map;
    }
    
    public void setMap(AbstractMap map) {
    	this.map = map;
    }
    
    public boolean isHit() {
        int nextX = 0;
        int nextY = 0;
        // キャラクターの向いている方向の1歩となりの座標
        switch (direction) {
            case LEFT:
                nextX = x - 1;
                nextY = y;
                break;
            case RIGHT:
                nextX = x + 1;
                nextY = y;
                break;
            case UP:
                nextX = x;
                nextY = y - 1;
                break;
            case DOWN:
                nextX = x;
                nextY = y + 1;
                break;
        }
        if (nextX > -1 && nextX < map.getCol() && nextY > -1 && nextY < map.getRow()) {
        	return map.isHit(nextX, nextY);
        }
        return true;
    }
    
    public int getMoveType() {
        return moveType;
    }
}

