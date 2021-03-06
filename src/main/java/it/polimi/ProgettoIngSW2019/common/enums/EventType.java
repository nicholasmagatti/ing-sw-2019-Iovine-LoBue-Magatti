package it.polimi.ProgettoIngSW2019.common.enums;

/**
 * Lables used to indicate the kind of event in the communication between server and client
 */
public enum EventType {
    //Request View to Controller
    REQUEST_SPAWN_CARDS,                             //toController: request draw 1 card to spawn
    REQUEST_SPAWN,                                   //toController: request spawn
    REQUEST_INITIAL_SPAWN,
    REQUEST_INITIAL_SPAWN_CARDS,                     //toController: request draw 2 card to spawn
    REQUEST_WEAPONS_CAN_RELOAD,                      //toController: request weapons can be reloaded
    REQUEST_RELOAD,                                  //toController: request the reload of a specific weapon
    REQUEST_ENDTURN_INFO,                            //toController: in the end turn state score point and the player goes in the endTurn state
    REQUEST_WEAPON_INFO,                             //toController: request info to shoot
    REQUEST_SHOOT,                                   //toController: request choice to shoot
    REQUEST_GRAB,                                    //toController: request choice to grab
    REQUEST_GRAB_INFO,                               //toController: request choice to grab info
    REQUEST_GRAB_WEAPON,                             //toController: request choice to grab a weapon
    REQUEST_LOGIN,
    REQUEST_MOVE_INFO,                              //toController: request info to move
    REQUEST_MOVE,                                   //toController: request choice to move
    REQUEST_SETUP,
    REQUEST_GAME_IS_STARTED,
    REQUEST_GAME_DATA,
    END_TURN_DUE_USER_DISCONNECTION,
    REQUEST_NEWTON_INFO,



    //Response Controller to View
    RESPONSE_REQUEST_SPAWN_CARDS,                   //toView: response draw card to spawn
    RESPONSE_REQUEST_INITIAL_SPAWN_CARDS,           //toView: response draw 2 cards to spawn
    RESPONSE_REQUEST_WEAPONS_CAN_RELOAD,            //toView: response with a list of weapons can be reloaded
    RESPONSE_REQUEST_WEAPON_INFO,                   //toView: response info shoot
    RESPONSE_REQUEST_GRAB_INFO,
    RESPONSE_NEW_LOGIN,
    RESPONSE_RECONNECT,
    RESPONSE_SETUP,
    RESPONSE_GAME_IS_STARTED,
    RESPONSE_REQUEST_MOVE_INFO,                     //toView: response move info
    RESPONSE_GAME_DATA,
    RESPONSE_NEWTON_INFO,


    //Other things XD
    SCORE_DEAD_PLAYERS,                             //toView: send info score at the end of the turn
    INPUT_TIME_EXPIRED,
    ERROR,                                          //toView: send message error
    WEAPONS_CAN_BUY,                                //toView: send the weapons can buy in the sp
    DISCARD_WEAPON,                                 //toView: send to the player his weapon to choose one to discard before buying another
    GO_IN_GAME_SETUP,                               //toView: to alert clients that they need to move into the game setup state
    CHECK_IS_ALIVE,                                 //toView: to verify that the clients are active
    CAP_REACHED,                                    //toView:  warn that the number of players to start the game has been reached
    NOT_ALIVE,                                      //per segnalare che un certo client non è vivo
    USER_HAS_DISCONNECTED,                          //toView: warns other players that a user has logged out
    START_ACTION_TIMER,
    STOP_ACTION_TIMER,
    TELEPORTER,
    NEWTON,
    TAGBACK_GRENADE,
    TARGETING_SCOPE,
    CAN_USE_TARGETING_SCOPE,
    CAN_USE_TAGBACK,
    TEST,



    //Update LM from Controller
    UPDATE_MY_POWERUPS,                             //toView: send obj MyPowerUpsLM
    UPDATE_MY_LOADED_WEAPONS,                       //toView: send obj MyLoadedWeaponsLM
    UPDATE_PLAYER_INFO,                             //toView: send obj PlayerDataLM
    UPDATE_MAP,                                     //toView: update mapLM
    UPDATE_KILLSHOTTRACK,                           //toView: update killShotTrackLM

    //messages from Controller
    MSG_ENEMY_DRAW_POWERUP,                         //msg to send to enemies: player draw cards
    MSG_DRAW_MY_POWERUP,                            //msg to send to the player: draw card
    MSG_POWERUPS_DISCARDED_AS_AMMO,                 //msg to send to all with the powerUps discarded for ammo
    MSG_POWERUP_DISCARDED_TO_SPAWN,
    MSG_ALL_RELOAD_WEAPON,                          //msg to send to all enemy info reload
    MSG_DOUBLEKILL,                                 //msg to send to all players doubleKill
    MSG_NEW_TURN,                                   //msg to all players new turn, changed player
    MSG_WEAPON_BUY,                                 //msg to all players, the player bought the weapon
    MSG_WEAPON_SWAP,                                //msg to all players, the player swapped a weapon card and bought another
    MSG_MY_N_ACTION_LEFT,                           //msg to all player, n action left of the player
    MSG_FIRST_TURN_PLAYER,                          //msg to the player who had to spawn for the first time
    MSG_TARGETING_SCOPE,
    MSG_TAGBACK_GRENADE,
    MSG_BEFORE_ENEMY_ACTION_OR_RELOAD,
    MSG_USE_POWERUP,
    MSG_PLAYER_SPAWN,
    MSG_CONCLUSION,
    MSG_SCORE_ALIVE_PLAYER,
    MSG_SCORE_KILLSHOT_TRACK,
    MSG_FINAL_RESULTS,
}
