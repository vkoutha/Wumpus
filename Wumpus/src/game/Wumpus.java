package game;

import java.awt.Graphics;

public class Wumpus {
	
	private int row, column;
	
	public Wumpus() {
		do {
		row = (int) (Math.random() * GameData.TILE_AMOUNT);
		column = (int) (Math.random() * GameData.TILE_AMOUNT);
		}while(row == 0 && column == 0);
	}
	
	public void move() {
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
		System.out.println("Row: " + row + "\tColumn: " + column);
	}
	
	private void battlePlayer() {
		
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	/**
	 * Not used in order to account for needed layering on tile
	 * @param g
	 */
	public void render(Graphics g) {
		g.drawImage(GameData.wumpusSprite, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}
