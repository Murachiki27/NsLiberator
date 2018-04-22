package game.client.battle;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import game.client.chara.BattleChara;
import game.client.chara.BattleEnemy;
import game.client.gui.GuiBattleStatus;
import game.client.gui.GuiEnemySkillWindow;
import game.client.gui.GuiMenuBox;
import game.client.gui.GuiMessageWindow;
import game.client.image.ImageUtils;
import game.common.data.BasicData;
import game.common.image.BattleImageItem;
import game.common.input.EnumKeyInput;
import game.common.input.TypingKeyListener;

public class TMBattleState {
	
	private enum TurnState { EMPTY, PLAYER, ENEMY }
	
	private enum SelectMenu { MENU_EMPTY, MENU_MAIN, MENU_ITEM, MENU_SELECT }
	
	private enum PlayerAction { AP_NONE, AP_SKILL, AP_ITEM, AP_DEFENCE, AP_EXIT }
	
	private enum EnemyAction { AE_NONE, AE_SKILL, AE_ITEM, AE_EVENT, AE_DEFENCE, AE_EXIT }
	
	private enum PlayerPart { PP_EMPTY, PP_ACT_START, PP_ACT_UPDATE, PP_ACT_END, PP_ACT_ANI, PP_DMG_ANI, PP_END }
	
	private enum EnemyPart { PE_EMPTY, PE_ACT_EVENT, PE_ACT_NAME, PE_ACT_ANI, PE_DMG_ANI, PE_END_EVENT, PE_END }
	
	/** 現在のターン数 */
	private int currentTurn = 0;
	/** 現在のターン権限側 */
	private TurnState currentTurnOwner;
	/** 現在の選択できるメニュー */
	private SelectMenu selectMenu;
	/** プレイヤーの行動 */
	private PlayerAction playerAction;
	/** 敵側の行動 */
	private EnemyAction enemyAction;
	/** プレイヤー側のバトル進行 */
	private PlayerPart playerPart;
	/** 敵側のバトル進行 */
	private EnemyPart enemyPart;
	
	
	private CharacterList<BattleChara> playerList;
	private EnemyList<BattleEnemy> enemyList;
	
	
	
	
	private Image background;
	private Image selectCursor;
	private Image timerIcon;
	
	private BattleImageItem[] weaponImages;
	
	private GuiEnemySkillWindow skillNameWindow;
	private GuiMessageWindow eventMessageWindow;
	private GuiMessageWindow resultMessageWindow;
	
	private GuiMenuBox actionMenu, skillMenu, itemMenu;
	
	
	private GuiBattleStatus[] charaGui;
	
	
	private TypingKeyListener typingKey;
	
	
	public TMBattleState() {
		this.playerList = new CharacterList<>();
		this.enemyList = new EnemyList<>();
		
		this.weaponImages = new BattleImageItem[4];
		this.charaGui = new GuiBattleStatus[4];
	}
	
	public void init() {
		for (int i = 0; i < this.weaponImages.length; i++) {
			this.weaponImages[i] = new BattleImageItem();
		}
		
		this.eventMessageWindow = new GuiMessageWindow(142.0F, 16.0F, 0, 160, 384, 64);
	}
	
	public void enter() {
		for (int i = 0; i < this.weaponImages.length; i++) {
			this.weaponImages[i].setImage(ImageUtils.getWeaponImage(0));
		}
		for (int i = 0; i < BasicData.getPlayerData().getMemberSize(); i++) {
			BattleChara chara = BasicData.getPlayerData().getCharaToIndex(i);
			this.playerList.add(chara);
			this.charaGui[i] = new GuiBattleStatus(chara, i);
		}
		
		
		
	}
	
	public void leave() {
		this.currentTurn = 0;
		this.currentTurnOwner = TurnState.EMPTY;
		this.selectMenu = SelectMenu.MENU_EMPTY;
		this.playerAction = PlayerAction.AP_NONE;
		this.enemyAction = EnemyAction.AE_NONE;
		this.playerPart = PlayerPart.PP_EMPTY;
		this.enemyPart = EnemyPart.PE_EMPTY;
		
		this.playerList.clear();
		this.enemyList.clear();
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < BasicData.getPlayerData().getMemberSize(); i++) {
			float dy = getStatusGuiPosY(i);
			charaGui[i].setPopup(dy);
			charaGui[i].draw(g);
		}
	}
	
	public void update(int delta) {
		switch (this.currentTurnOwner) {
		case EMPTY:
			break;
		case ENEMY:
			break;
		case PLAYER:
			break;
		default:
			break;
		}
	}
	
	public void updatePlayer(int delta) {
		switch (this.playerPart) {
		case PP_ACT_START:
			
			break;
		case PP_ACT_UPDATE:
			break;
		case PP_ACT_END:
			break;
		case PP_ACT_ANI:
			break;
		case PP_DMG_ANI:
			break;
		case PP_END:
			break;
		case PP_EMPTY:
			break;
		default:
			break;
		}
	}
	
	public void updateEnemy(int delta) {
		switch (this.enemyPart) {
		case PE_ACT_EVENT:
			if (this.enemyList.firstEventUpdate(this.currentTurn)) {
				this.enemyPart = EnemyPart.PE_ACT_NAME;
				
				this.skillNameWindow.setText(this.enemyList.getSkill().getName());
			}
			break;
		case PE_ACT_NAME:
			if (this.skillNameWindow.update(delta)) {
				this.skillNameWindow.reset();
				this.enemyPart = EnemyPart.PE_ACT_ANI;
			}
			break;
		case PE_ACT_ANI:
			break;
		case PE_DMG_ANI:
			break;
		case PE_END_EVENT:
			break;
		case PE_END:
			this.enemyList.next();
			if (this.enemyList.canActionQuickness(this.playerList.getStatus())) {
				this.enemyPart = EnemyPart.PE_ACT_EVENT;
			} else {
				this.enemyPart = EnemyPart.PE_EMPTY;
				this.changeTurnOwner();
			}
			break;
		case PE_EMPTY:
			break;
		default:
			break;
		}
	}
	
	public void keyPressed(int key, char c, EnumKeyInput input) {
		if (this.eventMessageWindow.isVisible()) {
			this.eventMessageWindow.keyPressed(key, c, input);
		}
	}
	
	
	public void addEnemy(BattleEnemy enemy) {
		this.enemyList.add(enemy);
	}
	
	public void setTypingWord(String word) {
		this.typingKey.setCharacter(word);
	}
	
	public void setTypingWordToMap() {
		String word = BasicData.getMapData().getMapToId(BasicData.getPlayerData().stayMapNo).getWord();
		this.typingKey.setCharacter(word);
	}
	
	public void changeBackground() {
		
	}
	
	public void setMessage(String name, String message) {
		this.eventMessageWindow.setMessage(name, message);
		this.eventMessageWindow.showGui();
	}
	
	public void changeTurnOwner() {
		this.currentTurnOwner = (this.currentTurnOwner == TurnState.ENEMY ? TurnState.PLAYER : TurnState.ENEMY);
	}
	
	private float getStatusGuiPosY(int guiNo) {
		float y = 0;
		if (this.currentTurnOwner == TurnState.PLAYER && guiNo == this.playerList.getCurrentCharaIndex()) {
			y = -20.0F;
		}
		return y;
	}
}
