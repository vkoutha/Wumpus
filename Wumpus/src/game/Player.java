package game;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.GameData.MovementDirections;

public class Player {
	
	private int row, column;
	private GameData.MovementDirections movementDirection;
	private BufferedImage spriteToUse;
	
	public Player() {
		spriteToUse = GameData.characterBackwardsStillSprite;
		movementDirection = MovementDirections.DOWN;
	}
	
	public void move(MovementDirections direction) {
		int prevRow = row, prevCol = column;
		if(direction == MovementDirections.UP && row > 0) {
			spriteToUse = GameData.characterFowardsStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterForwardsRunningSprite;
				row--;
			}
		}else if (direction == MovementDirections.DOWN && row < GameData.TILE_AMOUNT-1) {
			spriteToUse = GameData.characterBackwardsStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterBackwardsRunningSprite;
				row++;
			}
		}else if (direction == MovementDirections.LEFT && column > 0) {
			spriteToUse = GameData.characterLeftStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterLeftRunningSprite;
				column--;
			}
		}else if (direction == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT-1) {
			spriteToUse = GameData.characterRightStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterRightRunningSprite;
				column++;
			}
		}
		Tile.updateAffectedTiles();
		if(row == Game.game.getWumpus().getRow() && column == Game.game.getWumpus().getColumn()) {
			battleWumpus();
		}
		if(prevRow != row || prevCol != column) {
			Game.game.getWumpus().move();
		}
		Game.game.getTiles()[row][column].setDiscovered(true);
		movementDirection = direction;
	}
	
	private void battleWumpus() {
		double chance = (Math.random()*100);
		if(Game.game.getToolbar().weaponAvailable()) {
			if(chance <= 80) {
				Game.game.setWinner(true);
			}else {
				Game.game.setWinner(false);
			}
		}else {
			if(chance <= 20) {
				Game.game.setWinner(true);
			}else {
				Game.game.setWinner(false);
			}
		}
	}
	
	
	public void shoot() {
		if(movementDirection == MovementDirections.UP && row > 0) {
			Game.game.explodeTile(row-1, column);
		}else if (movementDirection == MovementDirections.DOWN && row < GameData.TILE_AMOUNT-1) {
			Game.game.explodeTile(row+1, column);
		}else if (movementDirection == MovementDirections.LEFT && column > 0) {
			Game.game.explodeTile(row, column-1);
		}else if (movementDirection == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT-1) {
			Game.game.explodeTile(row, column+1);
		}
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void render(Graphics g) {
		g.drawImage(spriteToUse, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}