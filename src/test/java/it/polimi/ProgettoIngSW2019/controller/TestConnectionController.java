package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.LoginResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TestConnectionController {
    private final String username = "Pinco";
    private final String password = "Pallo";
    private final String hostname = "PincoPallo";
    private ClientMessageReceiver clientMsgRcv;
    private LoginHandler loginHandler;
    private VirtualView testVW;
    private ConnectionController conn;
    private LoginRequest testLoginRequest;
    private ArgumentCaptor<Event> eventCapture = ArgumentCaptor.forClass(Event.class);
    private ArgumentCaptor<List<String>> hostnameListCapture = ArgumentCaptor.forClass(List.class);
    private LoginResponse testLoginResponse;

    @Before
    public void setup(){
        /*
            Inizializzazione
         */
        clientMsgRcv = mock(ClientMessageReceiver.class);
        loginHandler = spy(LoginHandler.class);
        testVW = spy(VirtualView.class);
        conn = new ConnectionController(testVW, loginHandler);

        testVW.addObserver(conn);
        testVW.registerMessageReceiver(hostname, clientMsgRcv);

        testLoginRequest = new LoginRequest(username, password, hostname);
    }

    @Test
    public void RequestGameIsStartedTrueTest(){
        /*
            Mock del login handler: quando viene richiesto se il gioco è iniziato ritorna true
         */
        when(loginHandler.isGameStarted()).thenReturn(true);

        /*
            Simulazione inoltro evento da client verso il connection controller
         */
        Event testEventRequest = new Event(EventType.REQUEST_GAME_IS_STARTED, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);

        /*
            Intercetta i parametri sul mock della virtual view
         */
        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        /*
            Controlla che ciò che è arrivato sulla virtual view sia coerente
         */
        testLoginResponse = (LoginResponse)deserialize(eventCapture.getValue().getMessageInJsonFormat(), LoginResponse.class);

        assertEquals(EventType.RESPONSE_GAME_IS_STARTED, eventCapture.getValue().getCommand());
        assertTrue(testLoginResponse.isLoginSuccessfull());


    }

    @Test
    public void RequestGameIsStartedFalseTest(){
        Event testEventRequest = new Event(EventType.REQUEST_GAME_IS_STARTED, serialize(testLoginRequest));

        when(loginHandler.isGameStarted()).thenReturn(false);

        testVW.forwardEvent(testEventRequest);

        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        testLoginResponse = (LoginResponse)deserialize(eventCapture.getValue().getMessageInJsonFormat(), LoginResponse.class);

        assertEquals(EventType.RESPONSE_GAME_IS_STARTED, eventCapture.getValue().getCommand());
        assertFalse(testLoginResponse.isLoginSuccessfull());


    }

    @Test
    public void RequestLoginBeforeStartValidUsernameTest(){
        Event testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));

        when(loginHandler.isGameStarted()).thenReturn(false);

        testVW.forwardEvent(testEventRequest);

        assertEquals(1, loginHandler.getSessions().size());
        assertEquals(username, loginHandler.getSessions().get(0).getUsername());
        assertEquals(password, loginHandler.getSessions().get(0).getPassword());
        assertEquals(hostname, loginHandler.getSessions().get(0).gethostname());
    }

    @Test
    public void RequestLoginBeforeStartNOTValidUsernameTest(){
        Event testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));

        when(loginHandler.isGameStarted()).thenReturn(false);

        testVW.forwardEvent(testEventRequest);

        /*
            Reset(Mock) ti permette di "annullare" la chiamata precedente dello stesso mock per
            poter effettuare la verify correttamente sulla seconda chiamata
         */
        reset(testVW);
        testVW.forwardEvent(testEventRequest);

        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        testLoginResponse = (LoginResponse)deserialize(eventCapture.getValue().getMessageInJsonFormat(), LoginResponse.class);

        assertEquals(1, loginHandler.getSessions().size());
        assertEquals(username, loginHandler.getSessions().get(0).getUsername());
        assertEquals(password, loginHandler.getSessions().get(0).getPassword());
        assertEquals(hostname, loginHandler.getSessions().get(0).gethostname());
        assertEquals(EventType.RESPONSE_NEW_LOGIN, eventCapture.getValue().getCommand());
        assertFalse(testLoginResponse.isLoginSuccessfull());
    }

    @Test
    public void RequestLoginAfterStartValidCredentialTest(){
        Event testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        /*
            Primo inserimento
         */
        when(loginHandler.isGameStarted()).thenReturn(false);
        testVW.forwardEvent(testEventRequest);
        /*
            Controllo riconnessione
         */
        reset(loginHandler, testVW);
        when(loginHandler.isGameStarted()).thenReturn(true);
        testVW.forwardEvent(testEventRequest);

        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        LoginResponse testLoginResponse = (LoginResponse)deserialize(eventCapture.getValue().getMessageInJsonFormat(), LoginResponse.class);

        assertEquals(EventType.RESPONSE_RECONNECT, eventCapture.getValue().getCommand());
        assertTrue(testLoginResponse.isLoginSuccessfull());
    }

    @Test
    public void RequestLoginAfterStartNOTValidCredentialTest(){
        /*
            Primo inserimento
         */
        Event testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        when(loginHandler.isGameStarted()).thenReturn(false);
        testVW.forwardEvent(testEventRequest);
        /*
            Controllo riconnessione
         */
        testLoginRequest = new LoginRequest(username, "pwe", hostname);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));

        reset(loginHandler, testVW);
        when(loginHandler.isGameStarted()).thenReturn(true);

        testVW.forwardEvent(testEventRequest);

        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        LoginResponse testLoginResponse = (LoginResponse)deserialize(eventCapture.getValue().getMessageInJsonFormat(), LoginResponse.class);

        assertEquals(EventType.RESPONSE_RECONNECT, eventCapture.getValue().getCommand());
        assertFalse(testLoginResponse.isLoginSuccessfull());
    }

    @Test
    public void RequestLoginAfterStartCapReachedTest(){
        /*
            Primo inserimento
         */
        Event testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        when(loginHandler.isGameStarted()).thenReturn(false);
        testVW.forwardEvent(testEventRequest);

        //Secondo
        testLoginRequest = new LoginRequest("a", "a", "a");
        testVW.registerMessageReceiver("a", clientMsgRcv);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);
        //Terzo
        testLoginRequest = new LoginRequest("b", "b", "b");
        testVW.registerMessageReceiver("b", clientMsgRcv);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);
        //Quarto
        testLoginRequest = new LoginRequest("c", "c", "c");
        testVW.registerMessageReceiver("c", clientMsgRcv);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);
        //Quinto
        testLoginRequest = new LoginRequest("d", "d", "d");
        testVW.registerMessageReceiver("d", clientMsgRcv);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);

        reset(loginHandler, testVW);
        when(loginHandler.isGameStarted()).thenReturn(true);

        //Sesto --> ecceessivo
        testLoginRequest = new LoginRequest("f", "f", "f");
        testVW.registerMessageReceiver("f", clientMsgRcv);
        testEventRequest = new Event(EventType.REQUEST_LOGIN, serialize(testLoginRequest));
        testVW.forwardEvent(testEventRequest);

        verify(testVW).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        assertEquals(EventType.CAP_REACHED, eventCapture.getValue().getCommand());
    }

    private String serialize(Object objToSerialize){
        Gson gsonReader = new Gson();
        String serializedObj = gsonReader.toJson(objToSerialize, objToSerialize.getClass());

        return serializedObj;
    }

    private Object deserialize(String json, Class<?> cls){
        Gson gsonReader = new Gson();
        Object deserializedObj = gsonReader.fromJson(json, cls);

        return deserializedObj;
    }
}
