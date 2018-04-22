package game.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import game.client.image.ImageUtils;
import game.common.input.EnumKeyInput;
import game.sound.SoundListener;

/**
 * メニューボックスの基礎クラス
 * 
 * @author Murachiki27
 *
 */
public abstract class AbstractMenuBox extends TMGuiScreen {
	// 選択肢リスト
	private List<String> list = new ArrayList<String>();
    /** カーソル画像 */
    private Image cursorImage;
    
    private Image downMark;
    private Image upMark;
    
	/** メニューのセレクト番号 */
	private int select;
	/** 初期メニューセレクト番号 */
	private int initSelect;
	
	private int selWidth;
	private int selHeight;
	
	// 表示を多次元的に配列
	private boolean isMultiselect = false;
	
	/** メニュー選択で最後まで行ったときに0に戻らないか */
	private boolean isNotReturn;
	/** メニューの間隔 */
	private float val = 40.0F;
	private float valWidth;
	/** メニューのサイズ */
	private int size = 0;
	
	private int sizeW = 1;
	private int sizeH;
	/** カーソルの画像の大きさ */
	private float cursorScale = 1.0F;
	/** 1ページに表示できる列数 */
	private int maxDrawHeight = 8;
	
	public AbstractMenuBox(float x, float y) {
		this.posX = x;
		this.posY = y;
		this.cursorImage = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(80, 0, 16, 16);
		this.downMark = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(32, 0, 16, 16);
		this.upMark = ImageUtils.getGuiImage(GuiImageInfo.ICON).getSubImage(48, 0, 16, 16);
	}
	
	private void init() {
		this.size = this.list.size();
		if (this.isMultiselect) {
			this.sizeH = this.size / this.sizeW + 1;
		}
	}
	
	public void addMenuStrings(String e) {
		list.add(e);
		this.init();
	}
	
	public void addMenuStrings(int index , String e) {
		int i = index;
		if (this.list.isEmpty()) i = 0;
		list.add(i, e);
		this.init();
	}
	
	public void addAllMenuStrings(Collection<? extends String> c) {
		this.list.addAll(c);
		this.init();
	}
	
	public void addMenuPackStrings(GuiMenuPack pack) {
		this.list.addAll(pack.getPack());
		this.init();
	}
	
	public void setMultiMode(int branch, float valWidth) {
		this.sizeW = branch;
		this.valWidth = valWidth;
		this.isMultiselect = true;
		this.init();
	}
	
	public void setCursorscale(float scale) {
		this.cursorScale = scale;
	}
	
	public void setInterval(float val) {
		this.val = val;
	}
	
	public void setNotReturn() {
		this.isNotReturn = true;
	}
	
	public void setDrawHeight(int maxHeight) {
		this.maxDrawHeight = maxHeight;
	}
	
	public void setInitCursor(int select) {
		this.initSelect = select;
		this.select = select;
	}
	
	@Override
	public void draw(Graphics g) {
		if (!this.isVisible()) return;
		
		g.setColor(Color.white);
		if (!this.list.isEmpty()) {
			float dx;
			float dy;
			int dh;
			if (this.isMultiselect) {
				for (int i = 0; i < this.maxDrawHeight * this.sizeW; i++) {
					dx = this.posX + this.valWidth * (i % this.sizeW);
					dy = this.posY + (this.val * (i / this.sizeW));
					dh = this.selHeight >= this.maxDrawHeight ? (this.selHeight - (this.maxDrawHeight - 1)) * this.sizeW : 0;
					if (i + dh < this.size) {
						if (this.list.get(i + dh) != null) {
							g.drawString(this.list.get(i + dh), dx + 22, dy - 5);
						}
						if (this.select == i + dh) {
							this.cursorImage.draw(dx, dy, this.cursorScale);
						}
					}
					if (this.maxDrawHeight < this.size) {
						if (this.selHeight < this.sizeH) {
							this.downMark.draw(this.posX + 50.0F, this.posY);
						}
						if (dh > 0) {
							this.upMark.draw(this.posX + 50.0F, this.posY + (this.maxDrawHeight * this.val) + 10.0F);
						}
					}
				}
			} else {
				for (int i = 0; i < this.maxDrawHeight; i++) {
					dx = this.posX;
					dy = this.posY + ((i) * this.val);
					dh = this.select >= this.maxDrawHeight ? this.select - (this.maxDrawHeight - 1) : 0;
					if (i + dh < this.size) {
						if (this.list.get(i + dh) != null) {
							g.drawString(this.list.get(i + dh), dx + 22, dy - 5);
						}
						if (this.select == i + dh) {
							this.cursorImage.draw(dx, dy, this.cursorScale);
						}
					}
					if (this.maxDrawHeight < this.size) {
						if (this.select < this.size) {
							this.downMark.draw(this.posX + 50.0F, this.posY);
						}
						if (dh > 0) {
							this.upMark.draw(this.posX + 50.0F, this.posY + (this.maxDrawHeight * this.val) + 10.0F);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void keyPressed(int key, char c, EnumKeyInput input) {
		super.keyPressed(key, c, input);
		switch (input) {
		case KEY_DOWN:
			this.moveDown();
			break;
		case KEY_LEFT:
			this.moveLeft();
			break;
		case KEY_RIGHT:
			this.moveRight();
			break;
		case KEY_UP:
			this.moveUp();
			break;
		default:
			break;
		}
	}
	
	private void moveUp() {
		if (!this.list.isEmpty()) SoundListener.play(0);
		
		if (this.isMultiselect) {
			this.select -= this.sizeW;
			this.selHeight -= 1;
			if (this.selHeight < 0) {
				if (this.isNotReturn) {
					this.select = this.selWidth;
					this.selHeight = 0;
				} else {
					int sel1 = this.selWidth + (this.sizeW * (this.sizeH - 1));
					int selH1 = this.sizeH - 1;
					if (sel1 >= this.size) {
						sel1 -= this.sizeW;
						selH1 -= 1;
					}
					this.select = sel1;
					this.selHeight = selH1;
				}
			}
		} else {
			this.select -= 1;
			if (this.select < 0) {
				this.select = this.size - 1;
			}
		}
	}
	
	private void moveDown() {
		if (!this.list.isEmpty()) SoundListener.play(0);
		
		if (this.isMultiselect) {
			this.select += this.sizeW;
			this.selHeight += 1;
			int sel1 = this.sizeW * this.selHeight + this.selWidth;
			if (this.selHeight >= this.sizeH || sel1 >= this.size) {
				if (isNotReturn) {
					this.select = sel1 - this.sizeW;
					this.selHeight -= 1;
				} else {
					this.select = (this.sizeW * this.sizeH + this.selWidth) - (this.sizeW * this.sizeH);
					this.selHeight = 0;
				}
			}
		} else {
			this.select += 1;
			if (this.select >= this.size) {
				this.select = 0;
			}
		}
	}
	
	private void moveLeft() {
		if (this.isMultiselect) {
			if (!this.list.isEmpty()) SoundListener.play(0);
			
			this.selWidth -= 1;
			if (this.selWidth < 0) {
				if (this.isNotReturn) {
					this.selWidth = 0;
				} else {
					int sel1 = this.sizeW * this.selHeight + this.sizeW - 1;
					int selH1 = this.sizeW - 1;
					if (sel1 >= this.size) {
						sel1 = this.size - 1;
						int i = (this.sizeW * this.sizeH) - this.size;
						selH1 -= i;
					}
					this.select = sel1;
					this.selWidth = selH1;
				}
			} else {
				this.select -= 1;
			}
		}
	}
	
	private void moveRight() {
		if (this.isMultiselect) {
			if (!this.list.isEmpty()) SoundListener.play(0);
			
			this.select += 1;
			this.selWidth += 1;
			int sel1 = this.sizeW * this.selHeight + this.selWidth;
			if (this.selWidth >= this.sizeW || sel1 >= this.size) {
				if (this.isNotReturn) {
					this.select = sel1 - 1;
					this.selWidth -= 1;
				} else {
					this.select = this.selHeight * this.sizeW;
					this.selWidth = 0;
				}
			}
		}
	}
	
	public void reset() {
		this.select = this.initSelect;
	}
	
	public void removeAll() {
		if (!list.isEmpty()) {
			this.list.clear();
		}
		this.select = 0;
		this.initSelect = 0;
		this.selWidth = 0;
		this.selHeight = 0;
		this.size = 0;
		this.sizeH = 0;
		this.sizeW = 0;
	}
	
	public int get() {
		return this.select;
	}
	
	public boolean isEmpty() {
		return this.list.isEmpty();
	}
	
	public int getSelectWidth() {
		return this.selWidth;
	}
	public int getSelectHeight() {
		return this.selHeight;
	}
	
	public String getText(int index) {
		return this.list.get(index);
	}
	
	public int getSize() {
		return this.size;
	}
	public int getMaxWidth() {
		return this.sizeW;
	}
	public int getMaxHeight() {
		return this.sizeH;
	}
}
