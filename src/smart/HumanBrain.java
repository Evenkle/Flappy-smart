package smart;

import game.Bird;
import game.Game;

public class HumanBrain implements Brain {
    public boolean thinksAboutJumping = false;

    @Override
    public void setContext(Bird body, Game game) {}

    @Override
    public boolean thinksAboutJumping() {
        return thinksAboutJumping;
    }
}
