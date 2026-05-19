ClassRoom 2.0 - Interaktivna Platforma za Učenje
ClassRoom 2.0 je moderna Android aplikacija dizajnirana da olakša interakciju između profesora i studenata kroz digitalizaciju prisustva, kvizove uživo i AI asistenciju.
1. Kako pokrenuti aplikaciju
Da biste uspješno pokrenuli aplikaciju lokalno, pratite ove korake:
1.
Klonirajte repozitorij:
Shell Script
git clone https://github.com/vash-username/ClassRoom20.git
2.
Otvorite projekt: Otvorite projekt u Android Studio (Ladybug ili noviji).
3.
Firebase Konfiguracija:
◦
Preuzmite svoj google-services.json sa Firebase konzole.
◦
Postavite ga u app/ direktorij projekta.
◦
Omogućite Authentication (Email/Password, Google, Facebook) i Firestore Database u Firebase konzoli.
4.
Google/Facebook API Ključevi:
◦
U datoteci app/src/main/res/values/strings.xml zamijenite placeholder-e (default_web_client_id, facebook_app_id, facebook_client_token) svojim stvarnim ključevima koje ste dobili u Google
Cloud i Facebook Developer konzolama.
5.
Pokretanje: Povežite Android uređaj ili pokrenite emulator i kliknite na Run u Android Studiju.
2. Opis implementiranih rješenja
Aplikacija nudi niz modernih rješenja za pametnu učionicu:
•
Napredna Autentifikacija: Podržava klasičnu registraciju, kao i brzu prijavu putem Google i Facebook naloga. Sistem automatski kreira korisničke profile u Firestore bazi podataka bez potrebe za ručnom registracijom nakon prve prijave.
•
Pametno QR Prisustvo: Profesor generiše dinamički QR kod sa vremenskim ograničenjem. Podaci o sesiji se čuvaju u oblaku, što omogućava studentima prijavu čak i ako se profesor odjavi. Sistem uključuje sinhronizaciju vremena, toleranciju na neusklađene satove i strogu zaštitu od duplog skeniranja.
•
Kviz Uživo sa Rang Listom: Interaktivni kvizovi sa preciznim tajmerom u formatu minuta i sekundi. Rezultati se automatski pretvaraju u ocjene i upisuju u E-Dnevnik, sprečavajući duple unose za isti kviz.
•
Gemini AI Asistent: Integrisan Google-ov AI model (Gemini) koji služi kao personalni asistent za učenje, pomažući u rješavanju akademskih pitanja u realnom vremenu.

Digitalni E-Dnevnik i Materijali: Centralizovano mjesto za profesore za pregled studenata i dodjeljivanje ocjena, uz sistem za upload materijala i slanje rješenja zadaća direktno na Firebase Storage.
3. Tehnologije i AI alati
Korištene Tehnologije:
•
Programski jezik: Kotlin
•
UI Framework: Jetpack Compose (Moderni deklarativni UI)
•
Backend: Firebase (Auth, Firestore Database, Cloud Storage)
•
Skeniranje: Google ML Kit Code Scanner
•
Biblioteke: Coil (za slike), Navigation Compose, Credential Manager, Facebook SDK.
AI Alati:
•
Gemini AI API: Korišten za implementaciju pametnog AI asistenta unutar aplikacije.
•
Android Studio AI Bot: Korišten tokom razvoja za pisanje čistijeg koda, optimizaciju UI layout-a i bržu integraciju kompleksnih Firebase funkcija.
