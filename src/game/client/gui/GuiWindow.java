package game.client.gui;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import game.client.image.ImageUtils;

public class GuiWindow extends TMGuiScreen {
	public GuiWindow(float x, float y, int width, int height) {
		this.posX = x;
		this.posY = y;
		this.width = width;
		this.height = height;
		this.guiImage = ImageUtils.getGuiImage(GuiImageInfo.NORMAL_WINDOW);
	}
	
	@Override
    public void draw(Graphics g) {
		if (!this.isVisible()) return;
		drawContinuousTexturedBox(g, this.posX, this.posY, 0, 0, this.width, this.height, 200, 20, 2, 3, 2, 2);
		g.setColor(Color.white);
    }
    
	private void drawContinuousTexturedBox(Graphics g, float x, float y, int u, int v, int width, int height, int textureWidth, int textureHeight,
			int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;
        
        // Draw Border
        // Top Left
        //g.drawImage(guiImage, x, y, x + width, y + height, u, v, leftBorder, topBorder);
        drawImage(g, x, y, u, v, leftBorder, topBorder);
        // Top Right
        drawImage(g, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);
        // Bottom Left
        drawImage(g, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder);
        // Bottom Right
        drawImage(g, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder);
        
        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            // Top Border
        	drawImage(g, x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder);
            // Bottom Border
            drawImage(g, x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder);
            
            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
            	drawImage(g, x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight));
        }
        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            // Left Border
        	drawImage(g, x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight));
            // Right Border
            drawImage(g, x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight));
        }
	}
	private void drawImage(Graphics g, float x, float y, int u, int v, int width, int height) {
		g.drawImage(this.guiImage, x, y, x + width, y + height, u, v, u + width, v + height);
	}
}
