package it.polimi.ProgettoIngSW2019.common.Message;

import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;

import java.util.List;

public class ReloadInfo extends Info {
    private List<WeaponLM> weaponsCanReload;
    private String message;


    public ReloadInfo(int idPlayer, List<WeaponLM> weaponsCanReload) {
        super(idPlayer);
        this.weaponsCanReload = weaponsCanReload;
        message = "Weapons you can reload: ";
    }



    public ReloadInfo(int idPlayer) {
        super(idPlayer);
        message = "You can't reload";
    }
}
