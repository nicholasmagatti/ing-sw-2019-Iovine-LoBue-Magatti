package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SetupRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupResponse;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupController implements Observer<Event> {
    private VirtualView virtualView;
    private LoginHandler loginHandler;
    private Maps maps;
    private GameTable gt;
    private TurnManager turnManager;
    private int valueOfMapChosen;
    private int numberOfSkullChosen;
    private SetupResponse setupResponse;

    public SetupController(VirtualView virtualView, LoginHandler loginHandler){
        this.virtualView = virtualView;
        this.loginHandler = loginHandler;
        maps = new Maps();
    }

    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_SETUP)){
            loginHandler.setInSetupState(false);
            SetupRequest setup = (SetupRequest)deserialize(event.getMessageInJsonFormat(), SetupRequest.class);
            valueOfMapChosen = setup.getMapChosen();
            numberOfSkullChosen = setup.getNrOfSkullChosen();
            setModel(setup);
            setController();

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

    //NOT TO BE TESTED
    private void setModel(SetupRequest setup){
        gt = new GameTable(maps.getMaps()[setup.getMapChosen()], setup.getNrOfSkullChosen());
        turnManager = new TurnManager(gt);
        loginHandler.setTurnManager(turnManager);
        gt.setPlayersBeforeStart(loginHandler.getSessions());
    }

    /**
     * Initialize the controller
     *
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private void setController(){
        CreateJson createJson = new CreateJson(turnManager);
        IdConverter idConverter = new IdConverter(gt);
        HostNameCreateList hostNameCreateList = new HostNameCreateList(turnManager);
        DistanceDictionary distance = new DistanceDictionary(gt.getMap());

        EndTurnController endTurnController = new EndTurnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        PayAmmoController payAmmoController = new PayAmmoController(createJson);
        GrabController grabController = new GrabController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);
        MoveController moveController = new MoveController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        PowerUpController powerUpController = new PowerUpController(turnManager);
        ReloadController reloadController = new ReloadController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);
        ShootController shootController = new ShootController(turnManager, idConverter, virtualView, createJson, hostNameCreateList, distance);
        SpawnController spawnController = new SpawnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);

        virtualView.addObserver(endTurnController);
        virtualView.addObserver(grabController);
        virtualView.addObserver(moveController);
        virtualView.addObserver(powerUpController);
        virtualView.addObserver(reloadController);
        virtualView.addObserver(shootController);
        virtualView.addObserver(spawnController);
    }

    /**
     * Generates the light model of the map chosen
     *
     * @param mapChosen integer that rapresent the map chosen by the player
     * @return map light model
     * @author: Luca Iovine
     */

    private MapLM getMapLMChosen(int mapChosen){
        MapLM mapLM;
        int rowCounter = 0;
        int colCounter;
        SquareLM[][] mapLMTmp = new SquareLM[3][4];
        Maps maps = new Maps();
        List<Integer> idPlayersOnSquare;
        List<WeaponLM> weaponLMList;
        int[] reloadCostInteger;
        int[] buyCostInteger;

        for(Square[] row: maps.getMaps()[mapChosen]){
            colCounter = 0;
            for(Square col: row){
                if(col != null) {
                    //ID giocatori sul quadrato
                    idPlayersOnSquare = new ArrayList<>();
                    for(Player p: col.getPlayerOnSquare()){
                        idPlayersOnSquare.add(p.getIdPlayer());
                    }
                    if (col.getSquareType().equals(SquareType.SPAWNING_POINT)) {
                        //Lista WeaponLM
                        SpawningPoint sp = (SpawningPoint)col;
                        weaponLMList = new ArrayList<>();

                        /*
                            Crezione della lista di weapon light model sul quadrato in questione
                         */
                        for(WeaponCard weapon: sp.getWeaponCards()) {
                            reloadCostInteger = new int[weapon.getreloadCost().size()];
                            for(int i = 0; i < weapon.getreloadCost().size(); i++){
                                reloadCostInteger[i] = AmmoType.intFromAmmoType(weapon.getreloadCost().get(i));
                            }
                            buyCostInteger = new int[weapon.getBuyCost().size()];
                            for(int i = 0; i < weapon.getBuyCost().size(); i++){
                                buyCostInteger[i] = AmmoType.intFromAmmoType(weapon.getBuyCost().get(i));
                            }

                            weaponLMList.add(new WeaponLM(weapon.getIdCard(), weapon.getName(), weapon.getDescription(), reloadCostInteger, buyCostInteger));
                        }

                            mapLMTmp[rowCounter][colCounter] = new SpawnPointLM(idPlayersOnSquare, weaponLMList,
                                col.getIsBlockedAtNorth(), col.getIsBlockedAtEast(), col.getIsBlockedAtSouth(), col.getIsBlockedAtWest(), col.getIdRoom());
                    } else {
                        AmmoCardLM ammoCardLM;
                        AmmoPoint ap = (AmmoPoint)col;
                        AmmoCard ammo = ap.getAmmoCard();
                        if(ammo.hasPowerUp())
                            ammoCardLM = new AmmoCardLM(ammo.getIdCard(), ammo.getAmmo().get(0), ammo.getAmmo().get(1));
                        else
                            ammoCardLM = new AmmoCardLM(ammo.getIdCard(), ammo.getAmmo().get(0), ammo.getAmmo().get(1), ammo.getAmmo().get(2));

                        mapLMTmp[rowCounter][colCounter] = new AmmoPointLM(idPlayersOnSquare, ammoCardLM,
                                col.getIsBlockedAtNorth(), col.getIsBlockedAtEast(), col.getIsBlockedAtSouth(), col.getIsBlockedAtWest(), col.getIdRoom());

                    }
                }
                else
                    mapLMTmp[rowCounter][colCounter] = null;

                colCounter++;
            }
            rowCounter++;
        }
        mapLM = new MapLM(mapLMTmp);

        return mapLM;
    }

    /**
     * Generates the list of light models of the players in game
     *
     * @return players light model
     * @author: Luca Iovine
     */

    private PlayerDataLM[] getPlayerLMList(){
        int counter = 0;
        PlayerDataLM tmpPlayerLM;
        PlayerDataLM[] playerDataLMList = new PlayerDataLM[gt.getPlayers().length];

        for(Player p: gt.getPlayers()){
            /*
                Creazione unloaded weapon lm
             */
            List<WeaponLM> weaponLMList = new ArrayList<>();
            int[] reloadCostInteger;
            int[] buyCostInteger;

            for(WeaponCard weapon: p.getUnloadedWeapons()) {
                reloadCostInteger = new int[weapon.getreloadCost().size()];
                for(int i = 0; i < weapon.getreloadCost().size(); i++){
                    reloadCostInteger[i] = AmmoType.intFromAmmoType(weapon.getreloadCost().get(i));
                }
                buyCostInteger = new int[weapon.getBuyCost().size()];
                for(int i = 0; i < weapon.getBuyCost().size(); i++){
                    buyCostInteger[i] = AmmoType.intFromAmmoType(weapon.getBuyCost().get(i));
                }

                weaponLMList.add(new WeaponLM(weapon.getIdCard(), weapon.getName(), weapon.getDescription(), reloadCostInteger, buyCostInteger));
            }
            /*
                Creazione player
             */
            tmpPlayerLM = new PlayerDataLM(p.getIdPlayer(), p.getCharaName(),
                    weaponLMList, p.getRedAmmo(), p.getBlueAmmo(), p.getYellowAmmo(),
                    p.getNumberOfSkulls(), p.isActive(), p.isPlayerDown(), p.getDamageLine(),
                    p.getMarkLine());

            playerDataLMList[counter] = tmpPlayerLM;
            counter++;
        }

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
     * @param p is the player who hold the loaded weapon
     * @return a list of MyLoadedWeaponsLM
     * @author: Luca Iovine
     */
    private MyLoadedWeaponsLM getLoadedWeapon(Player p) {
        List<WeaponLM> weaponLMList = new ArrayList<>();
        int[] reloadCostInteger;
        int[] buyCostInteger;

        for (WeaponCard weapon : p.getLoadedWeapons()) {
            reloadCostInteger = new int[weapon.getreloadCost().size()];
            for (int i = 0; i < weapon.getreloadCost().size(); i++) {
                reloadCostInteger[i] = AmmoType.intFromAmmoType(weapon.getreloadCost().get(i));
            }
            buyCostInteger = new int[weapon.getBuyCost().size()];
            for (int i = 0; i < weapon.getBuyCost().size(); i++) {
                buyCostInteger[i] = AmmoType.intFromAmmoType(weapon.getBuyCost().get(i));
            }

            weaponLMList.add(new WeaponLM(weapon.getIdCard(), weapon.getName(), weapon.getDescription(), reloadCostInteger, buyCostInteger));
        }
        return new MyLoadedWeaponsLM(weaponLMList);
    }

    /**
     * @param p is the player who hold the loaded weapon
     * @return a list of MyPowerUpLM
     * @author: Luca Iovine
     */
    private MyPowerUpLM getMyPowerUpLM(Player p){
        List<PowerUpLM> myPowerUp = new ArrayList<>();

        for(PowerUp pu: p.getPowerUps())
            myPowerUp.add(new PowerUpLM(pu.getIdCard(), pu.getName(), pu.getDescription(), pu.getGainAmmoColor()));

        return new MyPowerUpLM(myPowerUp);
    }

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
        List<SetupResponse> setupResponseList = new ArrayList<>();

        PlayerDataLM[] playerLMList = getPlayerLMList();
        MapLM mapLM = getMapLMChosen(valueOfMapChosen);
        KillshotTrackLM killshotTrackLM = new KillshotTrackLM(gt.getKillshotTrack(), numberOfSkullChosen);

        if(!loginHandler.isGameStarted())
            currentPlayer = loginHandler.getSessions().get(0).getUsername();
        else
            currentPlayer = turnManager.getCurrentPlayer().getCharaName();

        MyLoadedWeaponsLM myLoadedWeaponsLM = getLoadedWeapon(getPlayer(hostname));
        MyPowerUpLM myPowerUpLM = getMyPowerUpLM(getPlayer(hostname));
        myID = getPlayer(hostname).getIdPlayer();

        return new SetupResponse(currentPlayer, myID, playerLMList, mapLM, killshotTrackLM, hostname, myLoadedWeaponsLM, myPowerUpLM);
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
        Gson gsonReader = new Gson();
        String serializedObj = gsonReader.toJson(objToSerialize, objToSerialize.getClass());

        return serializedObj;
    }

    /**
     * Initialize the model based on the user choice of the map and number of skull
     *
     * @param setup
     * @author: Luca Iovine
     */
}
