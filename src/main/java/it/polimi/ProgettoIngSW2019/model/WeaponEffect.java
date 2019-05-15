package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.common.enums.TargetPerSquare;

import java.io.*;

public class WeaponEffect {
    private Gson gson = new Gson();
    private FileReader file;
    private int effDmg;
    private int effMark;
    private AreaOfEffect effAoe;
    private int effNrTarget;
    private TargetPerSquare effTargetPerSquare;
    private int mustMoveNrOfSquareTarget;
    private AreaOfEffect mustMoveTargetAOE;
    private int mustMoveNrOfSquareYourself;
    private AreaOfEffect mustMoveYourselfAOE;
    private boolean mustMoveTarget = false;
    private boolean mustMoveYourself = false;
    private int optionalMoveNrOfSquareTarget;
    private AreaOfEffect optionalMoveTargetAOE;
    private int optionalMoveNrOfSquareYourself;
    private AreaOfEffect optionalMoveYourselfAOE;
    private boolean optionalMoveTarget = false;
    private boolean optionalMoveYourself = false;

    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param pathFileEff path of the weapon effect file
     * @suthor: Luca Iovine
     */
    public WeaponEffect(String pathFileEff){
        try {
            file = new FileReader(pathFileEff);
            BufferedReader br = new BufferedReader(file);
            JsonObject allJsonObj = gson.fromJson(br, JsonObject.class);
            effDmg = allJsonObj.getAsJsonObject("effect").get("dmg").getAsInt();
            effMark = allJsonObj.getAsJsonObject("effect").get("mark").getAsInt();
            effAoe = AreaOfEffect.fromString(allJsonObj.getAsJsonObject("effect").get("aoe").getAsString());
            effNrTarget = allJsonObj.getAsJsonObject("effect").get("nr_target").getAsInt();
            effTargetPerSquare = TargetPerSquare.fromString(allJsonObj.getAsJsonObject("effect").get("target_per_square").getAsString());

            if (allJsonObj.getAsJsonObject("must_move").size() > 0) {
                mustMoveNrOfSquareTarget = allJsonObj.getAsJsonObject("must_move").get("move_target").getAsInt();
                mustMoveTargetAOE = AreaOfEffect.fromString(allJsonObj.getAsJsonObject("must_move").get("move_target_aoe_end").getAsString());
                if (mustMoveNrOfSquareTarget > 0)
                    mustMoveTarget = true;

                mustMoveNrOfSquareYourself = allJsonObj.getAsJsonObject("must_move").get("move_yourself").getAsInt();
                mustMoveYourselfAOE = AreaOfEffect.fromString(allJsonObj.getAsJsonObject("must_move").get("move_yoruself_aoe_end").getAsString());
                if (mustMoveNrOfSquareYourself > 0)
                    mustMoveYourself = true;
            }

            if (allJsonObj.getAsJsonObject("optional_move").size() > 0) {
                optionalMoveNrOfSquareTarget = allJsonObj.getAsJsonObject("optional_move").get("move_target").getAsInt();
                optionalMoveTargetAOE = AreaOfEffect.fromString(allJsonObj.getAsJsonObject("optional_move").get("move_target_aoe_end").getAsString());
                if (optionalMoveNrOfSquareTarget > 0)
                    optionalMoveTarget = true;

                optionalMoveNrOfSquareYourself = allJsonObj.getAsJsonObject("optional_move").get("move_yourself").getAsInt();
                optionalMoveYourselfAOE = AreaOfEffect.fromString(allJsonObj.getAsJsonObject("optional_move").get("move_yoruself_aoe_end").getAsString());
                if (optionalMoveNrOfSquareYourself > 0)
                    optionalMoveYourself = true;
            }
        }catch(FileNotFoundException e) {
            System.out.print("Error Occurred: WeaponEff file has not found");
        }
    }

    /**
     * Deals the damage and mark the player based on the card effect
     *
     * @param targetPlayer player to be damaged
     * @param fromPlayer player who activate the effect
     * @suthor: Luca Iovine
     */
    public void activateEffect(Player targetPlayer, Player fromPlayer) {
        targetPlayer.dealDamage(effDmg, fromPlayer);
        targetPlayer.markPlayer(effMark, fromPlayer);
    }

    /**
     * @return the AreaOfEffect of the effect
     * @suthor: Luca Iovine
     */
    public AreaOfEffect getEffAoe() {
        return effAoe;
    }

    /**
     * @return the number of target a player can chose as target
     * @suthor: Luca Iovine
     */
    public int getEffNrTarget(){
        return effNrTarget;
    }

    /**
     * @return the enum which represent how many target per square you can hit
     * @suthor: Luca Iovine
     */
    public TargetPerSquare getEffTargetPerSquare() {
        return effTargetPerSquare;
    }

    /**
     * @return the number of movement the target must move due to the effect
     * @suthor: Luca Iovine
     */
    public int getMustMoveNrOfSquareTarget() {
        return mustMoveNrOfSquareTarget;
    }

    /**
     * @return the delimited area where the target must move
     * @suthor: Luca Iovine
     */
    public AreaOfEffect getMustMoveTargetAOE() {
        return mustMoveTargetAOE;
    }

    /**
     * @return the number of movement yourself must move due to the effect
     * @suthor: Luca Iovine
     */
    public int getMustMoveNrOfSquareYourself() {
        return mustMoveNrOfSquareYourself;
    }

    /**
     * @return the delimited area where yourself must move
     * @suthor: Luca Iovine
     */
    public AreaOfEffect getMustMoveYourselfAOE() {
        return mustMoveYourselfAOE;
    }

    /**
     * @return boolean value to aknowledge if the target must move
     * @suthor: Luca Iovine
     */
    public boolean getMustMoveTarget(){
        return mustMoveTarget;
    }

    /**
     * @return boolean value to aknowledge if yourself must move
     * @suthor: Luca Iovine
     */
    public boolean getMustMoveYourself(){
        return mustMoveYourself;
    }

    /**
     * @return the number of movement the target could move (oprional) due to the effect
     * @suthor: Luca Iovine
     */
    public int getOptionalMoveNrOfSquareTarget() {
        return optionalMoveNrOfSquareTarget;
    }

    /**
     * @return the delimited area where the target could move (oprional)
     * @suthor: Luca Iovine
     */
    public AreaOfEffect getOptionalMoveTargetAOE() {
        return optionalMoveTargetAOE;
    }

    /**
     * @return the number of movement yourself could move (oprional) due to the effect
     * @suthor: Luca Iovine
     */
    public int getOptionalMoveNrOfSquareYourself() {
        return optionalMoveNrOfSquareYourself;
    }

    /**
     * @return the delimited area where yourself could move (oprional)
     * @suthor: Luca Iovine
     */
    public AreaOfEffect getOptionalMoveYourselfAOE() {
        return optionalMoveYourselfAOE;
    }

    /**
     * @return boolean value to aknowledge if the target could move (oprional)
     * @suthor: Luca Iovine
     */
    public boolean getOptionalMoveTarget(){
        return optionalMoveTarget;
    }

    /**
     * @return boolean value to aknowledge if  yourself could move (oprional)
     * @suthor: Luca Iovine
     */
    public boolean getOptionalMoveYourself(){
        return optionalMoveYourself;
    }
}
