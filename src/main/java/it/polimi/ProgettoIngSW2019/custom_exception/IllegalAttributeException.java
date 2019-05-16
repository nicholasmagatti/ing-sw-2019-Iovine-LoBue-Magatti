package it.polimi.ProgettoIngSW2019.custom_exception;

public class IllegalAttributeException extends RuntimeException {

    public IllegalAttributeException(String s) {
        System.out.println(s);
    }
}
