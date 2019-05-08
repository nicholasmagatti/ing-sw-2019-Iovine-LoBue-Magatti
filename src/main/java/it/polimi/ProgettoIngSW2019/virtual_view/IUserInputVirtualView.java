package it.polimi.ProgettoIngSW2019.virtual_view;

public interface IUserInputVirtualView<T> {
    void sendData(T msg);
    void retriveData();
}
