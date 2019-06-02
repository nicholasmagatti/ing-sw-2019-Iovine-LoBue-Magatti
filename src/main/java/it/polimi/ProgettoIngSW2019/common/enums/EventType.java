package it.polimi.ProgettoIngSW2019.common.enums;

public enum EventType {
    //Request View to Controller
    REQUEST_USE_POWERUP,
    REQUEST_POWERUP_AMMO,
    REQUEST_SPAWN_CARDS,                             //toController: request draw 1 card to spawn
    REQUEST_SPAWN,                                   //toController: request spawn
    REQUEST_INITIAL_SPAWN_CARDS,                     //toController: request draw 2 card to spawn
    REQUEST_WEAPONS_CAN_RELOAD,                      //toController: request weapons can be reloaded
    REQUEST_RELOAD,                                  //toController: request the reload of a specific weapon

    //TODO: CHANGE NAME
    REQUEST_ENDTURN_INFO,                            //toController: in the end turn state score point

    //Response Controller to View
    RESPONSE_REQUEST_USE_POWERUP,
    RESPONSE_REQUEST_POWERUP_AMMO,
    RESPONSE_REQUEST_SPAWN_CARDS,                   //toView: response draw card to spawn
    RESPONSE_REQUEST_SPAWN,                         //toView: response spawn (new map changed)
    RESPONSE_REQUEST_INITIAL_SPAWN_CARDS,           //toView: response draw 2 cards to spawn
    RESPONSE_REQUEST_WEAPONS_CAN_RELOAD,            //toView: response with a list of weapons can be reloaded

    //TODO: change name
    RESPONSE_REQUEST_ENDTURN_INFO,                  //toController:

    //Other things XD
    SCORE_DEAD_PLAYERS,
    LOGIN,
    CHECK_USERNAME_AVIABLITY,
    INPUT_TIME_EXPIRED,

    //Update LM from Controller
    UPDATE_MY_POWERUPS,                             //toView: send obj MyPowerUpsLM
    UPDATE_MY_LOADED_WEAPONS,                       //toView: send obj MyLoadedWeaponsLM
    UPDATE_PLAYER_INFO,                             //toView: send obj PlayerDataLM

    //messages from Controller
    MSG_ENEMY_DRAW_POWERUP,                         //msg to send to enemies: player draw cards
    MSG_DRAW_MY_POWERUP,                            //msg to send to the player: draw card
    MSG_POWERUPS_DISCARDED_AS_AMMO,                 //msg to send to all with the powerUps discarded for ammo
    MSG_ENEMY_SPAWN,                                //msg to send with all enemy spawn info
    MSG_ENEMY_RELOAD_WEAPON,
    MSG_MY_RELOAD_WEAPON,
}
