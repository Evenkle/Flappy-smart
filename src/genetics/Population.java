package genetics;

import game.Bird;
import game.Game;
import smart.NeuralNetwork;

import java.util.Arrays;
import java.util.Collections;

public class Population {

    Bird[] birds;
    Game game;

    public Population(int populationSize, Game game){
        birds = new Bird[populationSize];
        this.game = game;

        for (int i = 0; i < size(); i++) {
            Bird bird = new Bird(game, new NeuralNetwork());
            saveIndividual(i, bird);
        }
    }

    public Bird[] getBirds() {
        Arrays.sort(birds);
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
        Population pop = new Population(10, game);
        System.out.println(Arrays.toString(pop.birds));


    }
}
