package smart;

import game.Bird;

public interface Brain {
    void setBody(Bird body);
    boolean thinksAboutJumping();
}
