package it.polimi.ProgettoIngSW2019.common.enums;

/**
 * Enum used to locate the position of the possible target based on the weapon effect.
 *
 * @Author: Luca Iovine
 */
public enum AreaOfEffect {
    SAME_ROOM("same_room"),
    CAN_SEE ("can_see"),
    SAME_SQUARE ("same_square"),
    NEAR_ROOM_VISIBLE ("near_room_visible"),
    CANNOT_SEE ("cannot_see"),
    CAN_SEE_ATLEAST_ONE ("can_see_atleast_one"),
    CAN_SEE_ATLEAST_TWO ("can_see_atleast_two"),
    CHOOSE_DIRECTION ("choose_direction"), //TODO
    EXACTLY_ONE("exactly_one"),
    CAN_SEE_MAX_TWO("can_see_max_two"), //TODO
    ALL("all"),
    NONE ("none"),
    FLAMETHROWER ("same_directions_one"),
    VORTEX ("vortex"),
    HELLION("hellion");


    private String stringValue;

    AreaOfEffect(String stringValue){
        this.stringValue = stringValue;
    }

    public static AreaOfEffect fromString(String value) {
        for(AreaOfEffect aoe: values()){
            String aoeStringVal = aoe.stringValue;
            if(aoeStringVal.equals(value)){
                return aoe;
            }
        }
        return null;
    }
}
