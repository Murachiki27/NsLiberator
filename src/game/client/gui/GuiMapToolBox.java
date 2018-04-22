package game.client.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

import game.client.image.ImageUtils;
import game.client.map.AbstractMap;
import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.MapTile;
import game.client.map.MapTileObject;
import game.client.map.MapTileUtils;
import game.client.map.TreasureObject;
import game.client.map.chara.CharacterObject;
import game.client.map.data.TMAnonyBasicMap;
import game.client.map.data.TMAnonyParalMap;
import game.client.map.event.TMEventBase;
import game.screen.debug.MapCreate;
import game.system.debug.data.MapDebugData;
import game.system.debug.data.RegistryData;
import game.utils.TMTextField;

public class GuiMapToolBox extends TMGuiScreen implements ComponentListener {
	private enum EditBox {
		TOOL_EDIT,
		MAP_EDIT
	}
	
	public enum EditType {
		EDIT_CHARA,
		EDIT_TILE;
	}
	
	private MouseOverArea[] boxSelect = new MouseOverArea[2];
	private MouseOverArea[] charaSelect = new MouseOverArea[2];
	private MouseOverArea[] objectSelect = new MouseOverArea[2];
	private MouseOverArea[] editSelect = new MouseOverArea[2];
	
	private TMTextField mapNameField;
	private TMTextField mapTagField;
	private TMTextField mapIdField;
	private MouseOverArea outputArea;
	
	private EditBox editState = EditBox.TOOL_EDIT;
	private EditType editType = EditType.EDIT_CHARA;
	
	private int charaIndex = -1;
	private int tileIndex = 0;
	
	private MapCreate screen;
	private GameContainer container;
	
	public GuiMapToolBox(GameContainer container, MapCreate screen) {
		this.guiImage = ImageUtils.getDebugImage("#debug_tool_gui");
		
		this.posX = 480.0F;
		this.posY = 0.0F;
		
		for (int i = 0; i < 2; i++) {
			Image icon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32 + (i * 32), 32, 32, 32);
			this.boxSelect[i] = new MouseOverArea(container, icon, (int)this.posX + 20 + (i * 96), 50, this);
			this.boxSelect[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.8F));
			this.boxSelect[i].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
			if (i == 0) {
				this.boxSelect[i].setAcceptingInput(false);
			}
			
			this.charaSelect[i] = new MouseOverArea(container, icon, (int)this.posX + 20 + (i * 96), 150, this);
			this.charaSelect[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.8F));
			this.charaSelect[i].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
			if (RegistryData.getCharaObjSize() == 0 || RegistryData.getCharaObjSize() == 1) {
				this.charaSelect[i].setAcceptingInput(false);
			} else if (i == 0) {
				this.charaSelect[i].setAcceptingInput(false);
			}
			
			this.objectSelect[i] = new MouseOverArea(container, icon, (int)this.posX + 20 + (i * 96), 250, this);
			this.objectSelect[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.8F));
			this.objectSelect[i].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
			if (i == 0) {
				this.objectSelect[i].setAcceptingInput(false);
			}
			
			this.editSelect[i] = new MouseOverArea(container, icon, (int)this.posX + 20 + (i * 96), 380, this);
			this.editSelect[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 0.6F));
			this.editSelect[i].setMouseOverColor(new Color(1.0F, 1.0F, 1.0F, 0.9F));
			if (i == 0) {
				this.editSelect[i].setAcceptingInput(false);
			}
		}
		
		this.mapNameField = new TMTextField(container, (int)this.posX + 10, (int)this.posY + 120, 140, 35, this);
		this.mapTagField = new TMTextField(container, (int)this.posX + 10, (int)this.posY + 180, 140, 35, this);
		this.mapIdField = new TMTextField(container, (int)this.posX + 10, (int)this.posY + 240, 140, 35, this);
		this.mapNameField.setAcceptingInput(false);
		this.mapTagField.setAcceptingInput(false);
		this.mapIdField.setAcceptingInput(false);
		Image icon = ImageUtils.getDebugImage("#debug_icon").getSubImage(0, 0, 128, 32);
		this.outputArea = new MouseOverArea(container, icon, (int)this.posX + 10, (int)this.posY + 380, this);
		this.outputArea.setNormalColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
		this.outputArea.setMouseOverColor(new Color(0.7F, 0.7F, 0.7F, 1.0F));
		this.outputArea.setAcceptingInput(false);
		
		if (RegistryData.getCharaObjSize() > 0) {
			this.charaIndex = 0;
		}
		
		this.screen = screen;
		this.container = container;
	}
	
	@Override
	public void draw(Graphics g) {
		g.drawImage(this.guiImage, this.posX, this.posY);
		for (int i = 0; i < 2; i++) {
			this.boxSelect[i].render(this.container, g);
		}
		switch (this.editState) {
		case TOOL_EDIT:
			for (int i = 0; i < 2; i++) {
				this.charaSelect[i].render(this.container, g);
				this.objectSelect[i].render(this.container, g);
				this.editSelect[i].render(this.container, g);
			}
			g.drawString("オブジェクト", 560.0F - (g.getFont().getWidth("オブジェクト") / 2), 50);
			if (this.charaIndex > -1) {
				g.drawString(RegistryData.getCharacter(this.charaIndex).name, (this.posX + 80.0F) - (g.getFont().getWidth(RegistryData.getCharacter(this.charaIndex).name) / 2), 150);
			}
			switch (this.tileIndex) {
			case 0:
				g.drawString("ドア", (this.posX + 80.0F) - (g.getFont().getWidth("ドア") / 2), 250);
				break;
			case 1:
				g.drawString("宝箱", (this.posX + 80.0F) - (g.getFont().getWidth("宝箱") / 2), 250);
				break;
			case 2:
				g.drawString("イベント", (this.posX + 80.0F) - (g.getFont().getWidth("イベント") / 2), 250);
				break;
			}
			
			if (this.editType == EditType.EDIT_CHARA) {
				g.drawString("キャラクター", (this.posX + 80.0F) - (g.getFont().getWidth("キャラクター") / 2), 380);
			} else if (this.editType == EditType.EDIT_TILE) {
				g.drawString("タイルオブジェ", (this.posX + 80.0F) - (g.getFont().getWidth("タイルオブジェ") / 2), 380);
			}
			break;
		case MAP_EDIT:
			this.mapNameField.render(this.container, g);
			this.mapTagField.render(this.container, g);
			this.mapIdField.render(this.container, g);
			g.drawString("マップ情報", (this.posX + 80.0F) - (g.getFont().getWidth("マップ情報") / 2), 50.0F);
			g.drawString("マップ名", (this.posX + 80.0F) - (g.getFont().getWidth("マップ情報") / 2), 90.0F);
			g.drawString("マップタグ", (this.posX + 80.0F) - (g.getFont().getWidth("マップ情報") / 2), 150.0F);
			g.drawString("マップID", (this.posX + 80.0F) - (g.getFont().getWidth("マップ情報") / 2), 210.0F);
			this.outputArea.render(this.container, g);
			break;
		default:
			break;
		}
	}
	
	public CharacterObject getCharacter() {
		if (this.charaIndex > -1) {
			return RegistryData.getCharacter(this.charaIndex);
		} else {
			return null;
		}
	}
	
	public MapTileObject getTileObject(int x, int y) {
		if (this.tileIndex == 0) {
			DoorObject obj =  new DoorObject("", 0, x, y);
			obj.setPosition(0, 0, EnumDirection.DOWN);
			return obj;
		} else if (this.tileIndex == 1) {
			return new TreasureObject("", x, y);
		} else {
			return new TMEventBase("", x, y) {
				private static final long serialVersionUID = 1L;
				@Override
				public MapTile getTile() {
					return MapTileUtils.stone_tile;
				}
			};
		}
	}
	
	public EditType getEditType() {
		return this.editType;
	}

	@Override
	public void componentActivated(AbstractComponent ac) {
		for (int i = 0; i < 2; i++) {
			if (ac == this.boxSelect[i]) {
				if (this.editState == EditBox.TOOL_EDIT) {
					if (i == 1) {
						this.editState = EditBox.MAP_EDIT;
						this.boxSelect[0].setAcceptingInput(true);
						this.boxSelect[1].setAcceptingInput(false);
						this.toolAcceptingArea(false);
						this.mapAcceptingArea(true);
					}
				} else if (this.editState == EditBox.MAP_EDIT) {
					if (i == 0) {
						this.editState = EditBox.TOOL_EDIT;
						this.boxSelect[0].setAcceptingInput(false);
						this.boxSelect[1].setAcceptingInput(true);
						this.toolAcceptingArea(true);
						this.mapAcceptingArea(false);
					}
				}
			} else if (ac == this.charaSelect[i]) {
				if (i == 0) {
					this.charaIndex--;
					if (this.charaIndex <= 0) {
						this.charaIndex = 0;
						this.charaSelect[0].setAcceptingInput(false);
						this.charaSelect[1].setAcceptingInput(true);
					} else {
						this.charaSelect[0].setAcceptingInput(true);
						this.charaSelect[1].setAcceptingInput(true);
					}
				} else if (i == 1) {
					this.charaIndex++;
					if (this.charaIndex >= RegistryData.getCharaObjSize() - 1) {
						this.charaIndex = RegistryData.getCharaObjSize() - 1;
						this.charaSelect[0].setAcceptingInput(true);
						this.charaSelect[1].setAcceptingInput(false);
					} else {
						this.charaSelect[0].setAcceptingInput(true);
						this.charaSelect[1].setAcceptingInput(true);
					}
				}
			} else if (ac == this.objectSelect[i]) {
				if (i == 0) {
					this.tileIndex--;
					if (this.tileIndex <= 0) {
						this.tileIndex = 0;
						this.objectSelect[0].setAcceptingInput(false);
						this.objectSelect[1].setAcceptingInput(true);
					} else {
						this.objectSelect[0].setAcceptingInput(true);
						this.objectSelect[1].setAcceptingInput(true);
					}
				} else if (i == 1) {
					this.tileIndex++;
					if (this.tileIndex >= 2) {
						this.tileIndex = 2;
						this.objectSelect[0].setAcceptingInput(true);
						this.objectSelect[1].setAcceptingInput(false);
					} else {
						this.objectSelect[0].setAcceptingInput(true);
						this.objectSelect[1].setAcceptingInput(true);
					}
				}
			} else if (ac == this.editSelect[i]) {
				if (this.editType == EditType.EDIT_CHARA) {
					if (i == 1) {
						this.editType = EditType.EDIT_TILE;
						this.editSelect[0].setAcceptingInput(true);
						this.editSelect[1].setAcceptingInput(false);
					}
				} else if (this.editType == EditType.EDIT_TILE) {
					if (i == 0) {
						this.editType = EditType.EDIT_CHARA;
						this.editSelect[0].setAcceptingInput(false);
						this.editSelect[1].setAcceptingInput(true);
					}
				}
			}
		}
		
		if (ac == this.outputArea) {
			this.saveMapDataFile();
		}
	}
	
	public void allAcceptingArea(boolean acceptingInput) {
		for (int i = 0; i < 2; i++) {
			this.boxSelect[i].setAcceptingInput(acceptingInput);
			this.charaSelect[i].setAcceptingInput(acceptingInput);
			this.objectSelect[i].setAcceptingInput(acceptingInput);
			this.editSelect[i].setAcceptingInput(acceptingInput);
		}
		this.mapAcceptingArea(this.editState == EditBox.MAP_EDIT);
	}
	
	public void toolAcceptingArea(boolean acceptingInput) {
		for (int i = 0; i < 2; i++) {
			this.charaSelect[i].setAcceptingInput(acceptingInput);
			this.objectSelect[i].setAcceptingInput(acceptingInput);
			this.editSelect[i].setAcceptingInput(acceptingInput);
		}
	}
	
	public void mapAcceptingArea(boolean acceptingInput) {
		this.mapNameField.setFocus(false);
		this.mapTagField.setFocus(false);
		this.mapIdField.setFocus(false);
		this.outputArea.setFocus(false);
		this.mapNameField.setAcceptingInput(acceptingInput);
		this.mapTagField.setAcceptingInput(acceptingInput);
		this.mapIdField.setAcceptingInput(acceptingInput);
		this.outputArea.setAcceptingInput(acceptingInput);
	}
	
	private void saveMapDataFile() {
		String name;
		if (this.screen.isContinuedFile()) {
			name = RegistryData.mapData.get(this.screen.getMapIndex()).mapFileName;
		} else {
			int nameEtc = RegistryData.mapFileName.get(this.screen.getMapIndex()).length();
			name = RegistryData.mapFileName.get(this.screen.getMapIndex()).substring(0, nameEtc - 4);
		}
		
		this.screen.editMapBase.setMapName(this.mapNameField.getText());
		
		AbstractMap newMap = null;
		if (this.screen.editMapBase instanceof TMAnonyParalMap) {
			newMap = ((TMAnonyParalMap)this.screen.editMapBase).subMap(this.valueOfText(this.mapIdField));
		} else if (this.screen.editMapBase instanceof TMAnonyBasicMap) {
			newMap = ((TMAnonyBasicMap)this.screen.editMapBase).subMap(this.valueOfText(this.mapIdField));
		}
		try {
			ObjectOutputStream objOutStream = new ObjectOutputStream( new FileOutputStream("./debug/maps/" + name + ".mapdata"));
			MapDebugData mapData = new MapDebugData();
			mapData.mapCommandTag = this.mapTagField.getText();
			mapData.mapData = newMap;
			mapData.mapData.nullData();
			objOutStream.writeObject(mapData);
			objOutStream.close();
			this.screen.editMapBase.loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int valueOfText(TMTextField textField) {
		int value;
		try {
			value = Integer.valueOf(textField.getText());
		} catch (NumberFormatException e) {
			value = 0;
		}
		return value;
	}
	
	public void setNameField(String mapName, String tag, int mapId) {
		this.mapNameField.setText(mapName);
		this.mapTagField.setText(tag);
		this.mapIdField.setText(String.valueOf(mapId));
	}
}
