package game.client.gui;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import game.common.data.BasicData;

/**
 * Ver2.0以前の会話ウィンドウを作るクラス<p>
 * Ver2.0からGui定義になったため、このクラスは廃止されました。
 * 
 * @author Murachiki27
 * @deprecated
 * @see game.client.gui.GuiMessageWindow
 * 
 */
@Deprecated
public class MessageWindow {
    // 白枠の幅
    private static final int EDGE_WIDTH = 2;
    
    // フォントの大きさ
    public static final int FONT_WIDTH = 16;
    public static final int FONT_HEIGHT = 22;
    
    // 行間の大きさ
    protected static final int LINE_HEIGHT = 8;
    // 1行の最大文字数
    private static final int MAX_CHAR_IN_LINE = 20;
    // 1ページに表示できる最大行数
    private static final int MAX_LINES = 3;
    // 1ページに表示できる最大文字数
    private static final int MAX_CHAR_IN_PAGE = MAX_CHAR_IN_LINE * MAX_LINES;
    // 一番外側の枠
    private Rectangle rect;
    // 一つ内側の枠（白い枠線ができるように）
    private Rectangle innerRect;
    // 実際にテキストを描画する枠
    private Rectangle textRect;

    // メッセージウィンドウを表示中か
    private boolean isVisible = false;

    // カーソルのアニメーションGIF
    private Image cursorImage;
    
    // メッセージを格納した配列
    private char[] text = new char[128 * MAX_CHAR_IN_LINE];
    // 最大ページ
    private int maxPage;
    // 現在表示しているページ
    private int curPage = 0;
    // 表示した文字数
    private int curPos;
    // 次のページへいけるか（▼が表示されてればtrue）
    private boolean nextFlag = false;

    // テキストを流すタイマータスク
    private Timer timer;
    private TimerTask task;
    
    public MessageWindow(Rectangle rect) {
        this.rect = rect;

        innerRect = new Rectangle(
                rect.getX() + EDGE_WIDTH,
                rect.getY() + EDGE_WIDTH,
                rect.getWidth() - EDGE_WIDTH * 2,
                rect.getHeight() - EDGE_WIDTH * 2);

        textRect = new Rectangle(
                innerRect.getX() + 16,
                innerRect.getY() + 16,
                320,
                120);        
		try {
			cursorImage = new Image("img/cursor.gif");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
        timer = new Timer();
    }
    
    public void draw(Graphics g) {
        if (isVisible == false) return;
        
        // 枠を描く
        g.setColor(Color.white);
        g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        // 内側の枠を描く
        g.setColor(Color.black);
        g.fillRect(innerRect.getX(), innerRect.getY(), innerRect.getWidth(), innerRect.getHeight());
        
        g.setColor(Color.white);
        // 現在のページ（curPage）のcurPosまでの内容を表示
        for (int i=0; i < curPos; i++) {
            char c = text[curPage * MAX_CHAR_IN_PAGE + i];
            int dx = (int)(textRect.getX() + (FONT_WIDTH / 2 - getFontWidth(c) / 2) + FONT_WIDTH * (i % MAX_CHAR_IN_LINE));
            int dy = (int) (textRect.getY() + (LINE_HEIGHT + FONT_HEIGHT * (i / MAX_CHAR_IN_LINE)));
            g.drawString(String.valueOf(c), dx, dy);
        }
        g.setColor(Color.white);
        // 最後のページでない場合は矢印を表示する
        if (curPage <= maxPage && nextFlag) {
            int dx = (int)(textRect.getX() + (MAX_CHAR_IN_LINE / 2) * FONT_WIDTH - 8);
            int dy = (int)(textRect.getY() + (LINE_HEIGHT + FONT_HEIGHT) * 3);
            g.drawImage(cursorImage, dx, dy);
        }
    }

    /**
     * メッセージをセットする
     * @param msg メッセージ
     */
    public void setMessage(String msg) {
        curPos = 0;
        curPage = 0;
        nextFlag = false;

        // 全角スペースで初期化
        for (int i=0; i<text.length; i++) {
            text[i] = '　';
        }

        int p = 0;  // 処理中の文字位置
        for (int i=0; i<msg.length(); i++) {
            char c = msg.charAt(i);
            if (c == '\\') {
                i++;
                if (msg.charAt(i) == 'n') {  // 改行
                    p += MAX_CHAR_IN_LINE;
                    p = (p / MAX_CHAR_IN_LINE) * MAX_CHAR_IN_LINE;
                } else if (msg.charAt(i) == 'f') {  // 改ページ
                    p += MAX_CHAR_IN_PAGE;
                    p = (p / MAX_CHAR_IN_PAGE) * MAX_CHAR_IN_PAGE;
                } else if (msg.charAt(i) == 'p') { // プレイヤーネーム
                	String name = BasicData.getStatsData().getUserName();
                	for (int j = 0; j < name.length(); j++) {
                		char nc = name.charAt(j);
                		text[p++] = nc;
                	}
                }
            } else {
                text[p++] = c;
            }
        }
        
        maxPage = p / MAX_CHAR_IN_PAGE;
        
        // 文字を流すタスクを起動
        task = new DrawingMessageTask();
        timer.schedule(task, 0L, 20L);
    }
    
    /**
     * メッセージを先に進める
     * @return メッセージが終了したらtrueを返す
     */
    public boolean nextMessage() {
        // 現在ページが最後のページだったらメッセージを終了する
        if (curPage == maxPage && nextFlag) {
            task.cancel();
            task = null;  // タスクは終了しないと動き続ける
            return true;
        }
        // ▼が表示されてなければ次ページへいけない
        if (nextFlag && curPage < maxPage) {
            curPage++;
            curPos = 0;
            nextFlag = false;
        }
        return false;
    }
    
	private int getFontWidth(char c) {
		return BasicData.defaultUfont.getWidth(String.valueOf(c));
	}
    
    /**
     * ウィンドウを表示
     */
    public void show() {
        isVisible = true;
    }

    /**
     * ウィンドウを隠す
     */
    public void hide() {
        isVisible = false;
    }
    
    public boolean isAllVisble() {
    	return curPage == maxPage && nextFlag;
    }
    
    /**
     * ウィンドウを表示中か
     */
    public boolean isVisible() {
        return isVisible;
    }
    
    public boolean isEndpage() {
    	return curPage == maxPage;
    }
    
    // メッセージを1文字ずつ順に描画するタスク
    class DrawingMessageTask extends TimerTask {
        public void run() {
            if (!nextFlag) {
                char c = text[curPage * MAX_CHAR_IN_PAGE + curPos];
                if (c == '　') { // 無駄な処理を省くため、全角スペース(いわゆる改行、改ページなどで入力されなかったところ)があったときは、改行する。
                	curPos += MAX_CHAR_IN_LINE;
                	curPos = (curPos / MAX_CHAR_IN_LINE) * MAX_CHAR_IN_LINE;
                } else {
                    curPos++;  // 1文字増やす
                }
                // 1ページの文字数になったら▼を表示
                if (curPos >= MAX_CHAR_IN_PAGE) {
                    nextFlag = true;
                }
            }
        }
    }
}
