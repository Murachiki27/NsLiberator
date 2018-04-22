package game.api;

import java.io.Serializable;

/**
 * Liberator用
 * 
 * @author Murachiki27
 *
 */
public class MapObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean parallaxMapping;
	private int[][] map;
	private int[][] objMap;
	private int[][] sideMap;
	
	private int row, col;
	
	private String parallaxFile;
	
	public MapObject(boolean parallaxMapping) {
		this.parallaxMapping = parallaxMapping;
	}
	
	public boolean isParallaxMapping() {
		return this.parallaxMapping;
	}
	
	public void setBasicMap(int[][] map) {
		this.map = map;
	}
	
	public void setParallaxImageFile(String parallaxFile) {
		this.parallaxFile = parallaxFile;
	}
	
	public void setMapLayer(int[][] objMap, int[][] sideMap) {
		this.objMap = objMap;
		this.sideMap = sideMap;
	}
	
	public void setMapSize(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int[][] getBasicMap() {
		return this.map;
	}
	
	public String getParallaxImageFile() {
		return this.parallaxFile;
	}
	
	public int[][] getObjectMap() {
		return this.objMap;
	}
	
	public int[][] getRenderMap() {
		return this.sideMap;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.col;
	}
}