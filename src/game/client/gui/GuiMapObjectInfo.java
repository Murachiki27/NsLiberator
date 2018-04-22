package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;

import game.client.image.ImageUtils;
import game.client.item.TMItems;
import game.client.map.DoorObject;
import game.client.map.EnumDirection;
import game.client.map.MapTileObject;
import game.client.map.TreasureObject;
import game.client.map.chara.TMCharacter;
import game.client.map.chara.TMNpc;
import game.client.map.event.TMAnonymousEvent;
import game.client.map.event.TMEventBase;
import game.screen.debug.MapCreate;
import game.system.debug.data.RegistryData;
import game.utils.TMTextField;

public class GuiMapObjectInfo extends TMGuiScreen implements ComponentListener {
	
	private MouseOverArea closeArea;
	private MouseOverArea removeArea;
	
	private MouseOverArea[] dirArea = new MouseOverArea[4];
	
	private TMTextField tagField;
	
	private TMTextField charaNameField;
	private TMTextField charaTalkField;
	
	private TMTextField doorMapIdField;
	private TMTextField doorPosXField;
	private TMTextField doorPosYField;
	
	private TMTextField treItemField;
	private TMTextField treStackField;
	private MouseOverArea[] treTypeArea = new MouseOverArea[2];
	
	private TMTextField eventPathField;
	private TMTextField eventTileField;
	
	private TMCharacter charaBase;
	
	private MapTileObject objectBase;
	
	private int x, y;
	
	private MapCreate screen;
	private GameContainer container;
	
	public GuiMapObjectInfo(GameContainer container, MapCreate screen) {
		this.guiImage = ImageUtils.getDebugImage("#debug_info_gui");
		this.posX = 64.0F;
		this.posY = 100.0F;
		
		Image icon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(128, 0, 32, 32);
		this.closeArea = new MouseOverArea(container, icon, (int)this.posX + 480, (int)this.posY, this);
		this.closeArea.setNormalColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
		this.closeArea.setMouseOverColor(new Color(0.7F, 0.7F, 0.7F, 1.0F));
		
		icon = ImageUtils.getDebugImage("#debug_icon").getSubImage(0, 32, 32, 32);
		this.removeArea = new MouseOverArea(container, icon, (int)this.posX + 416, (int)this.posY, this);
		this.removeArea.setNormalColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
		this.removeArea.setMouseOverColor(new Color(0.7F, 0.7F, 0.7F, 1.0F));
		
		this.tagField = new TMTextField(container, (int)this.posX + 160, (int)this.posY + 64, 256, 30, this);
		this.tagField.setAcceptingInput(false);
		
		this.charaNameField = new TMTextField(container, (int)this.posX + 160, (int)this.posY + 32, 256, 30, this);
		this.charaNameField.setAcceptingInput(false);
		this.charaTalkField = new TMTextField(container, (int)this.posX + 160, (int)this.posY + 130, 256, 30, this);
		this.charaTalkField.setAcceptingInput(false);
		
		this.doorMapIdField = new TMTextField(container, (int)this.posX + 256, (int)this.posY + 130, 64, 30, this);
		this.doorPosXField = new TMTextField(container, (int)this.posX + 128, (int)this.posY + 162, 64, 30, this);
		this.doorPosYField = new TMTextField(container, (int)this.posX + 256, (int)this.posY + 162, 64, 30, this);
		this.doorMapIdField.setAcceptingInput(false);
		this.doorPosXField.setAcceptingInput(false);
		this.doorPosYField.setAcceptingInput(false);
		
		this.treItemField = new TMTextField(container, (int)this.posX + 192, (int)this.posY + 96, 256, 30, this);
		this.treStackField = new TMTextField(container, (int)this.posX + 160, (int)this.posY + 128, 64, 30, this);
		this.treItemField.setAcceptingInput(false);
		this.treStackField.setAcceptingInput(false);
		
		this.eventPathField = new TMTextField(container, (int)this.posX + 192, (int)this.posY + 96, 224, 30, this);
		this.eventTileField = new TMTextField(container, (int)this.posX + 192, (int)this.posY + 128, 224, 30, this);
		this.eventPathField.setAcceptingInput(false);
		this.eventTileField.setAcceptingInput(false);
		
		for (int i = 0; i < 4; i++) {
			icon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32 + (i * 32), 32, 32, 32);
			this.dirArea[i] = new MouseOverArea(container, icon, (int)this.posX + 192 + (i * 40), (int)this.posY + 96, this);
			this.dirArea[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
			this.dirArea[i].setMouseOverColor(new Color(0.7F, 0.7F, 0.7F, 1.0F));
			this.dirArea[i].setAcceptingInput(false);
		}
		for (int i = 0; i < 2; i++) {
			icon = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32 + (i * 32), 32, 32, 32);
			this.treTypeArea[i] = new MouseOverArea(container, icon, (int)this.posX + 256 + (i * 40), (int)this.posY + 160, this);
			this.treTypeArea[i].setNormalColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
			this.treTypeArea[i].setMouseOverColor(new Color(0.7F, 0.7F, 0.7F, 1.0F));
			this.treTypeArea[i].setAcceptingInput(false);
		}
		this.screen = screen;
		this.container = container;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		
		g.drawImage(this.guiImage, this.posX, this.posY);
		this.closeArea.render(this.container, g);
		this.removeArea.render(this.container, g);
		g.setColor(Color.white);
		g.drawString("タグ:", this.posX + 96.0F, this.posY + 64.0F);
		this.tagField.render(this.container, g);
		if (this.charaBase != null) {
			int dir = this.charaBase.getDirection().getDirId();
			g.drawImage(this.charaBase.getImage(), this.posX + 32, this.posY + 32, this.posX + 64, this.posY + 64, 0, dir * 32, 32, dir * 32 + 32);
			
			for (int i = 0; i < 4; i++) {
				this.dirArea[i].render(this.container, g);
			}
			g.drawString("名前:", this.posX + 96.0F, this.posY + 32.0F);
			this.charaNameField.render(this.container, g);
			g.drawString("向き:", this.posX + 96.0F, this.posY + 96.0F);
			g.drawString("会話:", this.posX + 96.0F, this.posY + 130.0F);
			this.charaTalkField.render(this.container, g);
			if (this.charaBase instanceof TMNpc) {
				g.drawString("職業:", this.posX + 96.0F, this.posY + 162.0F);
				if (((TMNpc)this.charaBase).getProfession() != null) {
					g.drawString(((TMNpc)this.charaBase).getProfession().getProfessionName(), this.posX + 96.0F, this.posY + 162.0F);
				} else {
					g.drawString("なし", this.posX + 192.0F, this.posY + 162.0F);
				}
				g.drawString("イベント:", this.posX + 96.0F, this.posY + 194.0F);
				if (((TMNpc)this.charaBase).getEvent() != null) {
					g.drawString(((TMNpc)this.charaBase).getEvent().getTag(), this.posX + 160.0F, this.posY + 194.0F);
				} else {
					g.drawString("なし", this.posX + 192.0F, this.posY + 194.0F);
				}
			}
		}
		
		if (this.objectBase != null) {
			this.objectBase.getTile().drawTile(this.posX + 32.0F, this.posY + 32.0F);
			g.drawString("タイル:", this.posX + 96.0F, this.posY + 32.0F);
			if (this.objectBase instanceof DoorObject) {
				g.drawString("ドア", this.posX + 192.0F, this.posY + 32.0F);
				g.drawString("向き:", this.posX + 96.0F, this.posY + 96.0F);
				for (int i = 0; i < 4; i++) {
					this.dirArea[i].render(this.container, g);
				}
				g.drawString(((DoorObject)this.objectBase).getDir().toString(), this.posX + 376.0F, this.posY + 96.0F);
				g.drawString("移動先マップID:", this.posX + 96.0F, this.posY + 130.0F);
				this.doorMapIdField.render(this.container, g);
				g.drawString("X:", this.posX + 96.0F, this.posY + 162.0F);
				g.drawString("Y:", this.posX + 224.0F, this.posY + 162.0F);
				this.doorPosXField.render(this.container, g);
				this.doorPosYField.render(this.container, g);
			} else if (this.objectBase instanceof TreasureObject) {
				g.drawString("宝箱", this.posX + 192.0F, this.posY + 32.0F);
				g.drawString("アイテム:", this.posX + 96.0F, this.posY + 96.0F);
				g.drawString("個数:", this.posX + 96.0F, this.posY + 128.0F);
				this.treItemField.render(this.container, g);
				this.treStackField.render(this.container, g);
				g.drawString("種類:", this.posX + 96.0F, this.posY + 160.0F);
				g.drawString(((TreasureObject)this.objectBase).getType().toString(), this.posX + 160.0F, this.posY + 160.0F);
				for (int i = 0; i < 2; i++) {
					this.treTypeArea[i].render(this.container, g);
				}
			} else if (this.objectBase instanceof TMEventBase) {
				g.drawString("イベント", this.posX + 192.0F, this.posY + 32.0F);
				g.drawString("ファイル名:", this.posX + 96.0F, this.posY + 96.0F);
				g.drawString("タイルタグ:", this.posX + 96.0F, this.posY + 128.0F);
				this.eventPathField.render(this.container, g);
				this.eventTileField.render(this.container, g);
			}
		}
	}
	
	
	public void setCharacter(TMCharacter charaBase, int x, int y) {
		this.charaBase = charaBase;
		this.x = x;
		this.y = y;
		if ((charaBase instanceof TMNpc) && !((TMNpc)charaBase).hasCustomTag()) {
			this.tagField.setAcceptingInput(true);
		}
		this.tagField.setText(charaBase.getTag());
		this.charaNameField.setAcceptingInput(true);
		this.charaNameField.setText(charaBase.getName());
		if (charaBase instanceof TMNpc) {
			this.charaTalkField.setAcceptingInput(true);
			this.charaTalkField.setText(((TMNpc)charaBase).getMessage());
		}
		for (int i = 0; i < 4; i++) {
			this.dirArea[i].setAcceptingInput(true);
		}
	}
	
	public void setObjectTile(MapTileObject objectBase, int x, int y) {
		this.objectBase = objectBase;
		this.x = x;
		this.y = y;
		this.tagField.setAcceptingInput(true);
		this.tagField.setText(objectBase.getTag());
		if (objectBase instanceof DoorObject) {
			this.doorMapIdField.setAcceptingInput(true);
			this.doorPosXField.setAcceptingInput(true);
			this.doorPosYField.setAcceptingInput(true);
			this.doorMapIdField.setText(String.valueOf(((DoorObject)objectBase).getDustMapId()));
			this.doorPosXField.setText(String.valueOf(((DoorObject)objectBase).getDustX()));
			this.doorPosYField.setText(String.valueOf(((DoorObject)objectBase).getDustY()));
			for (int i = 0; i < 4; i++) {
				this.dirArea[i].setAcceptingInput(true);
			}
		} else if (objectBase instanceof TreasureObject) {
			this.treItemField.setAcceptingInput(true);
			this.treStackField.setAcceptingInput(true);
			if (((TreasureObject)objectBase).getTreasureItem() != null) {
				this.treItemField.setText(((TreasureObject)objectBase).getTreasureItem().getItemTag());
			} else {
				this.treItemField.setText("");
			}
			for (int i = 0; i < 2; i++) {
				this.treTypeArea[i].setAcceptingInput(true);
			}
			this.treStackField.setText(String.valueOf(((TreasureObject)objectBase).getItemStack()));
		} else if (objectBase instanceof TMEventBase) {
			this.eventPathField.setAcceptingInput(true);
			this.eventTileField.setAcceptingInput(true);
			if (((TMEventBase)objectBase).objectPath != null) {
				this.eventPathField.setText(((TMEventBase)objectBase).objectPath);
			} else {
				this.eventPathField.setText("");
			}
			
			if (objectBase.getTile() != null) {
				this.eventTileField.setText(objectBase.getTile().getTag());
			} else {
				this.eventTileField.setText("");
			}
		}
	}
	
	private void removeAndCloseGui() {
		super.closeGui();
		
		this.allResetInput();
		
		if (this.charaBase != null) {
			this.screen.editMapBase.removeCharacter(this.charaBase);
			this.screen.editMapBase.repaint();
		} else if (this.objectBase != null) {
			this.screen.editMapBase.removeTileObject(this.objectBase);
			this.screen.editMapBase.repaint();
		}
		
		this.charaBase = null;
		this.objectBase = null;
		this.screen.setInputScreen(true);
	}
	
	@Override
	public void closeGui() {
		super.closeGui();
		
		this.allResetInput();
		
		if (this.charaBase != null) {
			if (this.charaBase instanceof TMNpc) {
				this.screen.editMapBase.removeCharacter(this.charaBase);
				TMNpc newNpc = new TMNpc(this.tagField.getText(), this.charaNameField.getText(), this.charaBase.getCharacterType(), this.charaBase.getMapObj());
				newNpc.setPosition(this.charaBase.getPosX(), this.charaBase.getPosY());
				newNpc.setDirection(this.charaBase.getDirection());
				newNpc.setImage(this.charaBase.getImagePath());
				newNpc.setMessage(this.charaTalkField.getText());
				newNpc.setMovearound(((TMNpc) this.charaBase).canMovearound());
				if (((TMNpc) this.charaBase).getEvent() != null) {
					newNpc.setEvent(((TMNpc) this.charaBase).getEvent());
				}
				newNpc.setProfession(((TMNpc) this.charaBase).getProfession());
				if (((TMNpc) this.charaBase).hasCustomTag()) {
					newNpc.setCustomTag();
				}
				this.screen.editMapBase.addCharacter(newNpc);
				this.screen.editMapBase.repaint();
			}
		} else if (this.objectBase != null) {
			this.screen.editMapBase.removeTileObject(this.objectBase);
			if (this.objectBase instanceof DoorObject) {
				DoorObject newDoor = new DoorObject(this.tagField.getText(), this.valueOfText(this.doorMapIdField), this.x, this.y);
				newDoor.setPosition(this.valueOfText(this.doorPosXField), this.valueOfText(this.doorPosYField), ((DoorObject)this.objectBase).getDir());
				this.objectBase = newDoor;
			} else if (this.objectBase instanceof TreasureObject) {
				TreasureObject newTre = new TreasureObject(this.tagField.getText(), this.x, this.y);
				newTre.setType(((TreasureObject)this.objectBase).getType());
				newTre.setItem(TMItems.getItemByTag(this.treItemField.getText()), this.valueOfText(this.treStackField));
				this.objectBase = newTre;
			} else if (this.objectBase instanceof TMEventBase) {
				TMAnonymousEvent newEvent;
				if (RegistryData.containsEvent(this.eventPathField.getText())) {
					newEvent = new TMAnonymousEvent(RegistryData.getEvent(this.eventPathField.getText()), this.tagField.getText(), this.x, this.y);
					newEvent.setMapTile(this.eventTileField.getText());
					newEvent.objectPath = this.eventPathField.getText();
				} else {
					newEvent = new TMAnonymousEvent(null, this.tagField.getText(), this.x, this.y);
					newEvent.setMapTile(this.eventTileField.getText());
				}
				this.objectBase = newEvent;
			}
			this.screen.editMapBase.addTileObject(this.objectBase);
			this.screen.editMapBase.repaint();
		}
		
		this.charaBase = null;
		this.objectBase = null;
		this.screen.setInputScreen(true);
	}
	
	private void allResetInput() {
		for (int i = 0; i < 4; i++) {
			this.dirArea[i].setFocus(false);
			this.dirArea[i].setAcceptingInput(false);
		}
		for (int i = 0; i < 2; i++) {
			this.treTypeArea[i].setFocus(false);
			this.treTypeArea[i].setAcceptingInput(false);
		}
		this.tagField.setFocus(false);
		this.tagField.setAcceptingInput(false);
		
		this.charaNameField.setFocus(false);
		this.charaNameField.setAcceptingInput(false);
		this.charaTalkField.setFocus(false);
		this.charaTalkField.setAcceptingInput(false);
		
		this.doorMapIdField.setFocus(false);
		this.doorPosXField.setFocus(false);
		this.doorPosYField.setFocus(false);
		this.doorMapIdField.setAcceptingInput(false);
		this.doorPosXField.setAcceptingInput(false);
		this.doorPosYField.setAcceptingInput(false);
		
		this.treItemField.setFocus(false);
		this.treStackField.setFocus(false);
		this.treItemField.setAcceptingInput(false);
		this.treStackField.setAcceptingInput(false);
		
		this.eventPathField.setFocus(false);
		this.eventTileField.setFocus(false);
		this.eventPathField.setAcceptingInput(false);
		this.eventTileField.setAcceptingInput(false);
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
	
	@Override
	public void componentActivated(AbstractComponent ac) {
		if (ac == this.closeArea) {
			this.closeGui();
		}
		if (ac == this.removeArea) {
			this.removeAndCloseGui();
		}
		for (int i = 0; i < 4; i++) {
			if (ac == this.dirArea[i]) {
				if (this.charaBase != null) {
					this.charaBase.setDirection(EnumDirection.getDirection(i));
				} else if (this.objectBase != null) {
					if (this.objectBase instanceof DoorObject) {
						((DoorObject)this.objectBase).setPosition(this.valueOfText(this.doorPosXField), this.valueOfText(this.doorPosYField), EnumDirection.getDirection(i));
					}
				}
			}
		}
		for (int i = 0; i < 2; i++) {
			if (ac == this.treTypeArea[i]) {
				if (this.objectBase != null) {
					if (i == 0) {
						((TreasureObject)this.objectBase).setType(TreasureObject.Type.WOODEN);
					} else if (i == 1) {
						((TreasureObject)this.objectBase).setType(TreasureObject.Type.RARE);
					}
				}
			}
		}
	}
}
