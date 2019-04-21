package it.polimi.ProgettoIngSW2019.model;

/**
 * Class used to determine the details of a token on the skull line
 * @author Nicholas Magatti
 */

public class KillToken {
    //name of the character (who made the kill shot)
    private String characterName;

    private boolean overkill;

    /**
     * Constructor
     * @param characterName
     * @param overkill
     */
    public KillToken(String characterName, boolean overkill){
        this.characterName = characterName;
        this.overkill = overkill;
    }

    /**
     * Get the the name of the character (the killer)
     * @return name of the character (the killer)
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Get true if the token represents an overkill, false otherwise
     * @return true if the token represents an overkill, false otherwise
     */
    public boolean isOverkill(){
        return overkill;
    }
}
