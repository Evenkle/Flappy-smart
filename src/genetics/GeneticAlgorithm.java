package genetics;

import game.Bird;
import game.Game;
import smart.NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final double uniformRate = 0.5;

    private static final double mutationRate = 0.015;
    private static final double mutationUpper = 0.5;
    private static final double mutationLower = -0.5;

    // num individuals that jumps over to the next generation.
    private static final int elitism = 3;
    private static final int elitismOffset = elitism;

    private static final int theBest = 4;

    // selects an individual at random from the birds that are worse than the elites
    private static final int ignores = 2;

    public static Population evolvePopulation(Population population){
        Bird[] oldBirds = population.getSortedBirds();

        Bird[] newBirds = new Bird[population.size()];
        List<Bird> generationBase = new ArrayList<>();

        Random randomGenerator = new Random();

        generationBase.addAll(Arrays.asList(oldBirds).subList(0, theBest));
        generationBase.add(tournament(oldBirds));

        for (int i = 0; i < elitism; i++) {
            newBirds[i] = new Bird(population.getGame(), oldBirds[i].getBrain());
        }

        for (int i = elitismOffset; i < population.size(); i++) {
            Bird bird1 = generationBase.get(randomGenerator.nextInt(generationBase.size()));
            generationBase.remove(bird1);
            Bird bird2 = generationBase.get(randomGenerator.nextInt(generationBase.size()));
            generationBase.add(bird1);
            Bird babyBird = crossover(bird1, bird2, population.getGame());
            newBirds[i] = babyBird;
        }
        System.out.println(newBirds[0].getBrain());
        for (int i = elitismOffset; i < population.size(); i++) {
            mutate(newBirds[i]);
        }

        return new Population(newBirds, population.getGame(), population.generation + 1);
    }

    private static Bird crossover(Bird bird1, Bird bird2, Game game){
        NeuralNetwork brain1 = (NeuralNetwork) bird1.getBrain();
        NeuralNetwork brain2 = (NeuralNetwork) bird2.getBrain();

        double[] inToHidden = new double[brain1.weightsInnToHidden.length];
        double[] hiddenToOut = new double[brain1.weightsHiddenToOut.length];

        for (int i = 0; i < brain1.weightsInnToHidden.length; i++) {
            if(Math.random() >= uniformRate){
                inToHidden[i] = brain1.weightsInnToHidden[i];
            } else {
                inToHidden[i] = brain2.weightsInnToHidden[i];
            }
        }
        for (int i = 0; i < brain1.weightsHiddenToOut.length; i++) {
            if(Math.random() >= uniformRate){
                hiddenToOut[i] = brain1.weightsHiddenToOut[i];
            } else {
                hiddenToOut[i] = brain2.weightsHiddenToOut[i];
            }
        }
        NeuralNetwork newBrain = new NeuralNetwork();
        newBrain.weightsInnToHidden = inToHidden;
        newBrain.weightsHiddenToOut = hiddenToOut;
        return new Bird(game, newBrain);
    }


    private static void mutate(Bird bird){
        NeuralNetwork brain = (NeuralNetwork) bird.getBrain();
        for (int i = 0; i < brain.weightsInnToHidden.length; i++) {
            if(Math.random() <= mutationRate){
                brain.weightsInnToHidden[i] += (Math.random() * (mutationUpper - mutationLower)) + mutationLower;
            }
        }
        for (int i = 0; i < brain.weightsHiddenToOut.length; i++) {
            if(Math.random() <= mutationRate){
                brain.weightsHiddenToOut[i] += (Math.random() * (mutationUpper - mutationLower)) + mutationLower;
            }
        }
    }

    private static Bird tournament(Bird[] birds){
        int lower = theBest - 1;
        int upper = birds.length - ignores;

        int winner = (int) (Math.random() * (upper - lower)) + lower;

        return birds[winner];
    }

    public static void main(String[] args) {
        Game game = new Game();

        /*
        Bird bird1 = new Bird(game, new NeuralNetwork());
        Bird bird2 = new Bird(game, new NeuralNetwork());
        NeuralNetwork brain1 = (NeuralNetwork) bird1.getBrain();
        NeuralNetwork brain2 = (NeuralNetwork) bird2.getBrain();

        Arrays.fill(brain1.weightsInnToHidden, 1);
        Arrays.fill(brain1.weightsHiddenToOut, 1);
        Arrays.fill(brain2.weightsInnToHidden, 2);
        Arrays.fill(brain2.weightsHiddenToOut, 2);

        System.out.println(brain1);
        System.out.println(brain2);
        */
        /*
        Bird bird3 = crossower(bird1, bird2, game);
        NeuralNetwork brain3 = (NeuralNetwork) bird3.getBrain();
        System.out.println(brain3);

        mutate(bird1);
        System.out.println(bird1.getBrain());
        */


        Bird[] birds = new Bird[10];
        for (int i = 0; i < birds.length; i++) {
            birds[i] = new Bird(game, new NeuralNetwork());
            NeuralNetwork brain = (NeuralNetwork) birds[i].getBrain();
            Arrays.fill(brain.weightsInnToHidden, i + 1);
            Arrays.fill(brain.weightsHiddenToOut, i + 1);
            System.out.println(brain);
        }
        Population pop = new Population(birds, game, 0);
        System.out.println(pop);

        Population pop2 = evolvePopulation(pop);

        Bird[] newBirds = pop2.getBirds();
        System.out.println(newBirds);
        for (Bird newBird : newBirds) {
            NeuralNetwork brain = (NeuralNetwork) newBird.getBrain();
            System.out.println(brain);
        }

    }
}
