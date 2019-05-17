package it.polimi.ProgettoIngSW2019.common.LightModel;

public class AmmoBoxLM {
    private int red;
    private int blue;
    private int yellow;


    /**
     * Get the number of red ammo
     * @return int, n. red ammo
     */
    public int getRed() {
        return red;
    }



    /**
     * Get the number of blue ammo
     * @return int, n. blue ammo
     */
    public int getBlue() {
        return blue;
    }



    /**
     * Get the number of yellow ammo
     * @return int, n. yellow ammo
     */
    public int getYellow() {
        return yellow;
    }


    /**
     * return a message info with all the number of user ammo
     * @return  String, the message info Ammo Box
     */
    public String getInfoAmmoBox() {
        String msg;
        msg = "N. red ammo: " + getRed() + "\nN. blue ammo: " + getBlue() + "\nN.yellow ammo: " + getYellow();
        return msg;
    }
}
