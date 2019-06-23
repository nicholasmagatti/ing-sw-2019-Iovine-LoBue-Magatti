package it.polimi.ProgettoIngSW2019.common.Message.toView;

public class LoginResponse {
    boolean loginSuccessfull;

    public LoginResponse(boolean loginSuccessfull){
        this.loginSuccessfull = loginSuccessfull;
    }

    public boolean isLoginSuccessfull() {
        return loginSuccessfull;
    }

}
