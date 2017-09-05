package genetics;

import game.Bird;
import game.Game;
import smart.Brain;
import smart.NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final double uniformRate = 0.5;
    private static final int generationBase = 5;

    private static final double mutationRate = 0.015;
    private static final double mutationUpper = 0.8;
    private static final double mutationLower = -0.8;

    // num individuals that jumps over to the next generation.
    private static final int elitism = 2;
    private static final int elitismOffset = elitism - 1;

    private static final int theBest = 4;

    // selects an individual at random from the birds that are worse than the elites
    private static final int ignores = 4;

    public static Population evolvePopulation(Population population){
        Bird[] oldBirds = population.getSotedBirds();

        Bird[] newBirds = new Bird[population.size()];
        List<Bird> generationBase = new ArrayList<>();

        Random randomGenerator = new Random();

        generationBase.addAll(Arrays.asList(oldBirds).subList(0, theBest));
        generationBase.add(tournament(oldBirds));

        System.arraycopy(oldBirds, 0, newBirds, 0, elitism);

        for (int i = elitismOffset; i < population.size(); i++) {
            Bird bird1 = generationBase.get(randomGenerator.nextInt(generationBase.size()));
            Bird bird2 = generationBase.get(randomGenerator.nextInt(generationBase.size()));
            Bird babyBird = crossower(bird1, bird2, population.getGame());
            newBirds[i] = babyBird;
        }

        for (int i = elitismOffset; i < population.size(); i++) {
            mutate(newBirds[i]);
        }

        return new Population(newBirds, population.getGame());
    }

    private static Bird crossower(Bird bird1, Bird bird2, Game game){
        NeuralNetwork brain1 = (NeuralNetwork) bird1.getBrain();
        NeuralNetwork brain2 = (NeuralNetwork) bird2.getBrain();

        double[] inToHidden = new double[brain1.weightsInnToHidden.length];
        double[] hiddenToOut = new double[brain2.weightsHiddenToOut.length];

        for (int i = 0; i < brain1.weightsInnToHidden.length; i++) {
            if(Math.random() >= uniformRate){
                inToHidden[i] = brain1.weightsInnToHidden[i];
            } else {
                inToHidden[i] = brain2.weightsInnToHidden[i];
            }
        }
        for (int i = 0; i < brain1.weightsHiddenToOut.length; i++) {
            if(Math.random() >= uniformRate){
                inToHidden[i] = brain1.weightsHiddenToOut[i];
            } else {
                inToHidden[i] = brain2.weightsHiddenToOut[i];
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
            if(Math.random() >= mutationRate){
                brain.weightsInnToHidden[i] += (Math.random() * (mutationUpper - mutationLower)) + mutationLower;
            }
        }
        for (int i = 0; i < brain.weightsHiddenToOut.length; i++) {
            if(Math.random() >= mutationRate){
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
}
