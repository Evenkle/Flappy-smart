package genetics;

import game.Bird;
import game.Game;
import smart.NeuralNetwork;

import java.util.Arrays;

public class Population {

    Bird[] birds;
    Game game;

    public final int generation;

    public Population(int populationSize, boolean initiate, Game game, int generation){
        birds = new Bird[populationSize];
        this.game = game;
        this.generation = generation;
        if(initiate) {
            for (int i = 0; i < size(); i++) {
                Bird bird = new Bird(game, new NeuralNetwork());
                saveIndividual(i, bird);
            }
        }
    }

    public Population(Bird[] birds, Game game, int generation){
        this.game = game;
        this.birds = birds.clone();
        this.generation = generation;
    }

    public Game getGame() {
        return game;
    }

    public Bird[] getSortedBirds() {
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

    @Override
    public String toString() {
        return Arrays.toString(birds);
    }

    public static void main(String[] args) {
        Game game = new Game();
        Population pop = new Population(10,true, game, 0);
        System.out.println(Arrays.toString(pop.getBirds()));


    }
}
