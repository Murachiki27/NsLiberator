package game.client.map.event.component;

import game.screen.ScreenMapState;

public class EventBattle extends EventComponent {
	private static final long serialVersionUID = 1L;
	
	private String[] enemyTag;
	
	public EventBattle(String... tags) {
		this.enemyTag = tags;
	}
	
	@Override
	public void start() {
		super.start();
		ScreenMapState.startBattle(this.enemyTag);
	}

	@Override
	public boolean continueEvent() {
		return false;
	}
}
