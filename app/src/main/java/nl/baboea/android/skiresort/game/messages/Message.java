package nl.baboea.android.skiresort.game.messages;

import nl.baboea.android.skiresort.Text;

/**
 * Created by Laurens on 15-11-2016.
 */
public class Message {

    private final long start;
    private Text text;
    private int ttl;
    private boolean valid = true;

    public Message(String text) {
        this.text = new Text(text);
        start = System.currentTimeMillis();
    }

    public void draw() {
        text.draw();
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public boolean isValid() {
        return valid && (System.currentTimeMillis() < start + ttl);
    }


    public Text getText() {
        return text;
    }

    @Override
    public String toString() {
        return text.toString();
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
