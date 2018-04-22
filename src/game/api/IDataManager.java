package game.api;

import java.io.Serializable;

/**
 * セーブデータ実装用インターフェース
 * 
 * @author Murachiki27
 *
 */
public interface IDataManager extends Serializable {
	public static final String SAVE_DIR = "./saves/data";
	
	public static final String PLAYER_DATA = "/player_dat0.dat";
	public static final String PLAYER_INVENTORY = "/player_dat1.dat";
	public static final String PLAYER_STATS = "/stats.dat";
	public static final String MAP_DATA = "/fields.dat";
	
	public void initData();
	
	public boolean load(int loadNo);
	
	public boolean save(int saveNo);
}
