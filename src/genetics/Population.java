package genetics;

import game.Bird;
import game.Game;
import smart.NeuralNetwork;

import java.util.Arrays;
import java.util.Collections;

public class Population {

    Bird[] birds;
    Game game;

    public Population(int populationSize, boolean initiate, Game game){
        birds = new Bird[populationSize];
        this.game = game;
        if(initiate) {
            for (int i = 0; i < size(); i++) {
                Bird bird = new Bird(game, new NeuralNetwork());
                saveIndividual(i, bird);
            }
        }
    }

    public Population(Bird[] birds, Game game){
        this.game = game;
        this.birds = birds.clone();
    }

    public Game getGame() {
        return game;
    }

    public Bird[] getSotedBirds() {
        Arrays.sort(birds);
        return birds;
    }

    public Bird[] getBirds() {
        return birds;
    }

    public void saveIndividual(int index, Bird bird){
        birds[index] = bird;
    }

    public int size(){
        return birds.length;
    }

    public static void main(String[] args) {
        Game game = new Game();
        Population pop = new Population(new Bird[]{new Bird(game, new NeuralNetwork())}, game);
        System.out.println(Arrays.toString(pop.birds));
    }
}
