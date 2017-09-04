package smart;

import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {

    private static final int numInputs = 2;
    private static final int numHidden = 6;
    private static final int numOutput = 1;

    double[] weightsInnToHidden = new double[numInputs*numHidden];
    double[] weightsHiddenToOut = new double[numHidden*numOutput];

    double[] hiddenNeurons = new double[numHidden];
    {
        Arrays.fill(hiddenNeurons, 0);
    }

    public NeuralNetwork(){
        randIntitWeights(weightsInnToHidden, numInputs, numHidden);
        randIntitWeights(weightsHiddenToOut, numHidden, numOutput);
    }

    /**
     * Return an array of randomly initialized doubles in the range -epsilon,epsilon
     * where epsilon = sqrt(6)/sqrt(in + out)
     */
    private void randIntitWeights(double[] weights, int numInn, int numOut){
        double epsilon = Math.sqrt(6)/Math.sqrt(numInn+numOut);
        Random r = new Random();
        for (int i = 0; i < weights.length; i++) {
            double newWeight;
            do {
                newWeight = (epsilon*2*r.nextDouble()) - epsilon;
            } while (newWeight == 0);
            weights[i] = newWeight;
        }
    }

    /**
     * Calculate the output from the input
     * @param inputs takes 2 inputs
     * @return calculated output
     */
    private double calculate(int... inputs){
        if(inputs.length != numInputs){
            throw new IllegalArgumentException();
        }
        double nevron = 0;
        for (int i = 0; i < numHidden; i++) {
            for (int j = 0; j < numInputs; j++) {
                nevron += inputs[j] * weightsInnToHidden[j+(2*i)];
            }
            hiddenNeurons[i] = Sigmoid.sigmoid(nevron);
        }

        double output = 0;

        for (int i = 0; i < numHidden; i++) {
            output += weightsHiddenToOut[i] * hiddenNeurons[i];
        }
        return Sigmoid.sigmoid(output);
    }
}
