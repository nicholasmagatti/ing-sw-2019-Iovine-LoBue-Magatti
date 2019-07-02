package it.polimi.ProgettoIngSW2019.controller;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestController {
    private ClientMessageReceiver clientMsgRcv;
    private final String hostname0 = "PincoHost";
    private final String hostname1 = "PalloHost";
    private final String host = "host";

    private Controller controller;
    private GameTable gameTable;
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private HostNameCreateList hostNameCreateList;

    private ArgumentCaptor<Event> eventCapture = ArgumentCaptor.forClass(Event.class);
    private ArgumentCaptor<List<String>> hostnameListCapture = ArgumentCaptor.forClass(List.class);

    private Player player0, player1;
    private PowerUp powerUp1;
    private WeaponCard weaponCard1, weaponCard2, weaponCard3;

    private DistanceDictionary distanceDictionary;


    @Before
    public void setUp() {
        Maps maps = new Maps();
        distanceDictionary = new DistanceDictionary(maps.getMaps()[0]);

        //cards
        powerUp1 = new PowerUp(21, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        weaponCard1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK RIFLE","", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json", distanceDictionary);
        weaponCard2 = new WeaponCard(1, DeckType.WEAPON_CARD, "ELECTROSCYTHE", "", Arrays.asList(AmmoType.BLUE), "ElectroSchyteEff.json", distanceDictionary);
        weaponCard3 = new WeaponCard(2, DeckType.WEAPON_CARD, "MACHINE GUN", "", Arrays.asList(AmmoType.BLUE, AmmoType.RED), "MachineGunEff.json", distanceDictionary);

        //Player
        player0 = mock(Player.class);
        player1 = mock(Player.class);

        when(player0.getIdPlayer()).thenReturn(0);
        when(player0.getHostname()).thenReturn(hostname0);
        when(player0.getCharaName()).thenReturn("Pinco");
        when(player0.getBlueAmmo()).thenReturn(3);
        when(player0.getRedAmmo()).thenReturn(3);
        when(player0.getYellowAmmo()).thenReturn(3);
        when(player0.getUnloadedWeapons()).thenReturn(Arrays.asList(weaponCard1, weaponCard2));
        when(player0.isActive()).thenReturn(true);
        when(player0.isPlayerDown()).thenReturn(false);
        when(player0.getPowerUps()).thenReturn(Arrays.asList(powerUp1));
        when(player0.getNumberOfSkulls()).thenReturn(3);
        when(player0.getDamageLine()).thenReturn(Arrays.asList("Priscilla", "Luca"));
        when(player0.getMarkLine()).thenReturn(Arrays.asList("Luca", "Luca", "Nick"));
        when(player0.getLoadedWeapons()).thenReturn(Arrays.asList(weaponCard3));
        when(player0.getNumberOfPoweUps()).thenReturn(1);

        when(player1.getIdPlayer()).thenReturn(1);
        when(player1.getHostname()).thenReturn(hostname1);
        when(player1.getCharaName()).thenReturn("Pallo");

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;


       clientMsgRcv = mock(ClientMessageReceiver.class);

       gameTable = spy(new GameTable(maps.getMaps()[0],5));
       when(gameTable.getPlayers()).thenReturn(players);

       turnManager = mock(TurnManager.class);
       when(turnManager.getGameTable()).thenReturn(gameTable);
       when(turnManager.getCurrentPlayer()).thenReturn(player0);

       virtualView = spy(VirtualView.class);
       idConverter = spy(new IdConverter(gameTable));
       createJson = new CreateJson(turnManager);

       hostNameCreateList = mock(HostNameCreateList.class);
       when(hostNameCreateList.addOneHostName(player0)).thenReturn(Arrays.asList(hostname0));
       when(hostNameCreateList.addOneHostName(player1)).thenReturn(Arrays.asList(hostname1));
       when(hostNameCreateList.addAllHostName()).thenReturn(Arrays.asList(hostname0, hostname1));

       controller = new SpawnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);

       virtualView.addObserver(controller);
       virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
       virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
       virtualView.registerMessageReceiver(host, clientMsgRcv);
    }


    @Test
    public void sendInfoTest() {
        String messageTest = "Test";
        EventType eventType = EventType.TEST;
        List<String> hostName = Arrays.asList(hostname0);
        controller.sendInfo(eventType, messageTest, hostName);

        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        String messageTestReceived = new Gson().fromJson(eventCapture.getValue().getMessageInJsonFormat(), String.class);

        assertEquals(eventType, eventCapture.getValue().getCommand());
        assertEquals(messageTest, messageTestReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void convertPlayerCorrectTest() {
        Player playerCreated = controller.convertPlayer(player0.getIdPlayer(), hostname0);

        assertEquals(player0.getIdPlayer(), playerCreated.getIdPlayer());
        assertEquals(player0.getCharaName(), playerCreated.getCharaName());
        assertEquals(player0.getHostname(), playerCreated.getHostname());
    }


    @Test
    public void convertPlayerWrongTest() {
        Player playerCreated = controller.convertPlayer(3, hostname0);

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertNull(playerCreated);
        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void convertPlayerWithIdCorrectTest() {
        Player playerCreated = controller.convertPlayerWithId(player0.getIdPlayer());

        assertEquals(player0.getIdPlayer(), playerCreated.getIdPlayer());
        assertEquals(player0.getCharaName(), playerCreated.getCharaName());
        assertEquals(player0.getHostname(), playerCreated.getHostname());
    }


    @Test
    public void convertPlayerWithIdWrongTest() {
        Player playerCreated = controller.convertPlayer(3, hostname0);
        assertNull(playerCreated);
    }


    @Test
    public void convertPowerUpCorrectTest() {
        PowerUp powerUpCreated = controller.convertPowerUp(player0, powerUp1.getIdCard());

        assertEquals(powerUp1.getIdCard(), powerUpCreated.getIdCard());
        assertEquals(powerUp1.getName(), powerUpCreated.getName());
        assertEquals(powerUp1.getGainAmmoColor(), powerUpCreated.getGainAmmoColor());
    }



    @Test
    public void convertPowerUpWrongTest() {
        PowerUp powerUpCreated = controller.convertPowerUp(player0, 2);

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertNull(powerUpCreated);
        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void convertWeaponCorrectTest() {
        WeaponCard weaponCardCreated = controller.convertWeapon(player0, weaponCard1.getIdCard(), true);

        assertEquals(weaponCard1.getIdCard(), weaponCardCreated.getIdCard());
        assertEquals(weaponCard1.getName(), weaponCardCreated.getName());
    }


    @Test
    public void convertWeaponWrongTest() {
        WeaponCard weaponCardCreated = controller.convertWeapon(player0, 60, true);

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertNull(weaponCardCreated);
        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void convertSquareCorrectTest() {
        int[] coordinates = new int[2];
        coordinates[1] = 2;
        Square squareCreated = controller.convertSquare(player0, coordinates);

        assertNotNull(squareCreated);

        for(int i = 0; i < 2; i++) {
            System.out.println("Here");
            assertEquals(coordinates[i], squareCreated.getCoordinates(gameTable.getMap())[i]);
        }

        assertEquals(SquareType.SPAWNING_POINT, squareCreated.getSquareType());
    }


    @Test
    public void convertSquareWrongTest() {
        int[] coordinates = new int[2];
        coordinates[1] = 5;
        Square squareCreated = controller.convertSquare(player0, coordinates);

        assertNull(squareCreated);
        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkHostNameCorrectTest() {
        assertTrue(controller.checkHostNameCorrect(player0, hostname0));
    }


    @Test
    public void checkHostNameWrongTest() {
        assertFalse(controller.checkHostNameCorrect(player0, hostname1));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname1, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkIdCorrectTest() {
        assertTrue(controller.checkIdCorrect(player0, player0.getIdPlayer(), hostname0));
    }


    @Test
    public void checkIdWrongTest() {
        assertFalse(controller.checkIdCorrect(player0, player1.getIdPlayer(), hostname1));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname1, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkCurrentPlayerCorrectTest() {
        assertTrue(controller.checkCurrentPlayer(player0));
    }


    @Test
    public void checkCurrentPlayerWrongTest() {
        assertFalse(controller.checkCurrentPlayer(player1));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname1, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkNoActionLeftCorrectTest() {
        when(turnManager.getActionsLeft()).thenReturn(0);
        assertTrue(controller.checkNoActionLeft(player0));
    }


    @Test
    public void checkNoActionLeftWrongTest() {
        when(turnManager.getActionsLeft()).thenReturn(1);
        assertFalse(controller.checkNoActionLeft(player0));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkHasActionLeftCorrectTest() {
        when(turnManager.getActionsLeft()).thenReturn(1);
        assertTrue(controller.checkHasActionLeft(player0));
    }


    @Test
    public void checkHasActionLeftWrongTest() {
        when(turnManager.getActionsLeft()).thenReturn(0);
        assertFalse(controller.checkHasActionLeft(player0));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkContainsUnloadedWeaponCorrectTest() {
        assertTrue(controller.checkContainsWeapon(player0, weaponCard1, true));
    }


    @Test
    public void checkContainsLoadedWeaponCorrectTest() {
        assertTrue(controller.checkContainsWeapon(player0, weaponCard3, false));
    }


    @Test
    public void checkContainsUnloadedWeaponWrongTest() {
        assertFalse(controller.checkContainsWeapon(player0, weaponCard3, true));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkContainsLoadedWrongTest() {
        assertFalse(controller.checkContainsWeapon(player0, weaponCard1, false));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(hostname0, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void checkHasEnoughAmmoCorrectTest() {
        List<AmmoType> ammoToPay = Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW, AmmoType.YELLOW);
        Player player2 = spy(new Player(0, "Nome1", gameTable, "host"));
        player2.getPowerUps().add(powerUp1);
        assertTrue(controller.checkHasEnoughAmmo(player2, ammoToPay));
    }


    @Test
    public void checkHasEnoughAmmoWrongTest() {

        List<AmmoType> ammoToPay = Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW, AmmoType.YELLOW);
        Player player2 = spy(new Player(0, "Nome1", gameTable, host));
        when(hostNameCreateList.addOneHostName(player2)).thenReturn(Arrays.asList(host));

        assertFalse(controller.checkHasEnoughAmmo(player2, ammoToPay));

        String messageError = GeneralInfo.MSG_ERROR;
        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        String messageErrorReceived = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(messageError, messageErrorReceived);
        assertEquals(host, hostnameListCapture.getValue().get(0));
    }


    @Test
    public void msgActionLeftTest() {
        when(turnManager.getActionsLeft()).thenReturn(2);
        when(turnManager.getCurrentPlayer()).thenReturn(player0);

        controller.msgActionLeft(player0);

        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        MessageActionLeft messageActionLeftCreated = new Gson().fromJson(eventCapture.getValue().getMessageInJsonFormat(), MessageActionLeft.class);

        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, eventCapture.getValue().getCommand());
        assertTrue(messageActionLeftCreated.getPowerUpsCanUse().isEmpty());
        assertEquals(2, messageActionLeftCreated.getnActionsLeft());
    }


    @Test
    public void msgActionLeftBisTest() {
        List<PowerUp> powerUps = new ArrayList<>();
        PowerUp powerUp = new PowerUp(5, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.NEWTON, "", new NewtonEff());
        powerUps.add(powerUp1);
        powerUps.add(powerUp);

        when(player0.getPowerUps()).thenReturn(powerUps);

        when(turnManager.getActionsLeft()).thenReturn(2);
        when(turnManager.getCurrentPlayer()).thenReturn(player0);

        controller.msgActionLeft(player0);

        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        MessageActionLeft messageActionLeftCreated = new Gson().fromJson(eventCapture.getValue().getMessageInJsonFormat(), MessageActionLeft.class);

        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, eventCapture.getValue().getCommand());
        assertEquals(2, messageActionLeftCreated.getnActionsLeft());
        assertEquals(powerUp.getIdCard(), messageActionLeftCreated.getPowerUpsCanUse().get(0).getIdPowerUp());
    }



    @Test
    public void illegalAttributeExceptionTest() {
        try {
            Player player = controller.convertPlayer(-1, hostname0);
            fail();
        }
        catch(IllegalAttributeException e){
            System.out.println(e);
        }

        try {
            Player player = controller.convertPlayerWithId(-1);
            fail();
        }
        catch(IllegalAttributeException e){
            System.out.println(e);
        }

        try {
            PowerUp powerUp = controller.convertPowerUp(player0, -1);
            fail();
        }
        catch(IllegalAttributeException e){
            System.out.println(e);
        }

        try {
            WeaponCard weaponCard = controller.convertWeapon(player0, -1, true);
            fail();
        }
        catch(IllegalAttributeException e){
            System.out.println(e);
        }
    }


    @Test
    public void nullPointerExceptionTest() {
        try {
            PowerUp powerUp = controller.convertPowerUp(null, 36);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            WeaponCard weaponCard = controller.convertWeapon(null, 0, true);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            Square square = controller.convertSquare(null, new int[2]);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            boolean check = controller.checkCurrentPlayer(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            boolean check = controller.checkNoActionLeft(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            boolean check = controller.checkHasActionLeft(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            boolean check = controller.checkContainsWeapon(null, weaponCard3, false);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            boolean check = controller.checkHasEnoughAmmo(null, Arrays.asList(AmmoType.YELLOW));
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }


    @Test
    public void nullPointerWeaponTest() {
        try {
            boolean check = controller.checkContainsWeapon(player0, null, false);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }
}
