package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.List;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class PowerUpState extends State {

    private String powerupName = "";

    private ActionState actionState;

    public PowerUpState(ActionState actionState){
        this.actionState = actionState;
    }

    public void setPowerupToUse(String powerup) {
        powerupName = powerup;
    }

    @Override
    void startState() {
        switch(powerupName){
            case GeneralInfo.NEWTON:
                //TODO
                break;

            case GeneralInfo.TARGETING_SCOPE:
                //TODO
                break;

            case GeneralInfo.TELEPORTER:
                //TODO
                break;

            case GeneralInfo.TAGBACK_GRENADE:
                //TODO
                break;

            case "":
                throw new RuntimeException("The name of the powerup to use has not been inserted before the " +
                        "call of the startState method.");

                default:
                    throw new RuntimeException("The name of the powerup is " + powerupName +
                            " and does not correspond to any of the names available.");

        }
    }

    @Override
    public void update(Event event) {
        /*TODO: quando ricevo il messaggio che mi dice che l'uso del powerup è andato
            a buon fine, metti "" powerupName e se avevi la grenade vai in state
            null, altrimenti vai in ActionState
         */
    }
}
