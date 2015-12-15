package nl.baboea.android.skiresort.game;

import java.util.ArrayList;

/**
 * Created by Laurens on 19-11-2015.
 */
public interface ParticipantContainer {
    public ArrayList<GameParticipant> getAll();
    public GameParticipant[] update(float secondsElapsed);
}
