package it.polimi.ProgettoIngSW2019.common.message;

import it.polimi.ProgettoIngSW2019.common.message.shootmsg.ShootMessage;

public interface IUserInputMsgForClient<T>{
    void setMessage(T message);
}
