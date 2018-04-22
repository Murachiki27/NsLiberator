package game.client.image;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.client.gui.GuiImageInfo;
import game.client.image.BattleImageInfo;
import game.client.map.BackgroundType;
import game.client.map.chara.EnumCharacterType;
import game.common.StingNames;
import game.common.ending.StaffRollData;

public class ImageUtils {
	
	private static Map<BackgroundType, Image> backgrounds = new HashMap<>();
	private static Map<String, Image> parallaxImages = new HashMap<>();
	
	private static Image[] weaponMagics;
	private static Map<String, Image> debugImages = new HashMap<>();
	private static Map<String, Image> debugBattleImages = new HashMap<>();
	
	private static Map<BattleImageInfo, Image> battleCharas = new HashMap<>();
	private static Map<String, Image> staffImages = new HashMap<>();
	
	private static Map<GuiImageInfo, Image> guiImages = new HashMap<>();
	private static Map<EnumCharacterType, Image> charaImage = new HashMap<>();
	
	public ImageUtils() {
		weaponMagics = new Image[2];
	}
	
	public boolean loadImages() {
		try {
			for (GuiImageInfo guiInfo : GuiImageInfo.values()) {
				registerImage(guiInfo);
			}
			for (EnumCharacterType chara : EnumCharacterType.values()) {
				registerImage(chara);
			}
			for (String name : StaffRollData.STAFF) {
				this.registerStaffImage(name, name);
			}
			
			registerImage(BackgroundType.FOREST, false);
			registerImage(BackgroundType.GOBLINS_CAVE, false);
			registerImage(BackgroundType.KING_GOBLIN_ROOM, false);
			registerImage(BackgroundType.DESERT, false);
			registerImage(BackgroundType.DESERT, true);
			registerImage(BackgroundType.MINE_SHAFT, false);
			registerImage(BackgroundType.DATA_SELECT, false);
			registerImage(BackgroundType.STAFF_ROOM, false);
			
			registerParallaxImage("debug");
			
			
			registerImage("weapon", 0, "weapon_magic");
			registerImage("weapon", 1, "weapon_sword");
			
			
			for (BattleImageInfo back : BattleImageInfo.values()) {
				registerImage(back);
			}
			
			registerDebugImage("#debug_tool_gui", "tool_box");
			registerDebugImage("#debug_info_gui", "info_window");
			registerDebugImage("#debug_icon", "debug_icon");
			
		} catch (SlickException e) {
			e.printStackTrace();
			return true;
		}
		return true;
	}
	
	/**
	 * 登録されているバトル画面上の背景を取得する
	 * @param backgroundType
	 * @return
	 */
	public static Image getBackgroundImage(BackgroundType backgroundType) {
		if (backgrounds.containsKey(backgroundType)) {
			return backgrounds.get(backgroundType);
		}
		return null;
	}
	
	/**
	 * 登録されている視差マッピングの背景を取得する
	 * @param fileName
	 * @return
	 */
	public static Image getParallaxImage(String fileName) {
		if (parallaxImages.containsKey(fileName)) {
			return parallaxImages.get(fileName);
		}
		return null;
	}
	
	/**
	 * 登録されている武器画像を取得する
	 * @param imageId
	 * @return
	 */
	public static Image getWeaponImage(int imageId) {
		return weaponMagics[imageId];
	}
	
	/**
	 * 登録されているバトル画面上のイメージを取得する
	 * @param info
	 * @return
	 */
	public static Image getBattleCharaImage(BattleImageInfo info) {
		if (battleCharas.containsKey(info)) {
			return battleCharas.get(info);
		}
		return null;
	}
	
	public static Image getStaffImage(String key) {
		if (staffImages.containsKey(key)) {
			return staffImages.get(key);
		}
		return null;
	}
	
	public static Image getDebugImage(String key) {
		if (debugImages.containsKey(key)) {
			return debugImages.get(key);
		}
		return null;
	}
	
	public static Image getDebugBattleImage(String key) {
		if (debugBattleImages.containsKey(key)) {
			return debugBattleImages.get(key);
		}
		return null;
	}
	
	/**
	 * 登録されているGuiイメージを取得する
	 * @param info
	 * @return
	 */
	public static Image getGuiImage(GuiImageInfo info) {
		if (guiImages.containsKey(info)) {
			return guiImages.get(info);
		}
		return null;
	}
	
	/**
	 * 登録されているマップ上のキャラクター画像を取得する
	 * @param type
	 * @return
	 */
	public static Image getMapCharaImage(EnumCharacterType type) {
		if (charaImage.containsKey(type)) {
			return charaImage.get(type);
		}
		return null;
	}
	
	
	/**
	 * バトル画面の背景を登録する
	 * @param backgroundType
	 * @param nightMode
	 * @throws SlickException
	 */
	private void registerImage(BackgroundType backgroundType, boolean nightMode) throws SlickException {
		backgrounds.put(backgroundType, new Image(nightMode ? backgroundType.getNightImageDir() : backgroundType.getNormalImageDir()));
	}
	
	/**
	 * 視差マッピング法を使ったマップ用に画像を登録する
	 * @param fileName
	 * @throws SlickException
	 */
	private void registerParallaxImage(String fileName) throws SlickException {
		parallaxImages.put(fileName, new Image("textures/map/backgrounds/" + fileName + ".png"));
	}
	
	
	private void registerImage(String key, int imageId, String path) throws SlickException {
		switch (key) {
		case "weapon":
			weaponMagics[imageId] = new Image(StingNames.weapons_path + path + ".png");
			break;
		}
	}
	
	
	/**
	 * 敵のバトル画面上でのイメージを登録する
	 * @param info
	 * @throws SlickException
	 */
	private void registerImage(BattleImageInfo info) throws SlickException {
		battleCharas.put(info, new Image("textures/character/" + info.getPath() + ".png"));
	}
	
	private void registerStaffImage(String key, String value) throws SlickException {
		staffImages.put(key, new Image("textures/character/staff/" + value + ".png"));
	}
	
	public static void registerDebugImage(String key) {
		try {
			debugImages.put(key, new Image("./debug/textures/map/" + key));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	private void registerDebugImage(String key, String value) throws SlickException {
		debugImages.put(key, new Image("textures/gui/debug/" + value + ".png"));
	}
	
	public static void registerDebugBattleImage(String key) {
		try {
			debugBattleImages.put(key, new Image("./debug/textures/battle/" + key));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Guiなどに使うパーツや、Guiの画像を登録する
	 * @param info
	 * @throws SlickException
	 */
	private void registerImage(GuiImageInfo info) throws SlickException {
		guiImages.put(info, new Image("textures/gui/" + info.getPath() + ".png"));
	}
	
	/**
	 * マップ上のキャラクターの画像を登録する
	 * @param info
	 * @throws SlickException
	 */
	private void registerImage(EnumCharacterType info) throws SlickException {
		charaImage.put(info, new Image("textures/map/character/" + info.getPath() + ".png"));
	}
}
