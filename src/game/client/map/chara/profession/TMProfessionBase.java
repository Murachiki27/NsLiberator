package game.client.map.chara.profession;

import java.io.Serializable;

/**
 * 
 * TMNpcにつける職業の基礎クラス
 * 
 * @author Murachiki27
 *
 */
public abstract class TMProfessionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int professionId;
	
	private final String professionName;
	
	public TMProfessionBase(int professionId, String professionName) {
		this.professionId = professionId;
		this.professionName = professionName;
	}
	
	public int getProfessionId() {
		return this.professionId;
	}
	
	public String getProfessionName() {
		return this.professionName;
	}
}
