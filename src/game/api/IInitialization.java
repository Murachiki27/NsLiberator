package game.api;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import game.common.input.EnumKeyInput;

public interface IInitialization {
	/**
	 * LoadingStateクラスのロードが終わってから別途で呼ばれる初期化メソッド
	 * @param gc
	 * @param sbg
	 */
	public void loadInit(GameContainer gc, StateBasedGame sbg) throws SlickException;
	
	/**
	 * TM用に上書きしたもの
	 * @param key
	 * @param c
	 * @param input
	 */
	public void keyPressed(int key, char c, EnumKeyInput input);
	
	/**
	 * TM用に上書きしたもの
	 * @param key
	 * @param c
	 * @param input
	 */
	public void keyReleased(int key, char c, EnumKeyInput input);
}
