public class Simulation {
    public static void main(String[] args) {
        int K = 3;
        Server[] servers = new Server[K];
        servers[0] = new Server(0, 30.0); // İyi sunucu
        servers[1] = new Server(1, 50.0); // Orta
        servers[2] = new Server(2, 70.0); // Kötü

        // INTERFACE KULLANIMI: İstenirse RoundRobinLoadBalancer ile değiştirilebilir
        LoadBalancer lb = new SoftmaxLoadBalancer(K, 10.0, 0.2);

        System.out.println("SİSTEM BAŞLATILDI (Interface: LoadBalancer, Strategy: Softmax)");
        System.out.println("---------------------------------------------------------------");

        for (int i = 1; i <= 600; i++) {
            // 1. Olasılıkları gör (Analiz için)
            double[] probs = lb.getProbabilities();

            // 2. Seçim yap (Interface metodu)
            int selectedIdx = lb.selectServer();

            // 3. İsteği gönder ve latency al
            double latency = servers[selectedIdx].processRequest();

            // 4. İstatistiği güncelle (Interface metodu)
            lb.updateStats(selectedIdx, latency);

            // Her 100 adımda bir durum raporu
            if (i % 100 == 0) {
                printReport(i, servers, probs, selectedIdx, latency);
            }

            // Non-stationary test: 300. adımda S0 bozulur, S2 iyileşir
            if (i == 300) {
                System.out.println("\n[!] NETWORK DEĞİŞİKLİĞİ: S0 yavaşlıyor, S2 hızlanıyor!\n");
                servers[0] = new Server(0, 100.0);
                servers[2] = new Server(2, 20.0);
            }
        }
    }

    private static void printReport(int i, Server[] servers, double[] probs, int sel, double lat) {
        System.out.print(String.format("Adım %3d | Seçilen: S%d (%.1fms) | Olasılıklar: ", i, sel, lat));
        for (int s = 0; s < servers.length; s++) {
            System.out.print(String.format("S%d:%%%d (Gerçek Mean:%.1f) ", s, (int)(probs[s]*100), servers[s].getActualMean()));
        }
        System.out.println();
    }
}