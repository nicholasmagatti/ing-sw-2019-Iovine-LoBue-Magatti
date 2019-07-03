package it.polimi.ProgettoIngSW2019.controller;


import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;


import java.util.ArrayList;
import java.util.List;

public class PayAmmoController {
    private CreateJson createJson;

    //ammo to spend
    private int blueCost = 0;
    private int redCost = 0;
    private int yellowCost = 0;

    //ammo in ammoBox
    private int blueAB = 0;
    private int redAB = 0;
    private int yellowAB = 0;

    //ammo in PowerUp
    private int bluePP = 0;
    private int redPP = 0;
    private int yellowPP = 0;




    public PayAmmoController(CreateJson createJson) {
        this.createJson = createJson;
    }


    /**
     * return the object with all the ammo the player can use to pay the cost
     * @param player            player
     * @param weaponCard        weapon to pay
     * @param ammoCost          ammo cost of the payment
     * @return                  the object with all the info
     */
    //tested
    public PayAmmoList ammoToPay(Player player, WeaponCard weaponCard, List<AmmoType> ammoCost) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(weaponCard == null)
            throw new NullPointerException("Weapon cannot be null");

        if(ammoCost == null)
            throw new NullPointerException("ammoCost cannot be null");

        if(player.getPowerUps() == null)
            throw new NullPointerException("list powerUps cannot be null");

        int[] ammoInAmmoBox = new int[AmmoType.contEnum()];
        int[] ammoCostArray = new int[AmmoType.contEnum()];

        if(!player.getPowerUps().isEmpty())
            ammoInPowerUp(player.getPowerUps());

        ammoInAmmoBox(player);

        List<PowerUp> powerUpList = new ArrayList<>();


        if(!player.hasEnoughAmmo(ammoCost))
            throw new IllegalAttributeException("Player: " + player.getIdPlayer() + "cannot pay");

        ammoCostInt(ammoCost);

        ammoCostArray[AmmoType.intFromAmmoType(AmmoType.BLUE)] = blueCost;
        ammoCostArray[AmmoType.intFromAmmoType(AmmoType.YELLOW)] = yellowCost;
        ammoCostArray[AmmoType.intFromAmmoType(AmmoType.RED)] = redCost;


        if(blueCost > 0) {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.BLUE)] = blueAB;
            if(bluePP > 0) {
                for(PowerUp powerUp: player.getPowerUps()) {
                    if(powerUp.getGainAmmoColor() == AmmoType.BLUE)
                        powerUpList.add(powerUp);
                }
            }
        }
        else {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.BLUE)] = 0;

        }


        if(yellowCost > 0) {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.YELLOW)] = yellowAB;
            if(yellowPP > 0) {
                for(PowerUp powerUp: player.getPowerUps()) {
                    if(powerUp.getGainAmmoColor() == AmmoType.YELLOW)
                        powerUpList.add(powerUp);
                }
            }
        }
        else {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.YELLOW)] = 0;
        }


        if(redCost > 0) {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.RED)] = redAB;
            if(redPP > 0) {
                for(PowerUp powerUp: player.getPowerUps()) {
                    if(powerUp.getGainAmmoColor() == AmmoType.RED)
                        powerUpList.add(powerUp);
                }
            }
        }
        else {
            ammoInAmmoBox[AmmoType.intFromAmmoType(AmmoType.RED)] = 0;
        }

        return new PayAmmoList(weaponCard.getIdCard(), ammoCostArray, ammoInAmmoBox, createJson.createPowerUpsListLM(powerUpList));
    }


    /**
     * Put the AmmoType list into int variables
     * @param ammoCost      list ammoType to convert
     */
    public void ammoCostInt(List<AmmoType> ammoCost) {
        blueCost = 0;
        redCost = 0;
        yellowCost = 0;

        if(ammoCost == null)
            throw new NullPointerException("ammoCost cannot be null");

        if(ammoCost.isEmpty())
            return;

        for(AmmoType element : ammoCost){
            switch (element){
                case BLUE:
                    blueCost++;
                    break;
                case RED:
                    redCost++;
                    break;
                case YELLOW:
                    yellowCost++;
                    break;
            }
        }
    }


    /**
     * count ammo from powerUps of the player
     * @param powerUps      list powerUps to count
     */
    public void ammoInPowerUp(List<PowerUp> powerUps) {
        //ammo in PowerUp
        bluePP = 0;
        redPP = 0;
        yellowPP = 0;

        for (PowerUp powerUp : powerUps) {
            switch (powerUp.getGainAmmoColor()) {
                case BLUE:
                    bluePP++;
                    break;
                case RED:
                    redPP++;
                    break;
                case YELLOW:
                    yellowPP++;
                    break;
            }
        }
    }


    /**
     * count the ammo in ammo box of the player
     * @param player        player
     */
    public void ammoInAmmoBox(Player player) {
        //ammo in ammoBox
        blueAB = 0;
        redAB = 0;
        yellowAB = 0;

        blueAB = player.getBlueAmmo();
        redAB = player.getRedAmmo();
        yellowAB = player.getYellowAmmo();
    }


    /**
     * check if ammo from view are corrects
     * @param ammoCost      ammo cost
     * @param powerUps      powerUps to use for the payment
     * @param ammoToPay     ammo from ammo box to pay
     * @return              true if it is all correct
     */
    //tested
    public boolean checkAmmoToPayFromView(List<AmmoType> ammoCost, List<PowerUp> powerUps, int[] ammoToPay) {
        if(ammoCost == null)
            throw new NullPointerException("ammo To Spend cannot be null");

        ammoCostInt(ammoCost);
        ammoInPowerUp(powerUps);

        blueAB = ammoToPay[AmmoType.intFromAmmoType(AmmoType.BLUE)];
        redAB = ammoToPay[AmmoType.intFromAmmoType(AmmoType.RED)];
        yellowAB = ammoToPay[AmmoType.intFromAmmoType(AmmoType.YELLOW)];

        if(((blueAB + bluePP) == blueCost) && ((redAB + redPP) == redCost) && ((yellowAB + yellowPP) == yellowCost))
            return true;
        else
            return false;
    }
}
