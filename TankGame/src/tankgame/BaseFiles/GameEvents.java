package tankgame.BaseFiles;

import java.awt.event.KeyEvent;
import java.util.Observable;

public class GameEvents extends Observable {

    public int type;
    public Object event;

    public void setValue(KeyEvent e) {
        this.type = 1;
        this.event = e;
        setChanged();
        notifyObservers(this);
    }

    public void setValue(String msg) {
        this.type = 2;
        this.event = msg;
        setChanged();
        notifyObservers(this);
    }
}
