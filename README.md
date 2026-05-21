# 🎓 ClassRoom 2.0 – Interaktivna Platforma za Upravljanje Nastavom

<p align="center">
  <img src="screenshots/intro.png" width="220"/>
</p>

<p align="center">
Modern Android aplikacija za digitalno upravljanje nastavom, QR prisustvo, live kvizove, AI asistenciju i interaktivno učenje u realnom vremenu.
</p>

---

# 🚀 O Projektu

ClassRoom 2.0 je Android aplikacija razvijena u Kotlinu koristeći Jetpack Compose, kreirana kao zatvoreni sistem za škole i fakultete.

Aplikacija omogućava profesorima:

* digitalno vođenje nastave,
* QR evidenciju prisustva,
* live kvizove,
* pregled statistike i rezultata,
* anonimni feedback studenata,
* AI podršku tokom nastave.

Studentima omogućava:

* brzo prisustvo putem QR skeniranja,
* interaktivne kvizove,
* pregled ocjena i bodova,
* AI pomoć pri učenju,
* praćenje predmeta i zadataka.

---

# 📱 Screenshots

## 🔐 Autentifikacija i Uvod

<p align="center">
  <img src="screenshots/intro.png" width="220"/>
  <img src="screenshots/loginpage.png" width="220"/>
  <img src="screenshots/registerPage.png" width="220"/>
  <img src="screenshots/roleSelection.png" width="220"/>
</p>

---

## 👨‍🏫 Profesor Dashboard & Student Dashboard

<p align="center">
  <img src="screenshots/profesorDashBoard.png" width="220"/>
  <img src="screenshots/StudentDashboard.png" width="220"/>
  <img src="screenshots/dashboard2.png" width="220"/>
  <img src="screenshots/DashboardDark.png" width="220"/>
</p>

---

## 📸 QR Prisustvo i Random Odabir

<p align="center">
  <img src="screenshots/QRprisustvo.png" width="220"/>
  <img src="screenshots/RandomPicker.png" width="220"/>
</p>

---

## 🏆 Live Kvizovi i Rang Liste

<p align="center">
  <img src="screenshots/LiveQuy.png" width="220"/>
  <img src="screenshots/RangList.png" width="220"/>
  <img src="screenshots/E-dnevnik.png" width="220"/>
</p>

---

## 💬 Feedback & Profil

<p align="center">
  <img src="screenshots/Feedback.png" width="220"/>
  <img src="screenshots/profilePage.png" width="220"/>
  <img src="screenshots/profileDark.png" width="220"/>
</p>

---

# ✨ Glavne Funkcionalnosti

## 🔐 Registracija i Uloge

Prilikom registracije korisnik bira svoju ulogu unosom pristupnog koda ustanove:

* Profesor → `PROF2026`
* Student → `STUD2026`

Aplikacija automatski pamti sesiju korisnika i omogućava automatsku prijavu pri ponovnom pokretanju aplikacije.

---

## 📚 Upravljanje Predmetima

### Profesor može:

* kreirati više predmeta,
* generisati jedinstvene kodove predmeta,
* brisati predmete,
* upravljati studentima.

### Student može:

* pridružiti se predmetu putem koda,
* dodavati nove predmete,
* pregledati aktivne predmete.

---

## 📸 QR Prisustvo u Realnom Vremenu

Profesor generiše QR kod koji traje 5 minuta.

Student:

* skenira QR kod,
* potvrđuje prisustvo,
* automatski dobija evidenciju u sistemu.

Sistem koristi:

* Google ML Kit,
* Firebase Firestore,
* vremensku validaciju QR sesije.

---

## 🏆 Live Kvizovi & E-Dnevnik

Profesor može:

* kreirati kvizove,
* dodavati pitanja i odgovore,
* pratiti rezultate u realnom vremenu.

Student može:

* rješavati kvizove,
* pratiti osvojene bodove,
* pregledati rang listu i rezultate.

### E-Dnevnik omogućava:

* pregled ocjena,
* statistiku bodova,
* komentare profesora,
* pregled prethodnih aktivnosti.

---

## 🤖 Groq AI Asistent

Aplikacija koristi Groq API za AI pomoć studentima tokom učenja.

AI asistent pomaže pri:

* objašnjavanju pojmova,
* učenju u realnom vremenu,
* generisanju odgovora i pomoći tokom rada.

---

## 📬 Zadaci, Feedback i Materijali

### Zadaci

Profesor može:

* dodati zadaću,
* napisati naslov i opis,
* obrisati zadatak.

### Anonimni Feedback

Student može poslati:

* pohvalu,
* prijedlog,
* žalbu.

Feedback ostaje anoniman.

### Materijali

UI za materijale je implementiran i spreman za buduću Firebase Storage integraciju.

---

# 🌙 Dark Mode

Aplikacija podržava:

* Light Mode
* Dark Mode

Kompletan interfejs automatski se prilagođava tamnoj temi.

---

# 🛠️ Tehnologije i Alati

| Tehnologija             | Opis                   |
| ----------------------- | ---------------------- |
| Kotlin                  | Programski jezik       |
| Jetpack Compose         | Moderni Android UI     |
| Firebase Firestore      | Cloud baza podataka    |
| Firebase Authentication | Registracija i prijava |
| Google ML Kit           | QR skeniranje          |
| Groq API                | AI Asistent            |
| Navigation Compose      | Navigacija             |
| Material 3              | Moderni dizajn         |
| Coil                    | Učitavanje slika       |

---

# 🧱 Struktura Projekta

```plaintext
com.example.classroom20
│
├── screens
├── navigation
├── components
├── models
├── firebase
├── utils
├── ui.theme
└── managers
```

> Projekat trenutno nema potpuno implementiranu MVVM arhitekturu kroz cijeli sistem. Kod je organizovan kroz screenove, helper klase i Firebase managere radi lakšeg održavanja i buduće nadogradnje.

---

# ⚠️ Trenutna Ograničenja

* Google/Facebook login nije potpuno funkcionalan
* Firebase Storage još nije implementiran
* Upload fajlova trenutno nema trajno cloud spremanje
* Profilne slike su samo UI funkcionalnost
* Materijali trenutno ne povlače stvarne dokumente

---

# ⚙️ Pokretanje Projekta

## 1️⃣ Kloniranje projekta

```bash
git clone https://github.com/AminaHeljaa/ClassRoom-2.0.git
```

---

## 2️⃣ Otvaranje u Android Studiju

* Android Studio Ladybug ili noviji
* Open Project
* Sačekati Gradle Sync

---

## 3️⃣ Firebase konfiguracija

Potrebno je:

* kreirati Firebase projekat,
* dodati Android aplikaciju,
* preuzeti `google-services.json`,
* ubaciti fajl u `app/` folder.

### Omogućiti:

* Firebase Authentication
* Firestore Database

---

## 4️⃣ Pokretanje aplikacije

* Pokrenuti emulator ili fizički uređaj
* Kliknuti ▶ Run

### Kodovi za registraciju:

* Profesor → `PROF2026`
* Student → `STUD2026`

---

# 🔮 Buduće Nadogradnje

Planirane funkcionalnosti:

* Firebase Storage integracija
* Upload PDF i PowerPoint materijala
* Push notifikacije
* Potpuna MVVM arhitektura
* Google/Facebook autentifikacija
* Statistika prisustva
* Napredniji AI tutor sistem

---

# 👩‍💻 Autor

Razvila:

### **Amina Helja**

Android aplikacija razvijena kao projekat koristeći:

* Kotlin
* Jetpack Compose
* Firebase
* Groq AI Integraciju

---

# 📄 Licenca

MIT License
