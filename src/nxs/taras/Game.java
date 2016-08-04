package nxs.taras;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean running;
	public static int WIDTH = 767; // ширина
	public static int HEIGHT = 479; // высота
	public static String NAME = "Game"; // заголовок окна
	public static ArrayList<Sprite> hero;
	static boolean leftPressed = false;
	static boolean rightPressed = false;
	static boolean upPressed = false;
	static boolean downPressed = false;
	static boolean spacePressed = false;
	static long latency = 5;
	private static int x = 630;
	private static int y = 310;
	private static int xEgg = 0;
	private static int yEgg = 0;
	private static int[][] spriteStates = { { 0, 2, 4 }, { 1, 3, 5 },
			{ 7, 9, 11 }, { 6, 8, 10 } };
	private static int currentSprite = 7;
	private static int currentEggSprite = 45;
	private static boolean up = false;
	private static boolean left = true;
	private double spriteChanger;
	private double eggSpriteChanger;
	private Image background;
	private HashSet<Point> blackPoints;
	private SpriteEgg eggs;
	
	
	

	public static void main(String[] args) {
		Game game = new Game();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // выход из
																// приложения по
																// нажатию
																// клавиши ESC
		frame.setLayout(new BorderLayout());
		frame.setUndecorated(true);
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(res.width/2-WIDTH/2, res.height/2-HEIGHT/2);
		frame.add(game, BorderLayout.CENTER); // добавляем холст на наш фрейм
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		game.start();
	}

	public ArrayList<Sprite> getSprite(String path) {
		List<Sprite> spriteList = new ArrayList<Sprite>();
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();

		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			for (int i = 13; i <= 277; i += 33) {
				BufferedImage temp = ImageIO.read(url).getSubimage(i, 75, 31,
						31);
				BufferedImage convertedImg = new BufferedImage(temp.getWidth(),
						temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
				convertedImg.getGraphics().drawImage(temp, 0, 0, null);
				imageList.add(convertedImg);
				convertedImg = new BufferedImage(temp.getWidth(),
						temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
				convertedImg.getGraphics().drawImage(temp, 0, 0, null);
				for (int h = 0; h < convertedImg.getHeight(); h++) {
					for (int w = 0; w <= convertedImg.getWidth() / 2; w++) {
						int t = convertedImg.getRGB(convertedImg.getWidth() - w
								- 1, h);
						convertedImg.setRGB(convertedImg.getWidth() - w - 1, h,
								convertedImg.getRGB(w, h));
						convertedImg.setRGB(w, h, t);
					}
				}
				imageList.add(convertedImg);
			}
			int r = 0;// red component 0...255
			int g = 0;// green component 0...255
			int b = 0;// blue component 0...255
			int a = 0;// alpha (transparency) component 0...255
			int col = (a << 24) | (r << 16) | (g << 8) | b;

			for (Iterator<BufferedImage> iterator = imageList.iterator(); iterator
					.hasNext();) {
				BufferedImage bufferedImage = (BufferedImage) iterator.next();
				for (int i = 0; i < bufferedImage.getWidth(); i++) {
					for (int j = 0; j < bufferedImage.getHeight(); j++) {
						Color c = new Color(bufferedImage.getRGB(i, j));
						if (c.equals(new Color(255, 0, 255))) {
							bufferedImage.setRGB(i, j, col);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Iterator<BufferedImage> iterator = imageList.iterator(); iterator.hasNext();) {
			BufferedImage bufferedImage = (BufferedImage) iterator.next();
			spriteList.add(new Sprite(Toolkit.getDefaultToolkit().createImage(
					bufferedImage.getSource())));
		}

		return (ArrayList<Sprite>) spriteList;
	}

	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta;
		init();
		spriteChanger = 0;

		while (running) {
			delta = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			try {
				update(delta);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			render();
			int s = (int)spriteChanger%3;
			eggSpriteChanger+=0.01;
			if(up)
				if(left)
					currentSprite = spriteStates[0][s];
				else
					currentSprite = spriteStates[1][s];
			else
				if(left)
					currentSprite = spriteStates[3][s];
				else
					currentSprite = spriteStates[2][s];
		}
	}

	public void init() {
		hero = getSprite("agumon.png");
		BufferedImage background = null;
		BufferedImage backgroundMask = null;
		try {
			background = ImageIO.read(this.getClass().getClassLoader().getResource("back.png"));
			backgroundMask = ImageIO.read(this.getClass().getClassLoader().getResource("background_mask.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		blackPoints = new HashSet<Point>();
		
		for (int i = 0; i < backgroundMask.getWidth(); i++) {
			for (int j = 0; j < backgroundMask.getHeight(); j++) {
				Color c = new Color(backgroundMask.getRGB(i, j));
				if (c.equals(new Color(0,0,0))) {
					blackPoints.add(new Point(i,j));
				}
			}
		}
		this.background = Toolkit.getDefaultToolkit().createImage(
				background.getSource());
		
		eggs = new SpriteEgg();
		placeEgg();
		
		addKeyListener(new KeyInputHandler());
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2); // создаем BufferStrategy для нашего холста
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics(); // получаем Graphics из созданной
											// нами BufferStrategy
		
		g.drawImage(background, 0, 0, null);
		eggs.draw(g, xEgg, yEgg, currentEggSprite + (int)eggSpriteChanger%3);
		hero.get(currentSprite).draw(g, x, y);
		g.dispose();
		bs.show(); // показать
	}

	public void update(long delta) throws InterruptedException {
		if (leftPressed == true) {
			left = true;
			if(isCross("l")){
					x--;
			Thread.sleep(latency);
			spriteChanger+=0.1;}
		}
		if (rightPressed == true) {
			left = false;
			if(isCross("r")){
					x++;
			Thread.sleep(latency);
			spriteChanger+=0.1;}
		}
		if (upPressed == true) {
			up = true;
			if(isCross("u")){
					y--;
			Thread.sleep(latency);
			spriteChanger+=0.1;}
		}
		if (downPressed == true) {
			up = false;
			if(isCross("d")){
					y++;
			Thread.sleep(latency);
			spriteChanger+=0.1;}
		}
		
		if (spacePressed == true) {
			jump();
		}
		
		if(spriteChanger>50000) spriteChanger = 0;
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	private boolean isCross (String dir){
		switch (dir) {
		case "l":
			for (int i = 18; i <= 28; i++) {
				if(blackPoints.contains(new Point(x+10, y+i))) return false;
			}
			break;
			
		case "r":
			for (int i = 18; i <= 28; i++) {
				if(blackPoints.contains(new Point(x+22, y+i))) return false;
			}
			break;
	
		case "u":
			for (int i = 10; i <= 22; i++) {
				if(blackPoints.contains(new Point(x+i, y+18))) return false;
			}
			break;
			
		case "d":
			for (int i = 10; i <= 22; i++) {
				if(blackPoints.contains(new Point(x+i, y+28))) return false;
			}
			break;
			
		default:
			break;
		}
		
		return true;
	}
	
	private void placeEgg(){
		int x = new Random().nextInt(767);
		int y = new Random().nextInt(479);
		int c = new Random().nextInt(47)*3;
		Rectangle r = new Rectangle(x, y, 31, 31);
		
		for (Iterator<Point> iterator = blackPoints.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			if(r.contains(new java.awt.Point(point.getX(), point.getY()))) {
				placeEgg();
				return;
			}
		}
		
		xEgg = x;
		yEgg = y;
		currentEggSprite = c;
		
	}
	
	private void jump() throws InterruptedException{
		placeEgg();
		spacePressed = false;
	}

}
