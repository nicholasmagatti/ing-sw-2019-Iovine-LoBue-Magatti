package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.LoginResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Nicholas Magatti
 */
public class LoginState extends State{

    private boolean ongoingGame;
    private boolean succesfullLogin;
    private String name;
    private String password;
    private String hostname;
    private SetupInfo setupInfo = null;
    private IdleState idleState;

    private SetupGameState setupGameState;
    private boolean isLocalOnly = false;

    /**
     * Constructor
     * @param setupGameState
     */
    public LoginState(SetupGameState setupGameState, IdleState idleState){
        this.idleState = idleState;
        this.setupGameState = setupGameState;
    }

    /**
     * Try to get the hostname. Send the hostname to the server in case of success,
     * asking if there is an ongoing game or not; print an error otherwise.
     */
    @Override
    public void startState(){
        //try to get the name of the host
        try {
            if(!isLocalOnly)
                hostname = InetAddress.getLocalHost().getHostName();
            else
                hostname = "host" + new Random().nextInt(10000);

            System.out.println(hostname);
            System.out.println("##########  WELCOME ON ADRENALINE!  ###########\n");
            //notify to server that you want to see if there is an ongoing game or not, sending your hostname
            LoginRequest loginRequest = new LoginRequest("", "", hostname);
            notifyEvent(loginRequest, EventType.REQUEST_GAME_IS_STARTED);

            login(ongoingGame);

            if(!succesfullLogin)
                askCredentials();

            if(ongoingGame){
                StateManager.triggerNextState(idleState);
            }else{
                while(setupInfo == null){
                    ToolsView.waitServerResponse();
                }
                goToGameSetup(setupInfo);
            }

        }
        catch (UnknownHostException e){
            System.out.println("There has been an error with your network.");
        }
    }

    @Override
    public void update(Event event) {
        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if (command == EventType.RESPONSE_GAME_IS_STARTED) {
            LoginResponse ongoingGame = new Gson().fromJson(jsonMessage, LoginResponse.class);
            this.ongoingGame = ongoingGame.isLoginSuccessfull();
        }

        if (command == EventType.RESPONSE_NEW_LOGIN) {
            LoginResponse loginResponse = new Gson().fromJson(jsonMessage, LoginResponse.class);
            succesfullLogin = loginResponse.isLoginSuccessfull();
            if(succesfullLogin){
                System.out.println("You successfully logged in.");
                System.out.println("Wait for the other players...");
            }
            else{
                System.out.println("This name has already been taken. Choose another one.");
            }
        }

        if (command == EventType.RESPONSE_RECONNECT) {
            LoginResponse loginResponse = new Gson().fromJson(jsonMessage, LoginResponse.class);
            succesfullLogin = loginResponse.isLoginSuccessfull();
            if(succesfullLogin){
                System.out.println("You successfully logged in. Welcome back!");
                //inform the SetupGameState that this is a reconnection in an ongoing game
                setupGameState.setInfoForReconnection(name, hostname);
                //ask the server to send me the light model
                LoginRequest msg = new LoginRequest("", "", hostname);
                notifyEvent(msg, EventType.REQUEST_GAME_DATA);
            }
            else{
                System.out.println("Wrong name or password. Write them again.");
            }
        }

        if (command == EventType.CAP_REACHED) {
            System.out.println("The maximum capacity of players have already been reached.");
            System.out.println("You cannot take part to this game. Wait for the next game.");
        }

        if (command == EventType.GO_IN_GAME_SETUP) {
            setupInfo = new Gson().fromJson(jsonMessage, SetupInfo.class);
        }
    }


    /**
     * Clean name and password from previous values.
     */
    private void cleanNameAndPassword(){
        name = "";
        password = "";
    }

    /**
     * Trigger the state of setup with the information needed.
     * @param setupInfo
     */
    void goToGameSetup(SetupInfo setupInfo){
        setupGameState.setInfoBeforeStartGame(name, hostname, setupInfo.getMapLMList(), setupInfo.getUsername());
        StateManager.triggerNextState(setupGameState);
    }

    /**
     * Tell the user if there is an ongoing game or not, then ask him/her to login.
     * @param ongoingGame - boolean that tells if there is an ongoing game
     */
    private void login(boolean ongoingGame){
        this.ongoingGame = ongoingGame;
        if(ongoingGame){
            System.out.println(
                    "There is an ongoing game. Login if you are reconnecting to the game. If not, wait until this game ends.");
        }
        else{
            System.out.println("The game hasn't started yet.");
        }
        askCredentials();
    }

    /**
     * Ask username and password to the user and send them to the server.
     */
    private void askCredentials(){
        cleanNameAndPassword();
        System.out.print("Write the name of your character: ");
        name = readUsername().toLowerCase();
        System.out.print("Write your password: ");
        password = readPassword();
        //notify to server
        System.out.println("Wait a moment...");
        LoginRequest loginRequest = new LoginRequest(name, password, hostname);
        notifyEvent(loginRequest, EventType.REQUEST_LOGIN);
    }

    /**
     * Keep asking the user to insert a name until it is valid and return the valid insertion
     * @return the valid input inserted by the user for the username
     */
    private String readUsername(){
        return readInputWithoutTimer(GeneralInfo.ILLEGAL_CHARACTERS_FOR_USERNAME);
    }

    /**
     * Keep asking the user to insert a password until it is valid and return the valid insertion
     * @return the valid input inserted by the user for the password
     */
    private String readPassword(){
        char [] emptyArray = {};
        return readInputWithoutTimer(emptyArray);
    }

    /**
     * Keep asking the user to insert an input until it is valid and return the valid insertion
     * @param illegalCharacters - the characters that should not be present in the inserted input
     * @return the valid input inserted by the user
     */
    private String readInputWithoutTimer(char[] illegalCharacters){
        Scanner scanner = new Scanner(System.in);
        String inputFromUser;
        final String ILLEGAL_INPUT_MESSAGE = "Illegal input. Insert correct input.";
        boolean exit;
        do{
            exit = true; //it will be set to false if there is an illegal character in the input
            inputFromUser = scanner.nextLine();
            //the string should not be empty or "you" (to avoid doubts when the user read "you")
            if(inputFromUser == null || inputFromUser.isEmpty() || inputFromUser.equalsIgnoreCase("you")){
                exit = false;
                System.out.println(ILLEGAL_INPUT_MESSAGE);
            }
            else{ //check if the input contains illegal characters
                for(char ill_c : illegalCharacters){
                    for(int i=0; i < inputFromUser.length(); i++){
                        if(inputFromUser.charAt(i) == ill_c){
                            exit = false;
                        }
                    }
                }
                if(!exit){ //if the input contains illegal characters
                    System.out.println(ILLEGAL_INPUT_MESSAGE);
                }
            }
        }while(!exit);

        return inputFromUser;
    }

    public void setIsLocalOnly(boolean isLocalOnly){
        this.isLocalOnly = isLocalOnly;
    }
}

