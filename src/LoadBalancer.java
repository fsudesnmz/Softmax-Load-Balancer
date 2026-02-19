public interface LoadBalancer {
    // Mevcut sunucu listesinden birini seçer
    int selectServer();

    // Geri bildirim (feedback) döngüsü: Seçilen sunucunun performansını bildirir
    void updateStats(int serverId, double latency);

    // Analiz için mevcut olasılıkları döndürür
    double[] getProbabilities();
}