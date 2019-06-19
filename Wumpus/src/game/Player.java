package game;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import game.GameData.ItemTypes;
import game.GameData.MovementDirections;

public class Player {

	private static int x, y, row, column, spriteIndex;
	private static int energyBallX, energyBallY;
	private static float opacity;
	private static boolean isSuperSayain, moving, firstItemFound, firstSwordFound, firstCompassFound;
	private static GameData.MovementDirections movementDirection = MovementDirections.DOWN;
	private static BufferedImage[] spritesToUse = GameData.characterBackwardsSprite;
	private static Timer energyBallTimer;

	static {
		opacity = 1f;
		energyBallX = -1;
		energyBallY = -1;
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
			if (direction != movementDirection) {
				setSprite(direction);
			}
			if (direction == MovementDirections.UP && row > 0) {
				if (movementDirection == direction) {
					moving = true;
					row--;
				}
			} else if (direction == MovementDirections.DOWN && row < GameData.BOARD_SIZE - 1) {
				if (movementDirection == direction) {
					moving = true;
					row++;
				}
			} else if (direction == MovementDirections.LEFT && column > 0) {
				if (movementDirection == direction) {
					moving = true;
					column--;
				}
			} else if (direction == MovementDirections.RIGHT && column < GameData.BOARD_SIZE - 1) {
				if (movementDirection == direction) {
					moving = true;
					column++;
				}
			}
			if(Game.game.getTiles()[row][column].getItem() == ItemTypes.COMPASS && !firstCompassFound){
				firstCompassFound = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!firstItemFound){
							firstItemFound = true;
							displayToast("Press X to pick a item!");
							try{
								Thread.sleep(50);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						displayToast("Press C to use the compass!");
					}
				}).start();
				
			}else if(Game.game.getTiles()[row][column].getItem() == ItemTypes.SWORD && !firstSwordFound){
				firstSwordFound = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(!firstItemFound){
							firstItemFound = true;
							displayToast("Press X to pick up an item!");
						}
						try{
							Thread.sleep(50);
						}catch(Exception e){
							e.printStackTrace();
						}
						displayToast("Press space with an explosive to shoot!");
					}
				}).start();
			}else if(Game.game.getTiles()[row][column].getItem() != ItemTypes.NONE && !firstItemFound){
				firstItemFound = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						displayToast("Press X to pick a item!");
					}
				}).start();
				
			}
			Game.game.getTiles()[row][column].setDiscovered(true);
			movementDirection = direction;
		}
	}
	
	private static void displayToast(String text){
		Toast toast = new Toast(text, (Player.getColumn()+3) * GameData.TILE_WIDTH, 
				(Player.getRow()+2) * GameData.TILE_HEIGHT);
		toast.showToast();
	}

	private static void setSprite(MovementDirections direction) {
		if (direction == MovementDirections.UP && row > 0) {
			spritesToUse = !isSuperSayain ? GameData.characterForwardsSprite : GameData.superSayainForwardsSprite;
		} else if (direction == MovementDirections.DOWN && row < GameData.BOARD_SIZE - 1) {
			spritesToUse = !isSuperSayain ? GameData.characterBackwardsSprite : GameData.superSayainBackwardsSprite;
		} else if (direction == MovementDirections.LEFT && column > 0) {
			spritesToUse = !isSuperSayain ? GameData.characterLeftSprite : GameData.superSayainLeftSprite;
		} else if (direction == MovementDirections.RIGHT && column < GameData.BOARD_SIZE - 1) {
			spritesToUse = !isSuperSayain ? GameData.characterRightSprite : GameData.superSayainRightSprite;
		}
	}

	public static void updatePos() {
		if (GameData.within(x, GameData.TILE_WIDTH * column, GameData.PLAYER_VELOCITY)
				&& GameData.within(y, GameData.TILE_HEIGHT * row, GameData.PLAYER_VELOCITY)) {
			finalizeMovement();
		} else {
			if (!GameData.within(x, GameData.TILE_WIDTH * column, GameData.PLAYER_VELOCITY)) {
				if (x < GameData.TILE_WIDTH * column) {
					x += GameData.PLAYER_VELOCITY;
				} else if (x > GameData.TILE_WIDTH * column) {
					x -= GameData.PLAYER_VELOCITY;
				}
			} else if (!GameData.within(y, GameData.TILE_HEIGHT * row, GameData.PLAYER_VELOCITY)) {
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
		x = GameData.TILE_WIDTH * column;
		y = GameData.TILE_HEIGHT * row;
		if (row == Wumpus.getRow() && column == Wumpus.getColumn()) {
			System.out.println("GOT TO HEREEEEE");
			battleWumpus();
		} else {
			if (moving == true) {
				Wumpus.setMoving(true);
			}
		}
		if (moving == true) {
			moving = false;
			spriteIndex = 0;
		}
	}

	private static void battleWumpus() {
		double chance = (Math.random() * 100);
		if (Toolbar.weaponAvailable()) {
			if (chance <= GameData.PLAYER_INTO_WUMPUS_CHANCE + GameData.SUPER_SAYAIN_INCREASE_CHANCE) {
				Game.game.setWinner(true);
			} else {
				Game.game.setWinner(false);
			}
		} else {
			if (chance <= GameData.PLAYER_INTO_WUMPUS_CHANCE) {
				Game.game.setWinner(true);
			} else {
				Game.game.setWinner(false);
			}
		}
	}

	public static void shoot() {
		if (!isSuperSayain) {
			if (movementDirection == MovementDirections.UP && row > 0) {
				Game.game.explodeTile(row - 1, column, GameData.explosionAnimation);
			} else if (movementDirection == MovementDirections.DOWN && row < GameData.BOARD_SIZE - 1) {
				Game.game.explodeTile(row + 1, column, GameData.explosionAnimation);
			} else if (movementDirection == MovementDirections.LEFT && column > 0) {
				Game.game.explodeTile(row, column - 1, GameData.explosionAnimation);
			} else if (movementDirection == MovementDirections.RIGHT && column < GameData.BOARD_SIZE - 1) {
				Game.game.explodeTile(row, column + 1, GameData.explosionAnimation);
			}
			Toolbar.removeItem(ItemTypes.EXPLOSIVE);
		} else {
			if (energyBallX == -1 && energyBallY == -1) {
				if (Player.isFacing(Wumpus.getRow(), Wumpus.getColumn())) {
					Wumpus.setOpacity(1f);
					Wumpus.setMoving(moving);
					spawnEnergyBall(Wumpus.getRow(), Wumpus.getColumn());
				} else if (movementDirection == MovementDirections.UP) {
					spawnEnergyBall(0, column);
				} else if (movementDirection == MovementDirections.DOWN) {
					spawnEnergyBall(GameData.BOARD_SIZE - 1, column);
				} else if (movementDirection == MovementDirections.LEFT) {
					spawnEnergyBall(row, 0);
				} else if (movementDirection == MovementDirections.RIGHT) {
					spawnEnergyBall(row, GameData.BOARD_SIZE - 1);
				}
			}
			Toolbar.removeItem(ItemTypes.EXPLOSIVE);
		}
	}

	private static void spawnEnergyBall(final int eRow, final int eCol) {
		energyBallX = x;
		energyBallY = y;
		final int row = Player.row;
		final int col = Player.column;
		energyBallTimer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (eRow < row) {
					energyBallY -= GameData.ENERGY_BALL_VELOCITY;
				} else if (eRow > row) {
					energyBallY += GameData.ENERGY_BALL_VELOCITY;
				} else if (eCol < col) {
					energyBallX -= GameData.ENERGY_BALL_VELOCITY;
				} else if (eCol > col) {
					energyBallX += GameData.ENERGY_BALL_VELOCITY;
				}
				int currRow = ((energyBallY) / GameData.TILE_HEIGHT);
				int currCol = ((energyBallX) / GameData.TILE_WIDTH);
				if (movementDirection == MovementDirections.DOWN) {
					currRow += 1;
				} else if (movementDirection == MovementDirections.RIGHT) {
					currCol += 1;
				}
				if ((energyBallX > GameData.FRAME_WIDTH || energyBallX < 0)
						|| (energyBallY > GameData.FRAME_HEIGHT || energyBallY < 0)) {
					energyBallX = -1;
					energyBallY = -1;
					energyBallTimer.stop();
				} else if (currRow >= 0 && currRow <= GameData.BOARD_SIZE - 1 && currCol >= 0
						&& currCol <= GameData.BOARD_SIZE - 1) {
					Game.game.getTiles()[currRow][currCol].setOpacity(0f);
					Game.game.getTiles()[currRow][currCol].removeItem();
				}
				if ((GameData.within(energyBallX, eCol * GameData.TILE_WIDTH, GameData.ENERGY_BALL_VELOCITY)
						&& GameData.within(energyBallY, eRow * GameData.TILE_HEIGHT, GameData.ENERGY_BALL_VELOCITY))) {
					Game.game.explodeTile(eRow, eCol, GameData.ultimateExplosionAnimation);
					energyBallX = -1;
					energyBallY = -1;
					energyBallTimer.stop();
					Wumpus.setMoving(true);
				}
			}
		});
		energyBallTimer.start();
	}

	public static void setSuperSayain(boolean superSayain) {
		isSuperSayain = superSayain;
		setSprite(movementDirection);
		if (superSayain == true) {
			Game.game.explodeTile(row, column, GameData.ultimateExplosionAnimation);
		}
	}

	public static void setLocation(int row, int column) {
		Player.row = row;
		Player.column = column;
		x = column / GameData.TILE_WIDTH;
		y = row / GameData.TILE_HEIGHT;
	}
	
	public static void setOpacity(float opacity){
		Player.opacity = opacity;
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

	public static boolean isSuperSayain() {
		return isSuperSayain;
	}

	private static boolean isFacing(int row, int column) {
		if (row > Player.row && column == Player.column) {
			if (movementDirection == MovementDirections.DOWN) {
				return true;
			}
		} else if (row < Player.row && column == Player.column) {
			if (movementDirection == MovementDirections.UP) {
				return true;
			}
		} else if (column > Player.column && row == Player.row) {
			if (movementDirection == MovementDirections.RIGHT) {
				return true;
			}
		} else if (column < Player.column && row == Player.row) {
			if (movementDirection == MovementDirections.LEFT) {
				return true;
			}
		}
		return false;
	}

	public static void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.SrcOver.derive(opacity));
		g2.drawImage(spritesToUse[spriteIndex], x, y, GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		if (energyBallX != -1 && energyBallY != -1) {
			g2.drawImage(GameData.energyBallSprite, energyBallX, energyBallY, GameData.TILE_WIDTH, GameData.TILE_HEIGHT,
					null);
		}
	}

}