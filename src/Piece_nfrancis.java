// Niven Francis
// AP Computer Science
// Mr. Balanda
// 3/14/14
// Tetris

import java.awt.Color;

// Reference for each piece:
/*
 -1,0    0,0     1,0     2,0 line
 -1,0    0,0     1,0     0,1 T
 -1,1    -1,0    0,0     1,0 L
 -1,0    0,0     1,0     1,1 J  
 -1,0    -1,1    0,0     0,1 square
 -1,0    0,0     0,1     1,1 Z
 -1,1    0,1     0,0     1,0 backwards Z
 */

public class Piece_nfrancis {
	public  int startx = 5;
	public  int starty = 0;
	public  int speed = 1;
	public  int tempx = 4;
	public  int tempy = 0;

    public int noffsetx = 12;
    public int noffsety = 8;
	
    // All arrays that contains values of each block.
    
    // These arrays are the main ones used on the actual grid.
	public  int[][] arrayx = { { 0, 1, 2, 3 }, { 0, 1, 2, 1 }, { 0, 0, 1, 2 }, { 0, 1, 2, 2 }, { 0, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
	public  int[][] arrayy = { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 1, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 1, 0, 1 }, { 0, 0, 1, 1 }, { 1, 1, 0, 0 } };
	
	// narray's are the arrays used for the next block window.
	public  int[][] narrayx = { { 0, 1, 2, 3 }, { 0, 1, 2, 1 }, { 0, 0, 1, 2 }, { 0, 1, 2, 2 }, { 0, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
	public  int[][] narrayy = { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 1, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 1, 0, 1 }, { 0, 0, 1, 1 }, { 1, 1, 0, 0 } };
	
	// carray's are the template arrays. So if a piece is rotated, arrayx is set to carrayx so that once a new block is spawned, it won't be rotated.
	public  final int[][] carrayx = { { 0, 1, 2, 3 }, { 0, 1, 2, 1 }, { 0, 0, 1, 2 }, { 0, 1, 2, 2 }, { 0, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 1, 1, 2 } };
	public  final int[][] carrayy = { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 1, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 1, 0, 1 }, { 0, 0, 1, 1 }, { 1, 1, 0, 0 } };
	
	// Arrayc is used to store the colors for each corresponding block.
	public  Color[] arrayc = { Color.cyan, Color.magenta, Color.orange, Color.blue, Color.yellow, Color.red, Color.green };
	
    public int rdm = 0;
    public int nrdm = (int) (Math.random() * 7);
    
    public int offsetx = 4;
    public int offsety = 0;
    
    public Tetris_nfrancis tetris;
    
    // Constructor that generates the piece and the next block piece to be used, as well as other values.
	public Piece_nfrancis(Tetris_nfrancis tempTetris, int nextPiece) {
	    tetris = tempTetris;
	    
	    rdm = nextPiece;
        nrdm = (int) (Math.random() * 7);
        offsetx = 4;
        offsety = 0;
        
        for (int x = 0; x < 4; x++) {
            arrayx[rdm][x] = carrayx[rdm][x];
        }
        
        for (int x = 0; x < 4; x++) {
            arrayy[rdm][x] = carrayy[rdm][x];
        }
        
        tempx = 4;
        tempy = 0;
	}
	
	// Rotation method which is called when the user presses W. It uses the rotation matrix to revolve the piece around a point.
	public  void rotate() {
		for(int x = 0; x < 4; x++)
			Board_nfrancis.grid[tempx + arrayx[rdm][x]][tempy + arrayy[rdm][x]] = Color.black;
		
		for(int x = 0; x < 4; x++) {
		    int temp2x = arrayx[rdm][x];
		    int temp2y = arrayy[rdm][x];
		    int rotx = temp2y * -1;
		    int roty = temp2x;
		    arrayx[rdm][x] = rotx;
		    arrayy[rdm][x] = roty;
		}
		
		tetris.collision(true);
		
		if(tetris.collide) {
			for(int x = 0; x < 4; x++) {
			int temp2x = arrayx[rdm][x];
		    int temp2y = arrayy[rdm][x];
		    int roty = temp2x * -1;
		    int rotx = temp2y;
		    arrayx[rdm][x] = rotx;
		    arrayy[rdm][x] = roty;
			}
		}
		
		tetris.collide = false;
		tetris.redraw();
	}
}