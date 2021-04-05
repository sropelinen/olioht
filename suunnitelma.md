# Suunnitelma
Tavoitteena on luoda sovellus, joka seuraa käyttäjän liikkumista. Liikkuminen erotellaan, sen mukaan, onko käyttäjä kävellyt, käyttänyt ajoneuvoa, kulkenut julkisilla jne. Näillä tiedoilla lasketaan käyttäjän hiilijalanjälkeä. Käyttäjä voi seurata myös halutessaan painonsa muuttumista.

## Kirjautuminen
Solvellus aukeaa LoginFragmentissä. Fragmentissa voi kirjautua sisään tai siirtyä luomaan tiliä RegisterFragmentissä. Tiliä luodessa käyttäjältä kysytään perustietoja, kuten ikä, paino ja pituus. Sovellus varmistaa, että käyttäjän salasana on tarpeeksi vahva.
Tilejä on useita ja niistä pidetään listaa paikallisessa tietokannassa Roomin avulla. Tietokannassa on käyttäjien nimet ja salasanat (SHA-512 + salt). Jos kirjautuminen onnistuu tai luodaan uusi tili, sovellus luo Profiili-instanssin ja siirtyy MainActivityn HomeFragmenttiin.

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
