package smart;

import game.Bird;
import game.Game;

public interface Brain {
    /**
     * This brain now has a body! Hold on to it, you might need it later on.
     * @param body A fully functional bird.
     * @param game The game this bird is playing in.
     */
    void setContext(Bird body, Game game);

    /**
     * Called by the bird's body when it needs to know whether to jump.
     * @return true if you should jump
     */
    boolean thinksAboutJumping();
}
