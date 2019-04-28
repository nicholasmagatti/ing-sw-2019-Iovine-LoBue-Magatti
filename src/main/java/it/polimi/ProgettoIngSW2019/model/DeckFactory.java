package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.id_generators.IdCardGenerator;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TargettingScopeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TeleporterEff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class DeckFactory: creates the deck
 * @author Priscilla Lo Bue
 */
public class DeckFactory {
    private List<Card> deck = new ArrayList<>();
    private IdCardGenerator idCardGenerator = new IdCardGenerator();

    private static final String TAGBACK_GRENADE = "TAGBACK GRENADE";
    private static final String NEWTON = "NEWTON";
    private static final String TARGETING_SCOPE = "TARGETING SCOPE";
    private static final String TELEPORTER = "TELEPORTER";

    private static final String descrTargetingScope = "You may play this card when you are dealing damage to one or more targets. " +
            "\nPay 1 ammo cube of any color. Choose 1 of those targets and give it an extra point of damage." +
            "\nNote: You cannot use this to do 1 damage to a target that is receiving only marks.";
    //tutte le descrizioni
    private static final String descrNewton = "";
    private static final String descrTagbackGrenade = "";
    private static final String descrTeleporter = "";

    /**
     * creates the deck from the deckType with all the cards
     * @param deckType      type of the deck
     * @return              deck, list of cards
     */
    public List<Card> setDeck(DeckType deckType){
        switch(deckType){
            case AMMO_CARD:
                //36 ammo cards: 12 with 3 ammo and 12 with 2 ammo and one powerUp
                //18 ammo cards with 3 ammo
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
                break;


            case POWERUP_CARD:

                //24 powerUps cards, 4 PowerUps, 4 color x2
                //6 Tagback grenade
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TAGBACK_GRENADE, descrTagbackGrenade, new TagbackGrenadeEff()));

                //6 Newton
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, NEWTON, descrNewton, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, NEWTON, descrNewton, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, NEWTON, descrNewton, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, NEWTON, descrNewton, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, NEWTON, descrNewton, new NewtonEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, NEWTON, descrNewton, new NewtonEff()));

                //6 Targeting Scope
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TARGETING_SCOPE, descrTargetingScope, new TargettingScopeEff()));

                //6 Teleporter
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TELEPORTER, descrTeleporter, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TELEPORTER, descrTeleporter, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TELEPORTER, descrTeleporter, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.YELLOW, TELEPORTER, descrTeleporter, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.RED, TELEPORTER, descrTeleporter, new TeleporterEff()));
                deck.add(new PowerUp(idCardGenerator.generateId(), DeckType.POWERUP_CARD, AmmoType.BLUE, TELEPORTER, descrTeleporter, new TeleporterEff()));
                break;


            case WEAPON_CARD:

                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "LOCK RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "ELECTROSCYTHE", "", Arrays.asList(AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "MACHINE GUN", "", Arrays.asList(AmmoType.BLUE, AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "TRACTOR BEAM", "", Arrays.asList(AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "T.H.O.R.", "", Arrays.asList(AmmoType.BLUE, AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "VORTEX CANNON", "", Arrays.asList(AmmoType.RED, AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "FURNACE", "", Arrays.asList(AmmoType.RED, AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "PLASMA GUN", "", Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "HEATSEEKER", "", Arrays.asList(AmmoType.RED, AmmoType.RED, AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "WHISPER", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE, AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "HELLION", "", Arrays.asList(AmmoType.RED, AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "FLAMETHROWER", "", Arrays.asList(AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "ZX-2", "", Arrays.asList(AmmoType.YELLOW, AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "GRENADE LAUNCHER", "", Arrays.asList(AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "SHOTGUN", "", Arrays.asList(AmmoType.YELLOW, AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "ROCKET LAUNCHER", "", Arrays.asList(AmmoType.RED, AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "POWER GLOVE", "", Arrays.asList(AmmoType.YELLOW, AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "RAIL GUN", "", Arrays.asList(AmmoType.YELLOW, AmmoType.YELLOW, AmmoType.BLUE), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "SHOCKWAVE", "", Arrays.asList(AmmoType.YELLOW), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "CYBERBLADE", "", Arrays.asList(AmmoType.YELLOW, AmmoType.RED), ""));
                deck.add(new WeaponCard(idCardGenerator.generateId(), DeckType.WEAPON_CARD, "SLEDGEHAMMER", "", Arrays.asList(AmmoType.YELLOW), ""));
                break;

            default:
                return deck;
        }
        return deck;
    }
}