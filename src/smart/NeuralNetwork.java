package smart;

import game.Bird;
import game.Game;
import game.Pillar;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork implements Brain {

    private Bird body;
    private Game game;

    private static final int numInputs = 2;
    private static final int numHidden = 6;
    private static final int numOutput = 1;

    public double[] weightsInnToHidden = new double[numInputs * numHidden];
    public double[] weightsHiddenToOut = new double[numHidden * numOutput];

    double[] hiddenNeurons = new double[numHidden];

    {
        Arrays.fill(hiddenNeurons, 0);
    }

    public NeuralNetwork() {
        randIntitWeights(weightsInnToHidden, numInputs, numHidden);
        randIntitWeights(weightsHiddenToOut, numHidden, numOutput);
    }

    /**
     * Return an array of randomly initialized doubles in the range -epsilon,epsilon
     * where epsilon = sqrt(6)/sqrt(in + out)
     */
    private void randIntitWeights(double[] weights, int numInn, int numOut) {
        double epsilon = Math.sqrt(6) / Math.sqrt(numInn + numOut);
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            double newWeight;
            do {
                newWeight = (epsilon * 2 * r.nextDouble()) - epsilon;
            } while (newWeight == 0);
            weights[i] = newWeight;
        }
    }

    /**
     * Calculate the output from the input
     *
     * @param inputs takes 2 inputs
     * @return calculated output
     */
    private double calculate(double... inputs) {
        if (inputs.length != numInputs) {
            throw new IllegalArgumentException();
        }
        double neuron = 0;
        for (int i = 0; i < numHidden; i++) {
            for (int j = 0; j < numInputs; j++) {
                neuron += inputs[j] * weightsInnToHidden[j + (2 * i)];
            }
            hiddenNeurons[i] = Sigmoid.sigmoid(neuron);
        }
        //System.out.println(Arrays.toString(hiddenNeurons));
        double output = 0;

        for (int i = 0; i < numHidden; i++) {
            output += weightsHiddenToOut[i] * hiddenNeurons[i];
        }
        return Sigmoid.sigmoid(output);
    }

    @Override
    public String toString() {
        String str = "";
        str += Arrays.toString(weightsInnToHidden);
        str += Arrays.toString(weightsHiddenToOut);
        return str;
    }

    @Override
    public void setContext(Bird body, Game game) {
        this.body = body;
        this.game = game;
    }

    @Override
    public boolean thinksAboutJumping() {
        Pillar pillar = game.getPillars().get(0);
        pillar.getGapY();

        // vertical distance to next checkpoint
        double heightDifferance = body.getYPos() - pillar.getGapY();

        // horizontal distance to next checkpoint
        double xDistance = pillar.getXPos() + pillar.getWidth() - body.getXPos();

        double out = calculate(heightDifferance, xDistance);
        return out > 0.5;
    }
}
