package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabWeaponChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.GrabInfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Priscilla Lo Bue
 */
public class GrabController extends Controller {
    private PayAmmoController payAmmoController;
    private Player grabberPlayer;
    private SpawningPoint spawningPointToGrab = null;
    private List<AmmoPoint> ammoPointToGrabList = new ArrayList<>();
    private Square squareToGrab;
    private Deck powerUpDeck;
    private WeaponCard weaponToDiscard = null;
    private WeaponCard weaponToGrab;


    public GrabController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList, PayAmmoController payAmmoController){
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        powerUpDeck = turnManager.getGameTable().getPowerUpDeck();
        this.payAmmoController = payAmmoController;
    }



    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_GRAB_INFO)) {
            checkGrabInfoFromView(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_GRAB)) {
            checkGrabFromView(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_GRAB_WEAPON)) {
            checkGrabWeaponFromView(event.getMessageInJsonFormat());
        }
    }



    public void checkGrabInfoFromView(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);

        grabberPlayer = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

        if(grabberPlayer != null) {
            if(checkCurrentPlayer(grabberPlayer)) {
                if(checkHasActionLeft(grabberPlayer)) {
                    grabInfo();
                }
            }
        }
    }


//todo
    public void checkGrabFromView(String messageJson) {
        GrabChoiceRequest grabChoiceRequest = new Gson().fromJson(messageJson, GrabChoiceRequest.class);

        if(grabberPlayer.getIdPlayer() == grabChoiceRequest.getIdPlayer()) {
            squareToGrab = convertSquare(grabberPlayer, grabChoiceRequest.getCoordinates());

            if(squareToGrab != null) {
                grabChoice();
            }
        }
        else {
            String message = "ERROR: Qualcosa è andato storto!";
            sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
        }
    }



    public void checkGrabWeaponFromView(String messageJson) {
        GrabWeaponChoiceRequest grabWeaponChoiceRequest = new Gson().fromJson(messageJson, GrabWeaponChoiceRequest.class);

        if(grabberPlayer.getIdPlayer() == grabWeaponChoiceRequest.getIdPlayer()) {

            if(grabberPlayer.getNumberOfWeapons() == 3) {

                if (grabWeaponChoiceRequest.getIdWeaponToDiscard() >= 0) {

                    for (WeaponCard weaponCard : grabberPlayer.getUnloadedWeapons()) {
                        if (weaponCard.getIdCard() == grabWeaponChoiceRequest.getIdWeaponToDiscard())
                            weaponToDiscard = weaponCard;
                    }

                    for (WeaponCard weaponCard : grabberPlayer.getLoadedWeapons()) {
                        if (weaponCard.getIdCard() == grabWeaponChoiceRequest.getIdWeaponToDiscard())
                            weaponToDiscard = weaponCard;
                    }

                    if (weaponToDiscard == null) {
                        String message = "ERROR: hai più di 3 armi in mano, per pescarne una devi lasciare una carta arma nell0 Spawning Point.";
                        sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
                        return;
                    }
                }
                else {
                    String message = "ERROR: hai più di 3 armi in mano, per pescarne una devi lasciare una carta arma nell0 Spawning Point.";
                    sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
                    return;
                }
            }

            SpawningPoint spawningPoint = (SpawningPoint) squareToGrab;

            for(int i = 0; i < spawningPoint.getWeaponCards().size(); i++) {
                if(spawningPoint.getWeaponCards().get(i).getIdCard() == grabWeaponChoiceRequest.getIdWeaponToDiscard())
                    weaponToGrab = spawningPoint.getWeaponCards().get(i);
            }

            if(weaponToGrab != null) {
                List<PowerUp> powerUps = new ArrayList<>();

                if(!grabWeaponChoiceRequest.getIdPowerUpToDiscard().isEmpty()) {
                    for (int i: grabWeaponChoiceRequest.getIdPowerUpToDiscard()) {
                        PowerUp powerUp = convertPowerUp(grabberPlayer, i);

                        if(powerUp == null)
                            return;
                        else if(grabberPlayer.getPowerUps().contains(powerUp)) {
                            powerUps.add(powerUp);
                        }
                    }
                }

                if(payAmmoController.checkAmmoToPayFromView(weaponToGrab.getBuyCost(), powerUps, grabWeaponChoiceRequest.getAmmoToDiscard())) {
                    grabWeapon(powerUps);
                }
                else {
                    String message = "ERROR: Hai sbagliato il pagamento: ";
                    sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
                }
            }
        }
        else {
            String message = "ERROR: Qualcosa è andato storto!";
            sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
        }
    }


    public void grabInfo(/*che mi serve?*/) {
        if(grabberPlayer.getPosition().getSquareType() != null) {

            if (grabberPlayer.getPosition().getSquareType() == SquareType.SPAWNING_POINT) {
                SpawningPoint spawningPoint = (SpawningPoint) grabberPlayer.getPosition();

                if (!spawningPoint.getWeaponCards().isEmpty()) {
                    boolean canBuy = false;

                    for(WeaponCard weaponCard: spawningPoint.getWeaponCards()) {
                        if(grabberPlayer.hasEnoughAmmo(weaponCard.getBuyCost()))
                            canBuy = true;
                    }

                    if(canBuy)
                        spawningPointToGrab = spawningPoint;
                }
            }

            else if (grabberPlayer.getPosition().getSquareType() == SquareType.AMMO_POINT) {
                AmmoPoint ammoPoint = (AmmoPoint) grabberPlayer.getPosition();

                if (ammoPoint.getAmmoCard() != null)
                    ammoPointToGrabList.add(ammoPoint);
            }

            if(!grabberPlayer.getPosition().getIsBlockedAtEast())
                checkNearSquare(grabberPlayer.getPosition().getEastSquare());

            if(!grabberPlayer.getPosition().getIsBlockedAtNorth())
                checkNearSquare(grabberPlayer.getPosition().getNorthSquare());

            if(!grabberPlayer.getPosition().getIsBlockedAtWest())
                checkNearSquare(grabberPlayer.getPosition().getWestSquare());

            if(!grabberPlayer.getPosition().getIsBlockedAtSouth())
                checkNearSquare(grabberPlayer.getPosition().getSouthSquare());


            String grabInfoResponseJson = createGrabInfoResponseJson();
            sendInfo(EventType.REQUEST_GRAB_INFO, grabInfoResponseJson, getHostNameCreateList().addOneHostName(grabberPlayer));
        }
        else
            throw new IllegalAttributeException("square player cannot be null");
    }



    public void checkNearSquare(Square squareToCheck) {

        if (squareToCheck.getSquareType() == SquareType.SPAWNING_POINT) {
            SpawningPoint spawningPoint = (SpawningPoint) squareToCheck;

            if (!spawningPoint.getWeaponCards().isEmpty()) {

                if (spawningPointToGrab == null) {
                    boolean canBuy = false;

                    for(WeaponCard weaponCard: spawningPoint.getWeaponCards()) {
                        if(grabberPlayer.hasEnoughAmmo(weaponCard.getBuyCost()))
                            canBuy = true;
                    }

                    if(canBuy)
                        spawningPointToGrab = spawningPoint;
                }

                else
                    throw new IllegalAttributeException("Something Wrong: SquareTypePlayer is not a Spawning Point but spawningPointToGrab is not null");
            }
        }

        if(squareToCheck.getSquareType() == SquareType.AMMO_POINT) {
            AmmoPoint ammoPoint = (AmmoPoint) grabberPlayer.getPosition().getWestSquare();

            if(ammoPoint.getAmmoCard() != null)
                ammoPointToGrabList.add(ammoPoint);
        }
    }



    public void grabChoice() {
        if(grabberPlayer.getPosition() != squareToGrab) {
            List<Square> consecutiveSquares = consecutiveSquares();

            if(consecutiveSquares.isEmpty())
                throw new IllegalAttributeException("The player's square doesn't have consecutive squares, something wrong!");

            boolean found = false;

            for(Square square:consecutiveSquares) {
                if(square == squareToGrab)
                    found = true;
            }

            if(!found) {
                String message = "ERROR: Hai non puoi scegliere questo Square per fare il grab";
                sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
                return;
            }
            else {
                grabberPlayer.moveTo(squareToGrab);
                String updateMapLM = getCreateJson().createMapLMJson();
                sendInfo(EventType.UPDATE_MAP, updateMapLM, getHostNameCreateList().addAllHostName());
            }
        }

        if(squareToGrab.getSquareType() == SquareType.SPAWNING_POINT) {
            grabWeaponInfo();
        }
        else {
            grabAmmo();
        }
    }


    public List<Square> consecutiveSquares() {
        List<Square> squares = new ArrayList<>();

        if(!grabberPlayer.getPosition().getIsBlockedAtEast())
            squares.add(grabberPlayer.getPosition().getEastSquare());

        if(!grabberPlayer.getPosition().getIsBlockedAtWest())
            squares.add(grabberPlayer.getPosition().getWestSquare());

        if(!grabberPlayer.getPosition().getIsBlockedAtNorth())
            squares.add(grabberPlayer.getPosition().getNorthSquare());

        if(!grabberPlayer.getPosition().getIsBlockedAtSouth())
            squares.add(grabberPlayer.getPosition().getSouthSquare());

        return squares;
    }



    public void grabWeaponInfo() {
        SpawningPoint spawningPoint = (SpawningPoint) squareToGrab;

        if(spawningPoint.getWeaponCards().isEmpty()) {
            String message = "ERROR: non ci sono armi, rifare la selezione dello square";
            sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(grabberPlayer));
            return;
        }
        else {
            List<WeaponCard> weaponsCanBuy = new ArrayList<>();
            List<PayAmmoList> paymentWeaponBuy = new ArrayList<>();

            for(WeaponCard weaponCard: spawningPoint.getWeaponCards()) {
                if(grabberPlayer.hasEnoughAmmo(weaponCard.getBuyCost())) {
                    weaponsCanBuy.add(weaponCard);
                }
            }

            if(!weaponsCanBuy.isEmpty()) {

                if(grabberPlayer.getNumberOfWeapons() == 3) {
                    List<WeaponCard> myWeapons = new ArrayList<>();

                    for(WeaponCard weaponCard: grabberPlayer.getLoadedWeapons()) {
                        myWeapons.add(weaponCard);
                    }

                    for(WeaponCard weaponCard: grabberPlayer.getUnloadedWeapons()) {
                        myWeapons.add(weaponCard);
                    }

                    String myWeaponsJson = getCreateJson().createWeaponsListLMJson(myWeapons);
                    sendInfo(EventType.DISCARD_WEAPON, myWeaponsJson, getHostNameCreateList().addOneHostName(grabberPlayer));
                }

                for(WeaponCard weaponCard: weaponsCanBuy) {
                    paymentWeaponBuy.add(payAmmoController.ammoToPay(grabberPlayer, weaponCard, weaponCard.getBuyCost()));
                }
            }
            String weaponsToPayJson = getCreateJson().createWeaponsToPayJson(grabberPlayer, weaponsCanBuy, paymentWeaponBuy);
            sendInfo(EventType.WEAPONS_CAN_BUY, weaponsToPayJson, getHostNameCreateList().addOneHostName(grabberPlayer));
        }
    }



    public void grabWeapon(List<PowerUp> powerUps) {
        grabberPlayer.pay(weaponToGrab.getBuyCost(), powerUps);
        grabberPlayer.grabWeapon(weaponToGrab, weaponToDiscard);

        if(weaponToDiscard == null) {
            String messageEnemyWeaponPayJson = getCreateJson().createMessageEnemyWeaponPay(grabberPlayer, weaponToGrab);
            sendInfo(EventType.MSG_WEAPON_BUY, messageEnemyWeaponPayJson, getHostNameCreateList().addAllExceptOneHostName(grabberPlayer));
        }
        else {
            String messagePlayerSwapWeapons = getCreateJson().createMessagePlayerSwapWeapons(grabberPlayer, weaponToDiscard, weaponToGrab);
            sendInfo(EventType.MSG_WEAPON_SWAP, messagePlayerSwapWeapons, getHostNameCreateList().addAllHostName());
        }

        String updateMyLoadedWeapons = getCreateJson().createMyLoadedWeaponsListLMJson(grabberPlayer);
        sendInfo(EventType.UPDATE_MY_LOADED_WEAPONS, updateMyLoadedWeapons, getHostNameCreateList().addOneHostName(grabberPlayer));
    }



    public void grabAmmo() {
        AmmoPoint ammoPoint = (AmmoPoint) squareToGrab;
        AmmoCard ammoCard = ammoPoint.getAmmoCard();
        PowerUp powerUp = null;

        if(ammoCard.hasPowerUp()) {
            if(grabberPlayer.getNumberOfPoweUps() != 3) {
                powerUp = (PowerUp) powerUpDeck.getCards().get(0);
            }
        }

        grabberPlayer.grabAmmoCardFromThisSquare();
        String updatePlayer = getCreateJson().createPlayerLMJson(grabberPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayer, getHostNameCreateList().addAllHostName());

        if(powerUp != null) {
            String messageDrawMyPowerUp = getCreateJson().createMessageDrawMyPowerUpJson(grabberPlayer, Arrays.asList(powerUp));
            sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageDrawMyPowerUp, getHostNameCreateList().addOneHostName(grabberPlayer));

            String messageEnemyDrawPowerUp = getCreateJson().createMessageEnemyDrawPowerUpJson(grabberPlayer, 1);
            sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyDrawPowerUp, getHostNameCreateList().addAllExceptOneHostName(grabberPlayer));

            String updateMyPowerUps = getCreateJson().createMyPowerUpsLMJson(grabberPlayer);
            sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUps, getHostNameCreateList().addOneHostName(grabberPlayer));
        }
    }



    public String createGrabInfoResponseJson() {
        GrabInfoResponse grabInfoResponse;

        int[] coordinates;
        List<int[]> coordinatesList = new ArrayList<>();

        if(ammoPointToGrabList != null) {
            for (AmmoPoint ammoPoint : ammoPointToGrabList) {
                coordinates = ammoPoint.getCoordinates(getTurnManager().getGameTable().getMap());
                coordinatesList.add(coordinates);
            }
        }

        if(spawningPointToGrab != null) {
            coordinates = spawningPointToGrab.getCoordinates(getTurnManager().getGameTable().getMap());
            coordinatesList.add(coordinates);
        }

        grabInfoResponse = new GrabInfoResponse(grabberPlayer.getIdPlayer(), coordinatesList);
        return new Gson().toJson(grabInfoResponse);
    }
}
