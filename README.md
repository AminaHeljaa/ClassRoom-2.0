# 🎓 ClassRoom 2.0 - Interaktivna Platforma za Upravljanje Nastavom

ClassRoom 2.0 je Android aplikacija kreirana u Jetpack Compose-u, osmišljena kao zatvoreni sistem za jednu školu. Aplikacija omogućava profesorima digitalno vođenje nastave, generisanje QR prisustva, kreiranje kvizova uživo i uvid u anonimne povratne informacije, dok studentima pruža prostor za praćenje nastave i interaktivno učenje uz AI asistenciju.

---

## 🛠️ Napomena o statusu razvoja (Pročitati prije pokretanja!)
* **Autentifikacija:** Integracija sa Google i Facebook prijavom je trenutno u zastoju (steka) i nije u potpunosti funkcionalna. Sistem se oslanja na internu registraciju.
* **Ograničenja baze podataka:** Zbog trenutnog nedostatka namjenskog cloud skladišta (Storage/Baza za fajlove), opcije za *upload materijala (prezentacija/dokumenata)*, *prilaganje zadaća od strane studenata* i *spasavanje profilne slike* su vizuelno implementirane u UI-ju, ali nemaju pozadinsku bazu za trajno čuvanje fajlova.

---

## 🚀 Funkcionalnosti Aplikacije

### 🔐 1. Registracija, Uloge i Pametno Pamćenje Sesije
* **Pristupni kodovi škole:** Prilikom prve registracije, korisnik unosi svoje podatke i bira ulogu unosom jedinstvenog koda ustanove:
  * Profesor pristupa aplikaciji uz kod: **`PROF2026`**
  * Student pristupa aplikaciji uz kod: **`STUD2026`**
* **Automatska prijava (Session Persistence):** Nakon što se jednom prijavi i izabere ulogu, aplikacija pamti korisnika. Prilikom ponovnog pokretanja, sistem automatski prepoznaje da li je u pitanju student ili profesor, preskačući ekrane za odabir.

### 📚 2. Upravljanje Predmetima (Navigaciona Traka)
* **Profesor dashboard:** Može kreirati jedan ili više predmeta, a aplikacija za svaki predmet generiše **nasumični jedinstveni kod**. Profesor takođe ima mogućnost brisanja predmeta.
* **Student dashboard:** Student se na predmete povezuje tako što unosi kod predmeta koji je dobio od profesora.

### 📸 3. Pametno QR Prisustvo u Realnom Vremenu
* Profesor unutar predmeta generiše QR kod koji **važi tačno 5 minuta**.
* Podatak o vremenu se upisuje u bazu, što omogućava da kod ostane aktivan i validan za studente čak i ako se profesor u međuvremenu zaključa telefon ili se odjavi iz aplikacije.

### 🏆 4. Live Kvizovi, E-Dnevnik i Rang Liste
* **Kreiranje kviza:** Profesor unosi naziv kviza, piše pitanja sa 4 ponuđena odgovora (od kojih je jedan tačan) i postavlja vremensko ograničenje.
* **Rang lista:** Studenti rade kviz, a njihovi rezultati se u realnom vremenu šalju na zajedničku rang listu.
* **E-Dnevnik (Profesor):** Ima uvid u statistiku studenta (broj urađenih kvizova, bodovi sa prethodnog kviza, ukupni bodovi). Profesor može direktno unijeti konačnu ocjenu, datum i komentar.
* **E-Dnevnik (Student):** Student transparentno vidi sve svoje ostvarene bodove, ocjene i komentare koje mu je profesor dodijelio.

### 🤖 5. Groq AI Asistent & Random Odabir
* **Groq AI integracija:** Budući da ostali AI modeli (poput zvaničnog Gemini-ja) imaju geografska ograničenja u Bosni i Hercegovini, unutar aplikacije je uspješno integrisan **Groq API**. Služi kao pametni asistent za pomoć studentima pri učenju u realnom vremenu.
* **Nasumičan odabir studenta:** Profesor ima funkciju "Random odabir" koja nasumično generiše i bira studenta sa predmeta za odgovaranje ili aktivnost.

### 📬 6. Anonimni Feedback & Zadaci
* **Zadaće:** Profesor može dodijeliti zadaću za određeni predmet (naslov i detaljan opis) i obrisati je po potrebi. Zadaća se odmah prikazuje studentu (uz napomenu o bazi za prilaganje fajlova).
* **Anonimne pohvale i žalbe:** Student može napisati feedback za profesora. Poruke stižu profesoru potpuno **anonimno**, omogućavajući iskrenu evaluaciju nastave.

### 🎨 7. Korisnički Profil & Tamni Režim (Dark Mode)
* **Uređivanje profila:** Korisnik može izmijeniti svoje ime (opcija za sliku je spremna u UI-ju).
* **Kompletan Dark Mode:** Kroz navigaciju je omogućen prelazak na tamni režim rada, pri čemu se kompletan vizuelni interfejs aplikacije prilagođava tamnoj temi za ugodniji rad noću.

---

## 🛠️ Tehnologije i Alati

* **Programski jezik:** Kotlin
* **UI Framework:** Jetpack Compose (Moderni deklarativni UI sa podrškom za Light/Dark teme)
* **Arhitektura:** MVVM
* **Baza podataka:** Firebase Firestore (za tekstualne podatke, kvizove, ocjene i QR sesije)
* **Skeniranje:** Google ML Kit Code Scanner
* **AI Integracija:** Groq API Key (implementiran u `GeminiManager.kt`)
