# 🚀 Softmax Stratejisi ile Adaptif Yük Dengeleme Simülasyonu

Bu proje, **çok sunuculu sistemlerde akıllı yük dengeleme (load balancing)** problemini çözmek için geliştirilen bir simülasyondur.

Sistem, klasik yöntemler yerine **reinforcement learning (pekiştirmeli öğrenme)** yaklaşımı kullanarak en iyi sunucuyu zaman içinde öğrenir.

Projede özellikle:

* 🎯 **Softmax (Boltzmann Exploration) stratejisi**
* 📊 **Q-value tabanlı öğrenme**
* 🔄 **Non-stationary ortam simülasyonu (zamanla değişen sunucu performansı)**
* 🧩 **Strategy Pattern + Interface kullanımı**

uygulanmıştır.

---

# 📌 Problem Tanımı

Gerçek dünyada sunucu performansı sabit değildir:

* Ağ gecikmeleri değişir
* Sunucu yükü artar / azalır
* Donanım performansı zamanla değişebilir

Bu nedenle **her zaman en hızlı sunucuyu seçmek** mümkün değildir.

Amaç:

> Sistemin deneyim kazanarak zaman içinde en iyi sunucuyu öğrenmesi.

Bu problem, literatürde **Multi-Armed Bandit** problemine benzer.

---

# 🧠 Neden Softmax Kullanıldı?

Load balancing için birkaç yöntem vardır:

| Yöntem         | Problem                   |
| -------------- | ------------------------- |
| Round Robin    | Performansı dikkate almaz |
| Random         | Verimsiz                  |
| Greedy         | Keşif yapmaz              |
| Epsilon-Greedy | Keşif kaba ve kontrolsüz  |

Softmax yöntemi ise:

✅ Daha iyi sunuculara daha yüksek olasılık verir
✅ Ama diğerlerini tamamen dışlamaz
✅ Keşif ve sömürü (exploration-exploitation) dengesini kurar

Matematiksel olarak:

```
P(i) = exp(Qi / T) / Σ exp(Qj / T)
```

Burada:

* `Qi` → Sunucu kalite tahmini
* `T` → Temperature (keşif seviyesi)

Temperature yüksek → daha rastgele seçim
Temperature düşük → daha greedy seçim

---

# 🏗️ Proje Mimarisi

## 1️⃣ LoadBalancer Interface

Strateji bağımsız tasarım sağlar.

```java
public interface LoadBalancer {
    int selectServer();
    void updateStats(int serverId, double latency);
    double[] getProbabilities();
}
```

Avantaj:

> Softmax yerine başka algoritma kolayca eklenebilir.

Örneğin:

* Round Robin
* UCB
* Thompson Sampling
* Epsilon Greedy

---

## 2️⃣ SoftmaxLoadBalancer

Ana öğrenme algoritmasıdır.

Özellikler:

* Q-Learning benzeri güncelleme
* Olasılıksal seçim
* Temperature kontrolü
* Numerik stabilite koruması

Reward fonksiyonu:

```
reward = -latency
```

Latency düşük → reward yüksek

Güncelleme kuralı:

```
Q = Q + α (reward − Q)
```

---

## 3️⃣ Server Sınıfı

Gerçek dünyayı simüle eder.

Özellikler:

* Gaussian noise
* Zamanla performans değişimi
* Minimum / maksimum sınırlar

```java
double noise = random.nextGaussian() * 2.0;
```

---

## 4️⃣ Simulation (Main)

Sistemin çalıştırıldığı kısımdır.

Simülasyon:

* 600 istek gönderir
* Her 100 adımda rapor verir
* 300. adımda ortam değişir

```java
if (i == 300) {
    servers[0] = new Server(0, 100.0);
    servers[2] = new Server(2, 20.0);
}
```

Bu sayede algoritmanın adaptasyon yeteneği test edilir.

---

# 🔄 Non-Stationary Environment Testi

300. adımda:

* En iyi sunucu bozulur
* En kötü sunucu iyileşir

Amaç:

> Algoritma yeni duruma adapte olabiliyor mu?

Softmax + Learning Rate sayesinde sistem yeniden öğrenir.

---

# 📊 Örnek Çıktı

```
Adım 100 | Seçilen: S0 (31.2ms) | Olasılıklar: S0:%70 S1:%20 S2:%10
Adım 200 | Seçilen: S0 (29.5ms) | Olasılıklar: S0:%85 S1:%10 S2:%5
...
[!] NETWORK DEĞİŞİKLİĞİ: S0 yavaşlıyor, S2 hızlanıyor!
...
Adım 500 | Seçilen: S2 (22.1ms) | Olasılıklar: S0:%10 S1:%15 S2:%75
```

---

# 🎯 Öğrenme Mantığı (Basit Anlatım)

Sistem şu döngüyü sürekli yapar:

1️⃣ Sunucu seç
2️⃣ Gecikmeyi ölç
3️⃣ Performans tahminini güncelle
4️⃣ Daha iyi sunucuya yönel

Bu süreç:

> Deneyimle öğrenen akıllı sistem oluşturur.

---

# 🧩 Tasarım Desenleri

Projede kullanılan yazılım prensipleri:

* Strategy Pattern
* Interface-based Design
* Reinforcement Learning yaklaşımı
* Separation of Concerns
* Modüler mimari

---

# ⚙️ Parametreler

| Parametre   | Açıklama          |
| ----------- | ----------------- |
| Temperature | Keşif seviyesi    |
| Alpha       | Öğrenme hızı      |
| Initial Q   | Başlangıç tahmini |

---

# 🚀 Geliştirme Fikirleri

Projeyi geliştirmek için:

* UCB algoritması eklemek
* Thompson Sampling eklemek
* Gerçek HTTP istekleri bağlamak
* Graf çizimi eklemek
* Adaptive temperature yapmak
* Deep Reinforcement Learning entegrasyonu

---

# 🎓 Akademik Bağlantı

Bu proje aşağıdaki alanlarla ilişkilidir:

* Reinforcement Learning
* Multi-Armed Bandit
* Distributed Systems
* Cloud Computing
* Adaptive Systems

---

# 👨‍💻 Çalıştırma

```bash
javac *.java
java Simulation
```

---

# ⭐ Özet

Bu proje:

✅ Akıllı yük dengeleme
✅ Öğrenen sistem
✅ Numerik stabil Softmax uygulaması
✅ Gerçekçi ortam simülasyonu

sunmaktadır.

---

# 📜 Lisans

MIT License 

---
