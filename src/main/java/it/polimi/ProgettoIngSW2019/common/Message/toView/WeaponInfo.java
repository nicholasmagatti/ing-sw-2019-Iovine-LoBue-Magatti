package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;

import java.util.ArrayList;
import java.util.List;

public class WeaponInfo {
    List<EnemyInfo> enemyVisible;
    boolean hasToMove;
    boolean isEnemyToMove;
    WeaponEffectType effectType;

    public WeaponInfo(List<EnemyInfo> enemyVisible, boolean isEnemyToMove, boolean hasToMove, WeaponEffectType effectType){
        this.effectType = effectType;
        this.enemyVisible = enemyVisible;
        this.hasToMove = hasToMove;
        this.isEnemyToMove = isEnemyToMove;
    }


    public List<EnemyInfo> getEnemyVisible() {
        return enemyVisible;
    }

    public WeaponEffectType getEffectType() {
        return effectType;
    }

    public boolean isEnemyToMove() {
        return hasToMove;
    }
}
