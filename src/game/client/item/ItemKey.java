package game.client.item;

import game.client.map.MapTileObject;
import game.client.map.chara.TMPlayer;
import game.screen.ScreenMapState;

public class ItemKey extends Item {
	private static final long serialVersionUID = 1L;

	private String keyTag;
	
	private String successMessage = "カギを使って開けた。";
	private String failedMessage = "それはここでは使えません。";
	
	public ItemKey(String keyTag) {
		super("tm:item_key");
		this.keyTag = keyTag;
		this.maxStackSize = 1;
	}
	
	public String getKeyTag() {
		return this.keyTag;
	}
	
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}
	
	public String getFailedMessage() {
		return this.failedMessage;
	}
	
	@Override
	public boolean onItemUse(TMPlayer player) {
		MapTileObject object = player.getNextToObject();
		if (object != null && object.hasNeedKeyObject()) {
			if (object.isCorrectItem(this.keyTag)) {
				object.successKeyObject();
				ScreenMapState.setMessageGui("", this.successMessage);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
