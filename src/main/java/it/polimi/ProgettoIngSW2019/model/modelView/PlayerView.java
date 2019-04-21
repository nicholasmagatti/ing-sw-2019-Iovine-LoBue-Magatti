package it.polimi.ProgettoIngSW2019.model.modelView;

import it.polimi.ProgettoIngSW2019.model.KillToken;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;

import java.util.ArrayList;

public class PlayerView {
    private int idPlayer;
    private String charaName;
    private ArrayList<KillToken> damageLine;
    private ArrayList<KillToken> markLine;
    private int numberOfSkulls;
    private ArrayList<WeaponCard> unloadedWeapons;
    private boolean startingPlayer;
    private ArrayList<AmmoType> ammoBox;

    public int getIdPlayer() {
        return idPlayer;
    }

    public String getCharaName() {
        return charaName;
    }

    public ArrayList<KillToken> getDamageLine() {
        return damageLine;
    }

    public ArrayList<KillToken> getMarkLine() {
        return markLine;
    }

    public int getNumberOfSkulls() {
        return numberOfSkulls;
    }

    public ArrayList<WeaponCard> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    public boolean isStartingPlayer() {
        return startingPlayer;
    }

    public ArrayList<AmmoType> getAmmoBox() {
        return ammoBox;
    }

    //TODO: override of toString
}
