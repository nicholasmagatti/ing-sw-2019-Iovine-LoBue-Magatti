package it.polimi.ProgettoIngSW2019.model;

public class User {

    private String username;
    private Player player;

    //to ask for connetion (first thing the user does)
    public void getConnection(){

    }

    public void startGame(){

    }

    public Player getPlayer() {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        Player temporaryVariable = new Player();
        return temporaryVariable;
    }
}
