package game;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Wumpus {

	private static int row, column;
	public static boolean moving;
	private static float opacity;

	static {
		opacity = 0f;
		do {
			row = (int) (Math.random() * GameData.BOARD_SIZE);
			column = (int) (Math.random() * GameData.BOARD_SIZE);
		} while (row == 0 && column == 0);
	}

	public static void move() {
		int rowVColumn = (int) (Math.random() * 2);
		int movement;
		if (rowVColumn == 0) {
			if (row == 0) {
				movement = 1;
			} else if (row == GameData.BOARD_SIZE - 1) {
				movement = -1;
			} else {
				movement = (int) (Math.random() * 2);
				if (movement == 0) {
					movement = 1;
				} else {
					movement = -1;
				}
			}
			row += movement;
		} else {
			if (column == 0) {
				movement = 1;
			} else if (column == GameData.BOARD_SIZE - 1) {
				movement = -1;
			} else {
				movement = (int) (Math.random() * 2);
				if (movement == 0) {
					movement = 1;
				} else {
					movement = -1;
				}
			}
			column += movement;
		}
		if (row == Player.getRow() && column == Player.getColumn()) {
			battlePlayer();
		}
	}

	public static void updateFadeMove() {
		if (opacity > 0f && moving && (row != Player.getRow() || column != Player.getColumn())) {
			opacity -= GameData.WUMPUS_FADE_SPEED;
			if (opacity <= 0f) {
				move();
				moving = false;
				opacity = 0f;
			}
		} else if (opacity + GameData.WUMPUS_FADE_SPEED <= 1f
				&& Game.game.getTiles()[row][column].isFlashlightAffected()) {
			opacity += GameData.WUMPUS_FADE_SPEED;
		}
//		if(opacity + GameData.WUMPUS_FADE_SPEED <= 1f && row == Player.getRow() && column == Player.getColumn()){
//			opacity += GameData.WUMPUS_FADE_SPEED;
//		}
	}

	public static void setOpacity(float opacity) {
		Wumpus.opacity = opacity;
	}

	public static void reset() {
		opacity = 0f;
		do {
			row = (int) (Math.random() * GameData.BOARD_SIZE);
			column = (int) (Math.random() * GameData.BOARD_SIZE);
		} while (row == 0 && column == 0);
	}

	public static void setMoving(boolean moving) {
		Wumpus.moving = moving;
	}

	private static void battlePlayer() {
		double chance = (Math.random() * 100);
		if (Toolbar.weaponAvailable()) {
			if (chance <= GameData.WUMPUS_INTO_PLAYER_CHANCE + GameData.SUPER_SAYAIN_INCREASE_CHANCE) {
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
	 * 
	 * @param g
	 */
	public static void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.SrcOver.derive(Wumpus.getOpacity()));
		g2.drawImage(GameData.wumpusSprite, column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT,
				GameData.TILE_WIDTH, GameData.TILE_HEIGHT, null);
		g2.setComposite(AlphaComposite.SrcOver.derive(1f));
	}

}
