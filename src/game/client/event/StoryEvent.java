package game.client.event;

import game.client.event.story.StoryFile;

public class StoryEvent extends Event {
	private static final long serialVersionUID = 1L;
	//ストーリーファイル
	private StoryFile story;
	
	public StoryEvent(int x, int y, int chipNo, String file) {
		super(x, y , chipNo, false);
		story = new StoryFile(file);
	}
    public String toString() {
        return "STORY:" + super.toString() + ":" + story.getString();
    }
    public StoryFile getStoryFile() {
		return story;
    	
    }
}
