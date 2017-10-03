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
            if(i == population.size() - 1){
                Bird bird1 = generationBase.get(0);
                Bird bird2 = generationBase.get(1);
                Bird babyBird = crossover(bird1, bird2, population.getGame());
                newBirds[i] = babyBird;
                break;
            }

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
}
