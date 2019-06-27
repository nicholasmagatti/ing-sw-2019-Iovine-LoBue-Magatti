package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Nicholas Magatti
 */
public class TestPlayer {

    private GameTable gameTable;
    private Square[][][] maps = new Maps().getMaps();
    private Player player1, player2, player3, player4;
    private AmmoPoint ammoPoint1, ammoPoint2, ammoPoint3;

    @Before
    public void setUp(){
        gameTable = new GameTable(maps[3],5);
        player1 = new Player(0, "Nome1", gameTable, "host");
        player2 = new Player(1, "Nome2", gameTable, "host");
        player3 = new Player(2, "Nome3", gameTable, "host");
        player4 = new Player(3, "Nome4", gameTable, "host");

        ammoPoint1 = new AmmoPoint(3, true, true, true, true);
        ammoPoint2 = new AmmoPoint(3, true, true, true, true);
        ammoPoint3 = new AmmoPoint(3, true, true, true, true);

        ammoPoint1.reset(gameTable.getAmmoDeck());
        ammoPoint2.reset(gameTable.getAmmoDeck());
        ammoPoint3.reset(gameTable.getAmmoDeck());

    }

    @Test
    public void testDealDamageCorrectly(){
        int nDamages = 5;
        int nMarks = 3;
        player1.markPlayer(nMarks, player2);
        player1.dealDamage(nDamages, player2);
        assertEquals(nDamages + nMarks, player2.getDamageLine().size());
        for(int i=0; i < 5; i++) {
            assertEquals(player1.getCharaName(), player2.getDamageLine().get(i));
        }
    }

    @Test
    public void nullPointerTarget(){
        try {
            player2.dealDamage(3, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
        try {
            player2.markPlayer(3, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }

    @Test
    public void exceptionOfTargetingHimself(){
        try {
            player1.dealDamage(2, player1);
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }
        try{
            player1.markPlayer(3, player1);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }

    /**
     * Verify the player is put down in the two cases of overkill and kill(standard kill)
     */
    @Test
    public void verifyPlayerPutDownAfterDamage(){
        //overkill
        player1.dealDamage(GeneralInfo.DAMAGE_TO_OVERKILL, player2);
        assertTrue(player2.isPlayerDown());
        //kill (but no overkill)
        player1.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player3);
        assertTrue(player3.isPlayerDown());
        //this time the damaged player should be still alive
        player1.dealDamage(6, player4);
        assertFalse(player4.isPlayerDown());
    }

    @Test
    public void tryToGrabAmmoFromSpawnPoint(){
        player1.moveTo(new SpawningPoint(0, true, true, true, true));
        try{
            player1.grabAmmoCardFromThisSquare();
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    @Test
    public void tryToGrabAmmoOnAmmoPointWhenEmpty(){
        player1.moveTo(ammoPoint1);
        player1.grabAmmoCardFromThisSquare();
        //now it is empty and the player tries to grab again from the same square (something that should never happen)
        try{
            player1.grabAmmoCardFromThisSquare();
            fail();
        }
        catch (NullPointerException e){
            System.out.println(e);
        }
    }

    @Test
    public void testHasEnoughAmmoDrawingCards(){

        //at first, the player has 1 of each color
        List<AmmoType> listOfAmmo = new ArrayList<>();
        //the list is empty
        assertTrue(player1.hasEnoughAmmo(listOfAmmo));
        //other way to say the ammo to spend is nothing, is using null as parameter
        assertTrue(player1.hasEnoughAmmo(null));
        //fill the listOfAmmo
        listOfAmmo.add(AmmoType.BLUE);
        assertTrue(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.add(AmmoType.YELLOW);
        listOfAmmo.add(AmmoType.RED);

        assertTrue(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.add(AmmoType.RED);
        assertFalse(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.add(AmmoType.YELLOW);
        listOfAmmo.add(AmmoType.BLUE);

        assertFalse(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.remove(AmmoType.RED);
        assertFalse(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.remove(AmmoType.YELLOW);
        listOfAmmo.remove(AmmoType.BLUE);

        assertTrue(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.add(AmmoType.BLUE);

        assertFalse(player1.hasEnoughAmmo(listOfAmmo));

        //now the player get more ammo
        player1.moveTo(ammoPoint1);
        player1.grabAmmoCardFromThisSquare();
        player1.moveTo(ammoPoint2);
        player1.grabAmmoCardFromThisSquare();
        player1.moveTo(ammoPoint3);
        player1.grabAmmoCardFromThisSquare();

        //reset the list to an empty list
        listOfAmmo = new ArrayList<>();

        for(int i=0; i < player1.getBlueAmmo(); i++){
            listOfAmmo.add(AmmoType.BLUE);
        }
        for (int i=0; i < player1.getRedAmmo(); i++){
            listOfAmmo.add(AmmoType.RED);
        }
        for(int i=0; i < player1.getYellowAmmo(); i++){
            listOfAmmo.add(AmmoType.YELLOW);
        }
        //the player should have exactly
        assertTrue(player1.hasEnoughAmmo(listOfAmmo));
        listOfAmmo.add(AmmoType.YELLOW);
        listOfAmmo.remove(AmmoType.YELLOW);
        listOfAmmo.remove(AmmoType.YELLOW);
        assertTrue(player1.hasEnoughAmmo(listOfAmmo));

        //reset the list again
        listOfAmmo = new ArrayList<>();
        /*add 7 units of blue ammo (the player can never have more that 6 units for
            each color(no more than 3 in the ammo box plus 3 thanks to the powerups in his/her hand)
         */
        for(int i=0; i < 7; i++) {
            listOfAmmo.add(AmmoType.RED);
        }
        assertFalse(player1.hasEnoughAmmo(listOfAmmo));
    }

    @Test
    public void tryToSetWrongName(){

        try{
            new Player(0, null, gameTable, "host");
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

        try{
            new Player(0, "", gameTable, "host");
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    @Test
    public void testIncreasingOfScore(){
        assertEquals(0, player1.getScore());
        player1.addPointsToScore(1);
        player1.addPointsToScore(6);
        assertEquals(7, player1.getScore());
    }

    @Test
    public void testLimitDamage(){
        player3.dealDamage(2, player1);
        player2.dealDamage(5, player1);
        player4.dealDamage(10, player1);
        assertEquals(GeneralInfo.DAMAGE_TO_OVERKILL, player1.getDamageLine().size());

        player4.dealDamage(20, player1);
        assertEquals(GeneralInfo.DAMAGE_TO_OVERKILL, player1.getDamageLine().size());

        player4.dealDamage(20, player3);
        assertEquals(GeneralInfo.DAMAGE_TO_OVERKILL, player3.getDamageLine().size());
    }

    @Test
    public void testCorrectRemovalOfMarks(){
        player2.dealDamage(3, player1);
        player2.markPlayer(2, player1);
        player4.dealDamage(1, player1);
        player4.markPlayer(1, player1);
        player3.markPlayer(1, player1);
        player4.markPlayer(1, player1);
        player3.markPlayer(1, player1);
        player4.dealDamage(1, player1);
        player3.dealDamage(1, player1);
        assertFalse(player1.getMarkLine().contains(player4.getCharaName()));
        assertEquals(10, player1.getDamageLine().size());
        assertEquals(2, player1.getMarkLine().size());
    }

    @Test
    public void testMax3MarksFromSamePlayer(){
        player3.markPlayer(2, player4);
        player3.markPlayer(2, player4);
        assertEquals(3, player4.getMarkLine().size());
        player3.dealDamage(1, player4);
        player3.markPlayer(1, player4);
        player1.markPlayer(3, player4);
        player3.markPlayer(3, player4);
        int marksFromPlayer3 = 0;
        for(String name : player4.getMarkLine()){
            if(name.equals(player3.getCharaName())){
                marksFromPlayer3++;
            }
        }
        assertEquals(3, marksFromPlayer3);
    }

}
