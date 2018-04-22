package game.client.map.event.component;

import game.screen.ScreenMapState;

public class EventChangeDrawMap extends EventComponent {
	private static final long serialVersionUID = 1L;
	
	public static final int CLOSE_MAP = -1;
	
	private final int mapId;
	private int cameraX;
	private int cameraY;
	
	public EventChangeDrawMap(int mapId, int cameraX, int cameraY) {
		this.mapId = mapId;
		this.cameraX = cameraX;
		this.cameraY = cameraY;
	}
	
	@Override
	public void start() {
		super.start();
		if (this.mapId < 0) {
			ScreenMapState.setCloseOtherMap();
		} else {
			ScreenMapState.setShowOtherMap(this.mapId, this.cameraX, this.cameraY);
		}
	}

	@Override
	public boolean continueEvent() {
		return false;
	}
}
