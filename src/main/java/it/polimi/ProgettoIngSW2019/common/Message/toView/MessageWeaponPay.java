package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;

/**
 * message to enemy player with reload info
 * @author Priscilla Lo Bue
 */
public class MessageWeaponPay extends Message{
    private int idWeapon;
    private String nameWeapon;


    /**
     * Constructor
     * @param idPlayer          id player reload
     * @param namePlayer        name player reload
     * @param idWeapon          id weapon reload
     * @param nameWeapon        name weapon reload
     */
    public MessageWeaponPay(int idPlayer, String namePlayer, int idWeapon, String nameWeapon) {
        super(idPlayer, namePlayer);

        if(namePlayer == null)
            throw new NullPointerException("name weapon cannot be null");

        if(idWeapon < 0)
            throw new IllegalArgumentException("Id weapon cannot be negative");

        this.idWeapon = idWeapon;
        this.nameWeapon = nameWeapon;
    }


    /**
     *
     * @return name weapon reloaded
     */
    public String getNameWeapon() {
        return nameWeapon;
    }


    /**
     *
     * @return  id weapon reloaded
     */
    public int getIdWeapon() {
        return idWeapon;
    }
}
