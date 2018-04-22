package game.client.event;

public class TalkEvent extends Event {
	private static final long serialVersionUID = 1L;
	// オブジェクトの種類
	public String objectType;
	// メッセージ
	public String message;
	
	public TalkEvent(int x, int y, int chipNo, String objectType, String message) {
		super(x, y, chipNo, true);
		this.objectType = objectType;
		this.message = message;
	}
}
