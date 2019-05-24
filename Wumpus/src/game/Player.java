package game;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {
	
	private int row, column;
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
		movementDirection = direction;
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