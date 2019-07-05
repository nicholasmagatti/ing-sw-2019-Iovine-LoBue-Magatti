package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class TestLoginHandler {
    LoginHandler loginHandler;
    private final String username_1 = "Pippo";
    private final String pwd_1 = "abc";
    private final String hostname_1 = "hostname1";

    private final String username_2 = "Pluto";
    private final String pwd_2 = "def";
    private final String hostname_2 = "hostname2";

    @Before
    public void setup(){
        loginHandler = new LoginHandler();
        loginHandler.generateNewLogin(username_1, pwd_1, hostname_1);
        loginHandler.generateNewLogin(username_2, pwd_2, hostname_2);
    }

    @Test
    public void checkPippoExist(){
        assertTrue(loginHandler.checkUserExist("Pippo"));
    }

    @Test
    public void checkPaperinoDoesNotExist(){
        assertFalse(loginHandler.checkUserExist("Paperino"));
    }

    @Test
    public void checkLoginIsCorrect(){
        GameTable gt = new GameTable(new Maps().getMaps()[0], 5);
        Player p1 = new Player(0, username_1, gt, hostname_1);
        gt.setPlayersBeforeStart(loginHandler.getSessions());
        TurnManager turnManager = spy(new TurnManager(gt));
        loginHandler.setTurnManager(turnManager);

        assertTrue(loginHandler.checkLoginValidity("Pippo", "abc", "hostname1"));
    }

    @Test
    public void checkLoginIsIncorrectWrongPWD(){
        assertFalse(loginHandler.checkLoginValidity("Pippo", "abce", "hostname1"));
    }

    @Test
    public void checkLoginIsIncorrectWrongHostname(){
        assertFalse(loginHandler.checkLoginValidity("Pippo", "abc", "hostname2"));
    }

    @Test
    public void disconnectBeforeGameStart(){
        GameTable gt = new GameTable(new Maps().getMaps()[0], 5);
        Player p1 = new Player(0, username_1, gt, hostname_1);
        gt.setPlayersBeforeStart(loginHandler.getSessions());
        TurnManager turnManager = spy(new TurnManager(gt));
        loginHandler.setTurnManager(turnManager);

        loginHandler.disconnectPlayer(hostname_1);
        assertTrue(loginHandler.getNrOfPlayerConnected() == 1);
        assertFalse(loginHandler.checkLoginValidity(username_1, pwd_1, hostname_1));
        assertFalse(loginHandler.checkUserExist(username_1));
    }
}
