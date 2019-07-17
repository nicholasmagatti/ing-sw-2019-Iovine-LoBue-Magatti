package it.polimi.ProgettoIngSW2019.model.id_generators;

/**
 * Class IdCardGenerator: it generates the id for the cards
 * @author Priscilla Lo Bue
 */
public class IdCardGenerator {
    int id = 0;

    /**
     * generate the Id
     * @return an incremented id
     */
    public int generateId() {
        return id++;
    }

    /**
     * Get the id
     * @return id
     */
    public int getId() {
        return id;
    }
}
