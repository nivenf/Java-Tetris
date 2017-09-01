// Niven Francis
// AP Computer Science
// Mr. Balanda
// 3/14/14
// Tetris

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Board_nfrancis extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public static final int SIZE = 25;
	public static final int BLOCKSIZE = 30;
	
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLS = 50;
    public static Color[][] grid = new Color[NUM_COLS][NUM_ROWS];
	
	public Tetris_nfrancis tetris;
    
	// Calls the images in the root folder. Please note the paused image (pawsd.jpg) is created entirely out of stars.
	private BufferedImage image;
	private BufferedImage image2;
	private BufferedImage paused;
	
	// Constructor that creates the board and displays the images.
	public Board_nfrancis(Tetris_nfrancis tempTetris) {
	    tetris = tempTetris;
		setPreferredSize(new Dimension(40 * SIZE, 20 * SIZE));
		setBackground(Color.BLACK);
		try {                
	          image = ImageIO.read(new File("tetrislogo.jpg"));
	          image2 = ImageIO.read(new File("gaemovur.jpg"));
	          paused = ImageIO.read(new File("pawsd.jpg"));
	       } catch (IOException ex) {
	       }
		for(int x = 0; x < NUM_COLS; x++) {
            for(int y = 0; y < NUM_ROWS; y++) {
                grid[x][y] = Color.black;
            }
        }
	}
	
	// This method puts the strings, lines, and tiles on the board. 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!tetris.gameOver && !tetris.pause) {
	        g.drawImage(image, 500, 0, null);
		}
		else if(tetris.gameOver) {
		    g.drawImage(image2, 500, 0, null);
		}
		else if(tetris.pause) {
		    g.drawImage(paused, 500, 0, null);
		}
		g.setColor(Color.GRAY);
		g.drawRect(0, 0, 201 - 1, getHeight() - 1);
		g.drawString("By Niven", 645, 150);
		g.drawString("LINES:", 580, 300);
		g.drawString("" + tetris.score, 650, 300);
        g.drawString("TIME:", 580, 350);
        g.drawString("" + tetris.timestep, 650, 350);
		g.drawString("Next", 258, 220);
		g.drawString("Block:", 254, 240);
		g.drawString("Controls", 780, 220);
		g.drawString("W or UP - Rotate", 780, 240);
		g.drawString("A/D or Left/Right - Move", 780, 260);
		g.drawString("T/G - Timescale", 780, 280);
		g.drawString("S or Down - Slow Drop", 780, 300);
		g.drawString("Space - Fast Drop", 780, 320);
		g.drawString("N - New Game", 780, 340);
		g.drawString("P - Pause", 780, 360);
		g.drawString("O - Unpause", 780, 380);
		g.drawLine(780, 221, 827, 221);
		// Box around score/time
		g.drawLine(560, 270, 700, 270);
		g.drawLine(560, 370, 700, 370);
		g.drawLine(560, 270, 560, 370);
		g.drawLine(700, 270, 700, 370);
		// Box around controls
		g.drawLine(760, 200, 930, 200);
		g.drawLine(760, 390, 930, 390);
		g.drawLine(760, 200, 760, 390);
		g.drawLine(930, 200, 930, 390);
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				drawTile(x*SIZE,y*SIZE,grid[x][y],g);
				g.drawLine(x * SIZE, 0, x * SIZE, getHeight());
				g.drawLine(0, y * SIZE, getWidth() - 775, y * SIZE);
			}
		}
		for (int x = 12; x < 16; x++) {
            for (int y = 0; y < 20; y++) {
                drawTile(x*SIZE,y*SIZE,grid[x][y],g);
            }
        }
	}

	private void drawTile(int x, int y, Color type, Graphics g) {
	    g.setColor(type);
        g.fillRect(x, y, BLOCKSIZE, BLOCKSIZE);
        g.setColor(Color.GRAY);
    }
}