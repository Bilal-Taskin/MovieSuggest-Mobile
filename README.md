# 📱 MovieSuggest - Akıllı Film Öneri ve Takip Platformu (Mobil Uygulama)

MovieSuggest, sinema tutkunlarının popüler filmleri keşfetmesini, kendilerine özel izleme listeleri oluşturmasını ve izledikleri filmlere puan/yorum bırakarak kişisel film arşivlerini yönetmelerini sağlayan modern ve dinamik bir **Android** uygulamasıdır.

Bu depo, projenin mobil arayüz ve istemci (client) kaynak kodlarını barındırmaktadır. Projenin C# ile geliştirilen backend servislerine [MovieSuggest-Backend](BURAYA_BACKEND_REPO_LINKINI_YAZIN) adresinden ulaşabilirsiniz.

---

## 🌐 Veri Entegrasyonu & TMDB API Mimarisi

Uygulamadaki gerçek zamanlı film verileri, küresel sinema veritabanı **The Movie Database (TMDB) API** entegrasyonu ile dinamik olarak yönetilmektedir. Veri çekme ve listeleme mimarisi şu adımlarla çalışır:

1. **RESTful API İstekleri:** Popüler filmler ve vizyondaki güncel yapımlar, TMDB'nin `movie/popular` uç noktasına (endpoint) asenkron HTTP istekleri atılarak JSON formatında çekilir.
2. **Arka Plan Görsel İşleme (Glide Integration):** TMDB API'den dönen saf veri içindeki poster kodları (Örn: `/abc123xyz.jpg`), Glide kütüphanesi yardımıyla TMDB'nin yüksek hızlı görsel sunucularına (`https://image.tmdb.org/t/p/w500`) bağlanır.
3. **Önbelleğe Alma (Caching) & Performans:** Görseller telefona indirilirken arka planda otomatik olarak önbelleğe (cache) alınır. Bu sayede kullanıcı sayfayı her yukarı-aşağı kaydırdığında internet kotası harcanmaz ve arayüzde donma/kasılma (stuttering) yaşanmaz.

4. ----

## 🚀 Proje Mimarisi ve Mühendislik Çözümleri

Bir yazılım mühendisi adayı olarak bu projede, kurumsal mimari standartlarına uyum sağlamak ve mobil-sunucu arasındaki veri transferini en optimize hale getirmek için şu kritik çözümler uygulanmıştır:

* **Çoklu Kullanıcı Hafıza Yönetimi (Multi-User SharedPrefs):** Kullanıcı oturumları ve kimlik bilgileri, uygulamanın yerel hafızasında `SharedPreferences` mimarisiyle güvenli bir şekilde saklanır. Böylece her kullanıcı sadece kendi listelerine erişebilir.
* **JWT Token Deşifreleme (Claim Parsing):** .NET Web API tarafından üretilen güvenli JWT Token'lar, Android tarafında saf Java algoritmalarıyla parçalanmış; kurumsal Microsoft Claim yapıları (`nameidentifier`) deşifre edilerek kullanıcı ID'si dinamik olarak elde edilmiştir.
* **HTTP 415 & 400 Optimizasyonu (JSON Data Binding):** Sunucu ile yaşanan veri transfer uyuşmazlıkları, eski tip form verileri yerine modern **saf JSON (`HashMap<String, Object>`)** paketleme mimarisine geçilerek kökten çözülmüştür.

---

## 🛠️ Teknolojik Altyapı ve Kullanılan Elemanlar

Uygulamanın geliştirilmesinde, modern Android dünyasının kabul gördüğü kararlı ve performanslı kütüphaneler tercih edilmiştir:

* **Dil:** Java (Android SDK)
* **Ağ Yönetimi (Network):** **Retrofit 2 & OkHttp3** (Asenkron API çağrıları, RESTful entegrasyonu)
* **Görsel Önbelleğe Alma (Image Caching):** **Glide** (TMDB film posterlerinin arka planda internet hızını yormadan, önbelleğe alınarak jilet gibi yüklenmesi)
* **Veri Listeleme:** **RecyclerView & Custom Adapters** (Binlerce veriyi performansı düşürmeden listeleyen dinamik yapılar)

---

## 🎨 Kullanıcı Arayüzü (UI) ve Tasarım Elemanları

Uygulama, sinema salonu atmosferini yaşatmak adına **Koyu Tema (Dark Mode)** konseptiyle ve modern bileşenlerle tasarlanmıştır:

* **Dashboard (Kontrol Paneli):** Kullanıcıyı karşılayan 2x2 matris yapısında modern **GridLayout** ve oval köşeli şık **CardView** menü tasarımları.
* **Özel Diyalog Pencereleri (AlertDialog):** Kullanıcının filmleri "İzledim" olarak işaretlediği esnada ekranda beliren, entegre **RatingBar** ve **EditText** barındıran modern film değerlendirme penceresi.
* **Görsel Tasarım:** Kullanıcı dostu emojilerle desteklenmiş, sade ve okunabilir tipografi.

---

## 📸 Uygulama Ekran Görüntüleri

### 🔐 1. Oturum Yönetimi & Ana Menü
| Giriş Ekranı | Kayıt Olma Ekranı | Kontrol Paneli (Dashboard) |
| :---: | :---: | :---: |
| <img width="240" alt="Giriş Ekranı" src="https://github.com/user-attachments/assets/006c5aee-92d8-4764-b4b2-573343946586" /> | <img width="240" alt="Kayıt Ekranı" src="https://github.com/user-attachments/assets/e326e222-1e13-403b-ae8b-0c1056581698" /> | <img width="240" alt="Dashboard" src="https://github.com/user-attachments/assets/c09de0e1-3e73-4705-beba-47afae1a50f7" /> |

### 🎬 2. Film Keşfi & İzleme Listesi İşlemleri
| Popüler Filmler | İzleme Listem | Film Değerlendirme (Dialog) |
| :---: | :---: | :---: |
| <img width="240" alt="Popüler Filmler" src="https://github.com/user-attachments/assets/7e421346-60f2-4abb-bb7d-70079a284e16" /> | <img width="240" alt="İzleme Listem" src="https://github.com/user-attachments/assets/4187a062-4f08-4bc3-9368-03a33d9bbd42" /> | <img width="240" alt="Yorum Değerlendirme" src="https://github.com/user-attachments/assets/fb9838ab-6ad3-4311-8c26-60ed68445f4e" /> |

### 🍿 3. Kişisel Sinema Arşivi & İncelemeler
| İzlediğim Filmler | Yaptığım Yorumlar |
| :---: | :---: |
| <img width="240" alt="İzlenenler" src="https://github.com/user-attachments/assets/2b343a30-403a-4469-8aaa-80b6253660ea" /> | <img width="240" alt="Yorumlarım" src="https://github.com/user-attachments/assets/c96b9ebc-07dc-4f76-a86d-949acef553c0" /> |

*(Not: Çıkış yap ekran görüntüsü en son işleme ait olduğu için arşiv sekmesinde listelenmiştir: <img width="18" src="https://github.com/user-attachments/assets/16d31b38-4e8d-4e36-b2ee-71747e33081f"/>)*

---

## 🏃 Projeyi Yerelde Çalıştırma

1. Bilgisayarınızda projenin C# Backend servisini ayağa kaldırın.
2. Bu depoyu bilgisayarınıza klonlayın veya indirin.
3. Android Studio ile projeyi açın.
4. `RetrofitClient.java` dosyasındaki `BASE_URL` değişkenine, backend servisinizin çalıştığı yerel IP adresini yazın.
5. `Build -> Rebuild Project` yaptıktan sonra emülatörde çalıştırabilirsiniz.
