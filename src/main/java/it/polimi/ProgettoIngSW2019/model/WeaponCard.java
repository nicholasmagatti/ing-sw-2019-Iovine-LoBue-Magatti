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
    private List<WeaponEffect> weaponEffect;
    private Boolean loaded;
    private List<AmmoType> reloadCost;

    /**
     * Costruttore della classe
     *
     * @param name
     * @param description
     * @param reloadCost
     * @param pathEffJson : percorso nella quale sono presenti i file JSON degli effetti delle carte arma
     * @author Luca Iovine
     */
    WeaponCard(String name, String description, List<AmmoType> reloadCost, String pathEffJson){
        this.name  = name;
        this.description = description;
        this.reloadCost = reloadCost;

        /*
        Here it load the json file and create the effect for the weapon.
        It uses lambda expression to do so.
         */

        try {
            BufferedReader br = new BufferedReader(new FileReader(pathEffJson));
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
            /*
            At index 0 we'll got the base effect
             */
            WeaponEffect effBase = (targetPlayer, ownerPlayer, movementDirections) -> {
                targetPlayer.dealDamage(jsonObj.get("dmg").getAsInt(), ownerPlayer);
                targetPlayer.markPlayer(jsonObj.get("mark").getAsInt(), ownerPlayer);
                targetPlayer.move(movementDirections);
                this.loaded = false;
            };

            weaponEffect.add(effBase);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Restituisce il nome dell'arma
     *
     * @return name
     *
     * @author Luca Iovine
     */
    public String getName(){
        return this.name;
    }

    /**
     * Restituisce la descrizione dell'arma
     *
     * @return description
     * @author Luca Iovine
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Restituisce il costo di ricarica dell'arma
     *
     * @return reloadCost
     * @author Luca Iovine
     */
    public List<AmmoType> getreloadCost(){
        return this.reloadCost;
    }

    /**
     * Ricarica l'arma consumando le munizioni disponibili del player
     *
     * @author Luca Iovine
     */
    public void reload(Player p){
        p.discardAmmo(reloadCost);
        this.loaded = true;
    }

    /**
     * Resistuisce un valore booleano per rappresentare se l'arma è carica o meno
     *
     * @return loaded
     * @author Luca Iovine
     */
    public Boolean isLoaded(){
        return this.loaded;
    }

    /**
     * Restituisce tutte le informazioni dell'arma
     *
     * @return weapon info
     * @author Luca Iovine
     */
    public String getWeaponCardInfo(){
        return "Nome: " + this.name + "\nDescrizione: " + this.description +
                "\nCosto di ricarica: " + this.reloadCost;
    }
}
