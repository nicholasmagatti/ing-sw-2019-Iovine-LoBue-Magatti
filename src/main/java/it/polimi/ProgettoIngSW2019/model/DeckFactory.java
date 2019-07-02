package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.*;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.id_generators.IdCardGenerator;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TargetingScopeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TeleporterEff;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class DeckFactory: creates the deck
 * @author Priscilla Lo Bue
 * @author Nicholas Magatti
 */
public class DeckFactory {
    private List<Card> deck;
    private IdCardGenerator idCardGenerator = new IdCardGenerator();
    private DistanceDictionary distanceDictionary;

    private Gson gson = new Gson();
    private JsonObject jsonObject;

    private static int firstIdAmmo = 0;
    private static int lastIdAmmo = 0;
    private static int firstIdPowerUp = 0;
    private static int lastIdPowerUp = 0;
    private static int firstIdWeapon = 0;
    private static int lastIdWeapon = 0;


    public  DeckFactory(DistanceDictionary distanceDictionary) {
        this.distanceDictionary = distanceDictionary;
    }


    /**
     * creates the deck from the deckType with all the cards
     * @param deckType      type of the deck
     * @return              deck, list of cards
     * @author Priscilla Lo Bue
     * @author Nichoals Magatti
     */
    List<Card> setDeck(DeckType deckType){
        switch(deckType){
            case AMMO_CARD:
                deck = new ArrayList<>();
                //36 ammo cards: 12 with 3 ammo and 12 with 2 ammo and one powerUp
                //18 ammo cards with 3 ammo
                firstIdAmmo = idCardGenerator.getId();
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.RED, AmmoType.RED));

                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.RED, AmmoType.RED));

                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.RED, AmmoType.RED));

                //12 ammo cards with a powerUp
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE));

                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.YELLOW));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.BLUE, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.RED));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE));
                deck.add(new AmmoCard(idCardGenerator.generateId(), DeckType.AMMO_CARD, AmmoType.RED, AmmoType.BLUE));

                lastIdAmmo = idCardGenerator.getId() - 1;
                break;


            case POWERUP_CARD:
                deck = new ArrayList<>();

                //for descriptions:
                jsonObject = prepareFile("PowerupsDescriptions");
                final String GRAN_DESC, NEWT_DESC, TARG_DESC, TELE_DESC;
                GRAN_DESC = getDescription(GeneralInfo.TAGBACK_GRENADE);
                NEWT_DESC = getDescription(GeneralInfo.NEWTON);
                TARG_DESC = getDescription(GeneralInfo.TARGETING_SCOPE);
                TELE_DESC = getDescription(GeneralInfo.TELEPORTER);


                //24 powerUps cards, 4 PowerUps, 4 color x2
                //6 TagBack grenade
                firstIdPowerUp = idCardGenerator.getId();

                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TAGBACK_GRENADE, GRAN_DESC, new TagbackGrenadeEff()));

                //6 Newton
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.NEWTON, NEWT_DESC, new NewtonEff()));

                //6 Targeting Scope
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TARGETING_SCOPE, TARG_DESC, new TargetingScopeEff()));

                //6 Teleporter
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TELEPORTER, TELE_DESC, new TeleporterEff()));
                lastIdPowerUp = idCardGenerator.getId() - 1;
                break;


            case WEAPON_CARD:
                jsonObject = prepareFile("WeaponsDescriptions");
                deck = new ArrayList<>();
                final String LOCK_RIFLE = "LOCK RIFLE";
                final String ELECTROSCYTHE = "ELECTROSCYTHE";
                final String MACHINE_GUN = "MACHINE GUN";
                final String TRACTOR_BEAM = "TRACTOR BEAM";
                final String THOR = "T.H.O.R.";
                final String VORTEX_CANNON = "VORTEX CANNON";
                final String FURNACE = "FURNACE";
                final String PLASMA_GUN = "PLASMA GUN";
                final String HEATSEEKER = "HEATSEEKER";
                final String WHISPER = "WHISPER";
                final String HELLION = "HELLION";
                final String FLAMETHROWER = "FLAMETHROWER";
                final String ZX_2 = "ZX-2";
                final String GRENADE_LAUNCHER = "GRENADE LAUNCHER";
                final String SHOTGUN = "SHOTGUN";
                final String ROCKET_LAUNCHER = "ROCKET LAUNCHER";
                final String POWER_GLOVE = "POWER GLOVE";
                final String RAILGUN = "RAILGUN";
                final String SHOCKWAVE = "SHOCKWAVE";
                final String CYBERBLADE = "CYBERBLADE";
                final String SLEDGEHAMMER = "SLEDGEHAMMER";

                firstIdWeapon = idCardGenerator.getId();
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, LOCK_RIFLE, getDescription(LOCK_RIFLE), Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, ELECTROSCYTHE, getDescription(ELECTROSCYTHE), Arrays.asList(AmmoType.BLUE), "ElectroSchyteEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, MACHINE_GUN, getDescription(MACHINE_GUN), Arrays.asList(AmmoType.BLUE, AmmoType.RED), "MachineGunEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, TRACTOR_BEAM, getDescription(TRACTOR_BEAM), Arrays.asList(AmmoType.BLUE), "TractorBeamEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, THOR, getDescription(THOR), Arrays.asList(AmmoType.BLUE, AmmoType.RED), "ThorEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, VORTEX_CANNON, getDescription(VORTEX_CANNON), Arrays.asList(AmmoType.RED, AmmoType.BLUE), "VortexCannonEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, FURNACE, getDescription(FURNACE), Arrays.asList(AmmoType.RED, AmmoType.BLUE), "FurnaceEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, PLASMA_GUN, getDescription(PLASMA_GUN), Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW), "PlasmaGunEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, HEATSEEKER, getDescription(HEATSEEKER), Arrays.asList(AmmoType.RED, AmmoType.RED, AmmoType.YELLOW), "HeatSeekerEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, WHISPER, getDescription(WHISPER), Arrays.asList(AmmoType.BLUE, AmmoType.BLUE, AmmoType.YELLOW), "WhisperEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, HELLION, getDescription(HELLION), Arrays.asList(AmmoType.RED, AmmoType.YELLOW), "HellionEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, FLAMETHROWER, getDescription(FLAMETHROWER), Arrays.asList(AmmoType.RED), "FalmeThrowerEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, ZX_2, getDescription(ZX_2), Arrays.asList(AmmoType.YELLOW, AmmoType.RED), "ZX2Eff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, GRENADE_LAUNCHER, getDescription(GRENADE_LAUNCHER), Arrays.asList(AmmoType.RED), "GrenadeLauncherEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, SHOTGUN, getDescription(SHOTGUN), Arrays.asList(AmmoType.YELLOW, AmmoType.YELLOW), "ShotGunEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, ROCKET_LAUNCHER, getDescription(ROCKET_LAUNCHER), Arrays.asList(AmmoType.RED, AmmoType.RED), "RocketLauncherEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, POWER_GLOVE, getDescription(POWER_GLOVE), Arrays.asList(AmmoType.YELLOW, AmmoType.BLUE), "PowerGlowEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, RAILGUN, getDescription(RAILGUN), Arrays.asList(AmmoType.YELLOW, AmmoType.YELLOW, AmmoType.BLUE), "RailGunEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, SHOCKWAVE, getDescription(SHOCKWAVE), Arrays.asList(AmmoType.YELLOW), "ShockWaveEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, CYBERBLADE, getDescription(CYBERBLADE), Arrays.asList(AmmoType.YELLOW, AmmoType.RED), "CyberBladeEff.json", distanceDictionary));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, SLEDGEHAMMER, getDescription(SLEDGEHAMMER), Arrays.asList(AmmoType.YELLOW), "SledgeHammerEff.json", distanceDictionary));
                lastIdWeapon = idCardGenerator.getId() - 1;
                break;

             default:
                 throw new IllegalArgumentException("It is not passed a DeckType");
        }
        return deck;
    }


    public static int getLastIdAmmo() {
        return lastIdAmmo;
    }


    public static int getFirstIdAmmo() {
        return firstIdAmmo;
    }


    public static int getFirstIdPowerUp() {
        return firstIdPowerUp;
    }


    public static int getLastIdPowerUp() {
        return lastIdPowerUp;
    }


    public static int getFirstIdWeapon() {
        return firstIdWeapon;
    }


    public static int getLastIdWeapon() {
        return lastIdWeapon;
    }


    /**
     * Create and return the object to use to get the attributes(the descriptions) of the file with
     * the specified name in the folder of the descriptions.
     * @param nameJsonFile - name of the json file, without the extension (.json)
     * @return the JsonObject to get the descriptions from the specified file
     * @author Nicholas Magatti
     */
    private JsonObject prepareFile(String nameJsonFile){
        FileReader descPwUpsF;
        String pathInProj = "\\resources\\json\\descritpions\\";
        String pathPowerUps = new File("").getAbsolutePath() + pathInProj + nameJsonFile + ".json";
        BufferedReader brPwUp = null;
        try {
            descPwUpsF = new FileReader(pathPowerUps);


            brPwUp = new BufferedReader(descPwUpsF);

        }
        catch (FileNotFoundException e) {
            System.out.println("Files for descriptions not found: we cannot start the game.");
            System.exit(-1);
        }

        return gson.fromJson(brPwUp, JsonObject.class);
    }

    /**
     * Get the description of the card with the indicated name belonging to
     * the json file linked to the json object used in that circumstance
     * @param name - name card
     * @return description of the card with the indicated name belonging to
     * the json file linked to the json object used in that circumstance
     * @author Nicholas Magatti
     */
    private String getDescription(String name){
        if(jsonObject != null){
            throw new NullPointerException("this method should not have been called if the json object is null.");
        }
        return gson.fromJson(jsonObject.get(name), String.class);
    }

}