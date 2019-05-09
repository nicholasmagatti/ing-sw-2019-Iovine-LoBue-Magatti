package it.polimi.ProgettoIngSW2019.common.message.shootmsg;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;

import java.io.Serializable;
import java.util.List;

public class ShootMsgForClient implements Serializable, IUserInputMsgForClient<ShootMessage> {
    ShootMessage msg;

    @Override
    public void setMessage(ShootMessage msg) {
        this.msg = msg;
    }

    public ShootMessage getMsg(){
        return msg;
    }


}
