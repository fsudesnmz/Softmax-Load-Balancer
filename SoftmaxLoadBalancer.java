import java.util.Arrays;

public class SoftmaxLoadBalancer implements LoadBalancer {
    private final double[] qValues;
    private final double temperature;
    private final double alpha; // Learning rate

    public SoftmaxLoadBalancer(int k, double temperature, double alpha) {
        this.qValues = new double[k];
        this.temperature = temperature;
        this.alpha = alpha;
        // Başlangıçta tüm sunuculara nötr (veya iyimser) bir değer atanır
        Arrays.fill(qValues, -20.0);
    }

    @Override
    public int selectServer() {
        double[] probs = getProbabilities();
        double r = Math.random();
        double cumulative = 0.0;

        for (int i = 0; i < probs.length; i++) {
            cumulative += probs[i];
            if (r <= cumulative) return i;
        }
        return probs.length - 1;
    }

    @Override
    public void updateStats(int serverId, double latency) {
        double reward = -latency; // Latency düşükse reward yüksektir
        // Q-Learning update kuralı
        qValues[serverId] += alpha * (reward - qValues[serverId]);
    }

    @Override
    public double[] getProbabilities() {
        double[] probs = new double[qValues.length];

        // --- NÜMERİK STABİLİTE ÇÖZÜMÜ ---
        double maxQ = Double.NEGATIVE_INFINITY;
        for (double q : qValues) if (q > maxQ) maxQ = q;

        double sum = 0.0;
        for (int i = 0; i < qValues.length; i++) {
            // (q - maxQ) yaparak exp(x) fonksiyonunun sonsuza (Infinity) gitmesini engelliyoruz
            probs[i] = Math.exp((qValues[i] - maxQ) / temperature);
            sum += probs[i];
        }

        for (int i = 0; i < probs.length; i++) {
            probs[i] /= sum;
        }
        return probs;
    }
}