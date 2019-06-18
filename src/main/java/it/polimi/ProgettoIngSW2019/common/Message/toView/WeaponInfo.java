package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;

import java.util.ArrayList;
import java.util.List;

public class WeaponInfo {
    private List<EnemyInfo> enemyVisible;
    private boolean hasToMove;
    private WeaponEffectType effectType;

    public WeaponInfo(List<EnemyInfo> enemyVisible, boolean hasToMove, WeaponEffectType effectType){
        this.effectType = effectType;
        this.enemyVisible = enemyVisible;
        this.hasToMove = hasToMove;
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
}
