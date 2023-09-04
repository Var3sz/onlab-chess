# Budapesti Műszaki és Gazdaságtudományi Egyetem

## Önálló labor (BMEVIAUAL01)

### Többszereplős sakk játék Android platformra


### Konzulens:

Dr. Ekler Péter

### Csapattagok:

Varga Ádám Marcell E22H8P

Waldmann Tamás Róbert EO229S

# Tartalomjegyzék

[1. Feladatleírás]

[2. Feladatspecifikáció]

[2.1. Az alkalmazás felépítése]

[2.1.1. Technikai megfontolások]

# 1. Feladatleírás

A félév során egy sakk játékot próbálunk meg elkészíteni Android platformra. A felhasználóknak (avagy játékosoknak) lehetőségük lesz különböző játékmódokban összemérni a tudásukat akár az online akár az offline térben.

# 2. Feladatspecifikáció

### 2.1. Az alkalmazás felépítése

Az alkalmazás több képernyőből ([2.2. Képernyők](#_sgpbq5yniagt)) fog állni, melyekhez többnyire eltérő funkciók fognak társulni.

Az alkalmazás futtatásakor mindig egy Welcome screen ([2.2.1.](#_1khwmu4fgclo)) fog megjelenni, ahonnan az általunk választott opció alapján vagy a Login (2.2.3.) vagy pedig a Registration (2.2.2.) képernyőre tudunk navigálni. Ez alól kivételt képez azon eset, amikor a felhasználó kijelentkezés nélkül zárja be az alkalmazást, ugyanis ebben az esetben egyből a "Main menu" képernyőn fog elindulni az alkalmazás. A Login képernyőről el lehet navigálni egy Forgotten password (2.2.4.) képernyőre, ahol az e-mail cím segítségével lehet jelszó emlékeztetőt kérni.

Registration opció választása esetén különböző adatokat kér az alkalmazás, ahol szabályok vannak megadva, például a jelszó hosszát érintően. Sikeres regisztráció esetén (erről pop-up message segítségével értesít az alkalmazás), a Login képernyőre kerülünk, ahonnan sikeres bejelentkezést követően juthatunk el a Main menu képernyőre.

A Main menu (2.2.5.) képernyő esetén a bal felső sarokban található ikon kijelentkezést tesz lehetővé és átirányítja a felhasználót a Welcome Screen-re. A jobb felső sarokban található ikon a Profile képernyőre (2.2.6.) irányít át, ahol a játékos adatai találhatók. A fiók adatait a jobb felső sarokban található ikonnal lehet szerkeszteni és törlés is lehetséges. Előbbit egy felugró ablakban tehetjük meg. A menüben található opciók közül az első három a különböző játékmódokkal való játékot tesz lehetővé. Ennek általános felépítését a Game képernyőn (2.2.7.) lehet látni.

A Friends képernyőn (2.2.8.) a bejelentkezett felhasználó barátlistája található, melyek közül barátot a nevük melletti ikonra kattintva lehet törölni. Ehhez egy megerősítés szükséges, melyre egy pop-up ablak szolgál.

Végül a Stats képernyő (2.2.9.) funkcióját tekintve arra szolgál, hogy elérhetővé tegye a bejelentkezett játékos számára azt az információt, hogy hány meccset játszott eddig és ebből hányszor nyert, illetve vesztett és a győzelmeinek aránya is nyilván van tartva.

#### 2.1.1. Technikai megfontolások

Az alkalmazást képernyőit és azok közötti navigációt Jetpack Compose használatával fogjuk megvalósítani. A 3D-s játék megvalósításához ARCore-t fogunk használni. Az autentikációt, a játékosok statisztikájának elmentését és a multiplayer játékmód megvalósítását Firebase segítségével fogjuk megvalósítani. Egy adott felhasználó barátainak listájához LazyColumn-t fogunk alkalmazni. Mindezek mellett REST API-t is használni fogunk, például a különböző sakkfeladványok eléréséhez.