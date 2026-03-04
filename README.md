# QA Automation Assessment

Merhaba, ben Emir Ata Yalçın. Bu proje, QA Engineer pozisyonu için hazırladığım UI otomasyon vaka çalışmasıdır.

Projeyi Java, Selenium WebDriver ve TestNG kullanarak, ölçeklenebilir ve sürdürülebilir olması adına **Page Object Model (POM)** tasarım desenine uygun olarak geliştirdim.

## 🚀 Teknolojiler
*   **Dil:** Java (JDK 17)
*   **Web Otomasyon:** Selenium WebDriver (v4.27.0)
*   **Test Framework:** TestNG
*   **Proje Yönetimi:** Maven
*   **Raporlama:** ExtentReports 5
*   **Sürücü Yönetimi:** WebDriverManager

## 📁 Proje Mimarisi (POM)

Kodların tekrarını önlemek ve bakımı kolaylaştırmak için test senaryoları ve sayfa nesneleri birbirinden ayrılmıştır.

*   `src/main/java/pages`: Web elementlerinin locatörleri ve sayfa aksiyonları yer alır.
*   `src/main/java/utils`: ExtentReports konfigürasyonları ve ortak WebDriver kurulum sınıfları yer alır.
*   `src/test/java/tests`: TestNG test senaryolarının çalıştığı dizindir.

## ⚙️ Uygulanan Optimizasyonlar
Geliştirme esnasında karşılaştığım bazı senkronizasyon ve UI durumlarına karşı aldığım aksiyonlar:
1.  **Dropdown Optimizasyonu:** Lokasyon ve Departman filtreleri için özel üretilen Select2 tabanlı widget'ların arayüz takılmalarına karşı doğrudan arkadaki native DOM `<select>` elementleriyle etkileşime geçtim.
2.  **Dinamik Element Kontrolü:** İlanların filtrelenmesi sırasında DOM'dan silinmeyen fakat CSS ile gizlenen (display: none) ilanların Assertions aşamasında hataya yol açmaması için görünürlük (isDisplayed) filtrelemesi ekledim.
3.  **Sticky Header:** Butonlara tıklamadan önce ekranı javascript ile kaydırarak (scrollIntoView), sayfanın üstündeki sticky navigasyon barının tıklama işlemlerini engellemesinin önüne geçtim.

## 🛠️ Nasıl Çalıştırılır?

Projeyi çalıştırmak için kök dizinde aşağıdaki Maven komutunu koşmanız yeterlidir:

```bash
mvn clean test
```

## 📊 Test Raporları
Test tamamlandığında otomatik olarak ExtentReports HTML raporu oluşturulur. Sonuçları detaylı olarak kendi tarayıcınızda açıp inceleyebilirsiniz:
*   `reports/TestReport.html`

---
Vakit ayırıp incelediğiniz için teşekkür ederim. Herhangi bir sorunuz olursa bana ulaşabilirsiniz.
