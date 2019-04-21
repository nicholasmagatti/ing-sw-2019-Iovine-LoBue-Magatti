package it.polimi.ProgettoIngSW2019.model.interfaces;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.enums.*;

import java.util.List;

public interface WeaponEffect {
    void activateEffectOn(Player targetPlayer, Player ownerPlayer, List<Directions> movementDirections);
}
