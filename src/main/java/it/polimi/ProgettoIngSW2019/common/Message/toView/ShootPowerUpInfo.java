package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

import java.util.List;

public class ShootPowerUpInfo {
    List<PowerUpLM> powerUpUsableList;
    List<EnemyInfo> enemy;
    List<PowerUpLM> powerUpAsPayment;
    int[] ammoAsPayment;

    public ShootPowerUpInfo(List<PowerUpLM> powerUpUsableList, List<EnemyInfo> enemy, List<PowerUpLM> powerUpAsPayment, int[] ammoAsPayment){
        this.powerUpUsableList = powerUpUsableList;
        this.enemy = enemy;
        this.powerUpAsPayment = powerUpAsPayment;
        this.ammoAsPayment = ammoAsPayment;
    }

    public List<PowerUpLM> getPowerUpUsableList() {
        return powerUpUsableList;
    }

    public List<EnemyInfo> getEnemy() {
        return enemy;
    }

    public int[] getAmmoAsPayment() {
        return ammoAsPayment;
    }

    public List<PowerUpLM> getPowerUpAsPayment() {
        return powerUpAsPayment;
    }
}
