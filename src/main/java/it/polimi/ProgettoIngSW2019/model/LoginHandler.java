package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.*;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageConnection;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.TypeAdapterSquareLM;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoginHandler extends Observable<Event> {
    //Key value: hostname
    private TurnManager turnManager;
    private final HashMap<String, Session> sessions;
    private List<String> usernameConnected;
    private boolean gameStarted = false;
    private boolean inSetupMode = false;
    private int nrOfPlayerConnected = 0;
    private final int timeForPing = 100;
    private final int timeBeforeStartingGame = 5000;
    private final int timeForInput = 180000;
    private boolean timerStarted = false;
    private Iterator<Map.Entry<String, Session>> entryIterator;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private TimerTask timerTask;
    private Timer timer;

    private String setupUser;

    public LoginHandler(){
        sessions = new HashMap<>();
        usernameConnected = new ArrayList<>();
        executorService.scheduleAtFixedRate(this::ping,0,timeForPing, TimeUnit.MILLISECONDS);
    }

    /**
     * It generate a new session based on the username and password pair.
     * Whenever the number of player reaches 3, a timer start. If the timer goes off, the game start with
     * the player connected at the moment.
     * If the player reaches 5, the game starts anyways, even if the timer is still up.
     * If, in any moment before the game is started, the players goes down the number of 3, the timer is stopped
     * and resetted.
     *
     * @param username chosen by the user
     * @param password one time password in order to reconnect
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED --> AT THE MOMENT
    public void generateNewLogin(String username, String password, String hostname) {
        Session s = new Session(username, password, hostname);
        sessions.put(hostname, s);
        usernameConnected.add(username);
        nrOfPlayerConnected++;
        System.out.println("Giocatori connessi: " + nrOfPlayerConnected);
        if(nrOfPlayerConnected == 3){
            System.out.println("Timer started");
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    entryIterator = sessions.entrySet().iterator();
                    goInSetup();
                }
            };
            timerStarted = true;
            timer.schedule(timerTask, timeBeforeStartingGame);
        }else if(nrOfPlayerConnected == 5){
            timerTask.cancel();
            timer.cancel();
            timer.purge();
            timerStarted = false;

            entryIterator = sessions.entrySet().iterator();
            goInSetup();
        }else if(nrOfPlayerConnected <= 3 && timerStarted){
            timerTask.cancel();
            timer.cancel();
            timer.purge();
            timerStarted = false;
        }
    }

    /**
     * Check if the user alredy exist or not
     *
     * @param username chosen by the user
     * @return true if the user exist, false otherwise
     * @author: Luca Iovine
     */
    /*
        TESTED --> checkPippoExist
                   checkPaperinoDoesNotExist
     */

    public boolean checkUserExist(String username){
        boolean result = false;

        if (usernameConnected.contains(username)) {
            result = true;
        }

        return result;
    }

    /**
     * @return ture if the game is started, false otherwise
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Check that the username/password is a valid combination and it's associated to the hostname
     * which have made the request.
     *
     * @param username chosen for the login
     * @param password chosen for the login
     * @param hostname of the client which sent the request
     * @return true if the parameters match, false otherwise.
     * @author: Luca Iovine
     */
    /*
        TESTED --> checkLoginIsCorrect
                   checkLoginIsIncorrectWrongPWD
                   checkLoginIsIncorrectWrongHostname
     */
    public boolean checkLoginValidity(String username, String password, String hostname){
        boolean result = false;
        Session sessionToCheck = sessions.get(hostname);

        if(sessionToCheck != null) {
            if (username.equalsIgnoreCase(sessionToCheck.getUsername()) && password.equalsIgnoreCase(sessionToCheck.getPassword())) {
                usernameConnected.add(username);
                turnManager.getPlayerFromCharaName(sessions.get(hostname).getUsername()).reactivatePlayer();
                result = true;
            }
        }

        return result;
    }

    /**
     * When the player are enough to start the game, this method is invoked and it notify the client
     * to change their context and moving them into the setup state
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    void goInSetup(){
        System.out.println("Going in setup");
        gameStarted = true;
        inSetupMode = true;
        setupUser = entryIterator.next().getValue().getUsername();

        for(String hostname: getActiveUsersHostname()) {
            List<MapLM> mapLMList = generateMapLMListForGameSetup();
            SetupInfo msg = new SetupInfo(setupUser, hostname, mapLMList);

            notify(new Event(EventType.GO_IN_GAME_SETUP, serialize(msg)));
        }
    }

    public void setInSetupMode(boolean inSetupMode) {
        this.inSetupMode = inSetupMode;
    }

    /**
     * @return the number of player connected at the moment
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public int getNrOfPlayerConnected() {
        return nrOfPlayerConnected;
    }

    /**
     *  It will check every X seconds if the player alredy connected are still alive sending a ping to each of
     *  them.
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void ping(){
        for(String hostname: getActiveUsersHostname()) {
            String username = sessions.get(hostname).getUsername();
            MessageConnection msg = new MessageConnection(username, hostname);

            notify(new Event(EventType.CHECK_IS_ALIVE, serialize(msg)));
        }
    }

    /**
     * Decrease the number of the active player.
     * In case the game is not started yet, it removes the association hostname/session so that the user is not
     * validated anymore.
     *
     * @param hostname of the user that has been disconnected
     * @author: Luca Iovine
     */

    //TESTED --> disconnectBeforeGameStart
    public void disconnectPlayer(String hostname){
        nrOfPlayerConnected--;
        usernameConnected.remove(sessions.get(hostname).getUsername());
        if(!isGameStarted()){
            sessions.remove(hostname);
        }else {
            if(!inSetupMode) {
                turnManager.getPlayerFromCharaName(sessions.get(hostname).getUsername()).suspendPlayer();
                if (turnManager.getCurrentPlayer().getHostname().equals(hostname)
                        || nrOfPlayerConnected < 3)
                    notify(new Event(EventType.END_TURN_DUE_USER_DISCONNECTION, ""));
            }else {
                goInSetup();
            }
        }
    }

    /**
     * @return the hostnames list of the user that are currently playing
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<String> getUsersHostname(){
        List<String> hostnames = new ArrayList<>();

        for(Map.Entry<String, Session> entry: sessions.entrySet())
            hostnames.add(entry.getValue().gethostname());

        return hostnames;
    }

    public List<String> getActiveUsersHostname(){
        List<String> hostnames = new ArrayList<>();

        for(Map.Entry<String, Session> entry: sessions.entrySet())
            if(usernameConnected.contains(entry.getValue().getUsername()))
                hostnames.add(entry.getValue().gethostname());

        return hostnames;
    }
    /**
     * @return the session list of the user that are currently playing
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<Session> getSessions(){
        List<Session> sessionList = new ArrayList<>();

        for(Map.Entry<String, Session> entry: sessions.entrySet())
            sessionList.add(entry.getValue());

        return sessionList;
    }

    /**
     * @return the list of light model maps to send to the view
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private List<MapLM> generateMapLMListForGameSetup(){
        int rowCounter;
        int colCounter;
        List<MapLM> mapLMList = new ArrayList<>();
        SquareLM[][] mapLMTmp;
        Maps maps = new Maps();

        for(Square[][] map: maps.getMaps()){
            mapLMTmp = new SquareLM[3][4];
            rowCounter = 0;
            for(Square[] row: map){
                colCounter = 0;
                for(Square col: row){
                    if(col != null) {
                        if (col.getSquareType().equals(SquareType.SPAWNING_POINT)) {
                            mapLMTmp[rowCounter][colCounter] = new SpawnPointLM(new ArrayList<>(), new ArrayList<>(),
                                    col.getIsBlockedAtNorth(), col.getIsBlockedAtEast(), col.getIsBlockedAtSouth(), col.getIsBlockedAtWest(), col.getIdRoom());
                        } else {
                            mapLMTmp[rowCounter][colCounter] = new AmmoPointLM(new ArrayList<>(), null,
                                    col.getIsBlockedAtNorth(), col.getIsBlockedAtEast(), col.getIsBlockedAtSouth(), col.getIsBlockedAtWest(), col.getIdRoom());

                        }
                    }
                    else
                        mapLMTmp[rowCounter][colCounter] = null;

                    colCounter++;
                }
                rowCounter++;
            }
            mapLMList.add(new MapLM(mapLMTmp));
        }

        return mapLMList;
    }

    /**
     * Start the timer that control how much time an user has left for the action
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void startActionTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                sendTimeExpired();
            }
        };
        timerStarted = true;
        timer.schedule(timerTask, timeForInput);
    }

    /**
     * Stop the timer that control how much time an user has left for the action
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void stopActionTimer(){
        timerTask.cancel();
        timer.cancel();
        timer.purge();
        timerStarted = false;
    }

    /**
     * Notify that the time is expired. Called when the timer task expires.
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private void sendTimeExpired(){
        MessageConnection msg = null;
        String host = "";
        if(!inSetupMode) {
            host = turnManager.getCurrentPlayer().getHostname();
            msg = new MessageConnection(turnManager.getCurrentPlayer().getCharaName(), host);
        }else{
            for(Map.Entry<String, Session> entry: sessions.entrySet()){
                if(entry.getValue().getUsername().equals(setupUser)) {
                    host = entry.getValue().gethostname();
                }
            }
            msg = new MessageConnection(setupUser, host);
        }
        stopActionTimer();
        notify(new Event(EventType.INPUT_TIME_EXPIRED, serialize(msg)));

        StringBuilder listOfHostname = new StringBuilder();
        for(int i = 0; i < getActiveUsersHostname().size()-1; i++){
            listOfHostname.append(getActiveUsersHostname().get(i)+";");
        }
        listOfHostname.append(getActiveUsersHostname().get(getActiveUsersHostname().size()-1));

        msg = new MessageConnection(setupUser, listOfHostname.toString());
        notify(new Event(EventType.USER_HAS_DISCONNECTED, serialize(msg)));
        disconnectPlayer(host);
    }

    /**
     * When the turn manager is created it can be setted through this method in order to let the
     * login handler use it
     *
     * @param turnManager the istance of turn manager
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void setTurnManager(TurnManager turnManager){
        this.turnManager = turnManager;
    }

    /**
     * To serialize information of the event
     *
     * @param objToSerialize object that needs to be serialized
     * @return object serialized
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private String serialize(Object objToSerialize){
        Gson gsonReader = new GsonBuilder()
                .registerTypeAdapter(SquareLM.class, new TypeAdapterSquareLM())
                .create();

        return gsonReader.toJson(objToSerialize, objToSerialize.getClass());
    }
}
