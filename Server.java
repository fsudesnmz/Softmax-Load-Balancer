import java.util.Random;

public class Server {
    private final int id;
    private double currentMeanLatency;
    private final Random random = new Random();

    public Server(int id, double initialMeanLatency) {
        this.id = id;
        this.currentMeanLatency = initialMeanLatency;
    }

    public double processRequest() {
        double noise = random.nextGaussian() * 2.0; // Küçük gürültü
        double latency = Math.max(1.0, currentMeanLatency + noise);
        evolve(); // Performans zamanla değişir
        return latency;
    }

    private void evolve() {
        // Her adımda performans %1 ihtimalle sert değişsin veya yavaşça kaysın
        currentMeanLatency += (random.nextDouble() - 0.5) * 1.5;
        currentMeanLatency = Math.max(10.0, Math.min(100.0, currentMeanLatency));
    }

    public int getId() { return id; }
    public double getActualMean() { return currentMeanLatency; }
}