package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.client_controller.*;

public class PlayerChoiceState extends Observable<UserInputEvent> implements IState {
    int choice;
    /*
        Coordinates to locate positions decided by the user
     */
    int x;
    int y;

    @Override
    public void menu() {

    }

    private void resolvePlayerRequest(int choice){

    }

    private String showCardOnCurrentSquareInfo(){
        //TODO: idealmente l'utente chiede di raccogliere, allora gli viene mostrato cosa c'è sul quadrato in cui si trova e,
        //TODO: nel caso siano delle armi, gli chiede quale delle 3 vuole.
        return "";
    }

    private String showPossibleTarget(int idWeaponCard){
        return "";
    }

    private String showWeaponInfo(int idWeaponCard){
        return "";
    }

    private String showPowerUpInfo(int idPowerUpCard){
        return "";
    }
}
