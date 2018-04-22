package game.client.gui;

import java.util.Arrays;
import java.util.Collection;

import game.common.data.EnumDifficulty;

public enum GuiMenuPack {
	DEFAULT("はい", "いいえ"),
	ITEM_USE("使う", "捨てる"),
	DIFFICULTY(EnumDifficulty.EASY.getDifficultyName(), EnumDifficulty.NORMAL.getDifficultyName(), EnumDifficulty.HARD.getDifficultyName()),
	TITLE_SELECT("はじめから", "つづきから", "設定", "終了"),
	OPTION_SELECT("BGM", "効果音", "画面サイズ", "キー設定リセット", "アップデート通知"),
	DISPLAY_SELECT("640 × 480", "800 × 600", "1280 × 960", "フルスクリーン"),
	POSE_SELECT("アイテム", "そうび", "ステータス", "ずかん", "マップ", "タイトルへ"),
	BATTLE_SELECT("こうげき", "アイテム", "ぼうぎょ", "にげる"),
	;
	
	private final String[] menuPack;
	
	private GuiMenuPack(String... menuPack) {
		this.menuPack = menuPack;
	}
	
	public Collection<String> getPack() {
		return Arrays.asList(this.menuPack);
	}
	
	public String[] getPackToArray() {
		return this.menuPack;
	}
}
