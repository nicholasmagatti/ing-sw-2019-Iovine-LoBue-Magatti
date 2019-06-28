package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ShootPowerUpInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class PowerUpState extends State {

    /**
     * @deprecated
     */
    private PowerUpLM powerup = null;
    private ShootPowerUpInfo shootPowerUpInfo = null;

    /**
     *
     * @param powerup
     * @deprecated
     */
    void setPowerupToUse(PowerUpLM powerup) {
        this.powerup = powerup;
    }

    @Override
    void startState() {
        /*
        if(powerup == null){
            throw new Error("Powerup is null: you should have set the powerup to use before trigger the powerUpState.");
        }
        switch(powerup.getName()){
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

                default:
                    throw new RuntimeException("The name of the powerup is " + powerup.getName() +
                            " and does not correspond to any of the names available.");

        }*/
        if(shootPowerUpInfo == null){
            throw new NullPointerException("The info about the powerup to use etc should have " +
                                            "been inserted before calling this method.");
        }

        List<PowerUpLM> usablePowerups = shootPowerUpInfo.getPowerUpUsableList();

        if(powerupsBeforeOrAfterAction(usablePowerups)){
            PowerUpLM chosenPwUp = choosePowerup(usablePowerups);
            if(chosenPwUp != null){ //timer not expired
                switch (chosenPwUp.getName()){
                    case GeneralInfo.NEWTON:
                        newton(chosenPwUp.getIdWeapon());
                        break;
                    case GeneralInfo.TELEPORTER:
                        teleporter(chosenPwUp.getIdWeapon());
                        break;
                        default:
                            throw new Error("The chosen weapon has the wrong name: " + chosenPwUp.getName());
                }
            }
        }

        //TODO if arriva SMG_USE_POWERUP e l'id player corrisponde al mio, allora resetto le info: shootPowerUpInfo = null;

    }

    @Override
    public void update(Event event) {

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.CAN_USE_TAGBACK) {
            ShootPowerUpInfo shootPowerUpInfo = new Gson().fromJson(jsonMessage, ShootPowerUpInfo.class);
            /*TODO: RICICLANDO il possibile dai metodi già fatti sotto,
                     quando ricevi messaggio per tagback grenade
                    chiedi all'utente se vuole utilizzare uno dei seguenti powerup quale vuole utilizzare (o se invece dice no):
                    se dice no non fare più niente, se dice di sì, se era solo uno procedi inserendo
                     le info negli attributi e fa triggerNextState(this), altrimenti gli chiedi quale e poi
                     inserisci le info negli attributi e fai triggerNextState(this)
             */

        }

        if(command == EventType.CAN_USE_TARGETING_SCOPE) {
            ShootPowerUpInfo shootPowerUpInfo = new Gson().fromJson(jsonMessage, ShootPowerUpInfo.class);
            /*TODO: RICICLANDO il possibile dai metodi già fatti sotto,
                     quando ricevi messaggio per targeting scope
                    chiedi all'utente se vuole utilizzare uno dei seguenti powerup quale vuole utilizzare (o se invece dice no):
                    se dice no non fare più niente, se dice di sì, se era solo uno procedi inserendo
                     le info negli attributi e fa triggerNextState(this), altrimenti gli chiedi quale e poi
                     inserisci le info negli attributi e fai triggerNextState(this)
             */
        }

    }

    /**
     * Ask the user if he/she wants to use one of the powerups of the presented list and
     * return null if the timer expired, otherwise return the user's answer (yes or no).
     * The exact strings for the answer (yes or no) are accessible with GeneralInfo.YES_COMMAND and GeneralInfo.NO_COMMAND.
     * @param usablePowerups - non-empty list of powerups that the player can you in the presented situation
     * @return null if the timer expired, otherwise return the user's answer (yes or no)
     */
    String askUsePowerup(List<PowerUpLM> usablePowerups){
        if(usablePowerups.isEmpty()){
            throw new RuntimeException("This method should not be called with an empty list of powerups.");
        }
        ToolsView.printListOfPowerups(usablePowerups);
        System.out.println(GeneralInfo.YES_COMMAND + ": use powerup");
        System.out.println(GeneralInfo.NO_COMMAND + ": don't");
        ToolsView.printGeneralOptions();
        System.out.print(GeneralInfo.ASK_INPUT);
        List<String> acceptableInputs = new ArrayList<>();
        acceptableInputs.add(GeneralInfo.YES_COMMAND);
        acceptableInputs.add(GeneralInfo.NO_COMMAND);
        return ToolsView.readUserChoice(acceptableInputs, true);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, trigger PowerUpState.
     * Warning: use this method when the only information to pass to the PowerUpState is the list of powerups.
     * @param usablePowerups - non-empty list of usable powerups
     */
    void triggerPowerupState(List<PowerUpLM> usablePowerups){
        shootPowerUpInfo = new ShootPowerUpInfo(usablePowerups, new ArrayList<>(), new ArrayList<>(), new int[0]);
        StateManager.triggerNextState(this);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, trigger PowerUpState.
     * @param shootPowerUpInfo
     */
    void triggerPowerupState(ShootPowerUpInfo shootPowerUpInfo){
        this.shootPowerUpInfo = shootPowerUpInfo;
        StateManager.triggerNextState(this);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, choose powerup to use between a non-empty list
     * and if there are more than two powerups to choose, return the user's choice if the timer did not expire
     * first, return null otherwise. If there is only one powerup, don't ask anything and return that powerup.
     * @param powerups - non-empty list of powerups
     * @return the powerup in the list if there is only one, the one chosen from the player if
     * there are at least two and the timer did not expire first, null otherwise
     */
    private PowerUpLM choosePowerup(List<PowerUpLM> powerups){
        PowerUpLM chosenPwUp;
        String userInput;
        if(powerups.isEmpty()){
            throw new RuntimeException("This method should not be called to 'choose' between a 'list' of one powerup.");
        }
        if(powerups.size() > 1) {
            List<String> acceptableInputs = new ArrayList<>();
            System.out.println("Choose the powerup to use:");
            for (int i = 0; i < powerups.size(); i++) {
                PowerUpLM p = powerups.get(i);
                System.out.println((i + 1) + ": " + p.getName() + "(" +
                        ToolsView.ammoTypeToString(p.getGainAmmoColor()) + ")");
                acceptableInputs.add(Integer.toString(i + 1));
            }

            ToolsView.printGeneralOptions();
            System.out.print(GeneralInfo.ASK_INPUT);

            userInput = ToolsView.readUserChoice(acceptableInputs, true);

            if (userInput != null) {
                int posInList = Integer.parseInt(userInput) - 1;
                chosenPwUp = powerups.get(posInList);
                return chosenPwUp;
            }
            else{//time expired
                return null;
            }
        }
        else{//if there is only one powerup to use, the user has already given consent to use it, so don't ask anything
            chosenPwUp = powerups.get(0);
            return chosenPwUp;
        }
    }

    /**
     * Return true if the received list of usable powerups is composed (only) by the
     * powerups you can use before or after an action during your turn.
     * @param usablePowerups
     * @return true if the received list of usable powerups is composed (only) by the
     * powerups you can use before or after an action during your turn; false otherwise.
     */
    private boolean powerupsBeforeOrAfterAction(List<PowerUpLM> usablePowerups) {
        for (PowerUpLM pwUp : usablePowerups) {
            if (!pwUp.getName().equals(GeneralInfo.TELEPORTER) && !pwUp.getName().equals(GeneralInfo.NEWTON)){
                return false;
            }
        }
        return true;
    }

    private int newton(int idWeapon){
        return 1;
        //TODO
    }

    private int teleporter(int idWeapon){
        return 1;
        //TODO
    }

}
