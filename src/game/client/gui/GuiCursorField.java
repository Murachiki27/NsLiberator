package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import game.client.image.ImageUtils;
import game.common.data.BasicData;
import game.common.input.EnumKeyInput;
import game.sound.SoundListener;

public class GuiCursorField extends TMGuiScreen {
	// カーソル
	private int cursor[][];
	
	// カーソル座標
	private int x, y;
	// カーソル選択サイズ
	private static final int WIDTH = 13;
	private static final int HEIGHT = 5;
	//private static final int CHARA_W = 11;
	private static final int CHARA_H = 5;
	
	private static final int TYPE_HIRA = 0;
	private static final int TYPE_KANA = 1;
	private static final int TYPE_EISU = 2;
	
	private static final String DAKUON = "かきくけこさしすせそたちつてとはひふへほカキクケコサシスセソタチツテトハヒフヘホ";
	private static final String HANDAKUON = "はひふへほハヒフヘホ";
	private static final String KOMOJI = "あいうえおつやゆよアイウエオツヤユヨ";
	
	private static final char[] HIRAGANA = {
			  'あ', 'い', 'う', 'え', 'お',
			  'か', 'き', 'く', 'け', 'こ',
			  'さ', 'し', 'す', 'せ', 'そ',
		      'た', 'ち', 'つ', 'て', 'と',
		      'な', 'に', 'ぬ', 'ね', 'の',
		      'は', 'ひ', 'ふ', 'へ', 'ほ',
		      'ま', 'み', 'む', 'め', 'も',
		      'や', '　', 'ゆ', '　', 'よ',
		      'ら', 'り', 'る', 'れ', 'ろ',
		      'わ', 'を', 'ん', 'ー', '・',
		      '゛', '゜', '小', '、', '。'};
	
	private static final char[] KATAKANA = {
			  'ア', 'イ', 'ウ', 'エ', 'オ',
			  'カ', 'キ', 'ク', 'ケ', 'コ',
			  'サ', 'シ', 'ス', 'セ', 'ソ',
		      'タ', 'チ', 'ツ', 'テ', 'ト',
		      'ナ', 'ニ', 'ヌ', 'ネ', 'ノ',
		      'ハ', 'ヒ', 'フ', 'ヘ', 'ホ',
		      'マ', 'ミ', 'ム', 'メ', 'モ',
		      'ヤ', '　', 'ユ', '　', 'ヨ',
		      'ラ', 'リ', 'ル', 'レ', 'ロ',
		      'ワ', 'ヲ', 'ン', 'ー', '・',
		      '゛', '゜', '小', '、', '。'};
	
	private static final char[] EISUU_S = {
			  '1', 'Q', 'A', 'Z', 'オ',
			  '2', 'W', 'S', 'X', 'コ',
			  '3', 'E', 'D', 'C', 'ソ',
		      '4', 'R', 'F', 'V', 'ト',
		      '5', 'T', 'G', 'B', 'ノ',
		      '6', 'Y', 'H', 'N', 'ホ',
		      '7', 'U', 'J', 'M', 'モ',
		      '8', 'I', 'K', ',', 'ヨ',
		      '9', 'O', 'L', '.', 'ロ',
		      '0', 'P', '+', '?', '・',
		      '-', '@', '*', '_', '。'};
	
	private static final int LOGO_SIZE = 20;
	private static final int FONT_SIZE = 20;
	private static final int SPACE_SIZE = 40;
	// 白枠の幅
	private static final int EDGE_WIDTH = 2;
	// 一番外側の枠
	//private static Rectangle rectText = new Rectangle(62, 30, 512 + EDGE_WIDTH * 2, 78 + EDGE_WIDTH * 2);
	// 一つ内側の枠（白い枠線ができるように）
	//private static Rectangle innerRectText = new Rectangle(rectText.getX() + EDGE_WIDTH, rectText.getY() + EDGE_WIDTH, rectText.getWidth() - EDGE_WIDTH * 2, rectText.getHeight() - EDGE_WIDTH * 2);
	// 一番外側の枠
	//private static Rectangle rectInput = new Rectangle(30, 126, 576 + EDGE_WIDTH * 2, 248 + EDGE_WIDTH * 2);
	// 一つ内側の枠（白い枠線ができるように）
	//private static Rectangle innerRectInput = new Rectangle(rectInput.getX() + EDGE_WIDTH, rectInput.getY() + EDGE_WIDTH, rectInput.getWidth() - EDGE_WIDTH * 2, rectInput.getHeight() - EDGE_WIDTH * 2);
	
	private GuiWindow textWindow;
	private GuiWindow inputWindow;
	
	private Image logo;
	
	// テキストフィールド
	private char[] text = new char[32];
	
	// 入力文字切り替え
	private int type;
	
	// 入力位置
	private int fieldCursor = 0;
	// 入力している最終位置
	private int maxFieldCursor;
	// 最大入力位置
	private int maxField;
	
	// 初期文
	private String initStr;
	// 説明文
	private String description;
	
	// 決定したかどうか
	private boolean isDone = false;
	
	public GuiCursorField(String description, int maxField) {
		this("", description, maxField);
	}
	
	public GuiCursorField(String str, String description, int maxField) {
		for (int i = 0; i < this.text.length; i++) {
			this.text[i] = ' ';
		}
		loadCursor();
		this.textWindow = new GuiWindow(62.0F, 30.0F, 512 + EDGE_WIDTH * 2, 78 + EDGE_WIDTH * 2);
		this.textWindow.showGui();
		this.inputWindow = new GuiWindow(30.0F, 126.0F, 576 + EDGE_WIDTH * 2, 248 + EDGE_WIDTH * 2);
		this.inputWindow.showGui();
		this.logo = ImageUtils.getGuiImage(GuiImageInfo.INPUT_LOGO);
		if (str != null) {
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				this.text[fieldCursor++] = c;
			}
			this.maxFieldCursor = this.fieldCursor;
			this.initStr = str;
		}
		this.description = description;
		this.maxField = maxField;
	}
	
	@Override
	public void draw(Graphics g) {
		this.textWindow.draw(g);
		this.inputWindow.draw(g);
		g.setColor(Color.white);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
            	float dx = (this.inputWindow.getX() + 37) + (SPACE_SIZE * i);
            	float dy = (this.inputWindow.getY() + 34) + (SPACE_SIZE * j);
            	float fx;
            	Color color;
            	if (cursor[x][y] == cursor[i][j]) {
            		g.drawRect(dx - 2, dy - 2, 24, 24);
            		color = new Color(Color.red);
            	} else {
            		color = new Color(Color.white);
            	}
        		g.setColor(color);
            	if (cursor[i][j] >= 0 && cursor[i][j] < 3) {
            		fx = SPACE_SIZE / 2 - (this.logo.getWidth() / 2);
            		int chipNo = cursor[i][j];
            		int ix = 0;
            		int iy = (chipNo % 3) * LOGO_SIZE;
            		logo.draw(dx + fx, dy, dx + fx + LOGO_SIZE, dy + LOGO_SIZE, ix, iy, ix + LOGO_SIZE, iy + LOGO_SIZE, color);
            	}else if (cursor[i][j] >= 60 && cursor[i][j] < 65) {
            		fx = SPACE_SIZE / 2 - (this.logo.getWidth() / 2);
            		int chipNo = cursor[i][j];
            		int ix = LOGO_SIZE;
            		int iy = (chipNo % 5) * LOGO_SIZE;
            		logo.draw(dx + fx, dy, dx + fx + LOGO_SIZE, dy + LOGO_SIZE, ix, iy, ix + LOGO_SIZE, iy + LOGO_SIZE, color);
            	} else if (cursor[i][j] >= 5 && cursor[i][j] < 60) {
                	char str;
                	if (type == TYPE_HIRA) {
                    	str = HIRAGANA[cursor[i][j] - CHARA_H];
                	} else if (type == TYPE_KANA) {
                		str = KATAKANA[cursor[i][j] - CHARA_H];
                	} else {
                		str = EISUU_S[cursor[i][j] - CHARA_H];
                	}
                	fx = FONT_SIZE / 2 - (this.getFontWidth(str) / 2);
                	//float fy = (this.getFontHeight(str) / 2);
                	g.drawString(String.valueOf(str), dx + fx, dy);
            	}
            }
        }
		g.setColor(Color.white);
		g.drawString(this.description, (this.textWindow.getCenterX() - (BasicData.defaultUfont.getWidth(this.description) / 2)), 40);
		for (int i = 0; i < maxField; i++) {
			float dx = (this.textWindow.getCenterX() - (this.maxField * FONT_SIZE / 2)) + (FONT_SIZE * i);
			g.setColor(new Color(0.3F, 0.3F, 0.3F));
			g.fillRect(dx, 70, 18, 25);
		}
		g.setColor(Color.white);
		for (int i = 0; i < maxFieldCursor; i++) {
			char c = text[i];
        	float dx = (this.textWindow.getCenterX() - (this.maxField * FONT_SIZE / 2)) + (FONT_SIZE * i);
			g.drawString(String.valueOf(c), dx, 70);
		}
		if (fieldCursor < maxField) {
			g.drawString("__", (this.textWindow.getCenterX() - (this.maxField * FONT_SIZE / 2)) + (FONT_SIZE * fieldCursor), 80);
		}
	}
	
	@Override
	public void keyPressed(int key, char c, EnumKeyInput input) {
		switch (input) {
		case KEY_DONE:
			SoundListener.play(1);
			enter();
			break;
		case KEY_DOWN:
			SoundListener.play(0);
			y++;
			if (y >= HEIGHT) {
				y = 0;
			}
			break;
		case KEY_LEFT:
			SoundListener.play(0);
			x--;
			if (x < 0) {
				x = WIDTH - 1;
			}
			break;
		case KEY_RIGHT:
			SoundListener.play(0);
			x++;
			if (x >= WIDTH) {
				x = 0;
			}
			break;
		case KEY_UP:
			SoundListener.play(0);
			y--;
			if (y < 0) {
				y = HEIGHT - 1;
			}
			break;
		default:
			break;
		}
	}
	
	private void enter() {
		if (cursor[x][y] > 4 && cursor[x][y] < 60) {
			if (fieldCursor < maxField) {
				if ((cursor[x][y] > 54 && cursor[x][y] < 58) && (type == TYPE_HIRA || type == TYPE_KANA)) {
					mergeChar();
				} else {
					switch (type) {
					case TYPE_HIRA:
						text[fieldCursor] = HIRAGANA[cursor[x][y] - CHARA_H];
						break;
					case TYPE_KANA:
						text[fieldCursor] = KATAKANA[cursor[x][y] - CHARA_H];
						break;
					case TYPE_EISU:
						text[fieldCursor] = EISUU_S[cursor[x][y] - CHARA_H];
						break;
					}
					fieldCursor++;
					this.maxFieldCursor = this.fieldCursor;
				}
			}
		} else {
			switch (cursor[x][y]) {
			case TYPE_HIRA:
				type = TYPE_HIRA;
				break;
			case TYPE_KANA:
				type = TYPE_KANA;
				break;
			case TYPE_EISU:
				type = TYPE_EISU;
				break;
			case 60:
				remove();
				break;
			case 61:
				space();
				break;
			case 62:
				skip(1);
				break;
			case 63:
				skip(0);
				break;
			case 64:
				this.isDone = true;
				break;
			}
		}
	}
	
	private void mergeChar() {
		if (cursor[x][y] == 55) {
			if (DAKUON.indexOf(text[fieldCursor]) > -1) {
				switch (text[fieldCursor]) {
				case 'か': text[fieldCursor] = 'が'; break;
				case 'き': text[fieldCursor] = 'ぎ'; break;
				case 'く': text[fieldCursor] = 'ぐ'; break;
				case 'け': text[fieldCursor] = 'げ'; break;
				case 'こ': text[fieldCursor] = 'ご'; break;
				case 'さ': text[fieldCursor] = 'ざ'; break;
				case 'し': text[fieldCursor] = 'じ'; break;
				case 'す': text[fieldCursor] = 'ず'; break;
				case 'せ': text[fieldCursor] = 'ぜ'; break;
				case 'そ': text[fieldCursor] = 'ぞ'; break;
				case 'た': text[fieldCursor] = 'だ'; break;
				case 'ち': text[fieldCursor] = 'ぢ'; break;
				case 'つ': text[fieldCursor] = 'づ'; break;
				case 'て': text[fieldCursor] = 'で'; break;
				case 'と': text[fieldCursor] = 'ど'; break;
				case 'は': text[fieldCursor] = 'ば'; break;
				case 'ひ': text[fieldCursor] = 'び'; break;
				case 'ふ': text[fieldCursor] = 'ぶ'; break;
				case 'へ': text[fieldCursor] = 'べ'; break;
				case 'ほ': text[fieldCursor] = 'ぼ'; break;
				case 'カ': text[fieldCursor] = 'ガ'; break;
				case 'キ': text[fieldCursor] = 'ギ'; break;
				case 'ク': text[fieldCursor] = 'グ'; break;
				case 'ケ': text[fieldCursor] = 'ゲ'; break;
				case 'コ': text[fieldCursor] = 'ゴ'; break;
				case 'サ': text[fieldCursor] = 'ザ'; break;
				case 'シ': text[fieldCursor] = 'ジ'; break;
				case 'ス': text[fieldCursor] = 'ズ'; break;
				case 'セ': text[fieldCursor] = 'ゼ'; break;
				case 'ソ': text[fieldCursor] = 'ゾ'; break;
				case 'タ': text[fieldCursor] = 'ダ'; break;
				case 'チ': text[fieldCursor] = 'ヂ'; break;
				case 'ツ': text[fieldCursor] = 'ヅ'; break;
				case 'テ': text[fieldCursor] = 'デ'; break;
				case 'ト': text[fieldCursor] = 'ド'; break;
				case 'ハ': text[fieldCursor] = 'バ'; break;
				case 'ヒ': text[fieldCursor] = 'ビ'; break;
				case 'フ': text[fieldCursor] = 'ブ'; break;
				case 'ヘ': text[fieldCursor] = 'ベ'; break;
				case 'ホ': text[fieldCursor] = 'ボ'; break;
				}
			}
		} else if (cursor[x][y] == 56) {
			if (HANDAKUON.indexOf(text[fieldCursor]) > -1) {
				switch (text[fieldCursor]) {
				case 'は': text[fieldCursor] = 'ぱ'; break;
				case 'ひ': text[fieldCursor] = 'ぴ'; break;
				case 'ふ': text[fieldCursor] = 'ぷ'; break;
				case 'へ': text[fieldCursor] = 'ぺ'; break;
				case 'ほ': text[fieldCursor] = 'ぽ'; break;
				case 'ハ': text[fieldCursor] = 'パ'; break;
				case 'ヒ': text[fieldCursor] = 'ピ'; break;
				case 'フ': text[fieldCursor] = 'プ'; break;
				case 'ヘ': text[fieldCursor] = 'ペ'; break;
				case 'ホ': text[fieldCursor] = 'ポ'; break;
				}
			}
		} else if (cursor[x][y] == 57) {
			if (KOMOJI.indexOf(text[fieldCursor]) > -1) {
				switch (text[fieldCursor]) {
				case 'あ': text[fieldCursor] = 'ぁ'; break;
				case 'い': text[fieldCursor] = 'ぃ'; break;
				case 'う': text[fieldCursor] = 'ぅ'; break;
				case 'え': text[fieldCursor] = 'ぇ'; break;
				case 'お': text[fieldCursor] = 'ぉ'; break;
				case 'つ': text[fieldCursor] = 'っ'; break;
				case 'や': text[fieldCursor] = 'ゃ'; break;
				case 'ゆ': text[fieldCursor] = 'ゅ'; break;
				case 'よ': text[fieldCursor] = 'ょ'; break;
				case 'ア': text[fieldCursor] = 'ァ'; break;
				case 'イ': text[fieldCursor] = 'ィ'; break;
				case 'ウ': text[fieldCursor] = 'ゥ'; break;
				case 'エ': text[fieldCursor] = 'ェ'; break;
				case 'オ': text[fieldCursor] = 'ォ'; break;
				case 'ツ': text[fieldCursor] = 'ッ'; break;
				case 'ヤ': text[fieldCursor] = 'ャ'; break;
				case 'ユ': text[fieldCursor] = 'ュ'; break;
				case 'ヨ': text[fieldCursor] = 'ョ'; break;
				}
			}
		}
	}
	
	private void remove() {
		if (fieldCursor > 0) {
			text[fieldCursor - 1] = ' ';
			fieldCursor--;
			this.maxFieldCursor = this.fieldCursor;
		}
	}
	
	private void space() {
		if (fieldCursor < maxField) {
			text[fieldCursor] = '　';
			fieldCursor++;
			this.maxFieldCursor = this.fieldCursor;
		}
	}
	
	private void skip(int dir) {
		if (dir == 0) {
			if (fieldCursor > 0) {
				fieldCursor--;
			}
		} else {
			if (fieldCursor < maxFieldCursor) {
				fieldCursor++;
			}
		}
	}
	
	public void reset() {
		x = 0;
		y = 0;
		this.maxFieldCursor = this.fieldCursor = 0;
		this.isDone = false;
        for (int i = 0; i < this.text.length; i++) {
            this.text[i] = ' ';
        }
		if (this.initStr != null) {
			for (int i = 0; i < this.initStr.length(); i++) {
				char c = this.initStr.charAt(i);
				this.text[fieldCursor++] = c;
			}
			this.maxFieldCursor = this.fieldCursor;
		}
	}
	
	public String checkDoneString(boolean isDone) {
		if (isDone) {
			this.isDone = false;
			return String.valueOf(this.text).trim();
		}
		return null;
	}
	
	public boolean checkEnter() {
		return this.isDone;
	}
	
	private int getFontWidth(char str) {
		return BasicData.defaultUfont.getWidth(String.valueOf(str));
	}
	
	private void loadCursor() {
		cursor = new int[WIDTH][HEIGHT];
		int coord = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
            	cursor[i][j] = coord++;
            }
        }
	}
}
