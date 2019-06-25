package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;

/**
 * message to player with his reload info
 * @author Priscilla Lo Bue
 */
public class MessagePlayerSwapWeapons extends MessageWeaponPay {
    private int idWeaponDiscarded;
    private String nameWeaponDiscarded;


    /**
     * Constructor
     * @param idPlayer          id player reload
     * @param namePlayer        name player reload
     * @param idWeapon          id weapon reload
     * @param nameWeapon        name weapon reload
     */
    public MessagePlayerSwapWeapons(int idPlayer, String namePlayer, int idWeapon, String nameWeapon, int idWeaponDiscarded, String nameWeaponDiscarded) {
        super(idPlayer, namePlayer, idWeapon, nameWeapon);

        this.idWeaponDiscarded = idWeaponDiscarded;
        this.nameWeaponDiscarded = nameWeaponDiscarded;
    }


    /**
     * get id of the weapon to discard
     * @return          id weapon to discard
     */
    public int getIdWeaponDiscarded() {
        return idWeaponDiscarded;
    }


    /**
     * get name of the weapon to discard
     * @return      name of the weapon to discard
     */
    public String getNameWeaponDiscarded() {
        return nameWeaponDiscarded;
    }
}
