package game;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import game.GameData.MovementDirections;

public class Player {

	private static int x, y, row, column, spriteIndex;
	private static boolean moving;
	private static GameData.MovementDirections movementDirection = MovementDirections.DOWN;
	private static BufferedImage[] spritesToUse = GameData.characterBackwardsSprite;

	static {
		new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (moving) {
					spriteIndex++;
					if (spriteIndex == spritesToUse.length) {
						spriteIndex = 0;
					}
				}
			}
		}).start();
	}

	public static void move(MovementDirections direction) {
		if (!moving) {
			if (direction == MovementDirections.UP && row > 0) {
				spritesToUse = GameData.characterForwardsSprite;
				if (movementDirection == direction) {
					moving = true;
					row--;
				}
			} else if (direction == MovementDirections.DOWN && row < GameData.TILE_AMOUNT - 1) {
				spritesToUse = GameData.characterBackwardsSprite;
				if (movementDirection == direction) {
					moving = true;
					row++;
				}
			} else if (direction == MovementDirections.LEFT && column > 0) {
				spritesToUse = GameData.characterLeftSprite;
				if (movementDirection == direction) {
					moving = true;
					column--;
				}
			} else if (direction == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT - 1) {
				spritesToUse = GameData.characterRightSprite;
				if (movementDirection == direction) {
					moving = true;
					column++;
				}
			}
			//Game.game.getTiles()[row][column].setDiscovered(true);
			movementDirection = direction;
		}
	}

	public static void updatePos() {
		if (GameData.within(x, GameData.TILE_WIDTH * column) && GameData.within(y, GameData.TILE_HEIGHT * row) ) {
			finalizeMovement();
		}else {
			if (x < GameData.TILE_WIDTH * column) {
				x += GameData.PLAYER_VELOCITY;
			} else if (x > GameData.TILE_WIDTH * column) {
				x -= GameData.PLAYER_VELOCITY;
			}
			if (y < GameData.TILE_HEIGHT * row) {
				y += GameData.PLAYER_VELOCITY;
			} else if (y > GameData.TILE_HEIGHT * row) {
				y -= GameData.PLAYER_VELOCITY;
			}
		}
		
	}
	
	private static void finalizeMovement() {
		Tile.updateAffectedTiles();
//		if (row == Wumpus.getRow() && column == Wumpus.getColumn()) {
//			battleWumpus();
//		}
		if(moving == true) {
			Wumpus.move();
			moving = false;
			spriteIndex = 0;
		}
		
	}

	private static void battleWumpus() {
		double chance = (Math.random() * 100);
		if (Toolbar.weaponAvailable()) {
			if (chance <= 80) {
				Game.game.setWinner(true);
			} else {
				Game.game.setWinner(false);
			}
		} else {
			if (chance <= 20) {
				Game.game.setWinner(true);
			} else {
				Game.game.setWinner(false);
			}
		}
	}

	public static void shoot() {
		if (movementDirection == MovementDirections.UP && row > 0) {
			Game.game.explodeTile(row - 1, column);
		} else if (movementDirection == MovementDirections.DOWN && row < GameData.TILE_AMOUNT - 1) {
			Game.game.explodeTile(row + 1, column);
		} else if (movementDirection == MovementDirections.LEFT && column > 0) {
			Game.game.explodeTile(row, column - 1);
		} else if (movementDirection == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT - 1) {
			Game.game.explodeTile(row, column + 1);
		}
	}

	public static int getRow() {
		return row;
	}

	public static int getColumn() {
		return column;
	}

	public static void render(Graphics g) {
		g.drawImage(spritesToUse[spriteIndex], x, y, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}