package it.polimi.ProgettoIngSW2019.common.Message.toView;

public class LoginResponse {
    boolean loginSuccessfull;

    public LoginResponse(boolean usernameAviable){
        this.loginSuccessfull = usernameAviable;
    }

    public boolean isLoginSuccessfull() {
        return loginSuccessfull;
    }

}
