package it.polimi.ProgettoIngSW2019.custom_exception;

public class NotPartOfBoardException extends RuntimeException {

    public NotPartOfBoardException(String s) {
        System.out.println(s);
    }
}
