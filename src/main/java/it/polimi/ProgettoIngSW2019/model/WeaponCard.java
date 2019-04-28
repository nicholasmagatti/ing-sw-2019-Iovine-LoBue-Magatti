package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.model.enums.*;
import it.polimi.ProgettoIngSW2019.model.interfaces.WeaponEffect;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Classe WeaponCard
 *
 * @author Luca Iovine
 */
public class WeaponCard extends Card {
    private String name;
    private String description;
    private Gson gson = new Gson();
    private AreaOfEffect aoe;
    private int moveDueToBaseEff;
    private List<WeaponEffect> weaponEffects;
    private Boolean loaded;
    private List<AmmoType> reloadCost;

    /**
     * Constructor
     *
     * @param name name of the weapon
     * @param description textual description of the card
     * @param reloadCost ammo needed to reload the weapon
     * @param pathFileEff : path of json file to associate the proper effect
     * @author Luca Iovine
     */
    WeaponCard(int idCard, DeckType cardType, String name, String description, List<AmmoType> reloadCost, String pathFileEff){
        super(idCard, cardType);
        this.name  = name;
        this.description = description;
        this.reloadCost = reloadCost;

        /*
        Here it load the json file and create the effect for the weapon.
        It uses lambda expression to do so.
         */

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathFileEff));
            JsonObject jsonObj = gson.fromJson(br, JsonObject.class);

            /*
            Based on the aoe type, the player will see the possible target.
            This will be done thanks to a dictionary where the AreaOfEffect is associated with a function
            which evaluate the possible targets in range of the player who called it.
             */
            this.aoe = AreaOfEffect.valueOf(jsonObj.get("aoe").toString());
            /*
            Movement due the activation of an effect:
            the game will check if moveDueToBaseEff > 0. If it is, then the player who has used the card could
            decide to move his target.
             */
            this.moveDueToBaseEff = jsonObj.get("moveDueToBaseEff").getAsInt();

            WeaponEffect effBase = (targetPlayer, ownerPlayer) -> {
                targetPlayer.dealDamage(jsonObj.get("dmg").getAsInt(), ownerPlayer);
                targetPlayer.markPlayer(jsonObj.get("mark").getAsInt(), ownerPlayer);
                this.loaded = false;
            };
            weaponEffects.add(effBase);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * @return name of the weapon
     * @author Luca Iovine
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return description of the weapon
     * @author Luca Iovine
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return ammo need to reload the weapon
     * @author Luca Iovine
     */
    public List<AmmoType> getreloadCost(){
        return this.reloadCost;
    }

    /**
     * Reload weapon using ammo from the ammobox of the player
     *
     * @author Luca Iovine
     */
    public void reload(Player p){
        p.discardAmmo(reloadCost);
        this.loaded = true;
    }

    /**
     * @return if the weapon is loaded (true) or unloaded (false)
     * @author Luca Iovine
     */
    public Boolean isLoaded(){
        return this.loaded;
    }

    /**
     * @return weapon info in a formatted String
     * @author Luca Iovine
     */
    public String getWeaponCardInfo(){
        return "Nome: " + this.name + "\nDescrizione: " + this.description +
                "\nCosto di ricarica: " + this.reloadCost;
    }
}
