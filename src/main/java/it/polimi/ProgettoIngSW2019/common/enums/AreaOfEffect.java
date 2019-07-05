package it.polimi.ProgettoIngSW2019.common.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Enum used to locate the position of the possible target based on the weapon effect.
 *
 * @Author: Luca Iovine
 */
public enum AreaOfEffect {
    NORTH_DIRECTION("north_direction"),
    SOUTH_DIRECTION("south_direction"),
    EAST_DIRECTION("east_direction"),
    WEST_DIRECTION("west_direction"),
    @SerializedName("same_room")
    SAME_ROOM("same_room"),
    @SerializedName("can_see")
    CAN_SEE ("can_see"),
    @SerializedName("same_square")
    SAME_SQUARE ("same_square"),
    @SerializedName("near_room_visible")
    NEAR_ROOM_VISIBLE ("near_room_visible"),
    @SerializedName("cannot_see")
    CANNOT_SEE ("cannot_see"),
    @SerializedName("can_see_at_least_one")
    CAN_SEE_ATLEAST_ONE ("can_see_at_least_one"),
    @SerializedName("can_see_at_least_two")
    CAN_SEE_ATLEAST_TWO ("can_see_at_least_two"),
    @SerializedName("up_to_one")
    UP_TO_ONE("up_to_one"),
    @SerializedName("up_to_two")
    UP_TO_TWO("up_to_two"),
    @SerializedName("all")
    ALL("all");

    private String stringValue;

    AreaOfEffect(String stringValue){
        this.stringValue = stringValue;
    }
}
