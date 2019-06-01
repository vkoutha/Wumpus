package game;

import java.awt.Graphics;

public class Wumpus {
	
	private static int row, column;
	
	static {
		do {
			row = (int) (Math.random() * GameData.TILE_AMOUNT);
			column = (int) (Math.random() * GameData.TILE_AMOUNT);
		}while(row == 0 && column == 0);
	}
	
	public static void move() {
		int rowVColumn = (int) (Math.random()*2);
		int movement;
		if(rowVColumn == 0) {
			if(row == 0) {
				movement = 1;
			}else if (row == GameData.TILE_AMOUNT-1) {
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
			}else if (column == GameData.TILE_AMOUNT-1) {
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
	
	private static void battlePlayer() {
		double chance = (Math.random()*100);
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
