// Niven Francis
// AP Computer Science
// Mr. Balanda
// 3/14/14
// Tetris

import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import java.awt.event.KeyAdapter;

public class Tetris_nfrancis extends JFrame {

	private static final long serialVersionUID = 1L;

	public int nextX = 0;
	public int nextY = 0;
	public boolean checkGameOver = false;
	public boolean inGame = true;
	
	public boolean up = false;
	public boolean down = false;
	public boolean left = false;
	public boolean right = false;
	public boolean pause = false;
	
	public int score = 0;
    
    public Board_nfrancis board;
    public Piece_nfrancis piece;
    
    public boolean epic = true;

	public boolean collide = true;
	public int timestep = 500;

	public boolean first = true;
	public boolean start = true;
    
	public boolean gameOver = false;
	
	// Main method.
	public static void main(String[] args) {
		new Tetris_nfrancis();
	}

	// Constructor that creates a board and piece object of random value between 0 and 7.
	public Tetris_nfrancis() {
		super("TETRIS");setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		board = new Board_nfrancis(this);
		add(board, BorderLayout.CENTER);
		piece = new Piece_nfrancis(this,(int) (Math.random() * 7));
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		keys();
		runGame();
	}
	
	// Method that starts the game, sets all values, and runs other methods including the timer method.
	public void runGame(){
	    while (start) {
            if (collide) {
                piece = new Piece_nfrancis(this,piece.nrdm);
                epic = true;
                checkGameOver();
                collide = false;
                first = true;
                pause = false;
                lineCheck();
            }
            timer();
        }
	}
	
	// Redraws the piece. Used after hitting a button so the piece will immediately be drawn.
	public void redraw() {
		for(int x = 0; x < 4; x++)
			Board_nfrancis.grid[piece.tempx + piece.arrayx[piece.rdm][x]][piece.tempy + piece.arrayy[piece.rdm][x]] = Color.black;
		for(int x = 0; x < 4; x++) {
        	int xg = piece.arrayx[piece.rdm][x] + piece.offsetx;
        	int yg = piece.arrayy[piece.rdm][x] + piece.offsety;
        	Board_nfrancis.grid[xg][yg] = piece.arrayc[piece.rdm];
		}
		piece.tempx = piece.offsetx;
		piece.tempy = piece.offsety;
		board.repaint();
	}
	
	// Soft/slow drop. Increments the piece's offsety value by 1 so it's faster than the regular timer, but slower than hard/fast drop.
	public void sdrop() {
		piece.offsety++;
		collision(false);
		if(collide) {
			epic = false;
			piece.offsety--;
		}
		redraw();
	}
	
	// Moves the piece to the left by adjusting its offsetx value.
	public void left() {
		piece.offsetx--;
		collision(false);
		if(collide){
			piece.offsetx++;
			collide = false;
			return;
		}
		redraw();
	}
	
	// Moves the piece to the right by adjusting its offsetx value.
	public void right() {
		piece.offsetx++;
		collision(false);
		if(collide){
			piece.offsetx--;
			collide = false;
			return;
		}
		redraw();
	}
	
	// Hard/fast drop which immediately places the block to the lowest possible position on the board.
	public void hdrop() {
		while(!collide) {
			piece.offsety++;
			collision(false);
		}
		collision(false);
		if(collide) {
			epic = false;
			piece.offsety--;
		}
		redraw();
	}
	
	// The method that draws the pieces, pushes the piece down, gets rid of the previous piece, and checks for collisions.
	public void timer() {
		int xg;
		int yg;
		int nxg;
		int nyg;
		for (int x = 0; x < 4; x++) {
			// This part makes the program go into an infinite loop of sleep once it's game over. The game can then be reset if the N key is pressed (new game).
		    while(gameOver) {
		        try {
                    Thread.sleep(9);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
		    }
		    
			if (!first) {
				// Removes the previous piece so the piece can move down without having any leftover segments.
				Board_nfrancis.grid[piece.arrayx[piece.rdm][x] + piece.tempx][piece.arrayy[piece.rdm][x]+ piece.tempy] = Color.black;
			}
			
			if (first) {
				for (int x1 = 0; x1 < 4; x1++)
					Board_nfrancis.grid[piece.arrayx[piece.rdm][x1] + piece.noffsetx][piece.arrayy[piece.rdm][x1]+ piece.noffsety] = Color.black;
			}

			collision(false);
			
			if (collide && !first) {
				piece.offsety--;
			}
			
			if (!first) {
				update();
			}
			// This draws the piece onto the actual grid.
			for (int x1 = 0; x1 < 4; x1++) {
				xg = piece.arrayx[piece.rdm][x1] + piece.offsetx;
				yg = piece.arrayy[piece.rdm][x1] + piece.offsety;
				Board_nfrancis.grid[xg][yg] = piece.arrayc[piece.rdm];
			}
			// This draws the next block to the side of the grid showing the user what the next block is.
			for (int q = 0; q < 4; q++) {
				nxg = piece.narrayx[piece.nrdm][q] + piece.noffsetx;
				nyg = piece.narrayy[piece.nrdm][q] + piece.noffsety;
				Board_nfrancis.grid[nxg][nyg] = piece.arrayc[piece.nrdm];
			}
			
			piece.tempx = piece.offsetx;
			piece.tempy = piece.offsety;
			board.repaint();
			
			// The timestep that controls how fast the piece will drop.
			try {
				Thread.sleep(timestep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Moves the piece's offsety value down (by adding).
			if (!pause)
				piece.offsety++;
			first = false;
		}
	}
	
	// Collision method that checks to see if the block can be moved, rotated, or if it's game over.
	// What it does is that if the block is to the side of the grid, and rotates, the block actually DOES rotate. But if it's invalid (checked using an if statement), it moves the piece back to where it was.
	public void collision(boolean rotated) {
		for (int x = 0; x < 4; x++) {
			if ((piece.arrayx[piece.rdm][x] + piece.offsetx < 0)
					|| (piece.arrayx[piece.rdm][x] + piece.offsetx > 8)) {
				collide = true;
				return;
			}
			if ((piece.arrayy[piece.rdm][x] + piece.offsety < 0)) {
				collide = true;
				if (!rotated)
					start = false;
				piece.offsety++;
				return;
			}
			if (piece.offsety + piece.arrayy[piece.rdm][x] >= 20) {
				collide = true;
				piece.offsety = 20 - piece.arrayy[piece.rdm][x];
				return;
			}
		}
		if (rotated) {
			for (int x = 0; x < 4; x++) {
				int temp2x = piece.arrayx[piece.rdm][x];
				int temp2y = piece.arrayy[piece.rdm][x];
				int roty = temp2x * -1;
				int rotx = temp2y;
				piece.arrayx[piece.rdm][x] = rotx;
				piece.arrayy[piece.rdm][x] = roty;
			}
		}
		update();
		if (rotated) {
			for (int x = 0; x < 4; x++) {
				int temp2x = piece.arrayx[piece.rdm][x];
				int temp2y = piece.arrayy[piece.rdm][x];
				int rotx = temp2y * -1;
				int roty = temp2x;
				piece.arrayx[piece.rdm][x] = rotx;
				piece.arrayy[piece.rdm][x] = roty;
			}
		}
		for (int x = 0; x < 4; x++) {
			if (Board_nfrancis.grid[piece.offsetx + piece.arrayx[piece.rdm][x]][piece.offsety + piece.arrayy[piece.rdm][x]] != Color.black) {
				collide = true;
				return;
			}
		}
		if (piece.offsety == 0 && collide) {
			checkGameOver();
		}
		for (int x = 0; x < 9; x++) {
			if (piece.offsety == 0 && Board_nfrancis.grid[x][0] != Color.black) {
				checkGameOver();
			}
		}
	}
	
	// Once the N key is pressed, this method is called. It removes all blocks from grid and generates new values for the next pieces.
	public void newGame() {
		first = false;
		for (int x = 0; x < Board_nfrancis.NUM_COLS; x++) {
			for (int y = 0; y < Board_nfrancis.NUM_ROWS; y++) {
				Board_nfrancis.grid[x][y] = Color.black;
				board.repaint();
			}
		}
		piece = new Piece_nfrancis(this,(int) (Math.random() * 7));
		collide = false;
		timestep = 500;
		first = true;
		start = true;
		pause = false;
		gameOver = false;
	}
	
	// Update makes sure that the previous piece is removed. Used when colliding.
	public void update() {
		for (int x = 0; x < 4; x++) {
			Board_nfrancis.grid[piece.arrayx[piece.rdm][x] + piece.tempx][piece.arrayy[piece.rdm][x] + piece.tempy] = Color.black;
		}
	}
	
	// Checks if it's game over. Also sets the Tetris logo image to the "gaemovur" image via changing the boolean value of gameOver to true.
	public void checkGameOver(){
		for (int x1 = 0; x1 < 4; x1++){
			int xg = piece.arrayx[piece.rdm][x1] + piece.offsetx;
			int yg = piece.arrayy[piece.rdm][x1] + piece.offsety;
			if(Board_nfrancis.grid[xg][yg] != Color.black) {
				pause = true;
				start = false;
				gameOver = true;
				board.repaint();
			}
		}
	}
	
	// Checks each y value in the grid to see if each x value is != black (filled). 
	public void lineCheck() {
		for (int y = 0; y < 20; y++) {
			int count = 0;
			for (int x = 0; x < 9; x++) {
				if (Board_nfrancis.grid[x][y] != Color.black)
					count++;
			}
			if (count == 9) {
				score += 1;
				for (int x = 0; x < 9; x++) {
					Board_nfrancis.grid[x][y] = Color.black;
				}
				board.repaint();
				moveline(y);
			}
		}
	}
	
	// If a line is cleared, this method moves all values above the line down.
	public void moveline(int row) {
		Color temp;
		for (int y = row; y > 0; y--) {
			for (int x = 0; x < 9; x++) {
				temp = Board_nfrancis.grid[x][y];
				if (temp != Color.black) {
					Board_nfrancis.grid[x][y] = Color.black;
					Board_nfrancis.grid[x][y + 1] = temp;

				}
			}
		}
	}
	
	// Enables the keys of a keyboard to be used.
	public void keys() {
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// When user presses W or UP
				if(epic = false)
					return;
				if ((key == KeyEvent.VK_W) && !pause) {
					piece.rotate();
				} 
				else if ((key == KeyEvent.VK_UP) && !pause) {
					piece.rotate();
				}
				// When user presses S or DOWN
				if ((key == KeyEvent.VK_S) && !collide && !pause) {
					sdrop();
				} 
				else if ((key == KeyEvent.VK_DOWN) && !pause) {
					sdrop();
				}
				// When user presses A or LEFT
				if ((key == KeyEvent.VK_A) && !collide && !pause) {
					left();
				} 
				else if ((key == KeyEvent.VK_LEFT) && (left = true) && !pause) {
					left();
				}
				// When user presses D or RIGHT
				if ((key == KeyEvent.VK_D) && !collide && !pause) {
					right();
				} 
				else if ((key == KeyEvent.VK_RIGHT) && (right = true) && !pause) {
					right();
				}
				// When user presses Space
				if ((key == KeyEvent.VK_SPACE) && (collide == false) && !pause) {
					hdrop();
				}
				// When user presses N
				if ((key == KeyEvent.VK_N)) {
					newGame();
					score = 0;
	    			redraw();
				}
				if ((key == KeyEvent.VK_T) && !pause) {
                    timestep += 100;
                    if (timestep > 1600) 
                        timestep = 1600;
				}
				if ((key == KeyEvent.VK_G) && !pause) {
                    timestep -= 100;
                    if (timestep < 100) 
                        timestep = 100;
                }
				if ((key == KeyEvent.VK_P) && (!pause) && !collide) {
					pause = true;
                }
				if ((key == KeyEvent.VK_O) && (pause)) {
					pause = false;
                }
				
			}
		});
	}
}