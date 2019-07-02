package it.polimi.ProgettoIngSW2019;

import it.polimi.ProgettoIngSW2019.controller.ConnectionController;
import it.polimi.ProgettoIngSW2019.controller.SetupController;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.network_handler.NetworkHandler;
import it.polimi.ProgettoIngSW2019.view.*;
import it.polimi.ProgettoIngSW2019.virtual_view.IVirtualView;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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
                e.printStackTrace();
                System.exit(-1);
            }
        }else if(args[0].equalsIgnoreCase("--Client") || args[0].equalsIgnoreCase("--C")
        || args[0].equalsIgnoreCase("--localClient")){
            try {
                IVirtualView virtualView = (IVirtualView) Naming.lookup(RMISettings.SERVICE_NAME);
                IdleState idleState = new IdleState();
                ReloadState reloadState = new ReloadState(idleState);
                PowerUpState powerUpState = new PowerUpState(idleState);
                ActionState actionState = new ActionState(powerUpState, reloadState);
                GrabState grabState = new GrabState(actionState);
                ShootState shootState = new ShootState(actionState);
                MoveState moveState = new MoveState(actionState);
                SpawnState spawnState = new SpawnState(idleState);
                SetupGameState setupGameState = new SetupGameState(spawnState, idleState);
                LoginState loginState = new LoginState(setupGameState, idleState);
                GeneralMessageObserver generalMessageObserver = new GeneralMessageObserver();

                idleState.linkState(actionState, spawnState);
                actionState.linkToMoveGrabShoot(moveState, grabState, shootState);

                NetworkHandler networkHandler = new NetworkHandler(virtualView);

                if(args[0].equalsIgnoreCase("--localClient"))
                    loginState.setIsLocalOnly(true);

                StateManager stateManager = new StateManager();
                stateManager.triggerState(loginState);
            }catch(MalformedURLException | NotBoundException | RemoteException e){
                System.out.println("Non è stato possibile avviare il server.");
                System.exit(-1);
            }
        }else
            System.out.println(args[0]);
    }
}
