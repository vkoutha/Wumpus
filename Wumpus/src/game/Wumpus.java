package game;

import java.awt.Graphics;

public class Wumpus {
	
	private static int row, column;
	public static boolean moving;
	private static float opacity;
	
	static {
		opacity = 0f;
		do {
			row = (int) (Math.random() * GameData.BOARD_SIZE);
			column = (int) (Math.random() * GameData.BOARD_SIZE);
		}while(row == 0 && column == 0);
	}
	
	public static void move() {
		int rowVColumn = (int) (Math.random()*2);
		int movement;
		if(rowVColumn == 0) {
			if(row == 0) {
				movement = 1;
			}else if (row == GameData.BOARD_SIZE-1) {
				movement = -1;
			}else{
				movement = (int) (Math.random()*2);
				if(movement == 0) {
					movement = 1;
				}else{
					movement = -1;
				}
			}
			row+= movement;
		}else {
			if(column == 0) {
				movement = 1;
			}else if (column == GameData.BOARD_SIZE-1) {
				movement = -1;
			}else{
				movement = (int) (Math.random()*2);
				if(movement == 0) {
					movement = 1;
				}else{
					movement = -1;
				}
			}
			column += movement;
		}
		if(row == Player.getRow() && column == Player.getColumn()) {
			battlePlayer();
		}
	}
	
	public static void updateFadeMove() {
		if(opacity > 0f && moving) {
			opacity -= GameData.WUMPUS_FADE_SPEED;
			if(opacity <= 0f) {
				move();
				moving = false;
				opacity = 0f;
			}
		}else if(opacity + GameData.WUMPUS_FADE_SPEED <= 1f && Game.game.getTiles()[row][column].isFlashlightAffected()) {
			opacity += GameData.WUMPUS_FADE_SPEED;	
		}
		if(row == Player.getRow() || column == Player.getColumn()){
			opacity = 1f;
		}
	}
	
	public static void reset() {
		opacity = 0f;
		do {
			row = (int) (Math.random() * GameData.BOARD_SIZE);
			column = (int) (Math.random() * GameData.BOARD_SIZE);
		}while(row == 0 && column == 0);
	}
	
	public static void setMoving(boolean moving) {
		Wumpus.moving = moving;
	}
	
	private static void battlePlayer() {
		double chance = (Math.random()*100);
		Game.game.startMusic(GameData.battleSong, false);
		if(Toolbar.weaponAvailable()) {
			if(chance <= 65) {
				Game.game.setWinner(true);
			}else {
				Game.game.setWinner(false);
			}
		}else {
			if(chance <= 5) {
				Game.game.setWinner(true);
			}else {
				Game.game.setWinner(false);
			}
		}
	}
	
	public static float getOpacity() {
		return opacity;
	}
	
	public static int getRow() {
		return row;
	}
	
	public static int getColumn() {
		return column;
	}
	
	/**
	 * Not used in order to account for needed layering on tile
	 * @param g
	 */
	public static void render(Graphics g) {
		//g.drawImage(GameData.wumpusSprite, x, y, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}
