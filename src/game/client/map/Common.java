package game.client.map;

/**
 * 
 * @author Murachiki27
 * @deprecated
 */
@Deprecated
public interface Common {
    // 方向を表す定数
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    
    // チップセットのサイズ（単位：ピクセル）
    public static final int CHIPSIZE = 32;
    
    public static final int EASY = 0;
    public static final int NOMAL = 1;
    public static final int HARD = 2;
    public static final int MANIA = 3;
    
    public static final int RUNNING = 0;
    public static final int CONTINUE = 1;
    public static final int CLOSE = 2;
    public static final int EXCEPTION = 3;
    
    public static final int NONE_QUEST = -1;
    public static final int ADD_QUEST = 0;
    public static final int CLEAR_QUEST = 1;
    
    public static final int FADE_BGM = 500;
}
