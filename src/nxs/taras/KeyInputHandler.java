package nxs.taras;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class KeyInputHandler extends KeyAdapter {
	
	public void keyPressed(KeyEvent e) { //клавиша нажата
	    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	        Game.leftPressed = true;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	        Game.rightPressed = true;
	    }	
	    if (e.getKeyCode() == KeyEvent.VK_UP) {
	        Game.upPressed = true;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	        Game.downPressed = true;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	        Game.spacePressed = true;
	    }
	} 	
	public void keyReleased(KeyEvent e) { //клавиша отпущена
	    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	    	Game.leftPressed = false;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	    	Game.rightPressed = false;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_UP) {
	        Game.upPressed = false;
	    }
	    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	        Game.downPressed = false;
	    }
	}
}