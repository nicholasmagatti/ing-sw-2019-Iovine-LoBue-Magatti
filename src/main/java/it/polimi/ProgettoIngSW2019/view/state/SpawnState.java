package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

import java.util.List;
import java.util.Scanner;

public class SpawnState extends Observable<UserInputMessage> implements IState{
    Scanner scanner;
    List<Integer> idPowerUp;
    String yesnoChoice;
    int powerupChoice;

    @Override
    public void menu(StateContext stateContext) {
        //Warn the server that the player is spawning and get from it the knowledge that the power up has been drawn
        notify(new SpawnMessage());
        System.out.println("Questi sono i power up che hai in mano.");
        for(Integer id: idPowerUp){
            //TODO: mostrare il nome carta
            System.out.println(id);
        }
        System.out.println("Vuoi vedere gli effetti prima di scartare (y/n)?");
        yesnoChoice = scanner.nextLine();

        if(yesnoChoice.equalsIgnoreCase("y")){
            for(Integer id: idPowerUp){
                //TODO: mostrare il nome carta
                showPowerUpInfo(id);
            }
        }

        System.out.println("Scegli la carta da scartare, il colore della stanza indica il punto di respawn: ");
        for(Integer id: idPowerUp){
            //TODO: mostrare il nome carta e colore dell'ammo
            System.out.println(id);
        }
        powerupChoice = scanner.nextInt();

        //TODO: bisognerà creare il messaggio
        notify(new SpawnMessage());
    }

    private void showPowerUpInfo(int idPowerUp){

    }
}
