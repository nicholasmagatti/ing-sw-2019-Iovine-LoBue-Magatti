package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;

import java.util.Scanner;

public class SetupGameState extends Observable<Event> implements IState {
    @Override
    public void menu(StateContext stateContext) {
        Scanner scanner = new Scanner(System.in);
        int mapChoice;
        int skullChoice;
        boolean checkAnswer = false;

        System.out.println("###########################################\n");
        System.out.println("Ma che piacevole sorpresa, quindi tu saresti il primo giocatore a connettersi.");
        System.out.println("Per te ho un compito importante, mi dovrai dire con che mappa vorrai giocare e con quali regole");
        System.out.println("Quale mappa di gioco sceglie tra le 4 disponibili: ");

        //TODO: in futuro farà notify con un messaggio di tipo "RequestMap" per fargli vedere le mappe prima di scegliere
        System.out.println("1 - Mappa A");
        System.out.println("2 - Mappa B");
        System.out.println("3 - Mappa C");
        System.out.println("4 - Mappa D\n");

        while(!checkAnswer) {
            System.out.print("Scegli [1 - 4] per selezionare la mappa: ");
            mapChoice = scanner.nextInt();
            if(mapChoice >= 1 && mapChoice <= 4)
                checkAnswer = true;
            else
                System.out.println("La scelta fatta non è ammissibile. Ritenta, sarai più fortunato.\n");
        }

        checkAnswer = false;

        System.out.println("Perfetto! Ora dovrai dirmi le regole con cui preferisci giocare: ");
        System.out.println("1. Usare 5 teschi. La partita dura un po' di meno.");
        System.out.println("2. Usare 8 teschi. La partita dura un po' di più.");
        while(!checkAnswer) {
            System.out.print("Scegli [1 - 2] per selezionare le regole: ");
            skullChoice = scanner.nextInt();
            if(skullChoice >= 1 && skullChoice <= 2)
                checkAnswer = true;
            else
                System.out.println("La scelta fatta non è ammissibile. Ritenta, sarai più fortunato.\n");
        }

        System.out.println("Impostiamo il gioco secondo tuo desiderio ...");
        //TODO

        System.out.println("Il tavolo di gioco è pronto!");
        System.out.println("Ora ti lascio in attesa degli altri giocatori");

        stateContext.setState(new WaitState());
        stateContext.startMenu();
    }
}
