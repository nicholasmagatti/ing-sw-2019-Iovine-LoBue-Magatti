package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.List;
import java.util.Scanner;

public class PlayerChoiceState extends Observable<Event> implements IState {
    Scanner scanner;
    int nrOfAction;
    int actionChoice;
    boolean checkAnswer = false;
    //Variable used to handle the move statement
    int x;
    int y;
    int nrOfMove;
    //Variable used to handle shoot statement
    int squareNumber;
    List<List<Integer>> targetIdList;
    int nrOfTarget;
    String targetPerSquare;
    int targetChoice;
    //Variable used to handle grab statement
    boolean isOnAmmoPoint;
    List<String> ammoCardValue;
    String yesnoValue;
    //Variable to handle infoItem statement
    int infoChoice;

    public PlayerChoiceState(){
        nrOfAction = 2;
    }

    @Override
    public void menu(StateContext stateContext) {
        scanner = new Scanner(System.in);

        while(nrOfAction != 0) {
            System.out.println("Hai ancora a disposzione " + nrOfAction + " azioni.");
            System.out.println("Che cosa vuoi fare: ");
            System.out.println("1 - Muoviti");
            System.out.println("2 - Raccogli per terra");
            System.out.println("3 - Spara");
            System.out.println("4 - Menu informazioni oggetti\n");

            while (!checkAnswer) {
                System.out.print("Scegli [1 - 3] la mossa che vuoi fare: ");
                actionChoice = scanner.nextInt();

                if (actionChoice >= 1 && actionChoice <= 3)
                    checkAnswer = true;
                else
                    System.out.println("La scelta fatta non è ammissibile. Ritenta, sarai più fortunato.\n");
            }
            checkAnswer = false;

            switch (actionChoice) {
                case 1:
                    move();
                    break;
                case 2:
                    grab();
                    break;
                case 3:
                    shoot();
                    break;
                case 4:
                    infoItem();
                    break;
            }

            nrOfAction--;
        }

        stateContext.setState(new ReloadState());
        stateContext.startMenu();
    }

    private void move(){
        System.out.println("Reminder: un movimento corrisponde a spostarsi di un quadrato escludendo la diagonale");
        System.out.println("Puoi fare un massimo di 3 movimenti.");

        while(!checkAnswer){
            System.out.print("Di quanti movimenti vuoi spostarti?\n");
            System.out.println("Numero di movimenti: ");

            nrOfMove = scanner.nextInt();

            if(nrOfMove >= 1 && nrOfMove <= 3)
                checkAnswer = true;
            else
                System.out.println("Ma te l'ho scritto prima che il numero massimo è 3! E se avessi scelto un numero negativo o zero mi chiedo quale sia il tuo QI");
                System.out.println("Rifail la scelta.\n");
        }
        System.out.println("In quale quadrato vuoi muoverti tra: ");
    }

    private void grab(){
        if(isOnAmmoPoint) {
            System.out.println("Vuoi raccogliere la carta munizioni: ");
            System.out.println(ammoCardValue);
            while (!checkAnswer) {
                System.out.print("Scegli la risposta (y/n): ");
                yesnoValue = scanner.nextLine();
                if (yesnoValue.equalsIgnoreCase("y")) {
                    //TODO
                    checkAnswer = true;
                } else if (!yesnoValue.equalsIgnoreCase("n")) {
                    System.out.println("Scelta non valida.");
                }
            }
        }
        else{
            System.out.println("Quale carta vuoi raccogliere fra: ");
            //TODO
        }
    }

    private void shoot(){
        System.out.println("Bene, a quanto pare vogliamo distruggere qualcuno.");
        System.out.println("Con che arma desideri massacrare la gente: ");
        //TODO
        System.out.println("I target che puoi colpire sono i seguenti: ");
        for(List<Integer> i: targetIdList){
            //TODO:
            System.out.println("Target nello square: " + i);
        }

        if(targetPerSquare.equals("all")){
            System.out.println("Verranno colpiti tutti i target nel quadrato");
            //TODO:
        }
        else {
            for (int i = 0; i < nrOfTarget; i++) {
                System.out.print("\nScegli il quadrato dove risiede il target che vuoi colpire: ");
                squareNumber = scanner.nextInt();

                switch (targetPerSquare) {
                    case "one_for_square":
                        System.out.println("Puoi scegliere fino ad un target per quadrato.");
                        System.out.println("Chi vuoi colpire: ");
                        for (int j = 0; j < targetIdList.get(squareNumber - 1).size(); j++) {
                            System.out.println((j + 1) + ". " + targetIdList.get(squareNumber - 1).get(j));
                        }
                        targetChoice = scanner.nextInt();
                        //TODO:
                        targetIdList.remove(squareNumber - 1);
                        break;
                    case "as_you_wish":
                        System.out.println("Chi vuoi colpire: ");
                        for (int j = 0; j < targetIdList.get(squareNumber - 1).size(); j++) {
                            System.out.println((j + 1) + ". " + targetIdList.get(squareNumber - 1).get(j));
                        }
                        targetChoice = scanner.nextInt();
                        //TODO:
                        targetIdList.get(squareNumber - 1).remove(targetChoice);
                }
            }
        }

        //TODO:
    }

    private void infoItem(){
        System.out.println("Di che cosa vuoi vedere le informazioni: ");
        System.out.println("1. Le tue armi in mano");
        System.out.println("2. Le tue armi scariche");
        System.out.println("3. Dei tuoi power up");
        System.out.println("4. I tuoi punti ferita");
        System.out.println("5. I punti ferita dei tuoi avversari\n");
        System.out.print("Scelta: ");

        infoChoice = scanner.nextInt();

        switch(infoChoice){
            case 1:
                showWeaponInHandInfo();
                break;
            case 2:
                showWeaponUnloadedInfo();
                break;
            case 3:
                showPowerUpInfo();
                break;
            case 4:
                showYourDamageLine();
                break;
            case 5:
                showEnemyDamageLine();
                break;
                default: System.out.println("Ritorno al menu di scelta");
        }
    }

    private void showWeaponInHandInfo(){

    }

    private void showWeaponUnloadedInfo(){

    }

    private void showPowerUpInfo(){

    }

    private void showYourDamageLine(){

    }

    private void showEnemyDamageLine(){

    }
}