package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ShootChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;

import java.util.ArrayList;
import java.util.List;

public class ShootState extends State {
    Gson gsonReader;
    ActionState actionState;
    StringBuilder sb;
    String msg;

    int j;
    int i;
    String userChoice;
    List<String> possibleChoice;
    int[] positionChosen;
    List<Integer> idEnemyChosen;
    List<EnemyInfo> enemyChosenInfoList;
    List<WeaponInfo> weaponInfoList;

    /**
     * @author: Luca Iovine
     */
    public ShootState(ActionState actionState){
        this.actionState = actionState;
        gsonReader = new Gson();
        sb = new StringBuilder();
    }

    @Override
    void startState() {
        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoRequest, EventType.REQUEST_WEAPON_INFO);

        interaction(weaponInfoList);
    }

    @Override
    public void update(Event event) {
        switch(event.getCommand()){
            case RESPONSE_REQUEST_WEAPON_INFO:
                weaponInfoList = gsonReader.fromJson(event.getMessageInJsonFormat(), List.class);
                break;
        }
    }

    private void interaction(List<WeaponInfo> weaponInfoList){
        if(weaponInfoList.isEmpty()){
            msg = "You have no valid weapon to hit any enemy.\n" +
                    "You will sent back to the selection of the action.";
            System.out.println(msg);
            try {
                Thread.sleep(3000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            actionState.startState();
        }else
            askWhichWeapon(weaponInfoList);
    }

    private void askWhichWeapon(List<WeaponInfo> weaponInfoList){
        int choiceNumber = 1;
        WeaponInfo weaponChosen;

        /*
            Creazione del messaggio da stampare
         */
        msg = "Which one of the following "+ weaponInfoList.size() + " do you want to use?\n" +
                "Indicate the corresponding number: \n";
        sb.append(msg);

        for(WeaponInfo weapon: weaponInfoList){
            possibleChoice.add(Integer.toString(choiceNumber));
            sb.append(choiceNumber + ": " + weapon.getWeaponName()+"\n");
            choiceNumber++;
        }

        /*
            Stampa del messaggio
         */
        System.out.println(sb);
        ToolsView.printGeneralOptions();

        resetParam();
        System.out.print("Make your choice: ");
        /*
            Input utente
         */
        userChoice = ToolsView.readUserChoice(possibleChoice, true);
        if(userChoice != null) {
            weaponChosen = weaponInfoList.get(Integer.parseInt(userChoice) - 1);
            if(weaponChosen.getEffectType().equals(WeaponEffectType.FLAMETHROWER))
                flamethrowerEffectMenu(weaponChosen);
            else if(weaponChosen.getEffectType().equals(WeaponEffectType.SAME_ROOM))
                sameroomEffectMenu(weaponChosen);
            else if(weaponChosen.getEffectType().equals(WeaponEffectType.TRACTOR_BEAM) ||
                    weaponChosen.getEffectType().equals(WeaponEffectType.SHIFT_ONE_MOVEMENT) ||
                    weaponChosen.getEffectType().equals(WeaponEffectType.VORTEX))
                withMovementEffectMenu(weaponChosen);
            else if(weaponChosen.getEffectType().equals(WeaponEffectType.ONE_PER_SQUARE))
                onepersquareEffectMenu(weaponChosen);
            else
                generalEffectMenu(weaponChosen);
        }
    }

    private void generalEffectMenu(WeaponInfo weaponChosen) {
        EnemyInfo enemyChosen;
        int choiceNumber = 1;
        /*
            Costruzione del messaggio
         */
        msg = "You can target to " + weaponChosen.getNumberOfTargetHittable() + " players.\n";
        sb.append(msg);
        for (i = weaponChosen.getNumberOfTargetHittable(); i > 0; i--){
            for (EnemyInfo enemy : weaponChosen.getEnemyVisible()) {
                possibleChoice.add(Integer.toString(choiceNumber));
                sb.append(choiceNumber + ": " + enemy.getName() + "\n");
                choiceNumber++;
            }
            choiceNumber = 1;
            msg = "You can still choose "+ i + " players.\n" +
                    "Choose the number corresponding to the name of the player you want to hit: ";
            sb.append(msg);

            System.out.println(sb);

             /*
                Input utente
             */
            userChoice = ToolsView.readUserChoice(possibleChoice, false);
            if(userChoice != null) {
                enemyChosen = weaponChosen.getEnemyVisible().get(Integer.parseInt(userChoice) - 1);
                enemyChosenInfoList.add(enemyChosen);
                idEnemyChosen.add(enemyChosen.getId());
                weaponChosen.getEnemyVisible().remove(Integer.parseInt(userChoice) - 1);
            }
        }

        if(userChoice != null){
            ShootChoiceRequest shootChoiceRequest = new ShootChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), weaponChosen.getWeaponId(), idEnemyChosen, null);
            notifyEvent(shootChoiceRequest, EventType.REQUEST_SHOOT);
        }
    }

    private void flamethrowerEffectMenu(WeaponInfo weaponChosen){
        int i;
        PlayerDataLM myData = null;

        List<EnemyInfo> northEnemy = new ArrayList<>();
        List<EnemyInfo> southEnemy = new ArrayList<>();
        List<EnemyInfo> eastEnemy = new ArrayList<>();
        List<EnemyInfo> westEnemy = new ArrayList<>();

        msg = "With this weapon you can shoot enemies in one of the four cardinal directions.\n";
        sb.append(msg);

        //Recupero la posizione del giocatore
        for(PlayerDataLM player: InfoOnView.getPlayers()){
            if(player.getIdPlayer() == InfoOnView.getMyId()) {
                myData = player;
                break;
            }
        }

        //Costruisco la suddivisione per coordinate
        for (EnemyInfo enemy : weaponChosen.getEnemyVisible()) {
            if(enemy.getPosition()[0] < InfoOnView.positionPlayer(myData)[0]){
                northEnemy.add(enemy);
            }
            if(enemy.getPosition()[0] > InfoOnView.positionPlayer(myData)[0]){
                southEnemy.add(enemy);
            }
            if(enemy.getPosition()[1] < InfoOnView.positionPlayer(myData)[1]){
                westEnemy.add(enemy);
            }
            if(enemy.getPosition()[1] > InfoOnView.positionPlayer(myData)[1]){
                eastEnemy.add(enemy);
            }
        }

        final String NORTH = "north";
        final String SOUTH = "south";
        final String EAST = "east";
        final String WEAST = "west";

        //Le quattro possibilità sono "north, south, west, east"
        possibleChoice.add(NORTH);
        possibleChoice.add(SOUTH);
        possibleChoice.add(WEAST);
        possibleChoice.add(EAST);

        //Costruisco il messaggio
        msg = "North: ";
        sb.append(msg);
        for(i = 0; i < northEnemy.size() - 1; i++){
           msg = northEnemy.get(i).getName() + ", ";
           sb.append(msg);
        }
        msg = northEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nSouth: ";
        sb.append(msg);
        for(i = 0; i < southEnemy.size() - 1; i++){
            msg = southEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = southEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nEast: ";
        sb.append(msg);
        for(i = 0; i < eastEnemy.size() - 1; i++){
            msg = eastEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = eastEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nWest: ";
        sb.append(msg);
        for(i = 0; i < westEnemy.size() - 1; i++){
            msg = westEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = westEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "Type one of the four directions to shoot the corresponding enemies: ";
        sb.append(msg);
        System.out.println(sb);

        /*
           Input utente
        */
        userChoice = ToolsView.readUserChoice(possibleChoice, false);
        if(userChoice != null) {
            switch(userChoice.toLowerCase()){
                case NORTH:
                    for(EnemyInfo enemyChosen: northEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case SOUTH:
                    for(EnemyInfo enemyChosen: southEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case EAST:
                    for(EnemyInfo enemyChosen: eastEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case WEAST:
                    for(EnemyInfo enemyChosen: westEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
            }

        }

        if(userChoice != null){
            ShootChoiceRequest shootChoiceRequest = new ShootChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), weaponChosen.getWeaponId(), idEnemyChosen, null);
            notifyEvent(shootChoiceRequest, EventType.REQUEST_SHOOT);
        }

    }

    private void onepersquareEffectMenu(WeaponInfo weaponChosen){
        idEnemyChosen = new ArrayList<>();
        int j;
        List<String> enemyOnSquare;
        List<List<String>> enemyDividedBySquare = new ArrayList<>();
        List<int[]> squares = new ArrayList<>();

        //Estrapolo tutti i quadrati in cui ho dei nemici
        for(EnemyInfo enemy: weaponChosen.getEnemyVisible()){
            if(!squares.contains(enemy.getPosition())){
                squares.add(enemy.getPosition());
            }
        }
        //Per ogni quadrato costruisco la lista appropiata in modo da avere una lista contenente
        //le liste di nomi suddivise per quadrato
        for(int[] square: squares){
            enemyOnSquare = new ArrayList<>();
            for(EnemyInfo enemy: weaponChosen.getEnemyVisible()){
                if(enemy.getPosition().equals(square)){
                    enemyOnSquare.add(enemy.getName());
                }
            }
            enemyDividedBySquare.add(enemyOnSquare);
        }
        /*
            Costruzione messaggio
         */
        msg = "For each square, you can select only one target.\n" +
                "You can hit to "+ weaponChosen.getNumberOfTargetHittable() + "players.\n";
        sb.append(msg);

        for (int s = weaponChosen.getNumberOfTargetHittable(); s > 0; s--) {
            for (i = 0; i < enemyDividedBySquare.size(); i++) {
                msg = "Square " + (i + 1) + ": ";
                sb.append(msg);
                for (j = 0; j < enemyDividedBySquare.get(i).size() - 1; j++) {
                    possibleChoice.add(enemyDividedBySquare.get(i).get(j));
                    msg = enemyDividedBySquare.get(i).get(j) + " - ";
                    sb.append(msg);
                }
                possibleChoice.add(enemyDividedBySquare.get(i).get(j));
                msg = enemyDividedBySquare.get(i).get(j);
                sb.append(msg);
            }

            msg = "You can still choose "+ s + " players.\n" +
                    "Choose the name of the player you want to shoot: ";
            sb.append(msg);

            System.out.println(sb);

             /*
                Input utente
             */
            userChoice = ToolsView.readUserChoice(possibleChoice, false);
            if(userChoice != null) {
                for(EnemyInfo enemyChosen: weaponChosen.getEnemyVisible()) {
                    if(enemyChosen.getName().equalsIgnoreCase(userChoice)) {
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                }
                for(List<String> enemyListOnSquare: enemyDividedBySquare){
                    if(enemyListOnSquare.contains(userChoice)){
                        enemyDividedBySquare.remove(enemyListOnSquare);
                        break;
                    }
                }
            }
        }

        if(userChoice != null){
            ShootChoiceRequest shootChoiceRequest = new ShootChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), weaponChosen.getWeaponId(), idEnemyChosen, null);
            notifyEvent(shootChoiceRequest, EventType.REQUEST_SHOOT);
        }
    }

    private void sameroomEffectMenu(WeaponInfo weaponChosen){
        List<Integer> rooms = new ArrayList<>();
        List<List<EnemyInfo>> enemyDividedByRoom = new ArrayList<>();
        List<EnemyInfo> enemyInRoom;

        //Estrapolo tutte le stanze in cui ho dei nemici
        for(EnemyInfo enemy: weaponChosen.getEnemyVisible()){
            if(!rooms.contains(enemy.getPosition())){
                rooms.add(enemy.getIdRoom());
            }
        }
        //Per ogni stanza costruisco la lista appropiata in modo da avere una lista contenente
        //le liste di nomi suddivise per stanza
        for(int room: rooms){
            enemyInRoom = new ArrayList<>();
            for(EnemyInfo enemy: weaponChosen.getEnemyVisible()){
                if(enemy.getIdRoom() == room){
                    enemyInRoom.add(enemy);
                }
            }
            enemyDividedByRoom.add(enemyInRoom);
        }

        for (i = 0; i < enemyDividedByRoom.size(); i++) {
            msg = "Room number " + (i + 1) + ": ";
            possibleChoice.add(Integer.toString(i+1));
            sb.append(msg);
            for (j = 0; j < enemyDividedByRoom.get(i).size() - 1; j++) {
                msg = enemyDividedByRoom.get(i).get(j).getName() + " - ";
                sb.append(msg);
            }
            msg = enemyDividedByRoom.get(i).get(j).getName();
            sb.append(msg);
        }

        msg = "Choose the number of the room (from 1 to " + (i-1) + ") to hit all the enemies in it: ";
        sb.append(msg);

        System.out.println(sb);

         /*
            Input utente
         */
        userChoice = ToolsView.readUserChoice(possibleChoice, false);
        if(userChoice != null){
            for(EnemyInfo enemyChosen: enemyDividedByRoom.get(Integer.parseInt(userChoice) - 1)) {
                enemyChosenInfoList.add(enemyChosen);
                idEnemyChosen.add(enemyChosen.getId());
            }
        }

        if(userChoice != null){
            ShootChoiceRequest shootChoiceRequest = new ShootChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), weaponChosen.getWeaponId(), idEnemyChosen, null);
            notifyEvent(shootChoiceRequest, EventType.REQUEST_SHOOT);
        }
    }

    private void withMovementEffectMenu(WeaponInfo weaponChosen){
        generalEffectMenu(weaponChosen);

        msg = "With this weapon you can move the opponent to one of this positions: \n";
        sb.append(msg);

        for(i= 0; i < enemyChosenInfoList.get(0).getMovement().size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + ToolsView.coordinatesForUser(enemyChosenInfoList.get(0).getMovement().get(i));
            sb.append(msg);
            if(enemyChosenInfoList.get(0).getMovement().get(i)[0] == enemyChosenInfoList.get(0).getPosition()[0] &&
                    enemyChosenInfoList.get(0).getMovement().get(i)[1] == enemyChosenInfoList.get(0).getPosition()[1])
                sb.append(" (Same position)\n");
            else
                sb.append("\n");
        }
        msg = "Choose a number(from 1 to " + i + ") to indicate where to move the target to: ";
        sb.append(msg);

        System.out.println(msg);

        userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            positionChosen = enemyChosenInfoList.get(0).getMovement().get(Integer.parseInt(userChoice) - 1);
        }

        if(userChoice != null){
            ShootChoiceRequest shootChoiceRequest = new ShootChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), weaponChosen.getWeaponId(), idEnemyChosen, positionChosen);
            notifyEvent(shootChoiceRequest, EventType.REQUEST_SHOOT);
        }
    }

    private void resetParam(){
        userChoice = null;
        possibleChoice = new ArrayList<>();
        positionChosen = null;
        idEnemyChosen= new ArrayList<>();
        enemyChosenInfoList = new ArrayList<>();
        sb = new StringBuilder();
    }
}
