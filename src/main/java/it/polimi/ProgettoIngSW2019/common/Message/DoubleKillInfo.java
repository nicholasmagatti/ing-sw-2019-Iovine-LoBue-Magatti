package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * DoubleKill info class
 * message if the player does more than one kill
 * @author Priscilla Lo Bue
 */
public class DoubleKillInfo extends Info {
    private String nameKiller;
    private boolean doubleKill;


    /**
     * Constructor from controller to view
     * @param idKiller      id killer player
     * @param nameKiller    name killer player
     * @param doubleKill    boolean if the killer did more than one kill
     */
    public DoubleKillInfo(int idKiller, String nameKiller, boolean doubleKill) {
        super(idKiller);

        if(nameKiller == null)
            throw new NullPointerException("nameKiller cannot be null");

        this.nameKiller = nameKiller;
        this.doubleKill = doubleKill;
    }


    /**
     * get id killer player
     * @return  id player
     */
    @Override
    public int getIdPlayer() {
        return super.getIdPlayer();
    }


    /**
     * get the name of the killer player
     * @return  name of the killer
     */
    public String getNameKiller() {
        return nameKiller;
    }


    /**
     * get if double kill
     * @return  boolean
     */
    public boolean getDoubleKill() {
        return doubleKill;
    }
}
