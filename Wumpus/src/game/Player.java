package game;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {
	
	private int row, column;
	private int flashlightRadius;
	private GameData.MovementDirections movementDirection;
	private BufferedImage spriteToUse;
	
	public Player() {
		spriteToUse = GameData.characterBackwardsStillSprite;
		movementDirection = null;
	}
	
	public void move(GameData.MovementDirections direction) {
		if(direction == GameData.MovementDirections.UP && row > 0) {
			spriteToUse = GameData.characterFowardsStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterForwardsRunningSprite;
				row--;
			}
		}else if (direction == GameData.MovementDirections.DOWN && row < GameData.TILE_AMOUNT-1) {
			spriteToUse = GameData.characterBackwardsStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterBackwardsRunningSprite;
				row++;
			}
		}else if (direction == GameData.MovementDirections.LEFT && column > 0) {
			spriteToUse = GameData.characterLeftStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterLeftRunningSprite;
				column--;
			}
		}else if (direction == GameData.MovementDirections.RIGHT && column < GameData.TILE_AMOUNT-1) {
			spriteToUse = GameData.characterRightStillSprite;
			if(movementDirection == direction) {
				spriteToUse = GameData.characterRightRunningSprite;
				column++;
			}
		}
		Game.game.getTiles()[row][column].setDiscovered(true);
		if(flashlightRadius > 0) {
			
		}
		movementDirection = direction;
	}
	
	public void shoot() {
		if(movementDirection == GameData.MovementDirections.UP && row > 0) {
			Game.game.explodeTile(row-1, column);
			Game.game.getTiles()[row-1][column].setDiscovered(true);
		}else if (movementDirection == GameData.MovementDirections.DOWN && row < GameData.TILE_AMOUNT-1) {
			Game.game.explodeTile(row+1, column);
			Game.game.getTiles()[row+1][column].setDiscovered(true);
		}else if (movementDirection == GameData.MovementDirections.LEFT && column > 0) {
			Game.game.explodeTile(row, column-1);
			Game.game.getTiles()[row][column-1].setDiscovered(true);
		}else if (movementDirection == GameData.MovementDirections.RIGHT && column < GameData.TILE_AMOUNT-1) {
			Game.game.explodeTile(row, column+1);
			Game.game.getTiles()[row][column+1].setDiscovered(true);
		}
	}
	
	public void addFlashlight() {
		flashlightRadius++;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getFlashlightRadius() {
		return flashlightRadius;
	}
	
	public void render(Graphics g) {
		g.drawImage(spriteToUse, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}