
package tankgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tankgame.BaseFiles.GameEvents;

public class KeyControl extends KeyAdapter {
    
    private GameEvents ge = TankGame.getGameEvents();

        public KeyControl() {
        }
        @Override
        public void keyReleased(KeyEvent e) {
            ge.setValue(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            ge.setValue(e);
        }
    }