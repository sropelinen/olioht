# Suunnitelma
Tavoitteena on luoda sovellus, joka auttaa käyttäjää pienentämään hiilijalanjälkeään. Käyttäjä pystyy kirjaamaan liikkumisensa sovellukseen. Liikkuminen erotellaan sen mukaan, onko käyttäjä kävellyt, käyttänyt ajoneuvoa, kulkenut julkisilla jne. Näillä tiedoilla lasketaan käyttäjän hiilijalanjälki käyttäen ilmastodieetin APIa (linkki alempana). Sovellus myös vertaa käyttäjän hiilijalanjälkeä aikaisempaan ja kertoo vinkkejä, kuinka sitä voisi pienentää. Käyttäjä voi seurata myös halutessaan painonsa muuttumista. Käyttäjä pystyy tarkastelemaan liikkumistaan ja painoaan tekstinä sekä erilaisina graafeina.

## Kirjautuminen
Sovellus aukeaa LoginFragmentissä. Fragmentissa voi kirjautua sisään tai siirtyä luomaan tiliä RegisterFragmentissä. Sovellus varmistaa, että käyttäjän salasana on tarpeeksi vahva.
Tilejä on useita ja niistä pidetään listaa paikallisessa tietokannassa Roomin avulla. Tietokannassa on käyttäjien nimet ja salasanat (SHA-512 + salt). Jos kirjautuminen onnistuu tai luodaan uusi tili, sovellus luo profiili-instanssin ja siirtyy MainActivityn HomeFragmenttiin.

## Profiili
Profiili on singleton-luokka, joka sisältää kaikki sisäänkirjautuneen henkilön tiedot. Tiedot luetaan salatusta json-tiedostosta, joka on tallennettu siten, että vain sovellus voi lukea ja kirjoittaa siihen. Profiili lataa tiedot DataHandler-luokan avulla. Jokaiselle käyttäjälle on oma json-tiedosto, joka toimii samalla myös logitiedostona. Tiedostossa on eroteltu jokaisena ajanhetkenä muutettu data.
```json
{
    "18721797": {
        "firstName": "Etu",
        "lastName": "Suku",
        "age": 20,
        "email": "email@email.com",
        "height": 180,
        "weight": 100,
        "home": "Helsinki"
    },
    "18721799": {
        "car": 10,
        "walk": 2,
        "weight": 102
    },
    "18721802": {
        "walk": 1,
        "home": "Varpaisjärvi"
    }
}
```

## MainActivity
MainActivity sisältää viisi fragmenttiä. Fragmenttien välillä voidaan siirtyä sivunavigaation avulla. Kun MainActivity avataan ensimmäisen kerran, se luo Profiili-olion, joka on kirjautuessa luotu instanssi. Jos käyttäjä kirjautuu ulos, profiili-instanssi tyhjennetään ja siirtytään LoginActivityn LoginFragmenttiin.

## HomeFragment
Etusivu sisältää tiedon hiilijalanjälkiarviosta kuluneelta viikolta, vertailun edelliseen viikkoon ja MessageBotin avulla luodun palautteen. Etusivulla on myös painike, jonka avulla pääsee lisäämään dataa AddTravelFragmenttiin.

## MessageBot
Sovellus luo tekstipätkiä käyttäjän syötteiden mukaan. Tekstipätkät näkyvät etusivulla. Teksti voi esimerkiksi kannustaa käyttäjää liikkumaan, jos sovellukselle on ilmoitettu paljon autoilua ja vähän kävelyä. MessageBot hyödyntää dataa (THL, Traficom), jonka avulla se voi verrata käyttäjän liikkumistottumuksia keskimääräiseen ihmiseen samassa ikäryhmässä tai samalla asuinalueella.

## AddTravelFragment
Käyttäjä lisää sovellukseen dataa teksikenttien ja painikkeiden avulla. AddTravelFragmentissä voi valita matkustustavan, päivän (kalenterilla) ja kilometrimäärän. Käyttäjä voi halutessaan päivittää tässä omaa painoaan. Päivitetyt tiedot tallennetaan profiiliin, joka tallentaa ne jsoniin.

## XMLParser
XMLParser on singleton-luokka, jonka tarkoitus on ladata tietoa valitusta rajapinnasta. Lataaminen tapahtuu vain AddTravelFragmentin datan lisäyksen jälkeen.

https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/CarEstimate

https://ilmastodieetti.ymparisto.fi/ilmastodieetti/calculatorapi/v1/TransportCalculator/PublicTransportEstimate

Tiedon lataaminen tapahtuu asynkronoidusti, joten getCO2Estimatessa siirrytään toiseen threadiin. Kun arvio on laskettu, arvio tallennetaan profiiliin, joka tallentaa ne jsoniin. Tämän jälkeen kutsutaan MainActivityn refresh() Handlerin ja Contextin avulla. refresh() päivittää HomeFragmentin ja ChartsFragmentin tiedot.

## SettingsFragment
Asetukset sisältää sovelluksen ulkoasuun ja käyttämiseen liittyviä asetuksia. Asetukset tallennetaan ViewModelin avulla, jotta kaikki MainActivityn fragmentit näkevät ne. Asetuksien kautta pystyyy siirtyä muokkaamaan käyttäjän perustietoja ja tarkastella logia json muodossa tekstikentässä.

## AccountFragment
Tilin asetuksissa käyttäjä voi muuttaa omia perustietojaan, kuten ikää, pituutta, painoa ja paikkakuntaa.

## ChartsFragment
Sovelluksessa on oma sivu kuvaajille, joista näkyy käyttäjän tietoja ajan suhteen. Tietojen aikaväliä voi muuttaa esimerkiksi spinnerin avulla. Kuvaajan alla on tekstinä lisätietoa aikavälin tiedoista.

## Kuvat
![UML](/uml-v2.png?raw=true)

UML:ssä ei ole mainittu kaikkia attribuutteja/metodeja yksityiskohtaisesti, muuttuvat projektin edetessä.

![Ulkoasu](/gui.png?raw=true)

Ensimmäiset versiot, saattavat muuttua projektin edetessä.
