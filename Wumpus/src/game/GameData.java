package game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameData {
	
	public static final String FRAME_NAME = "Wumpus";
	public static int FRAME_WIDTH = 800;
	public static int FRAME_HEIGHT = 800;
	public static int FRAME_WIDTH_DIFFERENCE = 0;
	public static int FRAME_HEIGHT_DIFFERENCE = 0;
	public static final int UPDATE_SPEED_MS = 20;
	public static final int EXPLOSION_ANIMATION_TIME_MS = 1500;
	
	public static final int TILE_AMOUNT = 10;
	public static int TILE_WIDTH = (FRAME_WIDTH/TILE_AMOUNT);
	public static int TILE_HEIGHT = (FRAME_HEIGHT/TILE_AMOUNT);

	public static BufferedImage rockSprite = null, grassSprite = null;
	public static BufferedImage characterSprite = null, characterFowardsStillSprite = null, characterForwardsRunningSprite = null,
			characterBackwardsStillSprite = null, characterBackwardsRunningSprite = null, characterLeftStillSprite = null, characterLeftRunningSprite = null,
			characterRightStillSprite = null, characterRightRunningSprite = null;
	public static BufferedImage flashlightSprite, compassSprite, goldSprite;
	private static Image explosionImage = null;
	public static ImageIcon explosionAnimation = null;
	
	static {
		try {
			rockSprite = ImageIO.read(GameData.class.getResource("/img/rockTile.png"));
			rockSprite = rockSprite.getSubimage(10, 10, rockSprite.getWidth()-20, rockSprite.getHeight()-20);
			grassSprite = ImageIO.read(GameData.class.getResource("/img/grassTile.jpg"));
			grassSprite = grassSprite.getSubimage(50, 50, grassSprite.getWidth()-70, grassSprite.getHeight()-80);
			characterSprite = ImageIO.read(GameData.class.getResource("/img/character.png"));
			characterBackwardsRunningSprite = characterSprite.getSubimage(0, 0, 460, 590);
			characterBackwardsStillSprite = characterSprite.getSubimage(470, 0, 460, 560);
			characterForwardsRunningSprite = characterSprite.getSubimage(0, 1810, 460, 590); 
			characterFowardsStillSprite = characterSprite.getSubimage(470, 1810, 460, 590);
			characterRightRunningSprite = characterSprite.getSubimage(0, 600, 460, 590);
			characterRightStillSprite = characterSprite.getSubimage(470, 600, 470, 590);
			characterLeftRunningSprite = characterSprite.getSubimage(470, 1200, 460, 580);
			characterLeftStillSprite = characterSprite.getSubimage(0, 1200, 460, 580);
			flashlightSprite = ImageIO.read(GameData.class.getResource("/img/flashlightSprite.png")); 
			compassSprite = ImageIO.read(GameData.class.getResource("/img/compassSprite.gif"));
			explosionAnimation = new ImageIcon(GameData.class.getResource("/img/explosionGIF.gif"));
			explosionImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH*1.25), TILE_HEIGHT, Image.SCALE_DEFAULT);
			explosionAnimation = new ImageIcon(explosionImage);
			//explosionAnimation.setImage(explosionBufferedImage.getScaledInstance(500, 500, Image.SCALE_DEFAULT));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public static BufferedImage imageToBufferedImage(Image im) {
	     BufferedImage bi = new BufferedImage
	        (im.getWidth(null),im.getHeight(null),BufferedImage.TYPE_INT_RGB);
	     Graphics g = bi.getGraphics();
	     g.drawImage(im, 0, 0, null);
	     g.dispose();
	     return bi;
	  }
	
	public static void rescaleAnimations() {
		explosionImage = explosionAnimation.getImage().getScaledInstance((int) (TILE_WIDTH*1.25), TILE_HEIGHT, Image.SCALE_DEFAULT);
		explosionAnimation = new ImageIcon(explosionImage);
	}
	
	public enum MovementDirections{
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public enum ItemTypes{
		FLASHLIGHT,
		COMPASS,
		GOLD,
		NONE
	}

}