package it.polimi.ProgettoIngSW2019;

import it.polimi.ProgettoIngSW2019.controller.ConnectionController;
import it.polimi.ProgettoIngSW2019.controller.SetupController;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.virtual_view.IVirtualView;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class MainClass {
    public static void main(String[] args) {
        if(args[0].equalsIgnoreCase("--Server") || args[0].equalsIgnoreCase("--S")) {
            VirtualView virtualView = new VirtualView();
            LoginHandler loginHandler = new LoginHandler();
            ConnectionController connectionController = new ConnectionController(virtualView, loginHandler);
            SetupController setupController = new SetupController(virtualView, loginHandler);

            virtualView.addObserver(connectionController);
            virtualView.addObserver(setupController);

            loginHandler.addObserver(virtualView);

            try{
                LocateRegistry.createRegistry(RMISettings.REGISTRY_PORT);
                IVirtualView exportedVW = (IVirtualView) UnicastRemoteObject.exportObject(virtualView, RMISettings.REGISTRY_PORT);
                Naming.rebind(RMISettings.SERVICE_NAME, exportedVW);

                System.out.println("Server ready");
            }catch(RemoteException | MalformedURLException e){
                System.out.println("Non è stato possibile avviare il server.");
            }finally {
                System.exit(-1);
            }
        }
    }
}
