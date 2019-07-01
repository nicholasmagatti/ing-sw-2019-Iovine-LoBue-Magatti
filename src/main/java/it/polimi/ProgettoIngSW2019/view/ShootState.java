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
    }

    @Override
    public void update(Event event) {
        switch(event.getCommand()){
            case RESPONSE_REQUEST_WEAPON_INFO:
                List<WeaponInfo> weaponInfoList = gsonReader.fromJson(event.getMessageInJsonFormat(), List.class);
                if(weaponInfoList.isEmpty()){
                    msg = "Non hai armi valide con cui colpire dei nemici.\n" +
                            "Verrai riportato alla selezione delle azioni.";
                    System.out.println(msg);
                    actionState.startState();
                }else
                    askWhichWeapon(weaponInfoList);
                break;
        }
    }

    private void askWhichWeapon(List<WeaponInfo> weaponInfoList){
        int choiceNumber = 1;
        WeaponInfo weaponChosen;

        /*
            Creazione del messaggio da stampare
         */
        msg = "Quali delle seguenti "+ weaponInfoList.size() + " armi vuoi usare?\n" +
                "Indicare il numero corrispondente: \n";
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
        System.out.print("Fai la tua scelta: ");
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
        msg = "Puoi colpire fino a " + weaponChosen.getNumberOfTargetHittable() + " giocatori.\n";
        sb.append(msg);
        for (i = weaponChosen.getNumberOfTargetHittable(); i > 0; i--){
            for (EnemyInfo enemy : weaponChosen.getEnemyVisible()) {
                possibleChoice.add(Integer.toString(choiceNumber));
                sb.append(choiceNumber + ": " + enemy.getName() + "\n");
                choiceNumber++;
            }
            choiceNumber = 1;
            msg = "Puoi scegliere ancora "+ i + " giocatori.\n" +
                    "Scegli il numero corrispondente al nome di chi vuoi colpire: ";
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

        msg = "Con quest'arma puoi colpire dei nemici in una delle quattro direzioni cardinali.\n";
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

        //Le quattro possibilità sono "nord, sud, ovest, est"
        possibleChoice.add("nord");
        possibleChoice.add("sud");
        possibleChoice.add("ovest");
        possibleChoice.add("est");

        //Costruisco il messaggio
        msg = "Nord: ";
        sb.append(msg);
        for(i = 0; i < northEnemy.size() - 1; i++){
           msg = northEnemy.get(i).getName() + ", ";
           sb.append(msg);
        }
        msg = northEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nSud: ";
        sb.append(msg);
        for(i = 0; i < southEnemy.size() - 1; i++){
            msg = southEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = southEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nEst: ";
        sb.append(msg);
        for(i = 0; i < eastEnemy.size() - 1; i++){
            msg = eastEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = eastEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "\nOvest: ";
        sb.append(msg);
        for(i = 0; i < westEnemy.size() - 1; i++){
            msg = westEnemy.get(i).getName() + ", ";
            sb.append(msg);
        }
        msg = westEnemy.get(i).getName() + ", ";
        sb.append(msg);

        msg = "Scrivi una delle quattro direzioni per colpire quei nemici: ";
        sb.append(msg);
        System.out.println(sb);

        /*
           Input utente
        */
        userChoice = ToolsView.readUserChoice(possibleChoice, false);
        if(userChoice != null) {
            switch(userChoice.toLowerCase()){
                case "nord":
                    for(EnemyInfo enemyChosen: northEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case "sud":
                    for(EnemyInfo enemyChosen: southEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case "est":
                    for(EnemyInfo enemyChosen: eastEnemy){
                        enemyChosenInfoList.add(enemyChosen);
                        idEnemyChosen.add(enemyChosen.getId());
                    }
                    break;
                case "ovest":
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
        msg = "Per ogni quadrato, puoi selezionare un solo nemico.\n" +
                "Puoi colpire fino a "+ weaponChosen.getNumberOfTargetHittable() + " giocatori.\n";
        sb.append(msg);

        for (int s = weaponChosen.getNumberOfTargetHittable(); s > 0; s--) {
            for (i = 0; i < enemyDividedBySquare.size(); i++) {
                msg = "Quadrato " + (i + 1) + ": ";
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

            msg = "Puoi scegliere ancora "+ s + " giocatori.\n" +
                    "Scegli il nome del giocatore che vuoi colpire: ";
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
            msg = "Stanza numero " + (i + 1) + ": ";
            possibleChoice.add(Integer.toString(i+1));
            sb.append(msg);
            for (j = 0; j < enemyDividedByRoom.get(i).size() - 1; j++) {
                msg = enemyDividedByRoom.get(i).get(j).getName() + " - ";
                sb.append(msg);
            }
            msg = enemyDividedByRoom.get(i).get(j).getName();
            sb.append(msg);
        }

        msg = "Scegli il numero della stanza (da 1 a " + (i-1) + ") per colpire tutti i nemici al suo interno: ";
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

        msg = "Con quest'arma puoi far muovere l'avversario in una di queste posizioni: \n";
        sb.append(msg);

        for(i= 0; i < enemyChosenInfoList.get(0).getMovement().size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + ToolsView.coordinatesForUser(enemyChosenInfoList.get(0).getMovement().get(i));
            sb.append(msg);
            if(enemyChosenInfoList.get(0).getMovement().get(i)[0] == enemyChosenInfoList.get(0).getPosition()[0] &&
                    enemyChosenInfoList.get(0).getMovement().get(i)[1] == enemyChosenInfoList.get(0).getPosition()[1])
                sb.append(" (Stessa posizione)\n");
            else
                sb.append("\n");
        }
        msg = "Indica il numero (da 1 a " + i + ") per decidere dove muoverlo: ";
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
