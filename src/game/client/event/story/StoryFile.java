package game.client.event.story;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import game.story.StoryList;

public class StoryFile implements Serializable {
	private static final long serialVersionUID = 1L;

	public List<String> storylists = new ArrayList<String>();
	
	private List<String> funclists = new ArrayList<String>();
	
	private List<StoryList> lists = new ArrayList<StoryList>();
	
	//ストーリーファイルのパス
	private String filePass;
	
	public StoryFile(String fileName) {
		loadFile(fileName);
		this.filePass = fileName;
	}
	private void loadFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                // 空行は読み飛ばす
                if (line.equals(""))
                    continue;
                // コメント行は読み飛ばす
                if (line.startsWith("//"))
                    continue;
                
                // 関数文
                if (line.startsWith("#define ")) {
                	StringTokenizer fst = new StringTokenizer(line.substring(8), ":");
                	String function = fst.nextToken();
                    if (function == null) {
                    	System.out.println("NullFunction");
                    } else {
                    	switch (function) {
                        case "move":
                        	String name = fst.nextToken(",").substring(1);
                        	funclists.add(name);
                        	break;
                        }
                    }
                    continue;
                }
                
                StringTokenizer st = new StringTokenizer(line, ",");
                // イベント情報を取得する
                // イベントタイプを取得してイベントごとに処理する
                int part = Integer.parseInt(st.nextToken());
                String eventType = st.nextToken();
                if (eventType == null) {
                	System.out.println("NullAction");
                } else {
                    switch (eventType) {
                    case "MOVE":
                    	makeMove(st, part); // 移動アクション
                    	break;
                    case "MAP":
                    	makeMoveMap(st, part); // マップ移動アクション
                    	break;
                    case "TALK":
                    	makeTalk(st, part); // 会話アクション
                    	break;
                    case "WORD":
                    	makeWord(st, part); // 字幕アクション
                    	break;
                    case "FIGHT":
                    	makeFight(st, part); // 戦闘アクション
                    	break;
                    case "CLOSE":
                    	makeClose(st, part); // 終了&削除アクション
                    	break;
                    case "END":
                    	makeEnd(st, part); // 終了アクション
                    	break;
                    case "WAIT":
                    	makeWait(st, part); // インターバルアクション
                    	break;
                    case "SCAN":
                    	makeScan(st, part); // 検知アクション
                    	break;
                    case "SKIP":
                    	makeSkip(st, part); // スキップアクション
                    	break;
                    case "LOCKSCROLL":
                    	makeLockScroll(st, part); // 固定スクロールアクション
                    	break;
                    case "SCROLL":
                    	makeScroll(st, part); // スクロールアクション
                    	break;
                    case "VIBRATION":
                    	makeVibration(st, part); // 振動アクション
                    	break;
                    case "GROUP":
                    	makeGroupAction(st, part); // 同時処理アクション
                    	break;
                    case "CHARA":
                    	meakChara(st, part); // キャラクター管理アクション
                    	break;
                    case "INVENTORY":
                    	meakInventory(st, part); // インベントリ管理アクション
                    	break;
                    case "PARTICLE":
                    	meakParticle(st, part); // マップパーティクルアクション
                    	break;
                    case "STATUS":
                    	meakStatus(st, part); // キャラステータス管理アクション
                    	break;
                    case "QUEST":
                    	makeQuest(st, part); // クエスト管理アクション
                    	break;
                    }
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	private void makeMove(StringTokenizer st, int part) {
        // 対象のID
        int charaId = Integer.parseInt(st.nextToken());
        // 向き
        int dir = Integer.parseInt(st.nextToken());
        // 移動距離
        int distance = Integer.parseInt(st.nextToken());
        // スピード
        int speed = Integer.parseInt(st.nextToken());
        
		StoryList list = new StoryList(charaId, dir, distance, speed);
		lists.add(list);
		storylists.add(part, "MOVE");
	}
	private void makeMoveMap(StringTokenizer st, int part) {
        // 対象のID
        int charaId = Integer.parseInt(st.nextToken());
        // 移動先のマップ番号
        int destMapNo = Integer.parseInt(st.nextToken());
        // キャラの向き
        int dir = Integer.parseInt(st.nextToken());
        // 移動先のX座標
        int destX = Integer.parseInt(st.nextToken());
        // 移動先のY座標
        int destY = Integer.parseInt(st.nextToken());
		StoryList list = new StoryList(charaId, destMapNo, dir, destX, destY);
		lists.add(list);
		storylists.add(part, "MAP");
	}
	private void makeTalk(StringTokenizer st, int part) {
		// キャラクターID
		int charaId = Integer.valueOf(st.nextToken());
        // メッセージ
        String message = st.nextToken();
        StoryList list = new StoryList(charaId, message);
        lists.add(list);
        storylists.add(part, "TALK");
	}
	private void makeWord(StringTokenizer st, int part) {
        // メッセージ
        String message = st.nextToken();
        StoryList list = new StoryList(message);
        lists.add(list);
        storylists.add(part, "WORD");
	}
	private void makeFight(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		for (int i = 0;i < 4; i++) {
			list.setEnemy(i, Integer.parseInt(st.nextToken()));
		}
		list.setFight();
		lists.add(list);
		storylists.add(part, "FIGHT");
	}
	private void makeClose(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		lists.add(list);
		storylists.add(part, "CLOSE");
	}
	private void makeEnd(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		list.setEnd();
		lists.add(list);
		storylists.add(part, "END");
	}
	private void makeWait(StringTokenizer st, int part) {
		// インターバルタイム
        int interval = Integer.parseInt(st.nextToken());
		StoryList list = new StoryList();
		list.setTime(interval);
		lists.add(list);
		storylists.add(part, "WAIT");
	}
	private void makeScan(StringTokenizer st, int part) {
		// 条件読み込み
		String type = st.nextToken();
		StoryList list = new StoryList();
		list.type = type;
		if (type != null) {
			switch (type) {
			case "ITEM":
				list.itemId = Integer.parseInt(st.nextToken());
				list.stack = Integer.parseInt(st.nextToken());
				list.limitType = st.nextToken();
				break;
			case "ARMOR":
				break;
			case "EVENT":
				break;
			case "QUEST":
				break;
			case "LIBRARY":
				String subType = st.nextToken();
				list.subType = subType;
				if (subType != null) {
					switch (subType) {
					case "COUNT":
						list.libId = Integer.parseInt(st.nextToken());
						list.stack = Integer.parseInt(st.nextToken());
						list.limitType = st.nextToken();
						break;
					case "SIZE":
						list.stack = Integer.parseInt(st.nextToken());
						list.limitType = st.nextToken();
						break;
					}
				}
				break;
			}
		}
		list.trueNo = Integer.parseInt(st.nextToken());
		list.falseNo = Integer.parseInt(st.nextToken());
		lists.add(list);
		storylists.add(part, "SCAN");
	}
	private void makeSkip(StringTokenizer st, int part) {
		// スキップする番地
        int skip = Integer.parseInt(st.nextToken());
		StoryList list = new StoryList(skip);
		lists.add(list);
		storylists.add(part, "SKIP");
	}
	private void makeLockScroll(StringTokenizer st, int part) {
		// 固定スクロールか
        boolean isLockScroll = Boolean.valueOf(st.nextToken());
		StoryList list = new StoryList(isLockScroll);
		lists.add(list);
		storylists.add(part, "LOCKSCROLL");
	}
	private void makeScroll(StringTokenizer st, int part) {
		// スクロールする距離
        int scrollX = Integer.parseInt(st.nextToken());
        int scrollY = Integer.parseInt(st.nextToken());
		StoryList list = new StoryList(scrollX, scrollY);
		lists.add(list);
		storylists.add(part, "SCROLL");
	}
	private void makeVibration(StringTokenizer st, int part) {
        int vibType = Integer.parseInt(st.nextToken());
        float vibStrength = Float.parseFloat(st.nextToken());
		StoryList list = new StoryList();
		list.vibType = vibType;
		list.vibStrength =vibStrength;
		lists.add(list);
		storylists.add(part, "VIBRATION");
	}
	private void makeGroupAction(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		while (st.hasMoreTokens()) {
			//System.out.println("true");
		}
		lists.add(list);
		storylists.add(part, "GROUP");
	}
	private void meakChara(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		String type = st.nextToken();
		list.charaId = Integer.valueOf(st.nextToken());
		list.type = type;
		if (type.equals("ADD")) {
			list.x = Integer.valueOf(st.nextToken());
			list.y = Integer.valueOf(st.nextToken());
			list.destMapNo = Integer.valueOf(st.nextToken());
			list.charaNo = Integer.valueOf(st.nextToken());
			list.name = st.nextToken();
			list.dir = Integer.valueOf(st.nextToken());
			list.message = st.nextToken();
		} else if (type.equals("ADD_EV")) {
			list.x = Integer.valueOf(st.nextToken());
			list.y = Integer.valueOf(st.nextToken());
			list.destMapNo = Integer.valueOf(st.nextToken());
			list.charaNo = Integer.valueOf(st.nextToken());
			list.name = st.nextToken();
			list.objType = st.nextToken();
			list.dir = Integer.valueOf(st.nextToken());
			list.message = st.nextToken();
		} else if (type.equals("SET")) {
			list.destMapNo = Integer.valueOf(st.nextToken());
			list.charaNo = Integer.valueOf(st.nextToken());
			list.name = st.nextToken();
			list.dir = Integer.valueOf(st.nextToken());
			list.message = st.nextToken();
		} else if (type.equals("REMOVE")){
			list.destMapNo = Integer.valueOf(st.nextToken());
		}
		lists.add(list);
		storylists.add(part, "CHARA");
	}
	private void meakInventory(StringTokenizer st, int part) {
		storylists.add(part, "INVENTORY");
	}
	private void meakParticle(StringTokenizer st, int part) {
		String type = st.nextToken();
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        float scale = Float.parseFloat(st.nextToken());
        if (type != null) {
            switch (type) {
            case "explosion":
            	break;
            }
        }
    	boolean loop = Boolean.valueOf(st.nextToken());
    	int interval = -1;
    	if (loop) {
    		interval = Integer.parseInt(st.nextToken());
    	}
    	StoryList list = new StoryList();
    	list.type = type;
    	list.x = x;
    	list.y = y;
    	list.scale = scale;
    	list.time = interval;
    	lists.add(list);
		storylists.add(part, "PARTICLE");
	}
	private void meakStatus(StringTokenizer st, int part) {
		storylists.add(part, "STATUS");
	}
	private void makeQuest(StringTokenizer st, int part) {
		StoryList list = new StoryList();
		String type = st.nextToken();
		list.type = type;
		if (type.equals("ADD")) {
			list.questId = Integer.parseInt(st.nextToken());
		}
		list.trueNo = Integer.parseInt(st.nextToken());
		list.falseNo = Integer.parseInt(st.nextToken());
    	lists.add(list);
		storylists.add(part, "QUEST");
	}
	
	public StoryList checkList(int i) {
    	StoryList story = (StoryList)lists.get(i);
        return story;
	}
	
	public int getSize() {
		return lists.size();
		
	}
    /**
     * パスを文字列に変換（デバッグ用）
     */
    public String getString() {
        return filePass;
    }
}
