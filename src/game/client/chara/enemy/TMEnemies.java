package game.client.chara.enemy;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import game.client.chara.BattleEnemy;

public class TMEnemies {
	public int rate;
	public static String[] descriptions = {
			"緑色の角を持った人型の生物。\n知能はそこまで高くはないが教育次第では\n人間と同等にまで成長するらしい。\n昔から労働力として使われていた。\nしかし、近年謎の病でやせ細り森に捨てられる\nということが増え、野生化した\n個体が問題になっている。",
			"人間などの兵士や冒険者から奪った兜を\n被っている。鎧は外し方が分からず入手\nできなかったらしい。手にはナイフを持っており\n舐めてかかると刺され危険。",
			"ゴブリンの中でも素質のあるものがなれる。\n何故かここのゴブリンメイジは杖の先に翡翠\nがついており、その他地域よりも強力な魔法を\n放てる。",
			"その可愛らしい外見に惑わされて近付いては\nいけない。その鋭い角で一突きにされ、物言わぬ\n物質になりたくなければ。その脚はバネのように\nなっており、弾丸のような速度で跳ぶことが\nできる。また、その角は折れても生え変わるため、\n岩などにも躊躇せず突っ込んでいく。",
			"森に住み着いているただの狼。魔法は放てない\nが集団で行動するので囲まれると非常に危険\nである。夜の森での死はだいたいがウルフが\n原因だ。しかし炎には弱いので松明や炎の魔法\nでひるませられれば逃げることが可能かも\nしれない。",
			"詳細無し",
			"詳細無し",
			"詳細無し",
	};
	
	private static Map<Integer, BattleEnemy> enemyHash = new HashMap<>();
	
	public static final int empty = -1;
	
	public static final int goblin = 0;
	public static final int goblin_war = 1;
	public static final int goblin_magic = 2;
	public static final int rabbit = 3;
	public static final int wolf = 4;
	public static final int slime = 5;
	public static final int cobolt = 6;
	public static final int dwarf = 7;
	public static final int snowman = 10;
	public static final int yeti = 11;
	public static final int ice_golem = 12;
	public static final int skeleton = 15;
	public static final int gargoyle = 16;
	public static final int lizardman = 17;
	public static final int redcap = 20;
	public static final int centaur = 21;
	
	public static final int king_goblin = 32;
	public static final int golem = 33;
	public static final int ifrito = 34;
	public static final int king_yeti = 35;
	
	public TMEnemies() {
	}
	
	public boolean loadEnemies() {
		enemyHash.put(goblin, new EnemyGoblin(EnumGoblinType.NORMAL));
		enemyHash.put(goblin_war, new EnemyGoblin(EnumGoblinType.WAR));
		enemyHash.put(goblin_magic, new EnemyGoblin(EnumGoblinType.MAGIC));
		enemyHash.put(rabbit, new EnemyRabbit());
		enemyHash.put(wolf, new EnemyWolf());
		enemyHash.put(slime, new EnemyRabbit());
		enemyHash.put(cobolt, new EnemyRabbit());
		enemyHash.put(dwarf, new EnemyRabbit());
		
		/*
		enemy[goblin] = new BattleEnemy("ゴブリン", NOMAL).setImage(BattleImageList.imageList[goblin], 2).addExperience(2);
		enemy[goblin].skillList.addAll(SkillList.GOBLIN);
		enemy[goblin].addLevel(1);
		
		enemy[goblin_war] = new BattleEnemy("ゴブリンウォーリア", NOMAL).setImage(BattleImageList.imageList[goblin_war], 2).addExperience(3);
		enemy[goblin_war].skillList.addAll(SkillList.GOBLIN_WAR);
		enemy[goblin_war].addLevel(1);
		enemy[goblin_war].setMessage("おらぁ!");
		
		enemy[goblin_magic] = new BattleEnemy("コブリンメイジ", NOMAL).setImage(BattleImageList.imageList[goblin_magic], 2).addExperience(3);
		enemy[goblin_magic].skillList.addAll(SkillList.GOBLIN_MAGIC);
		enemy[goblin_magic].addLevel(1);
		
		enemy[rabbit] = new BattleEnemy("ホーンラビット", NOMAL).setImage(BattleImageList.imageList[rabbit], 2).addExperience(4);
		enemy[rabbit].skillList.addAll(SkillList.RABBIT);
		enemy[rabbit].addLevel(2);
		
		enemy[wolf] = new BattleEnemy("ウルフ", NOMAL).setImage(BattleImageList.imageList[wolf], 2).addExperience(4);
		enemy[wolf].skillList.addAll(SkillList.WOLF);
		enemy[wolf].addLevel(10);
		
		enemy[slime] = new BattleEnemy("スライム", NOMAL).setImage(BattleImageList.imageList[slime], 2).addExperience(10);
		enemy[slime].skillList.addAll(SkillList.SLIME);
		enemy[slime].addLevel(10);
		
		enemy[cobolt] = new BattleEnemy("コボルト", NOMAL).setImage(BattleImageList.imageList[cobolt], 2).addExperience(11);
		enemy[cobolt].skillList.addAll(SkillList.COBOLT);
		enemy[cobolt].addLevel(10);
		
		enemy[dwarf] = new BattleEnemy("ドワーフ", NOMAL).setImage(BattleImageList.imageList[dwarf], 2).addExperience(12);
		enemy[dwarf].skillList.addAll(SkillList.DWARF);
		enemy[dwarf].addLevel(10);
		
		enemy[snowman] = new BattleEnemy("スノーマン", NOMAL).setImage(BattleImageList.imageList[snowman], 2).addExperience(26);
		enemy[snowman].skillList.addAll(SkillList.SNOW_MAN);
		enemy[snowman].addLevel(25);
		
		enemy[yeti] = new BattleEnemy("イエティ", NOMAL).setImage(BattleImageList.imageList[yeti], 2).addExperience(29);
		enemy[yeti].skillList.addAll(SkillList.YETI);
		enemy[yeti].addLevel(25);
		
		enemy[ice_golem] = new BattleEnemy("アイスゴーレム", NOMAL).setImage(BattleImageList.imageList[ice_golem], 2).addExperience(34);
		enemy[ice_golem].skillList.addAll(SkillList.ICE_GOLEM);
		enemy[ice_golem].addLevel(26);

		enemy[skeleton] = new BattleEnemy("スケルトン", NOMAL).setImage(BattleImageList.imageList[skeleton], 2).addExperience(56);
		enemy[skeleton].skillList.addAll(SkillList.SKELETON);
		enemy[skeleton].addLevel(41);
		
		enemy[gargoyle] = new BattleEnemy("ガーゴイル", NOMAL).setImage(BattleImageList.imageList[gargoyle], 2).addExperience(54);
		enemy[gargoyle].skillList.addAll(SkillList.GARGOYLE);
		enemy[gargoyle].addLevel(41);
		
		enemy[lizardman] = new BattleEnemy("リザードマン", NOMAL).setImage(BattleImageList.imageList[lizardman], 2).addExperience(59);
		enemy[lizardman].skillList.addAll(SkillList.LIZARDMAN);
		enemy[lizardman].addLevel(41);
		
		enemy[redcap] = new BattleEnemy("レッドキャップ", NOMAL).setImage(BattleImageList.imageList[redcap], 2).addExperience(108);
		enemy[redcap].skillList.addAll(SkillList.REDCAP);
		enemy[redcap].addLevel(65);
		
		enemy[centaur] = new BattleEnemy("ケンタウロス", NOMAL).setImage(BattleImageList.imageList[centaur], 2).addExperience(112);
		enemy[centaur].skillList.addAll(SkillList.CENTAUR);
		enemy[centaur].addLevel(65);
		
		enemy[king_goblin] = new BattleEnemy("キングゴブリン", GOBLIN_KING).setImage(BattleImageList.imageList[king_goblin], 1.5F).addExperience(20);
		enemy[king_goblin].skillList.addAll(SkillList.GOBLIN_KING);
		enemy[king_goblin].addLevel(5);
		
		enemy[golem] = new BattleEnemy("ゴーレム", GOLEM).setImage(BattleImageList.imageList[golem], 1.5F).addExperience(40);
		enemy[golem].skillList.addAll(SkillList.GOLEM);
		enemy[golem].addLevel(33);
		
		enemy[ifrito] = new BattleEnemy("イフリート", IFRITO).setImage(BattleImageList.imageList[ifrito], 1).addExperience(80);
		enemy[ifrito].skillList.addAll(SkillList.IFRITE);
		enemy[ifrito].addLevel(52);
		
		enemy[king_yeti] = new BattleEnemy("キングイエティ", YETI_KING).setImage(BattleImageList.imageList[king_yeti], 1).addExperience(120);
		enemy[king_yeti].skillList.addAll(SkillList.YETI_KING);
		enemy[king_yeti].addLevel(70);
		*/
		rate = 64;
		return true;
	}
	
	public static void registerEnemy(int id, BattleEnemy enemy) {
		enemyHash.put(id, enemy);
	}
	
	public static BattleEnemy getEnemyToId(int id) {
		return enemyHash.get(id);
	}
	
	public static boolean getEnemyExist(int enemyId) {
		if (enemyHash.containsKey(enemyId)) return true;
		return false;
	}
	
	public static int getEnemyIdToTag(String commandTag) {
		for (int data = 0; data < 8; data++) {
			if (commandTag == enemyHash.get(data).getCommandTag()) {
				return data;
			}
		}
		return empty;
	}
	
	public static BattleEnemy getEnemyToTag(String commandTag) {
		for (Map.Entry<Integer, BattleEnemy> enemy : enemyHash.entrySet()) {
			if (enemy.getValue().getCommandTag().equals(commandTag)) {
				return enemy.getValue();
			}
		}
		return null;
	}
	
	public static int getSize() {
		return enemyHash.size();
	}
	
	public static Set<Map.Entry<Integer, BattleEnemy>> getEntry() {
		return enemyHash.entrySet();
	}
}
