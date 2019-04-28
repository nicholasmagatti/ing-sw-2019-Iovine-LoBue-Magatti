package it.polimi.ProgettoIngSW2019.model.enums;

public enum AreaOfEffect {
    SAME_ROOM("same_room"),
    CAN_SEE ("can_see"),
    SAME_SQUARE ("same_square"),
    NEAR_ROOM_VISIBLE ("near_room_visible"),
    CANNOT_SEE ("cannot_see"),
    CAN_SEE_ATLEAST_ONE ("can_see_atleast_one"),
    CAN_SEE_ATLEAST_TWO ("can_see_atleast_two"),
    FLAMETHROWER ("same_directions_one"),
    VORTEX ("vortex");

    private String stringValue;

    AreaOfEffect(String stringValue){
        this.stringValue = stringValue;
    }
}
