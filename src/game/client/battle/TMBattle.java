package game.client.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import game.TypingMaster;
import game.client.chara.BattleEnemy;
import game.client.chara.CharaStatus;
import game.client.chara.enemy.TMEnemies;
import game.client.chara.skill.EnumSkillType;
import game.client.chara.skill.Skill;
import game.client.gui.GuiAnimationLabel;
import game.client.gui.GuiBar;
import game.client.gui.GuiBattleStatus;
import game.client.gui.GuiEnemySkillWindow;
import game.client.gui.GuiImageInfo;
import game.client.gui.GuiMenuBox;
import game.client.gui.GuiMenuPack;
import game.client.gui.GuiMessageWindow;
import game.client.gui.GuiWindow;
import game.client.gui.LabelAnimationType;
import game.client.image.ImageUtils;
import game.common.data.BasicData;
import game.common.data.PlayerData;
import game.common.image.BattleImageItem;
import game.common.input.EnumKeyInput;
import game.common.input.TypingKeyListener;
import game.common.lib.WordDictionary;
import game.common.lib.WordsString;
import game.common.settings.Options;
import game.screen.ScreenMapState;
import game.sound.SoundListener;
import game.utils.TMLog;
import game.utils.TMRandom;

public class TMBattle {
	
	private static final int TURN_NONE = -1;
	private static final int TURN_PLAYER = 0;
	private static final int TURN_ENEMY = 1;
	
	private static final int MAIN_MENU = 0;
	private static final int SKILL_MENU = 1;
	private static final int ITEM_MENU = 2;
	private static final int SELECT_CHARA = 3;
	
	private static final int NONE_ACT = 0;
	private static final int ACT_ATTACK = 1;
	private static final int ACT_ITEM = 2;
	private static final int ACT_DEFENCE = 3;
	private static final int ACT_EXIT = 4;
	
	private static final int PART_NONE = 0;
	private static final int PART_ACT_FLAG = 1;
	private static final int PART_ACTION = 2;
	private static final int PART_ACT_END = 3;
	private static final int PART_ANIMATION = 4;
	private static final int PART_DAMAGE = 5;
	private static final int PART_ORGANIZE = 6;
	private static final int PART_END = 7;
	
	private static final int EXIT_NONE = -1;
	private static final int EXIT_FAILED = 0;
	private static final int EXIT_SUCCESS = 1;
	
	
	// バトルのタイプ
	public int battleType;
	// バトルターン
	public int turn;
	// ターン種類は何か
	public int turnOwner = TURN_NONE;
	// メニューの順序
	public int menuPart;
	// バトルの進行順序
	public int battlePart;
	// 現在行動中のキャラクター番号
	public int activeCharaNo;
	// 相手を選択しているキャラクター番号
	public int vsCharaNo;
	
	// 行動の選択番号
	private int actionState;
	// タイピング可能時間
	//private float typingDuration;
	// タイピングタイマー
	private float typingTimer;
	// タイピングした文字数
	private int typingCharas;
	
	// 行動中のキャラクターが使うスキルタイプ
	private EnumSkillType skillType;
	
	// 獲得できる経験値
	private int exp;
	
	// 強制先制行動(敵のみ)あるか
	private boolean forcedBeforeAction = false;
	// タイピング中か
	//private boolean isTypingTime = false;
	// タイピング文字がコンプリート出来たか
	private boolean isComleted = false;
	// 戦闘回避出来たか
	private int exit = EXIT_NONE;
	// 敵を倒した
	private boolean isAllDelete = false;
	// 敵に倒された
	private boolean isAllDead = false;
	
	public List<BattleEnemy> enemyList = new ArrayList<BattleEnemy>();
	
	private Image background;
	private Image cursor;
	private Image typingIcon;
	
	private BattleImageItem[] weaponImages;
	private GuiMenuBox actionMenu, skillMenu, itemMenu;
	private GuiEnemySkillWindow skillNameWindow;
	private GuiMessageWindow chatWindow;
	private GuiMessageWindow messageWindow;
	private GuiWindow windowGui;
	private GuiBar typingBar;
	
	private WordDictionary strings;
	private TypingKeyListener typingKey;
	
	private GuiAnimationLabel[] damageBar;
	
	private GuiBattleStatus[] charaGui;
	
	//private MainSave save;
	private PlayerData playerData;
	
	private GameContainer container;
	private StateBasedGame game;
	
	public TMBattle(GameContainer container, StateBasedGame game) {
		weaponImages = new BattleImageItem[4];
		damageBar = new GuiAnimationLabel[4];
		charaGui = new GuiBattleStatus[4];
		this.container = container;
		this.game = game;
	}
	
	public void init() {
		for (int i = 0; i < weaponImages.length; i++) {
			weaponImages[i] = new BattleImageItem();
		}
		
		actionMenu = new GuiMenuBox(30.0F, 36.0F, 144, 192);
		actionMenu.addMenuPackStrings(GuiMenuPack.BATTLE_SELECT);
		actionMenu.showGui();
		skillMenu = new GuiMenuBox(50.F, 56.0F, 240, 320);
		skillMenu.showGui();
		itemMenu = new GuiMenuBox(50.F, 56.0F, 240, 320);
		itemMenu.showGui();
		
		skillNameWindow = new GuiEnemySkillWindow();
		chatWindow = new GuiMessageWindow(142.0F, 16.0F, 0, 160, 384, 64);
		messageWindow = new GuiMessageWindow(64.0F, 308.0F, 0, 0, 512, 160);
		windowGui = new GuiWindow(290, 60, 200, 120);
		windowGui.showGui();
		typingBar = new GuiBar(80.0F, 160.0F, 3.0F, 0);
		typingBar.showGui();
		strings = new WordDictionary();
		typingKey = new TypingKeyListener();
		typingIcon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(0, 16, 16, 16).getScaledCopy(3.0F);
		typingIcon.setFilter(Image.FILTER_NEAREST);
		for (int i =0; i < weaponImages.length; i++) {
			damageBar[i] = new GuiAnimationLabel(80 + i * 160, 260, 100);
			damageBar[i].setAnimationTime(50);
			damageBar[i].setAnimationType(LabelAnimationType.SHAKE);
		}
		cursor = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(0, 32, 32, 32);
	}
	
	public void initState() {
		//save = BasicData.getData();
		this.playerData = BasicData.getPlayerData();
		for (int i = 0; i < weaponImages.length; i++) {
			weaponImages[i].setImage(ImageUtils.getWeaponImage(0));
		}
		for (int i = 0; i < this.playerData.getMemberSize(); i++) {
			charaGui[i] = new GuiBattleStatus(this.playerData.getCharaToIndex(i), i);
		}
		activeCharaNo = 0;
		setEXP();
	}
	
	public void saveState() {
		isAllDelete = false;
		isAllDead = false;
		int size = enemyList.size();
		for (int i = 0; i < size; i++) {
			enemyList.removeAll(enemyList);
		}
	}
	
	public void render(Graphics g) {
		g.setFont(BasicData.defaultUfont);
		g.drawImage(background, 0, 0);
		for (int i = 0; i < enemyList.size(); i++) {
			if (!enemyList.get(i).isDead()) {
				enemyList.get(i).drawChara(getAllEnemyBasicCenterX() - (enemyList.get(i).getWidth() * enemyList.get(i).getScale() / 2) + (getAllEnemyBasicCenterX() * i), 0, g);
			}
		}
		for (int i = 0; i < this.playerData.getMemberSize(); i++) {
			float dy = getStatusGuiPosY(i);
			charaGui[i].setPopup(dy);
			charaGui[i].draw(g);
		}
		switch (turnOwner) {
		case TURN_PLAYER:
			Skill skill = getSkillIntoChara(getCharaStatus(activeCharaNo), skillMenu.get());
			switch (actionState) {
			case NONE_ACT:
				if (menuPart != SELECT_CHARA) {
					actionMenu.draw(g);
				}
				
				if (menuPart == SKILL_MENU) {
					skillMenu.draw(g);
					if (skillMenu.getSize() > 0) {
						windowGui.draw(g);
						g.drawString(skill.getDescription(), windowGui.getX() + 20, windowGui.getY() + 20);
					}
				} else if (menuPart == ITEM_MENU) {
					itemMenu.draw(g);
				} else if (menuPart == SELECT_CHARA) {
					g.drawString("" + vsCharaNo, 150, 100);
					if (getAllActionSkill(skill)) {
						if (getSupportSkill(skill)) {
							for (int i = 0; i < this.playerData.getMemberSize(); i++) {
								cursor.draw(80 + i * 160, charaGui[i].getY() + getStatusGuiPosY(i) - 32);
							}
						} else {
							for (int i = 0; i < enemyList.size(); i++) {
								if (!enemyList.get(i).isDead()) {
									cursor.draw(getAllEnemyBasicCenterX() - 16 / 2 + getAllEnemyBasicCenterX() * i, 100);
								}
							}
						}
					} else {
						if (getSupportSkill(skill)) {
							cursor.draw(80 - 16 / 2 + vsCharaNo * 160, charaGui[vsCharaNo].getY() + getStatusGuiPosY(vsCharaNo) - 32);
						} else {
							cursor.draw(getAllEnemyBasicCenterX() - 16 / 2 + getAllEnemyBasicCenterX() * vsCharaNo, 100);
						}
					}
				}
				break;
			case ACT_ATTACK:
				if (getAllActionSkill(skill)) {
					if (getSupportSkill(skill)) {
						if (battlePart < PART_ACT_END) {
							weaponImages[0].drawImage(g, Color.white);
						} else {
							for (int i = 0; i < this.playerData.getMemberSize(); i++) {
								if (!this.playerData.getCharaToIndex(i).isDead()) {
									weaponImages[i].drawImage(g, Color.white);
								}
							}
						}
					} else {
						if (battlePart < PART_ACT_END) {
							weaponImages[0].drawImage(g, Color.white);
						} else {
							for (int i = 0; i < enemyList.size(); i++) {
								if (!enemyList.get(i).isDead()) {
									weaponImages[i].drawImage(g, Color.white);
								}
							}
						}

					}
				} else {
					weaponImages[0].drawImage(g, Color.white);
				}
				
				if (getSupportSkill(skill)) {
					for (int j = 0; j < this.playerData.getMemberSize(); j++) {
						if (!this.playerData.getCharaToIndex(j).isDead()) {
							if (damageBar[j].isUsing()) {
								damageBar[j].draw(g);
							}
						}
					}
				} else {
					for (int j = 0; j < enemyList.size(); j++) {
						if (!enemyList.get(j).isDead()) {
							if (damageBar[j].isUsing()) {
								damageBar[j].draw(g);
							}
							// damageBar[j].draw(g, getAllEnemyBasicCenterX() - damageBar[j].getWidth() / 2 + getAllEnemyBasicCenterX() * j, 220);
							// TODO
						}
					}
				}
				
				if (battlePart == PART_ACTION) {
					typingBar.draw(g);
					typingKey.draw(g);
					g.drawImage(typingIcon, 16.0F, 160.0F);
				}
				if (battlePart == PART_ANIMATION) {
					if (isComleted) {
						BasicData.damageUfont.drawString(320 - (BasicData.damageUfont.getWidth("Complete!!!") / 2), 220, "Complete!!!", Color.cyan);
					}
				}
				
				break;
			case ACT_ITEM:
				break;
			case ACT_DEFENCE:
				break;
			case ACT_EXIT:
				break;
			}
			break;
		case TURN_ENEMY:
			if (battlePart == PART_ACT_FLAG) {
				chatWindow.draw(g);
			}
			if (battlePart == PART_ACTION) {
				skillNameWindow.draw(g);
			}
			for (int j = 0; j < this.playerData.getMemberSize(); j++) {
				if (damageBar[j].isUsing()) {
					damageBar[j].draw(g);
				}
			}
			break;
		}
		messageWindow.draw(g);
	}
	
	public void update(int delta) {
		if (forcedBeforeAction) {
			forcedBeforeAction = false;
		} else {
			if (isAllDelete || isAllDead) {
				if (isAllDelete) {
					if (!messageWindow.isVisible()) {
						if (enemyList.size() > 1) {
							messageWindow.setMessage(null, enemyList.get(0).getName() + "達 をやっつけた。\\n" + exp + " の経験値を手に入れた。");
						} else {
							messageWindow.setMessage(null, enemyList.get(0).getName() + " をやっつけた。\\n" + exp + " の経験値を手に入れた。");
						}
						messageWindow.showGui();
					}
				}
				if (isAllDead) {
					if (!messageWindow.isVisible()) {
						messageWindow.setMessage(null, "やられちゃった・・・");
						messageWindow.showGui();
					}
				}
			} else {
				switch (turnOwner) {
				case TURN_NONE:
					changeTurnOwner(); // メニュー決定の為、ここから開始
					break;
				case TURN_PLAYER:
					turnPlayer(delta);
					typingBar.update(this.typingTimer);
					break;
				case TURN_ENEMY:
					turnEnemy(delta);
					break;
				}
			}
		}
		for (int i = 0; i < this.playerData.getMemberSize(); i++) {
			charaGui[i].update();
		}
	}
	
	public void keyPressed(int key, char c, EnumKeyInput input) {
		if (isAllDelete || isAllDead || exit != EXIT_NONE) {
			if (input == EnumKeyInput.KEY_DONE) {
				SoundListener.play(0);
				if (messageWindow.nextMessage()) {
					if (exit != EXIT_NONE) {
						switch (exit) {
						case EXIT_SUCCESS:
							exit = EXIT_NONE;
							messageWindow.closeGui();
							this.game.enterState(TypingMaster.FIELDMAP, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.black, 500));
							break;
						case EXIT_FAILED:
							exit = EXIT_NONE;
							messageWindow.closeGui();
							changeBattler();
							actionState = NONE_ACT;
							menuPart = MAIN_MENU;
							battlePart = PART_NONE;
							break;
						}
					} else if (isAllDelete) {
						int levelNo;
						do {
							levelNo = checkLevelUp(activeCharaNo);
							if (levelNo != -1) {
								messageWindow.setMessage(null, this.playerData.getCharaToIndex(levelNo).getName() + "はレベル" + this.playerData.getCharaToIndex(levelNo).getLevel() + "になった!");
								activeCharaNo++;
								continue;
							}
							activeCharaNo++;
						} while (activeCharaNo < this.playerData.getMemberSize());
						for (int j = 0; j < this.playerData.getMemberSize(); j++) {
							this.playerData.getCharaToIndex(j).resetStatusPlus();
						}
						messageWindow.closeGui();
						this.game.enterState(TypingMaster.FIELDMAP, new FadeOutTransition(Color.black, 500), new FadeInTransition(Color.black, 500));
					} else if (isAllDead) {
						messageWindow.closeGui();
						BasicData.initAllData();
						ScreenMapState.setGameOver();
						this.game.enterState(TypingMaster.MAINMENU, new FadeOutTransition(Color.black, 1000), new FadeInTransition(Color.black, 500));
					}
				}
			}
		} else if (turnOwner == TURN_PLAYER) {
			if (actionState == NONE_ACT) {
				Skill skill = getSkillIntoChara(getCharaStatus(activeCharaNo), skillMenu.get());
				switch (menuPart) {
				case MAIN_MENU:
					actionMenu.keyPressed(key, c, input);
					break;
				case SKILL_MENU:
					skillMenu.keyPressed(key, c, input);
					break;
				case ITEM_MENU:
					itemMenu.keyPressed(key, c, input);
					break;
				case SELECT_CHARA:
					if (input == EnumKeyInput.KEY_LEFT) {
						if (!getAllActionSkill(skill)) {
							if (getSupportSkill(skill)) {
								do {
									vsCharaNo--;
									if (vsCharaNo < 0) {
										vsCharaNo = getLastCharaNo();
										break;
									}
								} while (this.playerData.getCharaToIndex(vsCharaNo).isDead());
							} else {
								do {
									vsCharaNo--;
									if (vsCharaNo < 0) {
										vsCharaNo = getLastEnemyNo();
										break;
									}
								} while (enemyList.get(vsCharaNo).isDead());
							}
						}
					}
					if (input == EnumKeyInput.KEY_RIGHT) {
						if (!getAllActionSkill(skill)) {
							if (getSupportSkill(skill)) {
								do {
									vsCharaNo++;
									if (vsCharaNo >= this.playerData.getMemberSize()) {
										vsCharaNo = getFirstCharaNo();
										break;
									}
								} while (this.playerData.getCharaToIndex(vsCharaNo).isDead());
							} else {
								do {
									vsCharaNo++;
									if (vsCharaNo >= enemyList.size()) {
										vsCharaNo = getFirstEnemyNo();
										break;
									}
								} while (enemyList.get(vsCharaNo).isDead());
							}
						}
					}
					break;
				}
				switch (input) {
				case KEY_DONE:
					switch (menuPart) {
					case MAIN_MENU:
						if (actionMenu.get() < 2) {
							menuPart = (actionMenu.get() == 0 ? SKILL_MENU : ITEM_MENU);
						} else {
							actionState = (actionMenu.get() == 2 ? ACT_DEFENCE : ACT_EXIT);
						}
						break;
					case SKILL_MENU:
						menuPart = SELECT_CHARA;
						if (getSupportSkill(skill)) {
							vsCharaNo = getFirstCharaNo();
						} else {
							vsCharaNo = getFirstEnemyNo();
						}
						break;
					case ITEM_MENU:
						menuPart = SELECT_CHARA;
						break;
					case SELECT_CHARA:
						actionState = (actionMenu.get() == 0 ? ACT_ATTACK : ACT_ITEM);
						break;
					}
					break;
				case KEY_CANCEL:
					switch (menuPart) {
					case SKILL_MENU:
					case ITEM_MENU:
						menuPart = MAIN_MENU;
						break;
					case SELECT_CHARA:
						menuPart = (actionMenu.get() == 0 ? SKILL_MENU : ITEM_MENU);
						break;
					}
					break;
				default:
					break;
				}
			} else {
				if (battlePart == PART_ACTION) {
					typingKey.keyPressedChara(c);
				}
			}
		}
	}
	
	private void turnPlayer(int delta) {
		if (actionState != NONE_ACT) {
			switch (actionState) {
			case ACT_ATTACK:
				funcAction(TURN_PLAYER, delta);
				break;
			case ACT_ITEM:
				break;
			case ACT_DEFENCE:
				funcDefence();
				break;
			case ACT_EXIT:
				funcExit();
				break;
			}
		}
	}
	
	private void turnEnemy(int delta) {
		funcAction(TURN_ENEMY, delta);
	}
	
	private void funcAction(int partOwner, int delta) {
		if (partOwner == TURN_PLAYER) {
			switch (battlePart) {
			case PART_NONE:
				WordsString ws = strings.setStringWard();
				typingKey.setCharacter(ws.getString());
				typingBar.setMaxPosition(4.0F);
				battlePart++;
				break;
			case PART_ACT_FLAG:
				weaponImages[0].alpha += delta * 0.0005F;
				if (weaponImages[0].alpha >= 1.0F) {
					battlePart = PART_ACTION;
				}
				break;
			case PART_ACTION:
				this.typingTimer += delta * 0.001;
				if (typingKey.checkComplete()) {
					isComleted = true;
					typingCharas = typingKey.getCharas() + 10;
					battlePart = PART_ACT_END;
				} else if (this.typingTimer > 4.0F) {
					typingCharas = typingKey.getCharas();
					battlePart = PART_ACT_END;
				}
				break;
			case PART_ACT_END:
				Skill skill = getSkillIntoChara(getCharaStatus(activeCharaNo), skillMenu.get());
				if (getAllActionSkill(skill)) {
					for (int i = 0; i < enemyList.size(); i++) {
						weaponImages[i].alpha = 1.0F;
						weaponImages[i].setMovePosition(getEnemyBasicCenterX(i));
					}
				} else {
					weaponImages[0].setMovePosition(getEnemyBasicCenterX(vsCharaNo));
				}
				battlePart = PART_ANIMATION;
				break;
			case PART_ANIMATION:
				boolean[] flag = null;
				if (flag == null) {
					 flag = new boolean[enemyList.size()];
				}
				
				if (flag != null) {
					for (int i = 0; i < enemyList.size(); i++) {
						weaponImages[i].rotate = delta * 0.5F;
						weaponImages[i].scale -= delta * 0.001F;
						weaponImages[i].updateImage();
						if (weaponImages[i].scale <= 0.0F) {
							flag[i] = true;
						}
					}
					
					if (getAND(flag)) {
						for (int j = 0; j < enemyList.size(); j++) {
							weaponImages[j].setStop();
						}
						skillType = getSkillIntoChara(getCharaStatus(activeCharaNo), skillMenu.get()).getType();
						battlePart = PART_DAMAGE;
					}
				}
				break;
			case PART_DAMAGE:
				if (attackFromPlayerSide()) {
					battlePart = PART_ORGANIZE;
				}
				break;
			case PART_ORGANIZE:
				resetUsedThings();
				if (getAliveOnEnemySide()) {
					battlePart++;
				} else {
					isAllDelete = true;
					activeCharaNo = 0;
					turnOwner = TURN_NONE;
					actionState = NONE_ACT;
					menuPart = MAIN_MENU;
					battlePart = PART_NONE;
					skillMenu.removeAll();
					itemMenu.removeAll();
				}
				break;
			case PART_END:
				changeBattler();
				actionState = NONE_ACT;
				menuPart = MAIN_MENU;
				battlePart = PART_NONE;
				break;
			}
		} else if (partOwner == TURN_ENEMY) {
			switch (battlePart) {
			case PART_NONE:
				this.enemyList.get(activeCharaNo).onUpdate();
				Skill skill = enemyList.get(activeCharaNo).getUseSkill();
				String name = enemyList.get(activeCharaNo).getName();
				String message = enemyList.get(activeCharaNo).getMessage();
				String skillName = skill.getName();
				vsCharaNo = getAttackTarget(skill);
				if (message != null) {
					chatWindow.setMessage(name, message);
					skillNameWindow.setText(skillName);
					chatWindow.showGui();
					battlePart = PART_ACT_FLAG;
				} else {
					skillNameWindow.setText(skillName);
					skillNameWindow.showGui();
					battlePart = PART_ACTION;
				}
				break;
			case PART_ACT_FLAG: // ボスなどの(一方的な)会話イベント用パート
				Input input = container.getInput();
				if (input.isKeyPressed(Options.KEY_ENTER)) {
					if (chatWindow.nextMessage()) {
						chatWindow.closeGui();
						skillNameWindow.showGui();
						battlePart = PART_ACTION;
					}
				}
				break;
			case PART_ACTION: // ボスの技名表示パート
				if (skillNameWindow.update(delta)) {
					skillNameWindow.reset();
					battlePart = PART_ACT_END;
				}
				break;
			case PART_ACT_END: // 表示が終わって攻撃へ
				battlePart = PART_DAMAGE; // TODO アニメーションするかのフラグがない
				skillType = getSkillIntoChara(enemyList.get(activeCharaNo), skillMenu.get()).getType();
				break;
			case PART_ANIMATION: // 特別なスキルの時はここでアニメーション
				break;
			case PART_DAMAGE: // ダメージ表示パート
				if (attackFromEnemySide()) {
					battlePart = PART_ORGANIZE;
				}
				break;
			case PART_ORGANIZE: // 結果を整理し、次のフラグを立てる
				resetUsedThings();
				this.enemyEndUpdate();
				if (getAliveOnPlayerSide()) {
					battlePart++;
				} else {
					isAllDead = true;
					activeCharaNo = 0;
					turnOwner = TURN_NONE;
					actionState = NONE_ACT;
					menuPart = MAIN_MENU;
					battlePart = PART_NONE;
					skillMenu.removeAll();
					itemMenu.removeAll();
				}
				break;
			case PART_END: // アクションモンスターをチェンジ
				changeBattler();
				battlePart = PART_NONE;
				break;
			}
		}
	}
	
	
	
	private void funcDefence() {
		this.playerData.getCharaToIndex(activeCharaNo).setStatusPlus(0, 30);
		changeBattler();
		actionState = NONE_ACT;
		menuPart = MAIN_MENU;
		battlePart = PART_NONE;
	}
	
	private void funcExit() {
		if (!messageWindow.isVisible()) {
			Random rand = new TMRandom();
			exit = (rand.nextBoolean() ? EXIT_SUCCESS : EXIT_FAILED);
			if (exit == EXIT_SUCCESS) {
				activeCharaNo = 0;
				turnOwner = TURN_NONE;
				actionState = NONE_ACT;
				menuPart = MAIN_MENU;
				battlePart = PART_NONE;
				skillMenu.removeAll();
				itemMenu.removeAll();
				messageWindow.setMessage(null, "うまく逃げ切れた!!!");
			} else if (exit == EXIT_FAILED) {
				messageWindow.setMessage(null, "逃げ切れなかったようだ・・・");
			}
			messageWindow.showGui();
		}
	}
	
	
	private boolean attackFromPlayerSide() {
		int damage;
		int firstEnemy = getFirstEnemyNo();
		int lastEnemy = getLastEnemyNo();
		int firstChara = getFirstCharaNo();
		int lastChara = getLastCharaNo();
		switch (skillType) {
		case ATTACK_ALL:
			break;
		case ATTACK_ONE:
			damage = outputDamageFromPlayer(vsCharaNo);
			if (!damageBar[vsCharaNo].isUsing()) {
				damageBar[vsCharaNo].setLabel(String.valueOf(damage));
				for (int i =0; i < weaponImages.length; i++) {
					if (i == vsCharaNo) {
						damageBar[i].setPosition(getAllEnemyBasicCenterX() - damageBar[i].getWidth() / 2 + getAllEnemyBasicCenterX() * i, 220.0F);
					}
				}
				damageBar[vsCharaNo].start();
			}
			if (!damageBar[vsCharaNo].isVisible()) {
				enemyList.get(vsCharaNo).damage(damage);
				if (enemyList.get(vsCharaNo).isDead()) {
					BasicData.getStatsData().getLibrary().addKillCount(TMEnemies.getEnemyIdToTag(enemyList.get(vsCharaNo).getCommandTag()));
				}
				return true;
			}
			break;
		case EFFECT_ALL:
			break;
		case EFFECT_ONE:
			break;
		case HEAL_ALL:
			for (int i = firstChara; i <= lastChara; i++) {
				damage = outputDamageFromPlayer(i);
				if (!damageBar[i].isUsing()) {
					damageBar[i].setLabel(String.valueOf(damage));
					damageBar[i].setPosition((80 - damageBar[i].getWidth() / 2) + (i * 160.0F), 375.0F);
					damageBar[i].start();
				}
				if (!damageBar[i].isVisible()) {
					getCharaStatus(i).heal(damage);
					if (i == lastChara) {
						return true;
					}
				}
			}
			break;
		case HEAL_ONE:
			damage = outputDamageFromPlayer(vsCharaNo);
			damageBar[vsCharaNo].setLabel(String.valueOf(damage));
			for (int i =0; i < weaponImages.length; i++) {
				if (i == vsCharaNo) {
					damageBar[i].setPosition((80 - damageBar[i].getWidth() / 2) + (i * 160.0F), 375.0F);
				}
			}
			damageBar[vsCharaNo].start();
			if (!damageBar[vsCharaNo].isVisible()) {
				getCharaStatus(vsCharaNo).heal(damage);
				return true;
			}
			break;
		case MAGIC_ATC_ALL:
			for (int i = firstEnemy; i <= lastEnemy; i++) {
				damage = outputDamageFromPlayer(i);
				damageBar[i].setLabel(String.valueOf(damage));
				damageBar[i].setPosition(getAllEnemyBasicCenterX() - damageBar[i].getWidth() / 2 + getAllEnemyBasicCenterX() * i, 220.0F);
				damageBar[i].start();
				if (!damageBar[i].isVisible()) {
					enemyList.get(i).damage(damage);
					if (enemyList.get(i).isDead()) {
						BasicData.getStatsData().getLibrary().addKillCount(TMEnemies.getEnemyIdToTag(enemyList.get(vsCharaNo).getCommandTag()));
					}
					if (i == lastEnemy) {
						return true;
					}
				}
			}
			break;
		case MAGIC_ATC_ONE:
			damage = outputDamageFromPlayer(vsCharaNo);
			damageBar[vsCharaNo].setLabel(String.valueOf(damage));
			for (int i =0; i < weaponImages.length; i++) {
				if (i == vsCharaNo) {
					damageBar[i].setPosition(getAllEnemyBasicCenterX() - damageBar[i].getWidth() / 2 + getAllEnemyBasicCenterX() * i, 220.0F);
				}
			}
			damageBar[vsCharaNo].start();
			if (!damageBar[vsCharaNo].isVisible()) {
				enemyList.get(vsCharaNo).damage(damage);
				if (enemyList.get(vsCharaNo).isDead()) {
					BasicData.getStatsData().getLibrary().addKillCount(TMEnemies.getEnemyIdToTag(enemyList.get(vsCharaNo).getCommandTag()));
				}
				return true;
			}
			break;
		case MINUS_EFFECT_ALL:
			break;
		case MINUS_EFFECT_ONE:
			break;
		case REV_ALL:
			break;
		case REV_ONE:
			break;
		default:
			break;
		}
		return false;
	}
	
	private boolean attackFromEnemySide() {
		int damage;
		//int firstEnemy = getFirstEnemyNo();
		//int lastEnemy = getLastEnemyNo();
		int firstChara = getFirstCharaNo();
		int lastChara = getLastCharaNo();
		switch (skillType) {
		case ATTACK_ALL:
			for (int i = firstChara; i <= lastChara; i++) {
				damage = outputDamageFromEnemy(i);
				damageBar[i].setLabel(String.valueOf(damage));
				damageBar[i].setPosition((80 - damageBar[i].getWidth() / 2) + (i * 160.0F), 375.0F);
				damageBar[i].start();
				if (!damageBar[i].isVisible()) {
					getCharaStatus(i).damage(damage);
					return true;
				}
			}
			break;
		case ATTACK_ONE:
			damage = outputDamageFromEnemy(vsCharaNo);
			damageBar[vsCharaNo].setLabel(String.valueOf(damage));
			for (int i =0; i < weaponImages.length; i++) {
				if (i == vsCharaNo) {
					damageBar[i].setPosition((80 - damageBar[i].getWidth() / 2) + (i * 160.0F), 375.0F);
				}
			}
			damageBar[vsCharaNo].start();
			if (!damageBar[vsCharaNo].isVisible()) {
				getCharaStatus(vsCharaNo).damage(damage);
				return true;
			}
			break;
		case EFFECT_ALL:
			break;
		case EFFECT_ONE:
			break;
		case HEAL_ALL:
			break;
		case HEAL_ONE:
			break;
		case MAGIC_ATC_ALL:
			break;
		case MAGIC_ATC_ONE:
			break;
		case MINUS_EFFECT_ALL:
			break;
		case MINUS_EFFECT_ONE:
			break;
		case REV_ALL:
			break;
		case REV_ONE:
			break;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * 使ったものをリセット
	 */
	private void resetUsedThings() {
		for (int i = 0; i < damageBar.length; i++) {
			damageBar[i].reset();
		}
		this.typingTimer = 0;
		if (turnOwner == TURN_PLAYER) {
			actionMenu.reset();
			skillMenu.reset();
			itemMenu.reset();
		}
		isComleted = false;
		skillType = null;
		typingCharas = 0;
	}
	
	private void enemyEndUpdate() {
		this.enemyList.get(this.activeCharaNo).updateSkillNo();
	}
	
	/**
	 * スキルをメニューにセットする
	 */
	private void setNextCharaSkillMenu() {
		if (skillMenu.getSize() > 0) {
			skillMenu.removeAll();
		}
		int size = getCharaStatus(activeCharaNo).skillList.size();
		for (int i = 0; i < size; i++) {
			if (getCharaStatus(activeCharaNo).skillList.get(i).active) {
				skillMenu.addMenuStrings(getCharaStatus(activeCharaNo).skillList.get(i).getName());
			}
		}
	}
	
	
	
	private CharaStatus getCharaStatus(int charaId) {
		return this.playerData.getCharaToIndex(charaId);
	}
	
	/**
	 * 選択されたスキルが全体にアクションするかを判断する
	 * @param skill 特定のスキル
	 * @return 全体アクションであるならTrue、それ以外はFalse
	 */
	public boolean getAllActionSkill(Skill skill) {
		return EnumSkillType.getAllActionType(skill.getType());
	}
	
	/**
	 * 選択されたスキルがサポートスキル(防御、回復、エンハンス系)かを判断する
	 * @param skill 特定のスキル
	 * @return サポートスキルであるならTrue、それ以外ならFalse
	 */
	public boolean getSupportSkill(Skill skill) {
		return EnumSkillType.getSupportType(skill.getType());
	}
	
	/**
	 * 操作側を交代
	 */
	private void changeTurnOwner() {
		if (turnOwner == TURN_NONE) {
			setNextCharaSkillMenu();
		}
		turnOwner = (turnOwner == TURN_PLAYER ? TURN_ENEMY : TURN_PLAYER);
		activeCharaNo = (turnOwner == TURN_ENEMY ? getFirstEnemyNo() : getFirstCharaNo());
		vsCharaNo = (turnOwner == TURN_ENEMY ? getFirstCharaNo() : getFirstEnemyNo());
	}
	
	/**
	 * 行動者を交代<p>
	 * 行動者がいない場合は、ターン終了
	 */
	private void changeBattler() {
		if (turnOwner == TURN_PLAYER) {
			do {
				activeCharaNo++;
				if (activeCharaNo >= this.playerData.getMemberSize()) {
					changeTurnOwner();
					TMLog.println("敵のターン開始");
					return;
				}
			} while (this.playerData.getCharaToIndex(this.activeCharaNo).isDead());
			vsCharaNo = getFirstEnemyNo();
			setNextCharaSkillMenu();
			TMLog.println("次のプレイヤーへ");
		} else if (turnOwner == TURN_ENEMY) {
			do {
				activeCharaNo++;
				if (activeCharaNo >= enemyList.size()) {
					changeTurnOwner();
					setNextCharaSkillMenu();
					return;
				}
			} while (enemyList.get(activeCharaNo).isDead());
			vsCharaNo = getFirstCharaNo();
		}
	}
	
	/**
	 * キャラクターが持っているスキルの中から選択したスキルを返す
	 * @param status キャラクターのステータス
	 * @param skillNo スキルの選択番号
	 * @return キャラクターが持っているスキル
	 */
	private Skill getSkillIntoChara(CharaStatus status, int skillNo) {
		return status.skillList.get(skillNo);
	}
	
	/**
	 * フィールドにいる敵(死んでいる敵も含む)の最大数から基礎的な画像の中心Xを求める
	 * @return 基礎的な中心X
	 */
	private float getAllEnemyBasicCenterX() {
		int maxEnemy = enemyList.size() + 1;
		return TypingMaster.WIDTH / maxEnemy;
	}
	
	/**
	 * 指定した敵の基礎的な画像の中心Xを求める
	 * @param enemyNo 敵の選択番号
	 * @return 選択された敵の基礎的な中心X
	 */
	private float getEnemyBasicCenterX(int enemyNo) {
		return getAllEnemyBasicCenterX() + getAllEnemyBasicCenterX() * enemyNo;
	}
	
	/**
	 * ターゲットを指定して出力する
	 * @param skill スキル
	 * @return ターゲットMo
	 */
	private int getAttackTarget(Skill skill) {
		int target;
		if (skill.getTargetId() != -1) {
			target = skill.getTargetId();
		} else {
			Random rand = new TMRandom();
			target = rand.nextInt(this.playerData.getMemberSize());
		}
		
		return target;
	}
	
	/**
	 * 与えられた配列型Booleanを論理積で結果を出力する
	 * @param flag 求めたい配列型Boolean
	 * @return すべてがTrueで返された時のみTrue、それ以外はFalse
	 */
	private boolean getAND(boolean[] flag) {
		boolean outf = true;
		for (Boolean f1 : flag) {
			outf &= f1;
		}
		return outf;
	}
	
	/**
	 * ステータスGuiの選手交代時のY座標を出力してくれる
	 * @param guiNo Guiの番号
	 * @param selectedGuiNo 現在選択されているGui番号
	 * @return
	 */
	private float getStatusGuiPosY(int guiNo) {
		float y = 0;
		if (turnOwner == TURN_PLAYER && guiNo == activeCharaNo) {
			y = -20.0F;
		}
		return y;
	}
	
	/**
	 * キャラクター単体が指定した敵に対して与えるダメージを算出し出力する。
	 * @param selectNo 敵指定番号
	 * @return トータルダメージ
	 */
	private int outputDamageFromPlayer(int selectNo) {
		Random rand = new TMRandom();
		CharaStatus chara = this.playerData.getCharaToIndex(activeCharaNo);
		int defDamage = chara.getDamage(); // キャラクターが持つダメージ
		int action = skillMenu.get();
		int totalDamage = 0;
		int skillAmount = chara.skillList.get(action).getSkillAmount(); // スキルによる効果量
		float typingDamage = 0;
		float avoid = 0; // 敵がもつ回避率
		
		switch (skillType) {
		case ATTACK_ALL:
		case ATTACK_ONE:
		case MAGIC_ATC_ALL:
		case MAGIC_ATC_ONE:
			int armDamage = 0; // キャラクターの装備しているアーマーが持つダメージ
			typingDamage = typingCharas * 0.5F + chara.getLevel(); // タイピングした文字数*0.5のダメージ
			int eneDefence = enemyList.get(selectNo).getHardness(); // ターゲットした敵がもつ防御力
			float enemyPenetrate = enemyList.get(selectNo).getPenetrate() * 0.01F; // 敵がダメージ貫通する割合(100で割った値)
			totalDamage = (int) ((defDamage + skillAmount + armDamage + typingDamage) - (eneDefence - (1.0F - enemyPenetrate)));
			avoid = enemyList.get(selectNo).getAvoidancePoint();
			break;
		case EFFECT_ALL:
		case EFFECT_ONE:
			break;
		case HEAL_ALL:
		case HEAL_ONE:
			typingDamage = typingCharas * 0.5F; // タイピングした文字数*0.5の回復量
			totalDamage = (int) (skillAmount + typingDamage * 2);
			break;
		case MINUS_EFFECT_ALL:
		case MINUS_EFFECT_ONE:
			break;
		case REV_ALL:
		case REV_ONE:
			break;
		default:
			break;
		}
		
		if (totalDamage < 1) {
			totalDamage = 1;
		}
		if (typingCharas == 0) {
			totalDamage = 0;
		}
		if ((avoid / 100) > rand.nextFloat()) {
			totalDamage = 0;
		}
		
		return totalDamage;
	}
	
	/**
	 * 敵単体が指定したキャラクターに対して与えるダメージを算出し出力する。
	 * @param selectNo キャラ指定番号
	 * @return トータルダメージ
	 */
	private int outputDamageFromEnemy(int selectNo) {
		int defDamage = enemyList.get(activeCharaNo).getDamage(); // キャラクターが持つダメージ
		int skillAmount = enemyList.get(activeCharaNo).getUseSkill().getSkillAmount(); // スキルによる効果量
		int armDamage = 0; // 敵が装備しているアーマーが持つダメージ(ひょっとしたらなしの可能性あり)
		int chaDefence = this.playerData.getCharaToIndex(selectNo).getHardness();
		int plusDefence = this.playerData.getCharaToIndex(selectNo).getPlusHardness(); // スキルや防御を使った時の防御量
		float charaPenetrate = this.playerData.getCharaToIndex(selectNo).getPenetrate() * 0.01F; // キャラクターがダメージ貫通する割合(100で割った値)
		int totalDamage = (int)((defDamage + skillAmount + armDamage) - (chaDefence + plusDefence - (1.0F - charaPenetrate)));
		if (totalDamage < 1) {
			totalDamage = 1;
		}
		return totalDamage;
	}
	
	/**
	 * 経験値を決定する
	 */
	private void setEXP() {
		if (!enemyList.isEmpty()) {
			int totalExp = 0;
			for (int i = 0; i < enemyList.size(); i++) {
				totalExp += enemyList.get(i).maxExp;
			}
			exp = totalExp / enemyList.size();
		}
	}
	
	/**
	 * レベルアップチェック
	 * @param index 経験値を与えるキャラクター
	 * @return レベルアップしたキャラクターNo
	 */
	private int checkLevelUp(int index) {
		if (index >= this.playerData.getMemberSize()) return -1;
		
		if (!this.playerData.getCharaToIndex(index).isDead()) {
			if (this.playerData.getCharaToIndex(index).addExp(exp)) {
				return index;
			}
		}
		return -1;
	}
	
	
	public void setImage(Image background) {
		this.background = background;
	}
	
	/**
	 * プレイヤーサイドが一人でも生き残っているか
	 * @return 一人でも生き残っているならTrue、それ以外はFalse
	 */
	private boolean getAliveOnPlayerSide() {
		for (int i = 0; i < this.playerData.getMemberSize(); i++) {
			if(!this.playerData.getCharaToIndex(i).isDead()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * エネミーサイドが一体でも生き残っているか
	 * @return 一体でも生き残っているならTrue、それ以外はFalse
	 */
	private boolean getAliveOnEnemySide() {
		for (int i = 0; i < enemyList.size(); i++) {
			if(!enemyList.get(i).isDead()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * エネミーサイドで生存している敵の中で一番左の敵の選択番号を返す
	 * <p>
	 * ありえないが、もし誰もいない場合は-1を返す
	 * @return 一番左の敵のインデックス
	 */
	private int getFirstEnemyNo() {
		for (int i = 0; i < enemyList.size(); i++) {
			if (!enemyList.get(i).isDead()) {
				return i;
			}
		}
		return -1;
	}
	private int getLastEnemyNo() {
		for (int i = enemyList.size() - 1; i >= 0; i--) {
			if (!enemyList.get(i).isDead()) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * プレイヤーサイドで生存しているキャラの中で一番左のキャラの選択番号を返す
	 * <p>
	 * ありえないが、もし誰もいない場合は-1を返す
	 * @return 一番左のキャラのインデックス
	 */
	private int getFirstCharaNo() {
		for (int i = 0; i < this.playerData.getMemberSize(); i++) {
			if (!this.playerData.getCharaToIndex(i).isDead()) {
				return i;
			}
		}
		return -1;
	}
	private int getLastCharaNo() {
		for (int i = this.playerData.getMemberSize() - 1; i >= 0; i--) {
			if (!this.playerData.getCharaToIndex(i).isDead()) {
				return i;
			}
		}
		return -1;
	}
	
}
