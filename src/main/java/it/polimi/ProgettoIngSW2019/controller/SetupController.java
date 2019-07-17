package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SetupRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.utilities.TypeAdapterSquareLM;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.Arrays;
import java.util.List;

/**
 * Class Setup Controller (before the start of the game)
 * @author Luca Iovine
 */
public class SetupController implements Observer<Event> {
    private VirtualView virtualView;
    private LoginHandler loginHandler;
    private Maps maps;
    private GameTable gt;
    private TurnManager turnManager;
    private SetupResponse setupResponse;
    private CreateJson createJson;

    /**
     * Constructor
     * @param virtualView
     * @param loginHandler
     */
    public SetupController(VirtualView virtualView, LoginHandler loginHandler){
        this.virtualView = virtualView;
        this.loginHandler = loginHandler;
        maps = new Maps();
    }

    /**
     * Get the information from the view that the user sends, check their correctness and is correct, perform
     * the consequent modifications
     * @param event
     */
    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_SETUP)){
            SetupRequest setup = (SetupRequest)deserialize(event.getMessageInJsonFormat(), SetupRequest.class);
            setModel(setup);
            setController();

            loginHandler.setInSetupMode(false);

            for(String hostname: loginHandler.getUsersHostname()) {
                setupResponse = createSetupResponse(hostname);
                virtualView.sendMessage(new Event(EventType.RESPONSE_SETUP, serialize(setupResponse)), Arrays.asList(hostname));
            }
        }
        if(event.getCommand().equals(EventType.REQUEST_GAME_DATA)){
            LoginRequest info = (LoginRequest) deserialize(event.getMessageInJsonFormat(), LoginRequest.class);
            setupResponse = createSetupResponse(info.getHostname());
            virtualView.sendMessage(new Event(EventType.RESPONSE_GAME_DATA, serialize(setupResponse)), Arrays.asList(info.getHostname()));
        }
    }

    /**
     * Initialize the model based on the user choice of the map and number of skull
     *
     * @param setup
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private void setModel(SetupRequest setup){
        gt = new GameTable(maps.getMaps()[setup.getMapChosen()], setup.getNrOfSkullChosen());
        gt.setPlayersBeforeStart(loginHandler.getSessions());
        turnManager = new TurnManager(gt);
        loginHandler.setTurnManager(turnManager);

        List<String> activePlayerHostname = loginHandler.getActiveUsersHostname();

        for(Player p: gt.getPlayers()){
            if(!activePlayerHostname.contains(p.getHostname()))
                p.suspendPlayer();
        }

        Player currentPlayer = turnManager.getCurrentPlayer();
        while(!currentPlayer.isActive()){
            turnManager.changeCurrentPlayer();
            currentPlayer = turnManager.getCurrentPlayer();
        }
    }

    /**
     * Initialize the controller
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private void setController(){
        createJson = new CreateJson(turnManager);
        IdConverter idConverter = new IdConverter(gt);
        HostNameCreateList hostNameCreateList = new HostNameCreateList(turnManager);
        DistanceDictionary distance = new DistanceDictionary(gt.getMap());

        PayAmmoController payAmmoController = new PayAmmoController(createJson);
        GrabController grabController = new GrabController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);
        MoveController moveController = new MoveController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        PowerUpController powerUpController = new PowerUpController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        ReloadController reloadController = new ReloadController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);
        ShootController shootController = new ShootController(turnManager, idConverter, virtualView, createJson, hostNameCreateList, distance);
        SpawnController spawnController = new SpawnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        EndTurnController endTurnController = new EndTurnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, spawnController);

        virtualView.addObserver(endTurnController);
        virtualView.addObserver(grabController);
        virtualView.addObserver(moveController);
        virtualView.addObserver(powerUpController);
        virtualView.addObserver(reloadController);
        virtualView.addObserver(shootController);
        virtualView.addObserver(spawnController);
        loginHandler.addObserver(endTurnController);
    }

    /**
     * Generates the list of light models of the players in game
     *
     * @return players light model
     * @author: Luca Iovine
     */

    /**
     * Generate the game data response for the player based on his hostname
     *
     * @param hostname to identify the player
     * @return the SetupResponse which hold the game data of the player
     * @author: Luca Iovine
     */
    private SetupResponse createSetupResponse(String hostname){
        int myID;
        String currentPlayer;

        PlayerDataLM[] playerLMList = getPlayerLMList();
        MapLM mapLM = createJson.createMapLM();
        KillshotTrackLM killshotTrackLM = createJson.createKillShotTrackLM();

        if(!loginHandler.isGameStarted())
            currentPlayer = loginHandler.getSessions().get(0).getUsername();
        else
            currentPlayer = turnManager.getCurrentPlayer().getCharaName();

        MyLoadedWeaponsLM myLoadedWeaponsLM = createJson.createMyLoadedWeaponsListLM(getPlayer(hostname));
        MyPowerUpLM myPowerUpLM = createJson.createMyPowerUpsLM(getPlayer(hostname));
        myID = getPlayer(hostname).getIdPlayer();

        return new SetupResponse(currentPlayer, myID, playerLMList, mapLM, killshotTrackLM, hostname, myLoadedWeaponsLM, myPowerUpLM);
    }

    /**
     * @return the list of all player light model
     * @author: Luca Iovine
     */
    private PlayerDataLM[] getPlayerLMList(){
        PlayerDataLM[] playerDataLMList = new PlayerDataLM[gt.getPlayers().length];
        for(int i = 0; i < gt.getPlayers().length; i++)
            playerDataLMList[i] = createJson.createPlayerLM(gt.getPlayers()[i]);

        return playerDataLMList;
    }

    /**
     * @param hostname of the player of the id searched
     * @return the id player associated with the hostname
     * @author: Luca Iovine
     */

    private Player getPlayer(String hostname){
        Player player = null;

        for(Player p: gt.getPlayers()){
            if(p.getHostname().equals(hostname)){
                player = p;
            }
        }

        return player;
    }

    /**
     * To deserialize information in the event
     *
     * @param json string that contains data in json format
     * @param cls class to deserialize
     * @return object deserialized
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private Object deserialize(String json, Class<?> cls){
        Gson gsonReader = new Gson();
        Object deserializedObj = gsonReader.fromJson(json, cls);

        return deserializedObj;
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(SquareLM.class, new TypeAdapterSquareLM())
                .create();

        return gson.toJson(objToSerialize, objToSerialize.getClass());
    }
}
