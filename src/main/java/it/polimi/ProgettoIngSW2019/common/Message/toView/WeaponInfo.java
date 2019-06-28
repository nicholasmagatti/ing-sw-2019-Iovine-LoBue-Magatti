package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;

import java.util.ArrayList;
import java.util.List;

public class WeaponInfo {
    private String weaponName;
    private int weaponId;
    private List<EnemyInfo> enemyVisible;
    private boolean hasToMove;
    private int numberOfTargetHittable;
    private WeaponEffectType effectType;

    public WeaponInfo(List<EnemyInfo> enemyVisible, boolean hasToMove, WeaponEffectType effectType, int weaponId, String weaponName, int numberOfTargetHittable){
        this.effectType = effectType;
        this.enemyVisible = enemyVisible;
        this.hasToMove = hasToMove;
        this.weaponId = weaponId;
        this.weaponName = weaponName;
        this.numberOfTargetHittable = numberOfTargetHittable;
    }


    public List<EnemyInfo> getEnemyVisible() {
        return enemyVisible;
    }

    public WeaponEffectType getEffectType() {
        return effectType;
    }

    public boolean hasMoveOption() {
        return hasToMove;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public int getNumberOfTargetHittable() {
        return numberOfTargetHittable;
    }
}
