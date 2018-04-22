package game.client.map.chara;

public enum EnumCharacterType {
	CHARACTER("character"),
	ENEMY("enemy"),
	BOSS("boss"),
	;
	
	private final String filePath;
	
	private EnumCharacterType(String filePath) {
		this.filePath = filePath;
	}
	
	public String getPath() {
		return this.filePath;
	}
	
	public static EnumCharacterType getName(String name) {
		if (name != null) {
			switch (name) {
			case "CHARACTER": return CHARACTER;
			case "ENEMY": return ENEMY;
			case "BOSS": return BOSS;
			default : return null;
			}
		} else {
			return null;
		}
	}
}
