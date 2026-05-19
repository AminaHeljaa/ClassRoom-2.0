# 🎓 ClassRoom 2.0 - Interaktivna Platforma za Upravljanje Nastavom

ClassRoom 2.0 je Android aplikacija kreirana u Jetpack Compose-u, osmišljena kao zatvoreni sistem za jednu školu. Aplikacija omogućava profesorima digitalno vođenje nastave, generisanje QR prisustva, kreiranje kvizova uživo i uvid u anonimne povratne informacije, dok studentima pruža prostor za praćenje nastave i interaktivno učenje uz AI asistenciju.

---

## 🛠️ Napomena o statusu razvoja (Pročitati prije pokretanja!)
* **Autentifikacija:** Integracija sa Google i Facebook prijavom je trenutno u zastoju (steka) i nije u potpunosti funkcionalna. Sistem se oslanja na internu registraciju. Korisnik se ne može prijaviti prije nego što se uspješno registruje unutar aplikacije.
* **Ograničenja baze podataka:** Zbog trenutnog nedostatka namjenskog cloud skladišta (Storage/Baza za fajlove), opcije za *upload materijala (prezentacija/dokumenata)*, *prilaganje zadaća od strane studenata* i *spasavanje profilne slike* su vizuelno implementirane u UI-ju, ali nemaju pozadinsku bazu za trajno čuvanje fajlova. **Isti slučaj je i sa posebnom karticom za "Materijale" unutar predmeta, koja je dizajnirana i spremna u interfejsu, ali trenutno ne povlači prave datoteke sa servera.**

---

## 🚀 Funkcionalnosti Aplikacije

### 🔐 1. Registracija, Uloge i Pametno Pamćenje Sesije
* **Pristupni kodovi škole:** Prilikom prve registracije, korisnik unosi svoje podatke i bira ulogu unosom jedinstvenog koda ustanove:
  * Profesor pristupa aplikaciji uz kod: **`PROF2026`**
  * Student pristupa aplikaciji uz kod: **`STUD2026`**
* **Automatska prijava (Session Persistence):** Nakon što se jednom prijavi i izabere ulogu, aplikacija pamti korisnika. Prilikom ponovnog pokretanja, sistem automatski prepoznaje da li je u pitanju student ili profesor, preskačući ekrane za odabir uloge i ponovnu prijavu.

### 📚 2. Upravljanje Predmetima (Navigaciona Traka)
* **Profesor dashboard:** Može kreirati jedan ili više predmeta, a aplikacija za svaki predmet generiše **nasumični jedinstveni kod**. Profesor takođe ima mogućnost brisanja predmeta.
* **Student dashboard:** Student se na predmete povezuje tako što unosi kod predmeta koji je dobio od profesora. Kroz navigaciju na stranici predmeta, student može na osnovu novih kodova dodavati još predmeta u svoj raspored.

### 📸 3. Pametno QR Prisustvo u Realnom Vremenu
* Profesor unutar predmeta generiše QR kod koji **važi tačno 5 minuta**.
* Podatak o vremenu se upisuje u bazu, što omogućava da kod ostane aktivan i validan za studente čak i ako se profesor u međuvremenu odjavi iz aplikacije ili zaključa telefon. Student ima tačno 5 minuta da skenira kod.

### 🏆 4. Live Kvizovi, E-Dnevnik i Rang Liste
* **Kreiranje kviza:** Profesor unosi naziv kviza, piše pitanja sa 4 ponuđena odgovora (od kojih je jedan tačan) i postavlja vremensko ograničenje za rad.
* **Rang lista:** Studenti rade kviz, a njihovi rezultati se u realnom vremenu šalju na zajedničku rang listu kako bi profesor imao uvid u to koliko je ko bodova ostvario.
* **E-Dnevnik (Profesor):** Ima uvid u statistiku studenta (koliko je ukupno kvizova uradio, bodovi sa prethodnog kviza, ukupni bodovi). Profesor može direktno unijeti konačnu ocjenu, datum i komentar.
* **E-Dnevnik (Student):** Student transparentno u svom e-dnevniku vidi sve svoje ostvarene bodove na kvizovima, ocjene i komentare koje mu je profesor dodijelio.

### 🤖 5. Groq AI Asistent & Random Odabir
* **Groq AI integracija:** Budući da ostali AI modeli (poput zvaničnog Gemini-ja) imaju geografska ograničenja u Bosni i Hercegovini, unutar aplikacije je uspješno integrisan **Groq API** (preuzet sa Groq stranice). Služi kao pametni asistent za pomoć studentima pri učenju u realnom vremenu.
* **Nasumičan odabir studenta:** Profesor ima funkciju "Random odabir" koja nasumično generiše i bira studenta sa predmeta za odgovaranje ili aktivnost na času.

### 📬 6. Anonimni Feedback, Zadaci & Materijali
* **Zadaće:** Profesor može dodijeliti zadaću za određeni predmet (piše naslov i detaljan opis) i obrisati je po potrebi. Zadaća se odmah prikazuje studentu (uz napomenu o nedostatku baze za slanje fajlova sa studentske strane).
* **Anonimne pohvale i žalbe:** Student može napisati feedback za profesora koji se sastoji od pohvale i žalbe. Ove poruke stižu profesoru potpuno **anonimno**, omogućavajući iskrenu i bezbjednu evaluaciju nastave.
* **Kartica Materijali:** Unutar svakog predmeta kreirana je posebna kartica namijenjena za pregled nastavnih materijala (poput prezentacija i dokumenata), koja služi kao spremna UI struktura za buduću nadogradnju sistema.

### 🎨 7. Korisnički Profil & Tamni Režim (Dark Mode)
* **Uređivanje profila:** Korisnik kroz profilnu karticu može izmijeniti svoje ime (opcija za dodavanje profilne slike je vizuelno spremna u UI-ju).
* **Kompletan Dark Mode:** Kroz navigaciju i profil je omogućen prelazak na tamni režim rada, pri čemu se kompletan vizuelni interfejs svih stranica unutar aplikacije prilagođava tamnoj temi za ugodniji rad noću.

---

## 🛠️ Tehnologije i Alati

* **Programski jezik:** Kotlin
* **UI Framework:** Jetpack Compose (Moderni deklarativni UI sa podrškom za Light/Dark teme)
* **Arhitektura:** MVVM (Model-View-ViewModel)
* **Baza podataka:** Firebase Firestore (za tekstualne podatke, kvizove, ocjene i QR sesije)
* **Skeniranje:** Google ML Kit Code Scanner
* **AI Integracija:** Groq API Key (implementiran u `GeminiManager.kt`)

---

## ⚙️ Kako pokrenuti aplikaciju lokalno

Da biste uspješno pokrenuli aplikaciju na svom računaru, pratite sljedeće korake:

### 1. Klonirajte repozitorij
Kopirajte ovu komandu u vaš terminal kako biste preuzeli izvorni kod projekta:
```bash
git clone [https://github.com/AminaHeljaa/ClassRoom-2.0.git](https://github.com/AminaHeljaa/ClassRoom-2.0.git)
2. Otvorite projekt u Android Studiju
Pokrenite Android Studio (verzija Ladybug ili novija).

Izaberite opciju Open i pronađite folder projekta koji ste upravo klonirali (ClassRoom20).

Sačekajte par minuta da se završi sinhronizacija i indeksiranje projekta (Gradle Sync).

3. Podešavanje Firebase Konfiguracije
Budući da se aplikacija oslanja na Firebase za bazu podataka i autentifikaciju, potrebno je da povezujete sopstveni Firebase projekt:

Otvorite Firebase Konzolu i kreirajte novi projekt.

Dodajte Android aplikaciju u okviru projekta (unesite vaš paket name, npr. com.example.classroom20).

Preuzmite konfiguracijsku datoteku google-services.json.

Kopirajte preuzeti fajl i zalijepite ga direktno u app/ direktorij vašeg projekta unutar Android Studija.

U okviru Firebase konzole, ručno omogućite sljedeće usluge:

Authentication: Uključite Email/Password metodu prijave.

Firestore Database: Pokrenite bazu podataka u testnom režimu.

4. Pokretanje na uređaju
Omogućite USB Debugging na svom stvarnom Android uređaju i povežite ga kablom sa računarom, ili pokrenite virtuelni uređaj (Emulator) u Android Studiju.

Kliknite na zeleno dugme Run (Pusti) u gornjoj alatnoj traci Android Studija (zeleni trougao ▶).

Pristup aplikaciji: Prilikom prve registracije na ekranu, unesi kod ustanove kako bi aplikacija prepoznala tvoju ulogu:

Za profesorski nalog koristi kod: PROF2026

Za studentski nalog koristi kod: STUD2026