package com.github.sropelinen.olioht;

public class UserDataChecker {

    /* All methods below validates different String variable, returns message if test failed */

    public String validateUserName(String userName) {
        if (userName.isEmpty()) {
            return "Add username!";
        }
        else if (!userName.matches("[A-Za-z0-9]+")) {
            return "Username can only contain letters a-z and numbers";
        } else {
            return null;
        }
    }

    public String validatePassword(String passWord) {
        if (passWord.isEmpty()) {
            return "Enter password";
        } else if (passWord.length() < 12) {
            return "Password must be 12 characters long";
        } else if (!passWord.matches(".*[a-z].*")) {
            return "Password must contain one lower case letter";
        } else if (!passWord.matches(".*[A-Z].*")) {
            return "Password must contain one upper case letter";
        } else if (!passWord.matches(".*[0-9].*")) {
            return "Password must contain one number";
        } else if (!passWord.matches(".*[^A-Za-z0-9].*")) {
            return "Password must contain one special character";
        }
        return null;
    }

    public String validateFirstName(String firstName) {
        if (firstName.isEmpty()) {
            return "Add first name";
        }
        else if (firstName.matches(".*[^A-Za-z].*")) {
            return "Name can only contain letters";
        }
        return null;
    }


    public String validateLastName(String lastName) {
        if (lastName.isEmpty()) {
            return "Add last name";
        } else if (lastName.matches(".*[^A-Za-z].*")) {
            return "Name can only contain letters";
        }
        return null;
    }

    public String validateHeight(String height) {
        if (height.isEmpty()) {
            return "Add height";
        }
        return null;
    }

    public String validateWeight(String weight) {
        if (weight.isEmpty()) {
            return "Add weight";
        }
        return null;
    }

    public String validateBirthDate(String date) {
        if (!date.matches(".*[/].*")) {
            return "Add date of birth";
        }
        return null;
    }
}
