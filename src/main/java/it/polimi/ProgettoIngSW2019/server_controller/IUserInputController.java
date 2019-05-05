package it.polimi.ProgettoIngSW2019.server_controller;

public interface IUserInputController {

    void move(int idPlayer);

    void shoot(int idPlayer, int idCard);

    void grabStuff(int xSquare, int ySquare);

    void reload(int idPlayer, int idCard);


}
