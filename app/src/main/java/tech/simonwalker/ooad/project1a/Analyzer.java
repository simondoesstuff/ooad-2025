package tech.simonwalker.ooad.project1a;

public class Analyzer {
    public double calculateMean(double[] data) {
        double sum = 0;

        for (double d : data) {
            sum += d;
        }

        return sum / data.length;
    }

    public double calculateStdDev(double[] data) {
        double mean = calculateMean(data);
        double standardDeviation = 0;

        for (var d : data) {
            standardDeviation += Math.pow(d - mean, 2);
        }

        return Math.sqrt(standardDeviation / data.length);
    }

    public double findMin(double[] data) {
        double min = Double.MAX_VALUE;

        for (var d : data) {
            if (d < min) {
                min = d;
            }
        }

        return min;
    }

    public double findMax(double[] data) {
        double max = Double.MIN_VALUE;

        for (double d : data) {
            if (d > max) {
                max = d;
            }
        }

        return max;
    }

    /**
     * performs all statistical analyses on a given array and prints the results in a formatted row
     * @param functionName The name of the random number generation function used.
     */
    public void analyzeAndPrint(double[] data, String functionName, int amnt) {
        double mean = calculateMean(data);
        double stdDev = calculateStdDev(data);
        double min = findMin(data);
        double max = findMax(data);

        System.out.printf("%-25s | %-10d | %-10.6f | %-15.6f | %-10.6f | %-10.6f\n", functionName, amnt, mean, stdDev, min, max);
    }
}
