package com.github.sropelinen.olioht;

public class UserDataChecker {

    // ToDo Tekstit pitää olla strings.xml tiedostossa
    public String validateUserName(String userName) {
        if (userName.isEmpty()) {
            return "Syötä käyttäjänimi";
        }
        else if (!userName.matches("[A-Za-z0-9]+")) {
            return "Nimi voi sisältää vain kirjaimia a-z ja numeroita";
        } else {
            return null;
        }
    }

    public String validatePassword(String passWord) {
        if (passWord.isEmpty()) {
            return "Syötä Salasana";
        } else if (passWord.length() < 12) {
            return "Salasanan pitää olla vähintään 12 merkkiä pitkä";
        } else if (!passWord.matches(".*[a-z].*")) {
            return "Salasanan pitää sisältää vähintään yksi pieni kirjain";
        } else if (!passWord.matches(".*[A-Z].*")) {
            return "Salasanan pitää sisältää vähintään yksi iso kirjain";
        } else if (!passWord.matches(".*[0-9].*")) {
            return "Salasanan pitää sisältää vähintään yksi numero";
        } else if (!passWord.matches(".*[^A-Za-z0-9].*")) {
            return "Salasanan pitää sisältää vähintään yksi erikoismerkki";
        }
        return null;
    }

    public String validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            return "Syötä etunimi";
        }
        else if (firstName.matches(".*[^A-Za-z].*")) {
            return "Etunimen täytyy sisältää vain kirjaimia";
        }
        return null;
    }


    public String validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            return "Syötä etunimi";
        } else if (lastName.matches(".*[^A-Za-z].*")) {
            return "Sukunimen täytyy sisältää vain kirjaimia";
        }
        return null;
    }

    public String validateHeight(String height) {
        if (height.isEmpty()) {
            return "Syötä pituus";
        }
        return null;
    }

    public String validateWeight(String weight) {
        if (weight.isEmpty()) {
            return "Syötä paino";
        }
        return null;
    }

    public String validateBirthDate(String date) {
        if (!date.matches(".*[/].*")) {
            return "Syötä syntymäaika";
        }
        return null;
    }
}
