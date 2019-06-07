package game;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;
import game.GameData.ItemTypes;
import game.GameData.MovementDirections;

public class Player {

	private static int x, y, row, column, spriteIndex;
	private static boolean isSuperSayain, moving;
	private static GameData.MovementDirections movementDirection = MovementDirections.DOWN;
	private static BufferedImage[] spritesToUse = GameData.characterBackwardsSprite;

	static {
		new Timer(GameData.SPRITE_UPDATE_SPEED_MS, new ActionListener() {
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
		Game.game.getTiles()[row][column].setOpacity(0);
		if (!moving) {
			if(direction != movementDirection) {
				setSprite(direction);
			}
			if (direction == MovementDirections.UP && row > 0) {
				if (movementDirection == direction) {
					moving = true;
					row--;
				}
			} else if (direction == MovementDirections.DOWN && row < GameData.TILE_AMOUNT - 1) {
				if (movementDirection == direction) {
					moving = true;
					row++;
				}
			} else if (direction == MovementDirections.LEFT && column > 0) {
				if (movementDirection == direction) {
					moving = true;
					column--;
				}
			} else if (direction == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT - 1) {
				if (movementDirection == direction) {
					moving = true;
					column++;
				}
			}
			if(moving == true) {
				Wumpus.setMoving(true);
				Wumpus.updateFadeMove();
			}
			Game.game.getTiles()[row][column].setDiscovered(true);
			movementDirection = direction;		
		}
	}

	private static void setSprite(MovementDirections direction) {
		if (direction == MovementDirections.UP && row > 0) {
			spritesToUse = !isSuperSayain ? GameData.characterForwardsSprite : GameData.superSayainForwardsSprite;
		} else if (direction == MovementDirections.DOWN && row < GameData.TILE_AMOUNT - 1) {
			spritesToUse = !isSuperSayain ? GameData.characterBackwardsSprite : GameData.superSayainBackwardsSprite;
		} else if (direction == MovementDirections.LEFT && column > 0) {
			spritesToUse = !isSuperSayain ? GameData.characterLeftSprite : GameData.superSayainLeftSprite;
		} else if (direction == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT - 1) {
			spritesToUse = !isSuperSayain ? GameData.characterRightSprite : GameData.superSayainRightSprite;
		}
	}

	public static void updatePos() {
		if (GameData.within(x, GameData.TILE_WIDTH * column) && GameData.within(y, GameData.TILE_HEIGHT * row) ) {
			finalizeMovement(); 
		}else{
			if(!GameData.within(x, GameData.TILE_WIDTH * column)){ 
				if (x < GameData.TILE_WIDTH * column) {
					x += GameData.PLAYER_VELOCITY;
				} else if (x > GameData.TILE_WIDTH * column) {
					x -= GameData.PLAYER_VELOCITY;
				}
			}else if (!GameData.within(y, GameData.TILE_HEIGHT * row)) {
				if (y < GameData.TILE_HEIGHT * row) {
					y += GameData.PLAYER_VELOCITY;
				} else if (y > GameData.TILE_HEIGHT * row) {
					y -= GameData.PLAYER_VELOCITY;
				}
			}
		}
	}
	
	private static void finalizeMovement() {
		Tile.updateTiles();
//		if (row == Wumpus.getRow() && column == Wumpus.getColumn()) {
//			battleWumpus();
//		}
		if(moving == true) {
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
	
	public static void attack() {
		if(isSuperSayain && Game.game.getTiles()[Wumpus.getRow()][Wumpus.getColumn()].isFlashlightAffected() 
				&& isFacing(Wumpus.getRow(), Wumpus.getColumn())) {
			Game.game.explodeTile(Wumpus.getRow(), Wumpus.getColumn(), GameData.ultimateExplosionAnimation);
		}
	}

	public static void shoot() {
		if (movementDirection == MovementDirections.UP && row > 0) {
			Game.game.explodeTile(row - 1, column, GameData.explosionAnimation);
		} else if (movementDirection == MovementDirections.DOWN && row < GameData.TILE_AMOUNT - 1) {
			Game.game.explodeTile(row + 1, column, GameData.explosionAnimation);
		} else if (movementDirection == MovementDirections.LEFT && column > 0) {
			Game.game.explodeTile(row, column - 1, GameData.explosionAnimation);
		} else if (movementDirection == MovementDirections.RIGHT && column < GameData.TILE_AMOUNT - 1) {
			Game.game.explodeTile(row, column + 1, GameData.explosionAnimation);
		}
		Toolbar.removeItem(ItemTypes.EXPLOSIVE);
	}
	
	public static void setSuperSayain(boolean superSayain) {
		isSuperSayain = superSayain;
		if(superSayain == true) {
			setSprite(movementDirection);
			Game.game.explodeTile(row, column, GameData.ultimateExplosionAnimation);
		}
	}
	
	public static void setXPos(int xPos) {
		x = xPos;
	}
	
	public static void setYPos(int yPos) {
		y = yPos;
	}

	public static int getRow() {
		return row;
	}

	public static int getColumn() {
		return column;
	}
	
	public static int getXPos() {
		return x;
	}
	
	public static int getYPos() {
		return y;
	}
	
	private static boolean isFacing(int row, int column) {
		if(row > Player.row) {
			if(movementDirection == MovementDirections.DOWN) {
				return true;
			}
		}else if (row < Player.row) {
			if(movementDirection == MovementDirections.UP) {
				return true;
			}
		}else if (column > Player.column) {
			if(movementDirection == MovementDirections.RIGHT) {
				return true;
			}
		}else if(column < Player.column) {
			if(movementDirection == MovementDirections.LEFT) {
				return true;
			}
		}
		return false;
	}

	public static void render(Graphics g) {
		g.drawImage(spritesToUse[spriteIndex], x, y, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
	}

}