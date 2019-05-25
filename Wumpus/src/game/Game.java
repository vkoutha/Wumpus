package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.GameData.ItemTypes;

public class Game implements ActionListener, KeyListener {

	public static Game game;
	private JFrame frame;
	private Timer timer;
	private Renderer renderer;
	private Tile[][] tiles;
	private Player player;
	private Toolbar toolbar;
	private JLabel explosionGIF;
	private Clip themePlayer;
	private boolean explosionInProgress;

	public Game() {
		frame = new JFrame(GameData.FRAME_NAME);
		explosionGIF = new JLabel();
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		renderer = new Renderer();
		renderer.setPreferredSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		frame.setMinimumSize(new Dimension(500, 500));
		frame.add(renderer);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		startMusic();
		timer.start();
	}

	private void initialize() {
		tiles = new Tile[GameData.TILE_AMOUNT][GameData.TILE_AMOUNT];
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				tiles[r][c] = new Tile(r, c);
			}
		}
		tiles[5][4].setItem(ItemTypes.FLASHLIGHT);
		tiles[3][3].setItem(ItemTypes.EXPLOSIVE);
		player = new Player();
		toolbar = new Toolbar();
		explosionGIF = new JLabel(GameData.explosionAnimation);
		explosionGIF.setBounds(500, 500, 100, 100);
		GameData.FRAME_WIDTH_DIFFERENCE = frame.getWidth() - GameData.FRAME_WIDTH;
		GameData.FRAME_HEIGHT_DIFFERENCE = frame.getHeight() - GameData.FRAME_HEIGHT;
	}

	private void startMusic() { 
		AudioInputStream audioIn;
		try {
			themePlayer = AudioSystem.getClip();
			audioIn =  AudioSystem.getAudioInputStream(getClass().getResource("/sound/theme.wav"));
			themePlayer.open(audioIn);
			themePlayer.start();
			themePlayer.loop(-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(Graphics g) {
		for (Tile[] tileArr : tiles)
			for (Tile tile : tileArr)
				tile.render(g);
		player.render(g);
		toolbar.render(g);
		g.setColor(Color.WHITE);
		g.drawString("Remaining Explosives: 5" , 5, 20);
	}

	private void update() {
		updateSize();
	}

	private void updateSize() {
		if (frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE != GameData.FRAME_WIDTH
				|| frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE != GameData.FRAME_HEIGHT) {
			renderer.setPreferredSize(new Dimension(frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE,
					frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE));
			GameData.FRAME_WIDTH = (int) frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE;
			GameData.FRAME_HEIGHT = (int) frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE;
			GameData.TILE_WIDTH = GameData.FRAME_WIDTH / GameData.TILE_AMOUNT;
			GameData.TILE_HEIGHT = GameData.FRAME_HEIGHT / GameData.TILE_AMOUNT;
			GameData.FRAME_EXTENDED_WIDTH = GameData.FRAME_WIDTH + 140;
			GameData.TOOLBAR_SLOT_HEIGHT = GameData.FRAME_HEIGHT/GameData.TOOLBAR_SLOTS;
			GameData.rescaleAnimations();
			renderer.remove(explosionGIF);
			explosionGIF = new JLabel(GameData.explosionAnimation);
		}
	}
	
	public void explodeTile(int row, int column) {
		explosionInProgress = true;
		explosionGIF.setBounds(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		renderer.add(explosionGIF);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				while(System.currentTimeMillis() - startTime < GameData.EXPLOSION_ANIMATION_TIME_MS);
				renderer.remove(explosionGIF);
				explosionInProgress = false;
			}
		}).start();
		tiles[row][column].setItem(ItemTypes.NONE);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			player.move(GameData.MovementDirections.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			player.move(GameData.MovementDirections.RIGHT);
			break;
		case KeyEvent.VK_UP:
			player.move(GameData.MovementDirections.UP);
			break;
		case KeyEvent.VK_DOWN:
			player.move(GameData.MovementDirections.DOWN);
			break;
		case KeyEvent.VK_SPACE:
			if(!explosionInProgress) {
				player.shoot();
			}
			break;
		case KeyEvent.VK_A:
			if(tiles[player.getRow()][player.getColumn()].getItem() != ItemTypes.NONE) {
				toolbar.addItem(tiles[player.getRow()][player.getColumn()].getItem());
				tiles[player.getRow()][player.getColumn()].removeItem();
			}
			break;
		case KeyEvent.VK_M:
			themePlayer.stop();
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		renderer.repaint();
		update();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public Player getPlayer() {
		return player;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				game = new Game();
			}
		});
	}

}