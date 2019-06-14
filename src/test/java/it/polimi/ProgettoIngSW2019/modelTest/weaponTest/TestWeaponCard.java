package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.fail;

public class TestWeaponCard {
    @Test
    public void assignWeaponEffectTest(){
        try{
            WeaponCard w1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json");
        }catch(NullPointerException e){
            fail();
        }
    }

    @Test
    public void assignWeaponEffectWrongTest(){
        try{
            WeaponCard w1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "TEST_WEAPONCARD.json");
            fail();
        }catch(NullPointerException e){

        }
    }
}
