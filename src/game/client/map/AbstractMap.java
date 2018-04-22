package game.client.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import game.TypingMaster;
import game.api.MapObject;
import game.client.chara.BattleEnemy;
import game.client.map.chara.TMCharacter;
import game.client.map.chara.TMNpc;
import game.client.particle.TMParticle;
import game.common.data.BasicData;
import game.screen.FieldBattleState;
import game.sound.MusicListener.Sequence;
import game.utils.TMLog;
import game.utils.TMRandom;

public abstract class AbstractMap implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAP_TILE_SIZE = 32;
	
	// マップID
	private String mapId;
	// マップ名
	private String mapName = "";
	// 背景のタイプ
	private BackgroundType backgroundType;
	// BGMタイプ
	private Sequence bgmType;
	
	// オブジェクト用マップ
	protected int[][] objMap;
	// 常に手前にあるタイル用マップ
	protected int[][] sideMap;
	
	// マップの行数・列数（単位：マス）
	private int row;
	private int col;
	
	// マップ全体の大きさ（単位：ピクセル）
	private int width;
	private int height;
	
	// パーティクル用ポジション
	protected int px;
	protected int py;
	
	protected transient MapTileUtils mapTile;
	protected transient TMParticle particles;
	
	private List<TMCharacter> charas = new ArrayList<>();
	private List<MapTileObject> objects = new ArrayList<>();
	
	private List<BattleEnemy> dayEnemies = new ArrayList<>();
	private List<BattleEnemy> nightEnemies = new ArrayList<>();
	
	public Random rand;
	
	private Color skyColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
	
	// 振動モード
	public int vibrationType = 0;
	// 振動の強さ
	public float vibratPower;
	// 振動の長さ(1/10s)
	public float vibratDuration;
	// 強制振動モード
	public boolean forcedVibration = false;
	
	/** マップが提供する文字 */
	private char[] chars = {'a', 's', 'd', 'f'};
	/** 打つ文字の最大数 */
	private int maxChars = 5;
	/** 打つ文字の最小数 */
	private int minChars = 3;
	
	/** キャラクターを表示しないか */
	public transient boolean isNotCharaRender;
	/** タイルオブジェクトを表示しないか */
	public transient boolean isNotObjectRender;
	
	private boolean isRepainting = false;
	
	public boolean canEventMark = false;
	
	public AbstractMap(int mapId, String mapFile) {
		this.mapId = String.format("%04d", mapId);
		this.loadData();
		this.load(mapFile);
	}
	
	public AbstractMap(int mapId, MapObject mapObject) {
		this.mapId = String.format("%04d", mapId);
		this.loadData();
		this.loadMapData(mapObject);
	}
	
	private void load(String mapFile) {
		try {
			ObjectInputStream objInStream = new ObjectInputStream(this.getClass().getResourceAsStream("/map/" + mapFile + ".map"));
			MapObject loadDara = (MapObject) objInStream.readObject();
			objInStream.close();
			
			this.loadMapData(loadDara);
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMapData(MapObject object) {
		this.setMapSize(object.getColumn(), object.getRow());
		this.objMap = object.getObjectMap();
		this.sideMap = object.getRenderMap();
	}
	
	public void nullData() {
		this.mapTile = null;
		this.particles = null;
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				chara.nullData();
			}
		}
		if (!this.dayEnemies.isEmpty()) {
			for (BattleEnemy enemy : this.dayEnemies) {
				enemy.nullData();
			}
		}
		if (!this.nightEnemies.isEmpty()) {
			for (BattleEnemy enemy : this.nightEnemies) {
				enemy.nullData();
			}
		}
	}
	
	public void loadData() {
		this.mapTile = BasicData.tiles;
		this.particles = BasicData.particles;
		this.rand = new TMRandom();
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				chara.initData();
			}
		}
		if (!this.dayEnemies.isEmpty()) {
			for (BattleEnemy enemy : this.dayEnemies) {
				enemy.initData();
			}
		}
		if (!this.nightEnemies.isEmpty()) {
			for (BattleEnemy enemy : this.nightEnemies) {
				enemy.initData();
			}
		}
		TMLog.println(this.mapId + " is loaded all.");
	}
	
	public void renderMap(Graphics g, float offsetX, float offsetY) {
		if (this.isRepainting) {
			this.isRepainting = false;
			return;
		}
		this.renderMap(g, offsetX, offsetY, TypingMaster.WIDTH, TypingMaster.HEIGHT);
	}
	
	public abstract void renderMap(Graphics g, float offsetX, float offsetY, int width, int height);
	
	public void renderBrightnessMap(Graphics g) {
		if (!this.hasNoSky()) {
			if (BasicData.getMapData().isDayTime()) {
				this.skyColor.a = 1.0F - this.getBrightness();
			} else {
				float timer = BasicData.getMapData().getGraphicalParameter();
				float tParam = BasicData.getMapData().getGraphicalTransParam();
				this.skyColor.r = 1.0F - timer;
				this.skyColor.g = 1.0F - tParam;
				this.skyColor.b = 1.0F - tParam;
				float alpha = ((1.0F - this.getBrightness()) + 0.56F);
				if (alpha > 1.0F) alpha = 1.0F;
				this.skyColor.a = alpha * (timer * 0.85F);
			}
		} else {
			this.skyColor.r = 1.0F;
			this.skyColor.g = 1.0F;
			this.skyColor.b = 1.0F;
			this.skyColor.a = this.getBrightness();
		}
		g.setColor(this.skyColor);
		g.fillRect(0, 0, TypingMaster.WIDTH, TypingMaster.HEIGHT);
		g.setColor(Color.white);
		g.drawString(this.toBrightnessString(), 50, 150);
	}
	
	/**
	 * マップ情報を更新します。<br>
	 * 同時にキャラクターやパーティクルの更新も行っています。
	 * 
	 * @param pause
	 * @param delta
	 */
	public void updateMap(boolean pause,int delta) {
		if (this.vibratDuration > 0) {
			this.vibratDuration -= delta * 0.01F;
		}
		
		if (!pause) {
			this.moveNPC();
		}
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				chara.updateCharacter(delta);
			}
		}
		this.particles.update(delta);
	}
	
	public void repaint() {
		this.isRepainting = true;
	}
	
	/**
	 * パラメーター(x、y)の座標にあるタイルやキャラクターをチェックし、<br>
	 * ぶつかる場合、Trueを返します。<p>
	 * 
	 * 判定は、次のように進行します。<br>
	 * ・(x,y)に物理レイヤーの層にAirタイル以外か、透明判定タイルがあるか<br>
	 * ・(x,y)にキャラクターが存在するか<br>
	 * ・(x,y)に衝突判定有りのタイルオブジェクトがあるか<br>
	 * 
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isHit(int x, int y) {
		if (this.objMap[y][x] != 0 || this.objMap[y][x] == -2) {
			return true;
		}
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				if (chara.getPosX() == x && chara.getPosY() == y) {
					return true;
				}
			}
		}
		if (!this.objects.isEmpty()) {
			for (MapTileObject object : this.objects) {
				if (object.getPosX() == x && object.getPosY() == y) {
					return object.isHitTile();
				}
			}
		}
		return false;
	}
	
	/**
	 * マップにキャラクターを追加します。
	 * @param chara
	 */
	public void addCharacter(TMCharacter chara) {
		chara.setMap(this);
		if (chara instanceof TMNpc) {
			if (((TMNpc)chara).getEvent() != null) {
				((TMNpc)chara).getEvent().mapBase = this;
			}
		}
		this.charas.add(chara);
	}
	
	/**
	 * マップにタイルオブジェクトを追加します。
	 * @param object
	 */
	public void addTileObject(MapTileObject object) {
		object.mapBase = this;
		this.objects.add(object);
	}
	
	/**
	 * マップからキャラクターを削除します。
	 * @param chara
	 */
	public void removeCharacter(TMCharacter chara) {
		this.charas.remove(chara);
	}
	
	/**
	 * マップからタイルオブジェクトを削除します。
	 * @param object
	 */
	public void removeTileObject(MapTileObject object) {
		this.objects.remove(object);
	}
	
	/**
	 * パラメーター(x、y)の座標にあるキャラクターを返します。
	 * @param x
	 * @param y
	 * @return
	 */
	public TMCharacter checkCharacter(int x, int y) {
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				if (chara.getPosX() == x && chara.getPosY() == y) {
					return chara;
				}
			}
		}
		return null;
	}
	
	/**
	 * パラメーター(x、y)の座標にあるタイルオブジェクトを返します。
	 * @param x
	 * @param y
	 * @return
	 */
	public MapTileObject checkTileObject(int x, int y) {
		if (!this.objects.isEmpty()) {
			for (MapTileObject object : this.objects) {
				if (object.getPosX() == x && object.getPosY() == y) {
					return object;
				}
			}
		}
		return null;
	}
	
	public void addDayEnemy(BattleEnemy enemy) {
		this.dayEnemies.add(enemy);
	}
	
	public void addNightEnemy(BattleEnemy enemy) {
		this.nightEnemies.add(enemy);
	}
	
	/**
	 * マップの名前を設定します。
	 * @param mapName
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	
	/**
	 * マップでの戦闘画面時の背景を設定します。
	 * @param backgroundType
	 */
	public void setBackgroundType(BackgroundType backgroundType) {
		this.backgroundType = backgroundType;
	}
	
	/**
	 * マップのBGMを設定します。
	 * @param bgmType
	 */
	public void setMapBGM(Sequence bgmType) {
		this.bgmType = bgmType;
	}
	
	/**
	 * マップ全体の明るさ(0.0F ～ 1.0F)
	 * @return
	 */
	public float getBrightness() {
		return 1.0F;
	}
	
	/**
	 * マップが時間で空の描写(明るさ)がされるか
	 * @return
	 */
	public boolean hasNoSky() {
		return false;
	}
	
	/**
	 * マップでモンスターとの戦闘を起こすか
	 * @return
	 */
	public boolean isExistMonster() {
		return true;
	}
	
	/**
	 * マップでモンスターと遭遇する確率
	 * @return
	 */
	public float getEnemySpawnProbability() {
		return 0.05F;
	}
	
	/**
	 * マップで日中にモンスターと遭遇するか
	 * @return
	 */
	public boolean isSpawnFromDaytime() {
		return true;
	}
	
	/**
	 * マップで夜中にモンスターと遭遇するか
	 * @return
	 */
	public boolean isSpawnFromNight() {
		return true;
	}
	
	/**
	 * マップの戦闘時に打つ文字を設定します。
	 * 
	 * @param cs
	 */
	public void setCharas(int minChars, int maxChars, char... cs) {
		this.minChars = minChars;
		this.maxChars = maxChars;
		this.chars = cs;
	}
	
	/**
	 * マップの戦闘時に打つ文字を出力します。
	 * 
	 * @return
	 */
	public String getWord() {
		int size = this.rand.nextInt(this.maxChars - this.minChars) + this.minChars;
		char[] newChar = new char[size];
		for (int i = 0; i < size; i++) {
			int index = this.rand.nextInt(this.chars.length);
			newChar[i] = this.chars[index];
		}
		return new String(newChar);
	}
	
	/**
	 * マップのサイズを決めます
	 * @param col
	 * @param row
	 */
	protected void setMapSize(int col, int row) {
		this.row = row;
		this.col = col;
		this.width = col * MAP_TILE_SIZE;
		this.height = row * MAP_TILE_SIZE;
	}
	
	private void moveNPC() {
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				if (chara instanceof TMNpc) {
					TMNpc npc = (TMNpc) chara;
					npc.moveUpdate();
				}
			}
		}
	}
	
	protected int pixelsToTiles(double pixels) {
		return (int) Math.floor(pixels / MAP_TILE_SIZE);
	}
	
	protected int tilesToPixels(int tiles) {
		return tiles * MAP_TILE_SIZE;
	}
	
	
	public String getMapID() {
		return this.mapId;
	}
	
	public String getMapName() {
		return this.mapName;
	}
	
	public List<TMCharacter> getCharacters() {
		return this.charas;
	}
	
	public List<MapTileObject> getObjects() {
		return this.objects;
	}
	
	public boolean isCharacterEmpty() {
		return this.charas.isEmpty();
	}
	
	public boolean isObjectEmpty() {
		return this.objects.isEmpty();
	}
	
	
	/**
	 * マップに格納されているキャラクターリストより、キャラクタータグと一致する<br>
	 * キャラクターを返します。
	 * 
	 * @param tag キャラクタータグ
	 * @return
	 */
	public TMCharacter getCharacter(String tag) {
		if (!this.charas.isEmpty()) {
			for (TMCharacter chara : this.charas) {
				if (chara.getTag().equals(tag)) {
					return chara;
				}
			}
		}
		return null;
	}
	
	/**
	 * マップに格納されているタイルオブジェクトリストより、オブジェクトタグと一致する<br>
	 * タイルオブジェクトを返します。
	 * 
	 * @param tag オブジェクトタグ
	 * @return
	 */
	public MapTileObject getTileObject(String tag) {
		if (!this.objects.isEmpty()) {
			for (MapTileObject object : this.objects) {
				if (object.getTag().equals(tag)) {
					return object;
				}
			}
		}
		return null;
	}
	
	public boolean isBattleStart() {
		boolean flag = this.isExistMonster() && this.rand.nextFloat() < this.getEnemySpawnProbability();
		if (BasicData.getMapData().isNightTime()) {
			return flag && this.isSpawnFromNight() && !this.nightEnemies.isEmpty();
		} else {
			return flag && this.isSpawnFromDaytime() && !this.dayEnemies.isEmpty();
		}
	}
	
	public void setBattleEnemy() {
		int maxCount = this.rand.nextInt(4) + 1;
		int i = 0;
		boolean flag;
		do {
			if (BasicData.getMapData().isNightTime()) {
				for (BattleEnemy enemy : this.nightEnemies) {
					flag = this.rand.nextFloat() < enemy.getSpawnPercent();
					if (flag && i < maxCount) {
						i++;
						FieldBattleState.addEnemy((BattleEnemy)enemy.clone());
					}
				}
			} else {
				for (BattleEnemy enemy : this.dayEnemies) {
					flag = this.rand.nextFloat() < enemy.getSpawnPercent();
					if (flag && i < maxCount) {
						i++;
						FieldBattleState.addEnemy((BattleEnemy)enemy.clone());
					}
				}
			}
		} while (i < maxCount);
	}
	
	
	public int getCol() {
		return this.col;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public BackgroundType getBackgroundType() {
		return this.backgroundType;
	}
	
	public Sequence getBGM() {
		return this.bgmType;
	}
	
	/* -------------------- DEBUG START ------------------------- */
	
	public String toBrightnessString() {
		return this.skyColor.toString();
	}
	
	/* -------------------- DEBUG END ------------------------- */
}
