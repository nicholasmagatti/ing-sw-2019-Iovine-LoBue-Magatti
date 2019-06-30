package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class TestCreateJson {
    private TurnManager turnManager;
    private GameTable gameTable;
    private CreateJson createJson;

    private List<WeaponCard> weaponsList = new ArrayList<>();
    private List<WeaponLM> weaponLMList = new ArrayList<>();
    private WeaponCard weaponCard1, weaponCard2, weaponCard3;
    private WeaponLM weaponLM1, weaponLM2, weaponLM3;
    private PowerUp powerUp1, powerUp2, powerUp3;
    private PowerUpLM powerUpLM1, powerUpLM2;
    private List<PowerUp> powerUps = new ArrayList<>();
    private List<PowerUpLM> powerUpLMS = new ArrayList<>();
    private AmmoCard ammoCard1, ammoCard2;
    private AmmoCardLM ammoCardLM1, ammoCardLM2;
    private Player player1;
    private PlayerDataLM playerLM1;


    @Before
    public void setUpTest() {
        turnManager = mock(TurnManager.class);
        Maps maps = new Maps();
        gameTable = spy(new GameTable(maps.getMaps()[0],5));
        when(turnManager.getGameTable()).thenReturn(gameTable);
        createJson = spy(new CreateJson(turnManager));

        //Weapon - WeaponLM
        weaponCard1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK RIFLE","", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json");

        int[] ammoCostReload1 = new int[3];
        int[] ammoCostBuy1 = new int[3];
        ammoCostReload1[1] = 2;
        ammoCostBuy1[1] = 1;
        weaponLM1 = new WeaponLM(0, "LOCK RIFLE", "", ammoCostReload1, ammoCostBuy1);


        //Weapon List - WeaponLM List
        weaponCard2 = new WeaponCard(1, DeckType.WEAPON_CARD, "ELECTROSCYTHE", "", Arrays.asList(AmmoType.BLUE), "ElectroSchyteEff.json");
        weaponCard3 = new WeaponCard(2, DeckType.WEAPON_CARD, "MACHINE GUN", "", Arrays.asList(AmmoType.BLUE, AmmoType.RED), "MachineGunEff.json");

        int[] ammoCostReload2 = new int[3];
        int[] ammoCostBuy2 = new int[3];
        ammoCostReload2[1] = 1;

        int[] ammoCostReload3 = new int[3];
        int[] ammoCostBuy3 = new int[3];
        ammoCostReload3[0] = 1;
        ammoCostReload3[1] = 1;
        ammoCostBuy3[0] = 1;

        weaponLM2 = new WeaponLM(1, "ELECTROSCYTHE", "", ammoCostReload2, ammoCostBuy2);
        weaponLM3 = new WeaponLM(2, "MACHINE GUN", "", ammoCostReload3, ammoCostBuy3);

        weaponsList.add(weaponCard1);
        weaponsList.add(weaponCard2);
        weaponLMList.add(weaponLM1);
        weaponLMList.add(weaponLM2);


        //PowerUp - PowerUpLM
        powerUp1 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp2 = new PowerUp(1,DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp3 = new PowerUp(2, DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());

        powerUpLM1 = new PowerUpLM(0, GeneralInfo.TAGBACK_GRENADE, "", AmmoType.YELLOW);
        powerUpLM2 = new PowerUpLM(1, GeneralInfo.TAGBACK_GRENADE, "", AmmoType.RED);

        powerUps.add(powerUp1);
        powerUps.add(powerUp2);

        powerUpLMS.add(powerUpLM1);
        powerUpLMS.add(powerUpLM2);


        //Ammo - AmmoLM
        ammoCard1 = new AmmoCard(0, DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);
        ammoCard2 = new AmmoCard(1, DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.YELLOW);

        ammoCardLM1 = new AmmoCardLM(0, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);
        ammoCardLM2 = new AmmoCardLM(1, AmmoType.YELLOW, AmmoType.YELLOW);


        //Player
        player1 = mock(Player.class);

        when(player1.getIdPlayer()).thenReturn(0);
        when(player1.getCharaName()).thenReturn("Pinco");
        when(player1.getBlueAmmo()).thenReturn(3);
        when(player1.getRedAmmo()).thenReturn(3);
        when(player1.getYellowAmmo()).thenReturn(3);
        when(player1.getUnloadedWeapons()).thenReturn(Arrays.asList(weaponCard1, weaponCard2));
        when(player1.isActive()).thenReturn(true);
        when(player1.isPlayerDown()).thenReturn(false);
        when(player1.getPowerUps()).thenReturn(Arrays.asList(powerUp1));
        when(player1.getNumberOfSkulls()).thenReturn(3);
        when(player1.getDamageLine()).thenReturn(Arrays.asList("Priscilla", "Luca"));
        when(player1.getMarkLine()).thenReturn(Arrays.asList("Luca", "Luca", "Nick"));
        when(player1.getLoadedWeapons()).thenReturn(Arrays.asList(weaponCard3));
        when(player1.getNumberOfPoweUps()).thenReturn(1);


        playerLM1 = mock(PlayerDataLM.class);
        when(playerLM1.getIdPlayer()).thenReturn(0);
        when(playerLM1.getNickname()).thenReturn("Pinco");
        when(playerLM1.getnBlueAmmo()).thenReturn(3);
        when(playerLM1.getnRedAmmo()).thenReturn(3);
        when(playerLM1.getnYellowAmmo()).thenReturn(3);
        //when(playerLM1.getUnloadedWeapons()).thenReturn(createJson.createWeaponsListLM(Arrays.asList(weaponCard1, weaponCard2)));
        when(playerLM1.getActive()).thenReturn(true);
        when(playerLM1.getDown()).thenReturn(false);
        //when(playerLM1.get()).thenReturn(createJson.createPowerUpsListLM(Arrays.asList(powerUp1, powerUp2)));
        when(playerLM1.getnSkulls()).thenReturn(3);
        when(playerLM1.getDamageLine()).thenReturn(Arrays.asList("Priscilla", "Luca"));
        when(playerLM1.getMarkLine()).thenReturn(Arrays.asList("Luca", "Luca", "Nick"));
        //when(playerLM1.getLoadedWeapons()).thenReturn(Arrays.asList(weaponCard3));
        when(playerLM1.getnPowerUps()).thenReturn(1);
        when(playerLM1.getnMyLoadedWeapons()).thenReturn(1);
    }


    @Test
    public void WeaponTest() {
        assertEquals(2, weaponCard1.getreloadCost().size());
        assertEquals(1, weaponCard1.getBuyCost().size());
    }


    @Test
    public void createWeaponLMTest() {
        WeaponLM weaponLMCreated = createJson.createWeaponLM(weaponCard1);
        assertEquals(weaponLM1.getIdWeapon(), weaponLMCreated.getIdWeapon());
        assertEquals(weaponLM1.getName(), weaponLMCreated.getName());
        assertEquals(weaponLM1.getDescription(), weaponLMCreated.getDescription());

        for(int i = 0; i < weaponLM1.getAmmoCostReload().length; i++) {
            assertEquals(weaponLM1.getAmmoCostReload()[i], weaponLMCreated.getAmmoCostReload()[i]);
        }

        for(int i = 0; i < weaponLM1.getAmmoCostBuy().length; i++) {
            assertEquals(weaponLM1.getAmmoCostBuy()[i], weaponLMCreated.getAmmoCostBuy()[i]);
        }
    }


    @Test
    public void createWeaponLMListTest() {
        List<WeaponLM> weaponLMListCreated = createJson.createWeaponsListLM(weaponsList);

        for(int i = 0; i < weaponLMList.size(); i++) {
            assertEquals(weaponLMList.get(i).getIdWeapon(), weaponLMListCreated.get(i).getIdWeapon());
            assertEquals(weaponLMList.get(i).getName(), weaponLMListCreated.get(i).getName());
            assertEquals(weaponLMList.get(i).getDescription(), weaponLMListCreated.get(i).getDescription());

            for(int j = 0; j < weaponLMList.get(i).getAmmoCostReload().length; j++) {
                assertEquals(weaponLMList.get(i).getAmmoCostReload()[j], weaponLMListCreated.get(i).getAmmoCostReload()[j]);
            }

            for(int j = 0; j < weaponLMList.get(i).getAmmoCostBuy().length; j++) {
                assertEquals(weaponLMList.get(i).getAmmoCostBuy()[j], weaponLMListCreated.get(i).getAmmoCostBuy()[j]);
            }
        }
    }


    @Test
    public void createPowerUpLMTest() {
        PowerUpLM powerUpLMCreated = createJson.createPowerUpLM(powerUp1);
        assertEquals(powerUpLM1.getIdPowerUp(), powerUpLMCreated.getIdPowerUp());
        assertEquals(powerUpLM1.getName(), powerUpLMCreated.getName());
        assertEquals(powerUpLM1.getDescription(), powerUpLMCreated.getDescription());
        assertEquals(powerUpLM1.getGainAmmoColor(), powerUpLMCreated.getGainAmmoColor());
    }


    @Test
    public void createPowerUpsListLMTest() {
        List<PowerUpLM> powerUpLMSCreated = createJson.createPowerUpsListLM(powerUps);

        for(int i = 0; i < powerUpLMS.size(); i++) {
            assertEquals(powerUpLMS.get(i).getIdPowerUp(), powerUpLMSCreated.get(i).getIdPowerUp());
            assertEquals(powerUpLMS.get(i).getName(), powerUpLMSCreated.get(i).getName());
            assertEquals(powerUpLMS.get(i).getGainAmmoColor(), powerUpLMSCreated.get(i).getGainAmmoColor());
        }
    }


    @Test
    public void createAmmoLMTest() {
        AmmoCardLM ammoCardLMCreated1 = createJson.createAmmoCardLM(ammoCard1);
        assertEquals(ammoCardLM1.getIdAmmoCard(), ammoCardLMCreated1.getIdAmmoCard());
        assertEquals(ammoCardLM1.hasPowerup(), ammoCardLMCreated1.hasPowerup());
        for(int i = 0; i < ammoCardLM1.getAmmo().size(); i++) {
            assertEquals(ammoCardLM1.getAmmo().get(i), ammoCardLMCreated1.getAmmo().get(i));
        }

        AmmoCardLM ammoCardLMCreated2 = createJson.createAmmoCardLM(ammoCard2);
        assertEquals(ammoCardLM2.getIdAmmoCard(), ammoCardLMCreated2.getIdAmmoCard());
        assertEquals(ammoCardLM2.hasPowerup(), ammoCardLMCreated2.hasPowerup());
        for(int i = 0; i < ammoCardLM2.getAmmo().size(); i++) {
            assertEquals(ammoCardLM2.getAmmo().get(i), ammoCardLMCreated2.getAmmo().get(i));
        }
    }


    @Test
    public void createPlayerLMTest() {
        PlayerDataLM playerLMCreated = createJson.createPlayerLM(player1);
        assertEquals(playerLM1.getIdPlayer(), playerLMCreated.getIdPlayer());
        assertEquals(playerLM1.getNickname(), playerLMCreated.getNickname());
        assertEquals(playerLM1.getnBlueAmmo(), playerLMCreated.getnBlueAmmo());
        assertEquals(playerLM1.getnRedAmmo(), playerLMCreated.getnRedAmmo());
        assertEquals(playerLM1.getnYellowAmmo(), playerLMCreated.getnYellowAmmo());
        assertEquals(playerLM1.getnMyLoadedWeapons(), playerLMCreated.getnMyLoadedWeapons());
        assertEquals(playerLM1.getnPowerUps(), playerLMCreated.getnPowerUps());
        assertEquals(playerLM1.getnSkulls(), playerLMCreated.getnSkulls());
        assertEquals(playerLM1.getDown(), playerLMCreated.getDown());
        assertEquals(playerLM1.getActive(), playerLMCreated.getActive());

        for(int i = 0; i < playerLM1.getDamageLine().size(); i++) {
            assertEquals(playerLM1.getDamageLine().get(i), playerLMCreated.getDamageLine().get(i));
        }

        for(int i = 0; i < playerLM1.getMarkLine().size(); i++) {
            assertEquals(playerLM1.getMarkLine().get(i), playerLMCreated.getMarkLine().get(i));
        }

        for(int i = 0; i < playerLM1.getUnloadedWeapons().size(); i++) {
            assertEquals(playerLM1.getUnloadedWeapons().get(i).getIdWeapon(), playerLMCreated.getUnloadedWeapons().get(i).getIdWeapon());
            assertEquals(playerLM1.getUnloadedWeapons().get(i).getName(), playerLMCreated.getUnloadedWeapons().get(i).getName());
        }
    }


    @Test
    public void createPlayerLMJsonTest() {
        String playerLMJsonCreated = createJson.createPlayerLMJson(player1);
        PlayerDataLM playerLMCreated = new Gson().fromJson(playerLMJsonCreated, PlayerDataLM.class);

        assertEquals(playerLM1.getIdPlayer(), playerLMCreated.getIdPlayer());
        assertEquals(playerLM1.getNickname(), playerLMCreated.getNickname());
        assertEquals(playerLM1.getnBlueAmmo(), playerLMCreated.getnBlueAmmo());
        assertEquals(playerLM1.getnRedAmmo(), playerLMCreated.getnRedAmmo());
        assertEquals(playerLM1.getnYellowAmmo(), playerLMCreated.getnYellowAmmo());
        assertEquals(playerLM1.getnMyLoadedWeapons(), playerLMCreated.getnMyLoadedWeapons());
        assertEquals(playerLM1.getnPowerUps(), playerLMCreated.getnPowerUps());
        assertEquals(playerLM1.getnSkulls(), playerLMCreated.getnSkulls());
        assertEquals(playerLM1.getDown(), playerLMCreated.getDown());
        assertEquals(playerLM1.getActive(), playerLMCreated.getActive());

        for(int i = 0; i < playerLM1.getDamageLine().size(); i++) {
            assertEquals(playerLM1.getDamageLine().get(i), playerLMCreated.getDamageLine().get(i));
        }

        for(int i = 0; i < playerLM1.getMarkLine().size(); i++) {
            assertEquals(playerLM1.getMarkLine().get(i), playerLMCreated.getMarkLine().get(i));
        }

        for(int i = 0; i < playerLM1.getUnloadedWeapons().size(); i++) {
            assertEquals(playerLM1.getUnloadedWeapons().get(i).getIdWeapon(), playerLMCreated.getUnloadedWeapons().get(i).getIdWeapon());
            assertEquals(playerLM1.getUnloadedWeapons().get(i).getName(), playerLMCreated.getUnloadedWeapons().get(i).getName());
        }
    }


    @Test
    public void createMyLoadedWeaponsLMTest() {
        MyLoadedWeaponsLM myLoadedWeaponsLMCreated = createJson.createMyLoadedWeaponsListLM(player1);
        MyLoadedWeaponsLM myLoadedWeaponsLM = new MyLoadedWeaponsLM(Arrays.asList(weaponLM3));

        for(int i = 0; i < myLoadedWeaponsLM.getLoadedWeapons().size(); i++) {
            assertEquals(myLoadedWeaponsLM.getLoadedWeapons().get(i).getIdWeapon(), myLoadedWeaponsLMCreated.getLoadedWeapons().get(i).getIdWeapon());
            assertEquals(myLoadedWeaponsLM.getLoadedWeapons().get(i).getName(), myLoadedWeaponsLMCreated.getLoadedWeapons().get(i).getName());
        }
    }


    @Test
    public void createMyLoadedWeaponLMJsonTest() {
        String myLoadedWeaponsLMJsonCreated = createJson.createMyLoadedWeaponsListLMJson(player1);
        MyLoadedWeaponsLM myLoadedWeaponsLM = new MyLoadedWeaponsLM(Arrays.asList(weaponLM3));
        MyLoadedWeaponsLM myLoadedWeaponsLMCreated = new Gson().fromJson(myLoadedWeaponsLMJsonCreated, MyLoadedWeaponsLM.class);

        for(int i = 0; i < myLoadedWeaponsLM.getLoadedWeapons().size(); i++) {
            assertEquals(myLoadedWeaponsLM.getLoadedWeapons().get(i).getIdWeapon(), myLoadedWeaponsLMCreated.getLoadedWeapons().get(i).getIdWeapon());
            assertEquals(myLoadedWeaponsLM.getLoadedWeapons().get(i).getName(), myLoadedWeaponsLMCreated.getLoadedWeapons().get(i).getName());
        }
    }


    @Test
    public void createMyPowerUpsLMTest() {
        MyPowerUpLM myPowerUpLMCreated = createJson.createMyPowerUpsLM(player1);
        MyPowerUpLM myPowerUpLM = new MyPowerUpLM(Arrays.asList(createJson.createPowerUpLM(powerUp1)));

        for(int i = 0; i < myPowerUpLM.getPowerUps().size(); i++) {
            assertEquals(myPowerUpLM.getPowerUps().get(i).getIdPowerUp(), myPowerUpLMCreated.getPowerUps().get(i).getIdPowerUp());
            assertEquals(myPowerUpLM.getPowerUps().get(i).getName(), myPowerUpLM.getPowerUps().get(i).getName());
            assertEquals(myPowerUpLM.getPowerUps().get(i).getGainAmmoColor(), myPowerUpLMCreated.getPowerUps().get(i).getGainAmmoColor());
        }
    }


    @Test
    public void createMyPowerUpsLMJsonTest() {
        String myPowerUpLMJsonCreated = createJson.createMyPowerUpsLMJson(player1);
        MyPowerUpLM myPowerUpLM = new MyPowerUpLM(Arrays.asList(createJson.createPowerUpLM(powerUp1)));
        MyPowerUpLM myPowerUpLMCreated = new Gson().fromJson(myPowerUpLMJsonCreated, MyPowerUpLM.class);

        for(int i = 0; i < myPowerUpLM.getPowerUps().size(); i++) {
            assertEquals(myPowerUpLM.getPowerUps().get(i).getIdPowerUp(), myPowerUpLMCreated.getPowerUps().get(i).getIdPowerUp());
            assertEquals(myPowerUpLM.getPowerUps().get(i).getName(), myPowerUpLM.getPowerUps().get(i).getName());
            assertEquals(myPowerUpLM.getPowerUps().get(i).getGainAmmoColor(), myPowerUpLMCreated.getPowerUps().get(i).getGainAmmoColor());
        }
    }


    @Test
    public void createMapLMTest() {
        int idPlayer = 1;

        Player player = mock(Player.class);
        when(player.getIdPlayer()).thenReturn(idPlayer);
        gameTable.getMap()[0][2].addPlayerOnSquare(player);

        MapLM mapLM = createJson.createMapLM();

        for(int i = 0; i < GeneralInfo.ROWS_MAP; i++) {
            for(int j = 0; j < GeneralInfo.COLUMNS_MAP; j++) {
                if(mapLM.getMap()[i][j] != null) {
                    assertTrue(((mapLM.getMap()[i][j] instanceof SpawnPointLM) || (mapLM.getMap()[i][j] instanceof AmmoPointLM)));

                    if(i == 0 && j == 2) {
                        if(!mapLM.getMap()[i][j].getPlayers().isEmpty()) {
                            System.out.println("here");
                            int id = mapLM.getMap()[i][j].getPlayers().get(0);
                            assertEquals(player.getIdPlayer(), id);
                        }
                    }
                }
            }
        }
    }


    @Test
    public void createKillShotTrackLMTest() {
        when(gameTable.getKillshotTrack()).thenReturn(Arrays.asList(new KillToken("priscilla", true), new KillToken("luca", false)));
        KillshotTrackLM killshotTrackLMCreated = createJson.createKillShotTrackLM();
        KillshotTrackLM killshotTrackLM = new KillshotTrackLM(Arrays.asList(new KillToken("priscilla", true), new KillToken("luca", false)), 5);

        assertEquals(killshotTrackLM.getInitialNumberOfSkulls(), killshotTrackLMCreated.getInitialNumberOfSkulls());

        for(int i = 0; i < killshotTrackLM.getTrack().size(); i++) {
            assertEquals(killshotTrackLM.getTrack().get(i).getCharacterName(), killshotTrackLMCreated.getTrack().get(i).getCharacterName());
            assertEquals(killshotTrackLM.getTrack().get(i).isOverkill(), killshotTrackLMCreated.getTrack().get(i).isOverkill());
        }
    }


    @Test
    public void createKillShotTrackLMJsonTest() {
        KillToken killToken1 = new KillToken("priscilla", true);
        KillToken killToken2 = new KillToken("luca", false);
        List<KillToken> killTokens = new ArrayList<>();
        killTokens.add(killToken1);
        killTokens.add(killToken2);
        when(gameTable.getKillshotTrack()).thenReturn(killTokens);
        when(gameTable.initialNumberOfSkulls()).thenReturn(5);

        String killShotTrackLMJsonCreated = createJson.createKillShotTrackLMJson();
        KillshotTrackLM killshotTrackLM = new KillshotTrackLM(killTokens, 5);
        KillshotTrackLM killshotTrackLMCreated = new Gson().fromJson(killShotTrackLMJsonCreated, KillshotTrackLM.class);

        assertEquals(killshotTrackLM.getInitialNumberOfSkulls(), killshotTrackLMCreated.getInitialNumberOfSkulls());

        for(int i = 0; i < killshotTrackLM.getTrack().size(); i++) {
            assertEquals(killshotTrackLM.getTrack().get(i).getCharacterName(), killshotTrackLMCreated.getTrack().get(i).getCharacterName());
            assertEquals(killshotTrackLM.getTrack().get(i).isOverkill(), killshotTrackLMCreated.getTrack().get(i).isOverkill());
        }
    }



    @Test
    public void createDrawCardsInfoJsonTest() {
        String drawCardsInfoJsonCreated = createJson.createDrawCardsInfoJson(player1, Arrays.asList(powerUp2, powerUp3));
        DrawCardsInfoResponse drawCardsInfoResponseCreated = new Gson().fromJson(drawCardsInfoJsonCreated, DrawCardsInfoResponse.class);
        DrawCardsInfoResponse drawCardsInfoResponse = new DrawCardsInfoResponse(player1.getIdPlayer(), createJson.createPowerUpsListLM(Arrays.asList(powerUp2, powerUp3)));

        assertEquals(drawCardsInfoResponse.getIdPlayer(), drawCardsInfoResponseCreated.getIdPlayer());

        for(int i = 0; i < drawCardsInfoResponse.getDrawnPowerUps().size(); i++) {
            assertEquals(drawCardsInfoResponse.getDrawnPowerUps().get(i).getIdPowerUp(), drawCardsInfoResponseCreated.getDrawnPowerUps().get(i).getIdPowerUp());
            assertEquals(drawCardsInfoResponse.getDrawnPowerUps().get(i).getName(), drawCardsInfoResponseCreated.getDrawnPowerUps().get(i).getName());
            assertEquals(drawCardsInfoResponse.getDrawnPowerUps().get(i).getGainAmmoColor(), drawCardsInfoResponseCreated.getDrawnPowerUps().get(i).getGainAmmoColor());
        }
    }


    @Test
    public void createMessagePowerUpsDiscardedJson() {
        String messagePowerUpsDiscardedJsonCreated = createJson.createMessagePowerUpsDiscardedJson(player1, powerUps);
        MessagePowerUpsDiscarded messagePowerUpsDiscarded = new MessagePowerUpsDiscarded(player1.getIdPlayer(), player1.getCharaName(), createJson.createPowerUpsListLM(powerUps));
        MessagePowerUpsDiscarded messagePowerUpsDiscardedCreated = new Gson().fromJson(messagePowerUpsDiscardedJsonCreated, MessagePowerUpsDiscarded.class);

        assertEquals(messagePowerUpsDiscarded.getIdPlayer(), messagePowerUpsDiscardedCreated.getIdPlayer());
        assertEquals(messagePowerUpsDiscarded.getNamePlayer(), messagePowerUpsDiscardedCreated.getNamePlayer());

        for(int i = 0; i < messagePowerUpsDiscarded.getPowerUpsToDiscard().size(); i++) {
            assertEquals(messagePowerUpsDiscarded.getPowerUpsToDiscard().get(i).getIdPowerUp(), messagePowerUpsDiscardedCreated.getPowerUpsToDiscard().get(i).getIdPowerUp());
        }
    }


    @Test
    public void createGrabWeaponSwapJsonTest() {
        String grabWeaponSwapJson = createJson.createGrabWeaponSwapJson(player1, weaponsList);
        GrabWeaponSwap grabWeaponSwap = new GrabWeaponSwap(player1.getIdPlayer(), createJson.createWeaponsListLM(weaponsList));
        GrabWeaponSwap grabWeaponSwapCreated = new Gson().fromJson(grabWeaponSwapJson, GrabWeaponSwap.class);

        assertEquals(grabWeaponSwap.getIdPlayer(), grabWeaponSwapCreated.getIdPlayer());

        for(int i = 0; i < grabWeaponSwap.getWeapons().size(); i++) {
            assertEquals(grabWeaponSwap.getWeapons().get(i).getIdWeapon(), grabWeaponSwapCreated.getWeapons().get(i).getIdWeapon());
            assertEquals(grabWeaponSwap.getWeapons().get(i).getName(), grabWeaponSwapCreated.getWeapons().get(i).getName());
        }
    }


    @Test
    public void createMessageDrawMyPowerUpJsonTest() {
        String messageDrawMyPowerUP = createJson.createMessageDrawMyPowerUpJson(player1, powerUps);
        int[] idPowerUps = new int[2];
        idPowerUps[1] = 1;
        List<String> namePowerUps = Arrays.asList(GeneralInfo.TAGBACK_GRENADE, GeneralInfo.TAGBACK_GRENADE);
        MessageDrawMyPowerUp messageDrawMyPowerUp = new MessageDrawMyPowerUp(player1.getIdPlayer(), player1.getCharaName(), idPowerUps, namePowerUps);
        MessageDrawMyPowerUp messageDrawMyPowerUpCreated = new Gson().fromJson(messageDrawMyPowerUP, MessageDrawMyPowerUp.class);

        assertEquals(messageDrawMyPowerUp.getIdPlayer(), messageDrawMyPowerUpCreated.getIdPlayer());
        assertEquals(messageDrawMyPowerUp.getNamePlayer(), messageDrawMyPowerUpCreated.getNamePlayer());

        for(int i = 0; i < messageDrawMyPowerUp.getNamePowerUps().size(); i++) {
            assertEquals(messageDrawMyPowerUp.getNamePowerUps().get(i), messageDrawMyPowerUpCreated.getNamePowerUps().get(i));
        }

        for(int i = 0; i < messageDrawMyPowerUp.getIdPowerUp().length; i++) {
            assertEquals(messageDrawMyPowerUp.getIdPowerUp()[i], messageDrawMyPowerUpCreated.getIdPowerUp()[i]);
        }
    }


    @Test
    public void nPowerUpsDrawn() {
        List<PowerUp> powerUps = Arrays.asList(powerUp1, powerUp2, powerUp3);

        try {
            String messageDrawMyPowerUpJson = createJson.createMessageDrawMyPowerUpJson(player1, powerUps);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println(e);
        }

        try {
            String messageEnemyDrawPowerUpJson = createJson.createMessageEnemyDrawPowerUpJson(player1, -1);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }



    @Test
    public void nullPointerWeaponsTest() {
        try {
            WeaponLM weaponLM = createJson.createWeaponLM(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//-------------------------

        try {
            List<WeaponLM> weaponLMList = createJson.createWeaponsListLM(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//-------------------------

        try {
            String grabWeaponSwapJson = createJson.createGrabWeaponSwapJson(player1, null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

    }


    @Test
    public void nullPointerPlayerTest() {
        try {
            PlayerDataLM playerDataLM = createJson.createPlayerLM(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//--------------------------

        try {
            String playerLMJson = createJson.createPlayerLMJson(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//---------------------------

        try {
            MyLoadedWeaponsLM myLoadedWeaponsLMCreated = createJson.createMyLoadedWeaponsListLM(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//---------------------------

        try {
            String myLoadedWeaponsLMJsonCreated = createJson.createMyLoadedWeaponsListLMJson(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//------------------------------

        try {
            MyPowerUpLM myPowerUpLMCreated = createJson.createMyPowerUpsLM(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
//------------------------------

        try {
            String myPowerUpLMCreatedJson = createJson.createMyPowerUpsLMJson(null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String drawCardsInfoResponseJson = createJson.createDrawCardsInfoJson(null, powerUps);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String messagePowerUpsDiscarded = createJson.createMessagePowerUpsDiscardedJson(null, powerUps);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }


        try {
            String grabWeaponSwapJson = createJson.createGrabWeaponSwapJson(null, weaponsList);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }


        try {
            String messageDrawMyPowerUpJson = createJson.createMessageDrawMyPowerUpJson(null, powerUps);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String messageEnemyDrawPowerUpJson = createJson.createMessageEnemyDrawPowerUpJson(null, 2);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String messageJson = createJson.createMessageJson(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            Message message = createJson.createMessage(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }


    @Test
    public void nullPointerPowerUpsTest() {
        try {
            PowerUpLM powerUpLM = createJson.createPowerUpLM(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }


        try {
            List<PowerUpLM> powerUpLMList = createJson.createPowerUpsListLM(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }


        try {
            String drawCardsInfoResponseJson = createJson.createDrawCardsInfoJson(player1, null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String messagePowerUpsDiscarded = createJson.createMessagePowerUpsDiscardedJson(player1, null);
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            String messageDrawMyPowerUpJson = createJson.createMessageDrawMyPowerUpJson(player1, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }


    @Test
    public void nullPointerAmmoCardTest() {
        try {
            AmmoCardLM ammoCardLM = createJson.createAmmoCardLM(null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }
}
