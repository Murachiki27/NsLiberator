package game.client.map;

public enum EnumDirection {
	LEFT(0),
	RIGHT(1),
	UP(2),
	DOWN(3);
	
	private final int dirNo;
	
	private EnumDirection(int dirNo) {
		this.dirNo = dirNo;
	}
	
	public int getDirId() {
		return this.dirNo;
	}
	
	public static EnumDirection getDirection(int dirId) {
		EnumDirection[] dirs = EnumDirection.values();
		for (EnumDirection dir : dirs) {
			if (dirId == dir.getDirId()) {
				return dir;
			}
		}
		return null;
	}
	
	public static EnumDirection getDirection(String name) {
		EnumDirection[] dirs = EnumDirection.values();
		for (EnumDirection dir : dirs) {
			if (name.equals(dir.toString())) {
				return dir;
			}
		}
		return null;
	}
}
