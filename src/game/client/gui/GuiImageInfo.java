package game.client.gui;

public enum GuiImageInfo {
	NORMAL_WINDOW("widgets"),
	BATTLE_STATUS("container/battle_status"),
	MENU_STATUS("container/menu_status"),
	TALK_WINDOW("container/talk_window"),
	MAP_WINDOW("map"),
	ICON("icons"),
	MAP_ICON("map_icon"),
	INPUT_LOGO("type"),
	;
	
	private final String filePath;
	
	private GuiImageInfo(String filePath) {
		this.filePath = filePath;
	}
	
	public String getPath() {
		return this.filePath;
	}
}
