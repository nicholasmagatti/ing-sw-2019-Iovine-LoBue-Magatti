package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.MyLoadedWeaponsLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;

/**
 * message to player with his reload info
 * @author Priscilla Lo Bue
 */
public class MessageMyWeaponReloaded extends MessageEnemyWeaponReloaded {
    private MyLoadedWeaponsLM myLoadedWeaponsLM;


    /**
     * Constructor
     * @param idPlayer          id player reload
     * @param namePlayer        name player reload
     * @param idWeapon          id weapon reload
     * @param nameWeapon        name weapon reload
     * @param playerLM          player reload LM updated
     * @param myLoadedWeaponsLM loaded weapons player reload
     */
    public MessageMyWeaponReloaded(int idPlayer, String namePlayer, int idWeapon, String nameWeapon, PlayerDataLM playerLM, MyLoadedWeaponsLM myLoadedWeaponsLM) {
        super(idPlayer, namePlayer, idWeapon, nameWeapon, playerLM);

        if(myLoadedWeaponsLM == null)
            throw new NullPointerException("myLoadedWeapon cannot be null");

        this.myLoadedWeaponsLM = myLoadedWeaponsLM;
    }


    /**
     *
     * @return  loaded weapons LM
     */
    public MyLoadedWeaponsLM getMyLoadedWeaponsLM() {
        return myLoadedWeaponsLM;
    }
}
