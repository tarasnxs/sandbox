package nxs.taras;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SpriteEgg {
    private ArrayList<Image> image; //изображение
    private BufferedImage temp;
    private int currentImage = 0;
    
    public SpriteEgg() {
    	image = new ArrayList<Image>();
    	try {
    		for(int i = 2; i<=240; i+=34){
    			int s=0;
    			for (int j = 2; j <=590; j+=34) {
    				if(s>=3&&s%3==0) j+=2;
    				s++;
    				temp = ImageIO.read(this.getClass().getClassLoader().getResource("eggs.png")).getSubimage(j, i, 31, 31);
    				BufferedImage convertedImg = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
    				convertedImg.getGraphics().drawImage(temp, 0, 0, null);
    				int col = (0 << 24) | (0 << 16) | (0 << 8) | 0;
    				for (int n = 0; n < convertedImg.getWidth(); n++) {
    					for (int m = 0; m < convertedImg.getHeight(); m++) {
    						Color c = new Color(convertedImg.getRGB(n, m));
    						if (c.equals(new Color(104, 248, 152))) {
    							convertedImg.setRGB(n, m, col);
    						}
    					}
    				}
    				image.add(convertedImg);
    				
				}
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public int getWidth() { //получаем ширину картинки
        return image.get(currentImage).getWidth(null);
    }

    public int getHeight() { //получаем высоту картинки
        return image.get(currentImage).getHeight(null);
    }

    
    public void draw(Graphics g,int x,int y, int spriteIndex) { //рисуем картинку
        g.drawImage(image.get(spriteIndex),x,y,null);
    }
}