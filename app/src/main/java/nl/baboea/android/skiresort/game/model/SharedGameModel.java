package nl.baboea.android.skiresort.game.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.baboea.android.skiresort.game.messages.Message;

/**
 * Created by Laurens on 15-11-2016.
 */
public class SharedGameModel {

    public static final String TAG = "SharedGameModel";
    private static SharedGameModel instance;

    public static SharedGameModel getInstance() {
        if(instance==null) instance = new SharedGameModel();
        return instance;
    }

    private List<Message> messages = new ArrayList<>();


    private SharedGameModel() {

    }

    public void add(Message message) {
        messages.add(message);
    }

    public void draw() {
        for (Message message : messages) {
            Log.d(TAG, "draw: drawing a message:  " + message);
            message.draw();
        }
        Iterator<Message> iterator = messages.iterator();
        while (iterator.hasNext()) {
            Message next = iterator.next();
            if(!next.isValid())iterator.remove();
        }
    }
}
