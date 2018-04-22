package game.api;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import game.common.input.EnumKeyInput;
import game.common.settings.Options;

/**
 * 
 * Slick2d用に開発されたBasicGameStateクラスをTM用に書き換えたもの<p>
 * Slick2dの場合、init()メソッドがTMオブジェクトを作る前に読み込んでしまうため<br>
 * NullPointerExceptionが働いてしまうので、それを防ぐ為にinit()メソッドを非推奨にして<br>
 * 新たにloadInit()メソッドを作り、定義などを行う。
 * 
 * @author kevin
 * @author Murachiki27
 *
 */
public abstract class TMGameState extends BasicGameState implements IInitialization {
	/**
	 * 
	 * @deprecated TM用のオブジェクトをここで定義するとNullPointerExceptionが働くため、<br>
	 * なるべく使用しないでください。
	 */
	@Deprecated
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {}
	
	@Override
	public void keyPressed(int key, char c) {
		EnumKeyInput keyInput = EnumKeyInput.KEY_EMPTY;
		if (key == Options.KEY_UP) {
			keyInput = EnumKeyInput.KEY_UP;
		}
		if (key == Options.KEY_DOWN) {
			keyInput = EnumKeyInput.KEY_DOWN;
		}
		if (key == Options.KEY_LEFT) {
			keyInput = EnumKeyInput.KEY_LEFT;
		}
		if (key == Options.KEY_RIGHT) {
			keyInput = EnumKeyInput.KEY_RIGHT;
		}
		if (key == Options.KEY_ENTER) {
			keyInput = EnumKeyInput.KEY_DONE;
		}
		if (key == Options.KEY_BACK) {
			keyInput = EnumKeyInput.KEY_CANCEL;
		}
		if (key == Options.KEY_INV) {
			keyInput = EnumKeyInput.KEY_MENU;
		}
		this.keyPressed(key, c, keyInput);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		EnumKeyInput keyInput = EnumKeyInput.KEY_EMPTY;
		if (key == Options.KEY_UP) {
			keyInput = EnumKeyInput.KEY_UP;
		}
		if (key == Options.KEY_DOWN) {
			keyInput = EnumKeyInput.KEY_DOWN;
		}
		if (key == Options.KEY_LEFT) {
			keyInput = EnumKeyInput.KEY_LEFT;
		}
		if (key == Options.KEY_RIGHT) {
			keyInput = EnumKeyInput.KEY_RIGHT;
		}
		if (key == Options.KEY_ENTER) {
			keyInput = EnumKeyInput.KEY_DONE;
		}
		if (key == Options.KEY_BACK) {
			keyInput = EnumKeyInput.KEY_CANCEL;
		}
		if (key == Options.KEY_INV) {
			keyInput = EnumKeyInput.KEY_MENU;
		}
		
		this.keyReleased(key, c, keyInput);
	}
}
