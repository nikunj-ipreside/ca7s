package com.music.ca7s.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppValidation {


    static Pattern pattern;
    static Matcher matcher;

    //EmptyFieldValidation
    public static boolean isEmptyFieldValidate(String strField) {
        boolean isEmptyField = false;
        if (strField.length() > 0)
            isEmptyField = true;
        return isEmptyField;
    }

    //BirthDateValidation
    public static boolean isBirthDateValidate(String birthdate) {
        boolean isValid = false;

        if (birthdate.matches(AppConstants.birthDatePattern)) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }


    //EmailValidation
    public static boolean isEmailValidate(String email) {
        boolean isValid = false;

        if (email.matches(AppConstants.emailPattern)) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

    //PasswordValidation
    public static boolean isPasswordValidate(String password) {
        pattern = Pattern.compile(AppConstants.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    //NumberValidation
    public static boolean isNumberValidate(String number) {
        boolean isValid = false;
        if (number.length() > 0 && number.length() == 10) {
            isValid = true;
        } else {
            isValid = false;
        }
        return isValid;
    }

}
