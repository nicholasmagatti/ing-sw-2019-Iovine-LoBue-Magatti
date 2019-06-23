package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SetupRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageConnection;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

public class SetupController implements Observer<Event> {
    private VirtualView virtualView;
    private LoginHandler loginHandler;
    private Maps maps;
    private GameTable gt;
    private TurnManager turnManager;

    public SetupController(VirtualView virtualView, LoginHandler loginHandler){
        this.virtualView = virtualView;
        this.loginHandler = loginHandler;
        maps = new Maps();
    }

    //TESTED
    @Override
    public void update(Event event) {
        /*
            -->
         */
        if(event.getCommand().equals(EventType.REQUEST_SETUP)){
            SetupRequest setup = (SetupRequest)deserialize(event.getMessageInJsonFormat(), SetupRequest.class);
            setModel(setup);
            setController();

            String startUsername = loginHandler.getSessions().get(0).getUsername();
            MessageConnection messageConnection = new MessageConnection(startUsername, "");
            virtualView.sendMessage(new Event(EventType.RESPONSE_SETUP, serialize(messageConnection)), loginHandler.getUsersHostname());
        }
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
    //NOT TO BE TESTED
    private void setModel(SetupRequest setup){
        gt = new GameTable(maps.getMaps()[setup.getMapChosen()], setup.getNrOfSkullChosen());
        turnManager = new TurnManager(gt);
        loginHandler.setTurnManager(turnManager);
        gt.setPlayersBeforeStart(loginHandler.getSessions());

        //TODO: chiedere eventuali setup da far partire
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
}
