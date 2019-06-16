package game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.GameData.GameState;
import game.GameData.ItemTypes;
import game.GameData.TrackingOptions;

public class Game implements ActionListener, KeyListener{

	public static Game game;
	private JFrame frame;
	private Timer timer;
	private Renderer renderer;
	private Tile[][] tiles;
	private GameState gameState;
	private JLabel explosionAnimation, ultimateExplosionAnimation, losingAnimation, winningAnimation;
	private Clip themePlayer;
	private boolean explosionInProgress, inCompassMenu, gameOver;

	public Game() {
		frame = new JFrame(GameData.FRAME_NAME);
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		renderer = new Renderer();
		renderer.setPreferredSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		renderer.setBackground(Color.LIGHT_GRAY);
		frame.setMinimumSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		frame.add(renderer);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startMusic(GameData.themeSong, true);
		gameState = GameState.MENU;
		addMenuWidgets();
		timer.start();
	}

	private void initialize() {
		tiles = new Tile[GameData.BOARD_SIZE][GameData.BOARD_SIZE];
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				tiles[r][c] = new Tile(r, c);
			}
		}
		Player.setSuperSayain(false);
		Player.setLocation(0, 0);
		Wumpus.reset();
		tiles[0][0].setDiscovered(true);
		initItems();
		GameData.FRAME_WIDTH_DIFFERENCE = frame.getWidth() - GameData.FRAME_EXTENDED_WIDTH;
		GameData.FRAME_HEIGHT_DIFFERENCE = frame.getHeight() - GameData.FRAME_HEIGHT;
		GameData.FRAME_WIDTH += 600 % GameData.BOARD_SIZE;
		GameData.FRAME_HEIGHT += 650 % GameData.BOARD_SIZE;
		GameData.FRAME_EXTENDED_WIDTH = GameData.FRAME_WIDTH + GameData.FRAME_EXTRA_WIDTH;
		frame.setPreferredSize(new Dimension(GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT));
		GameData.TILE_WIDTH = GameData.FRAME_WIDTH / GameData.BOARD_SIZE;
		GameData.TILE_HEIGHT = GameData.FRAME_HEIGHT / GameData.BOARD_SIZE;
		GameData.rescaleAnimations();
		explosionAnimation = new JLabel(GameData.explosionAnimation);
		ultimateExplosionAnimation = new JLabel(GameData.ultimateExplosionAnimation);
		losingAnimation = new JLabel(GameData.losingAnimation);
		winningAnimation = new JLabel(GameData.winningAnimation);
		gameOver = false;
	}

	private void initItems() {
		Toolbar.removeAll();
		Toolbar.addItem(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.EXPLOSIVE);
		assignNewRandomTile(ItemTypes.COMPASS);
		assignNewRandomTile(ItemTypes.SWORD);
		for(int i = 0; i < GameData.FLASHLIGHT_AMOUNT; i++) {
			assignNewRandomTile(ItemTypes.FLASHLIGHT);
		}
	}
	
	private void assignNewRandomTile(ItemTypes item) {
		int randRow, randCol;
		do {
			randRow = (int) (Math.random() * GameData.BOARD_SIZE);
			randCol = (int) (Math.random() * GameData.BOARD_SIZE);
		}while(randRow + randCol == 0 || tiles[randRow][randCol].getItem() != ItemTypes.NONE);
	 	tiles[randRow][randCol].setItem(item);		
	}
	
	public void setGameState(GameData.GameState state){
		renderer.removeAll();
		renderer.revalidate();
		switch(state){
		case MENU:
			gameState = GameState.MENU;
			addMenuWidgets();
			break;
		case SETTINGS:
			gameState = GameState.SETTINGS;
			addSettingsWidgets();
			break;
		case PAUSED:
			gameState = GameState.PAUSED;
			addPausedWidgets();
			break;
		case IN_GAME:
			gameState = GameState.IN_GAME;
			break;
		case RULES:
			gameState = GameState.RULES;
			break;
		}
	}
	
	private void addMenuWidgets() {
		renderer.setLayout(new BoxLayout(renderer, BoxLayout.PAGE_AXIS));
		JButton startGameButton = new JButton(GameData.startGameUnselectedIcon);
		startGameButton.setRolloverIcon(GameData.startGameSelectedIcon);
		startGameButton.setFocusable(false);
		GameData.removeBackground(startGameButton);
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				initialize();
				setGameState(GameState.IN_GAME);
			}
		});
		startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.wumpusLogo.getHeight()*2));
		renderer.add(startGameButton);

		JButton settingsMenuButton = new JButton(GameData.settingsUnselectedIcon);
		settingsMenuButton.setRolloverIcon(GameData.settingsSelectedIcon);
		settingsMenuButton.setFocusable(false);
		GameData.removeBackground(settingsMenuButton);
		settingsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setGameState(GameState.SETTINGS);
			}
		});
		settingsMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.VERTICAL_DISTANCE_BETWEEN_BUTTONS));
		renderer.add(settingsMenuButton);
		
		JButton rulesButton = new JButton(GameData.rulesUnselectedIcon);
		rulesButton.setRolloverIcon(GameData.rulesSelectedIcon);
		rulesButton.setFocusable(false);
		GameData.removeBackground(rulesButton);
		settingsMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setGameState(GameState.IN_GAME);
			}
		});
		rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		renderer.add(Box.createVerticalStrut(GameData.VERTICAL_DISTANCE_BETWEEN_BUTTONS));
		renderer.add(rulesButton);
	}
	
	private void addPausedWidgets(){
		addMenuWidgets();
	}
	
	private void addCompassMenuWidgets(){
		renderer.setLayout(null);
		JButton wumpusButton = new JButton(GameData.wumpusButtonIcon);
		wumpusButton.setFocusable(false);
		wumpusButton.setBounds(
				GameData.FRAME_WIDTH + GameData.TOOLBAR_FRAME_WIDTH, 
				(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + GameData.TOOLBAR_FRAME_HEIGHT,
				GameData.MINI_COMPASS_BUTTONS_WIDTH,
				GameData.MINI_COMPASS_BUTTONS_HEIGHT
		);
		wumpusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Toolbar.setTrackingItem(TrackingOptions.WUMPUS);
				setInCompassMenu(false);
			}
		});
		
		JButton flashlightButton = new JButton(GameData.flashlightButtonIcon);
		flashlightButton.setFocusable(false);
		flashlightButton.setBounds(
				GameData.FRAME_WIDTH + GameData.TOOLBAR_FRAME_WIDTH + GameData.MINI_COMPASS_BUTTONS_WIDTH, 
				(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + GameData.TOOLBAR_FRAME_HEIGHT,
				GameData.MINI_COMPASS_BUTTONS_WIDTH,
				GameData.MINI_COMPASS_BUTTONS_HEIGHT
		);
		flashlightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Toolbar.setTrackingItem(TrackingOptions.FLASHLIGHT);
				setInCompassMenu(false);
			}
		});
		JButton swordButton = new JButton(GameData.swordButtonIcon);
		swordButton.setFocusable(false);
		swordButton.setBounds(
				GameData.FRAME_WIDTH + GameData.TOOLBAR_FRAME_WIDTH, 
				(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + GameData.TOOLBAR_FRAME_HEIGHT + GameData.MINI_COMPASS_BUTTONS_HEIGHT,
				GameData.MINI_COMPASS_BUTTONS_WIDTH,
				GameData.MINI_COMPASS_BUTTONS_HEIGHT
		);
		swordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Toolbar.setTrackingItem(TrackingOptions.SWORD);
				setInCompassMenu(false);
			}
		});
		JButton explosiveButton = new JButton(GameData.explosiveButtonIcon);
		explosiveButton.setFocusable(false);
		explosiveButton.setBounds(
				GameData.FRAME_WIDTH + GameData.TOOLBAR_FRAME_WIDTH + GameData.MINI_COMPASS_BUTTONS_WIDTH, 
				(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + GameData.TOOLBAR_FRAME_HEIGHT + GameData.MINI_COMPASS_BUTTONS_HEIGHT,
				GameData.MINI_COMPASS_BUTTONS_WIDTH,
				GameData.MINI_COMPASS_BUTTONS_HEIGHT
		);
		explosiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Toolbar.setTrackingItem(TrackingOptions.EXPLOSIVE);
				setInCompassMenu(false);
			}
		});
		GameData.removeBackground(wumpusButton);
		GameData.removeBackground(flashlightButton);
		GameData.removeBackground(swordButton);
		GameData.removeBackground(explosiveButton);
		renderer.add(wumpusButton);
		renderer.add(flashlightButton);
		renderer.add(swordButton);
		renderer.add(explosiveButton);
	}
	
	private void addSettingsWidgets(){
		renderer.revalidate();
		final JSlider tileAmountSlider = new JSlider(3, 50);
		final JLabel tileAmountLbl = new JLabel("Board Size: " + tileAmountSlider.getValue() + "x" + tileAmountSlider.getValue());
		final JSlider playerSpeedSlider = new JSlider(1, 100);
		final JLabel playerSpeedLbl = new JLabel("Player Speed: " + playerSpeedSlider.getValue() + " m/s");
		final JSlider flashlightAmountSlider = new JSlider(0, tileAmountSlider.getValue()-1);
		final JLabel flashlightAmountLbl = new JLabel(flashlightAmountSlider.getValue() + " Flashlight");

		tileAmountSlider.setFocusable(false);
		tileAmountSlider.setOpaque(false);
		tileAmountSlider.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - tileAmountSlider.getPreferredSize().getWidth()/2),
				(GameData.FRAME_HEIGHT/5),
				(int) tileAmountSlider.getPreferredSize().getWidth(), 
				(int) tileAmountSlider.getPreferredSize().getHeight()
		);
		tileAmountLbl.setFont(new Font("Arial", Font.BOLD , 18));
		tileAmountLbl.setForeground(Color.WHITE);
		tileAmountLbl.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - tileAmountLbl.getPreferredSize().getWidth()/2),
				(GameData.FRAME_HEIGHT/5) - 50,
				GameData.FRAME_EXTENDED_WIDTH, 
				(int) tileAmountLbl.getPreferredSize().getHeight()
		);
		tileAmountSlider.addChangeListener(new ChangeListener() {
			@Override	
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				tileAmountLbl.setBounds(
						(int) (GameData.FRAME_EXTENDED_WIDTH/2 - tileAmountLbl.getPreferredSize().getWidth()/2),
						(GameData.FRAME_HEIGHT/5) - 50,
						GameData.FRAME_EXTENDED_WIDTH, 
						(int) tileAmountLbl.getPreferredSize().getHeight()
				);
				tileAmountLbl.setText("Board Size: " + tileAmountSlider.getValue() + "x" + tileAmountSlider.getValue());
				flashlightAmountSlider.setMaximum(tileAmountSlider.getValue()-1);
				//playerSpeedSlider.setMaximum(20);
			}
		});
		
		playerSpeedSlider.setFocusable(false);
		playerSpeedSlider.setOpaque(false);
		playerSpeedSlider.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - playerSpeedSlider.getPreferredSize().getWidth()/2),
				(2*GameData.FRAME_HEIGHT/5),
				(int) playerSpeedSlider.getPreferredSize().getWidth(), 
				(int) playerSpeedSlider.getPreferredSize().getHeight()
		);
		playerSpeedLbl.setFont(new Font("Arial", Font.BOLD , 18));
		playerSpeedLbl.setForeground(Color.WHITE);
		playerSpeedLbl.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - playerSpeedLbl.getPreferredSize().getWidth()/2),
				(2*GameData.FRAME_HEIGHT/5) - 50,
				GameData.FRAME_EXTENDED_WIDTH,
				(int) playerSpeedLbl.getPreferredSize().getHeight()
		);
		playerSpeedSlider.addChangeListener(new ChangeListener() {
			@Override	
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				playerSpeedLbl.setBounds(
						(int) (GameData.FRAME_EXTENDED_WIDTH/2 - playerSpeedLbl.getPreferredSize().getWidth()/2),
						(2*GameData.FRAME_HEIGHT/5) - 50,
						GameData.FRAME_EXTENDED_WIDTH, 
						(int) playerSpeedLbl.getPreferredSize().getHeight()
				);
				playerSpeedLbl.setText("Player Speed: " + playerSpeedSlider.getValue() + " m/s");
			}
		});
		
		flashlightAmountSlider.setFocusable(false);
		flashlightAmountSlider.setOpaque(false);
		flashlightAmountSlider.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - flashlightAmountSlider.getPreferredSize().getWidth()/2),
				(3*GameData.FRAME_HEIGHT/5),
				(int) flashlightAmountSlider.getPreferredSize().getWidth(), 
				(int) flashlightAmountSlider.getPreferredSize().getHeight()
		);
		flashlightAmountLbl.setFont(new Font("Arial", Font.BOLD , 18));
		flashlightAmountLbl.setForeground(Color.WHITE);
		flashlightAmountLbl.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - flashlightAmountLbl.getPreferredSize().getWidth()/2),
				(3*GameData.FRAME_HEIGHT/5) - 50,
				GameData.FRAME_EXTENDED_WIDTH,
				(int) flashlightAmountLbl.getPreferredSize().getHeight()
		);
		flashlightAmountSlider.addChangeListener(new ChangeListener() {
			@Override	
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				flashlightAmountLbl.setBounds(
						(int) (GameData.FRAME_EXTENDED_WIDTH/2 - flashlightAmountLbl.getPreferredSize().getWidth()/2),
						(3*GameData.FRAME_HEIGHT/5) - 50,
						GameData.FRAME_EXTENDED_WIDTH,
						(int) flashlightAmountLbl.getPreferredSize().getHeight()
				);
				if(flashlightAmountSlider.getValue() != 1) {
					flashlightAmountLbl.setText(flashlightAmountSlider.getValue() + " Flashlights");
				}else {
					flashlightAmountLbl.setText(flashlightAmountSlider.getValue() + " Flashlight");
				}
			}
		});
		tileAmountSlider.setValue(GameData.BOARD_SIZE);
		playerSpeedSlider.setValue(GameData.PLAYER_VELOCITY);
		flashlightAmountSlider.setValue(GameData.FLASHLIGHT_AMOUNT);
		JButton saveButton = new JButton(GameData.saveUnselectedIcon);
		saveButton.setRolloverIcon(GameData.saveSelectedIcon);
		saveButton.setFocusable(false);
		GameData.removeBackground(saveButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setGameState(GameState.MENU);
				GameData.BOARD_SIZE = tileAmountSlider.getValue();
				GameData.PLAYER_VELOCITY = playerSpeedSlider.getValue();
				GameData.FLASHLIGHT_AMOUNT = flashlightAmountSlider.getValue();
			}
		});
		saveButton.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - saveButton.getPreferredSize().getWidth()/2) - 150,
				(int) (3.7*GameData.FRAME_HEIGHT/5),
				(int) saveButton.getPreferredSize().getWidth(),
				(int) saveButton.getPreferredSize().getHeight()
		);
		JButton cancelButton = new JButton(GameData.cancelUnselectedIcon);
		cancelButton.setRolloverIcon(GameData.cancelSelectedIcon);
		cancelButton.setFocusable(false);
		GameData.removeBackground(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setGameState(GameState.MENU);
			}
		});
		cancelButton.setBounds(
				(int) (GameData.FRAME_EXTENDED_WIDTH/2 - cancelButton.getPreferredSize().getWidth()/2) + 150,
				(int) (3.7*GameData.FRAME_HEIGHT/5),
				(int) cancelButton.getPreferredSize().getWidth(),
				(int) cancelButton.getPreferredSize().getHeight()
		);
		renderer.setLayout(null);
		renderer.add(tileAmountSlider);
		renderer.add(tileAmountLbl);
		renderer.add(playerSpeedSlider);
		renderer.add(playerSpeedLbl);
		renderer.add(flashlightAmountSlider);
		renderer.add(flashlightAmountLbl);
		renderer.add(saveButton);
		renderer.add(cancelButton);
	}
	
	public void render(Graphics g) {
		switch(gameState) {
		case MENU:
			renderMenu(g);
			break;
		case SETTINGS:
			renderSettings(g);
			break;
		case RULES:
			renderRules(g);
			break;
		case PAUSED:
		case IN_GAME:
			renderInGame(g);
			break;
		}
	}
	
	private void renderMenu(Graphics g){
		g.drawImage(GameData.menuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
		g.drawImage(GameData.wumpusLogo, (GameData.FRAME_EXTENDED_WIDTH/2) - (GameData.wumpusLogo.getWidth()/2), 60, GameData.wumpusLogo.getWidth(), 
				GameData.wumpusLogo.getHeight(), null);
	}

	
	private void renderSettings(Graphics g){
		g.drawImage(GameData.settingsMenuBackground, 0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT, null);
	}
	
	private void renderRules(Graphics g){
		
	}
	
	private void renderPaused(Graphics g){
		g.setColor(Color.DARK_GRAY);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.SrcOver.derive(.8f));
		g2.fillRect(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
	}
	
	private void renderCompassMenu(Graphics g){
		System.out.println("RENDERING");
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		if(Toolbar.getItemSlot(ItemTypes.COMPASS) != -1) {
			g2.drawLine(GameData.FRAME_WIDTH + GameData.TOOLBAR_FRAME_WIDTH, 
					(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT/2), 
					GameData.FRAME_EXTENDED_WIDTH - GameData.TOOLBAR_FRAME_WIDTH, 
					(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + (GameData.TOOLBAR_SLOT_HEIGHT/2));
			g2.drawLine(GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/2), 
					(Toolbar.getItemSlot(ItemTypes.COMPASS) * GameData.TOOLBAR_SLOT_HEIGHT) + GameData.TOOLBAR_FRAME_HEIGHT, 
					GameData.FRAME_WIDTH + (GameData.FRAME_EXTRA_WIDTH/2),
					((Toolbar.getItemSlot(ItemTypes.COMPASS)+1) * GameData.TOOLBAR_SLOT_HEIGHT) - GameData.TOOLBAR_FRAME_HEIGHT);
		} 
	}
	
	private void renderInGame(Graphics g){
		for (Tile[] tileArr : tiles)
			for (Tile tile : tileArr)
				tile.render(g);
		Player.render(g);
		if(Wumpus.getRow() == Player.getRow() && Wumpus.getColumn() == Player.getColumn()) {
			Wumpus.render(g);
		}
		Toolbar.render(g);
		if(gameState == GameState.PAUSED) {
			renderPaused(g);
		}else if(inCompassMenu){
			renderCompassMenu(g);
		}
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(gameState) {
		case MENU:
			break;
		case SETTINGS:
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				setGameState(GameState.MENU);
				break;
			}
			break;
		case RULES:
			break;
		case IN_GAME:
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				Player.move(GameData.MovementDirections.LEFT);
				break;
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				Player.move(GameData.MovementDirections.RIGHT);
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				Player.move(GameData.MovementDirections.UP);
				break;
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				Player.move(GameData.MovementDirections.DOWN);
				break;
			case KeyEvent.VK_SPACE:
				if(!explosionInProgress) {
					Player.shoot();    
				}
				break;
			case KeyEvent.VK_C:
				if(Toolbar.compassAvailable() && inCompassMenu == false) {
					setInCompassMenu(true);
				}else {
					setInCompassMenu(false);
				}
				break;
			case KeyEvent.VK_X:
				if(tiles[Player.getRow()][Player.getColumn()].getItem() != ItemTypes.NONE) {
					Toolbar.addItem(tiles[Player.getRow()][Player.getColumn()].getItem());
					Tile.updateTiles();
					tiles[Player.getRow()][Player.getColumn()].removeItem();
				}
				break;
			case KeyEvent.VK_M:
				themePlayer.stop();
				break;
			case KeyEvent.VK_T:
				break;
			case KeyEvent.VK_ESCAPE:
				setGameState(GameState.PAUSED);
				break;
			}
			break;
		case PAUSED:
			switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				renderer.removeAll();
				if(inCompassMenu) {
					setInCompassMenu(true);
				}
				gameState = GameState.IN_GAME;
				break;
			}
			break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	
	private void playTheme(boolean loop){
			}
	
	private void playBattle(boolean loop){
		
	}
	
	public void startMusic(AudioInputStream stream, boolean loop) { 
		try {
			GameData.resetSounds();
			if(themePlayer != null && themePlayer.isOpen() ) {
				themePlayer.stop();
				themePlayer.close();
			}
			themePlayer = AudioSystem.getClip();
			themePlayer.open(stream);
			themePlayer.start();
			if(loop) {
				themePlayer.loop(-1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void update() {
		if(!gameOver) {
			Tile.updateTilesOpacity();
			Player.updatePos();
			Wumpus.updateFadeMove();
		}
	}

	private void updateSize() {
		if (frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE != GameData.FRAME_EXTENDED_WIDTH
				|| frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE != GameData.FRAME_HEIGHT) {
			renderer.setPreferredSize(new Dimension(frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE,
					frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE));
			GameData.FRAME_EXTENDED_WIDTH = (int) frame.getWidth() - GameData.FRAME_WIDTH_DIFFERENCE;
			GameData.FRAME_WIDTH = GameData.FRAME_EXTENDED_WIDTH - GameData.FRAME_EXTRA_WIDTH;
			GameData.FRAME_HEIGHT = (int) frame.getHeight() - GameData.FRAME_HEIGHT_DIFFERENCE;
			GameData.TILE_WIDTH = GameData.FRAME_WIDTH / GameData.BOARD_SIZE;
			GameData.TILE_HEIGHT = GameData.FRAME_HEIGHT / GameData.BOARD_SIZE;
			GameData.TOOLBAR_SLOT_HEIGHT = GameData.FRAME_HEIGHT/GameData.TOOLBAR_SLOTS;
			GameData.PLAYER_VELOCITY = (int) ((GameData.FRAME_WIDTH * GameData.FRAME_HEIGHT) / GameData.FRAME_SIZE_TO_VELOCITY);
			GameData.rescaleAnimations();
			explosionAnimation = new JLabel(GameData.explosionAnimation);
			ultimateExplosionAnimation = new JLabel(GameData.ultimateExplosionAnimation);
			losingAnimation = new JLabel(GameData.losingAnimation);
			winningAnimation = new JLabel(GameData.winningAnimation);
			Player.setXPos(Player.getColumn() * GameData.TILE_WIDTH);
			Player.setYPos(Player.getRow() * GameData.TILE_HEIGHT);
		}
	}
	
	public void explodeTile(final int row, final int column, final ImageIcon animationToUse) {
		explosionInProgress = true;
		final JLabel animation;
		if(animationToUse == GameData.explosionAnimation) {
			animation = explosionAnimation;
		}else {
			animation = ultimateExplosionAnimation;
		}
		animation.setBounds(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		renderer.add(animation);
		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				if(animation == explosionAnimation) {
					while(System.currentTimeMillis() - startTime < GameData.EXPLOSION_ANIMATION_TIME_MS) {
						tiles[row][column].setOpacity(0f); 
					}
				}else {
					while(System.currentTimeMillis() - startTime < GameData.ULTIMATE_EXPLOSION_ANIMATION_TIME_MS) {
						tiles[row][column].setOpacity(0f); 
					}
				}
				renderer.remove(animation);
				explosionInProgress = false;
				if(row == Wumpus.getRow() && column == Wumpus.getColumn()) {
					explosionBattle();
				}
			} 
		}).start();
		tiles[row][column].setDiscovered(true);
		tiles[row][column].setItem(ItemTypes.NONE);
	}
	
	private void explosionBattle() {
		double chance = Math.random() * 100;
		if(!Player.isSuperSayain()) {
			if(chance <= GameData.getExplosionChanceToWin()) {
				//setWinner(true);
				gameOver = true;
			}
		}else{
			if(chance <= GameData.getExplosionChanceToWin() + 60) {
				//setWinner(true);
				gameOver = true;
			}
		}
		if(gameOver) {
			frame.removeKeyListener(this);
			Wumpus.setOpacity(0f);
		}
	}
	
	public void setWinner(boolean playerWon) {
		frame.removeKeyListener(this);
		final JLabel animationToUse;
		if(playerWon) {
			animationToUse = winningAnimation;
		}else {
			animationToUse = losingAnimation;
		}
		startMusic(GameData.battleSong, false);
		System.out.println("Switched to battle song");
		new Thread(new Runnable() {
				@Override
				public void run() {
					GameData.pause(2);
					animationToUse.setBounds(0, 0, GameData.FRAME_EXTENDED_WIDTH, GameData.FRAME_HEIGHT);
					renderer.add(animationToUse);
					long startTime = System.currentTimeMillis();
					while(System.currentTimeMillis() - startTime < 15000);
					renderer.remove(animationToUse);
					initialize();
					frame.addKeyListener(Game.game);
					startMusic(GameData.themeSong, true);
					System.out.println("Switched back to theme song");
				}
		}).start();	
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		renderer.repaint();
		if(gameState == GameState.IN_GAME) {
			update();
		}
	}
	
	public void setInCompassMenu(boolean inMenu) {
		inCompassMenu = inMenu;
		if(inMenu == true) {
			addCompassMenuWidgets();
		}else {
			renderer.removeAll();
		}
	}
	
	public boolean inCompassMenu() {
		return inCompassMenu;
	}

	public JFrame getFrame() {
		return frame;
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public GameState getGameState() {
		return gameState;
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