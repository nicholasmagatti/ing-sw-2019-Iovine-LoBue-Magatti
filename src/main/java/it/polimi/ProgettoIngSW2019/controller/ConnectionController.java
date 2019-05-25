package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Message.LoginInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.model.Session;

public class ConnectionController implements Observer<Event> {
    LoginInfo loginInfo;
    Gson gson = new Gson();
    LoginHandler loginHandler;

    public ConnectionController(LoginHandler loginHandler){
        this.loginHandler = loginHandler;
    }

    /**
     * Handle the event sent by the virtual view.
     * When LOGIN event happen, create a new session and bind it to the client.
     * When CHECK_USERNAME_AVIABLITY event happen, check if the username can be used or not
     *
     * @param event contain the data
     * @author: Luca Iovine
     */
    public void update(Event event) {
        if(event.getCommand() == EventType.LOGIN) {
            deserializeInfo(event.getMessageInJsonFormat());
            createNewLogin(loginInfo.getUsername(), loginInfo.getHostname());
        }
        if(event.getCommand() == EventType.CHECK_USERNAME_AVIABLITY){
            String username = event.getMessageInJsonFormat();
            if(this.loginHandler.isUsernameAviable(username)){
                //TODO: notify che è disponibile, la view a questo punto genererà un commando di login
            }
            else{
                //TODO: notify che non è disponibile, la view a questo punto richiederà il nome utente
            }
        }
    }

    /**
     * Create a new session and notify it to the client which have been connected
     *
     * @param username of the player
     * @param hostname of player's client
     * @author: Luca Iovine
     */
    private void createNewLogin(String username, String hostname){
        Session session = null;

        if(loginHandler.isLogged(username, hostname))
            session = loginHandler.getSession(username);
        else
            session = loginHandler.newSession(username, hostname);

        //TODO: deve poi essere notificata la sessione al client
    }

    /**
     * Deserialize the login info which come in json form
     *
     * @param jsonLoginInfo string in json format which contain the login info
     * @author: Luca Iovine
     */
    private void deserializeInfo(String jsonLoginInfo){
        loginInfo = gson.fromJson(jsonLoginInfo, loginInfo.getClass());
    }
}
