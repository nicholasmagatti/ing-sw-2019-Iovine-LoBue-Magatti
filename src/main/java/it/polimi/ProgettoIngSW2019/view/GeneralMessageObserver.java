package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Part of the view that manages the messages for visualization received from the server.
 * @author Nicholas Magatti
 */
public class GeneralMessageObserver implements Observer<Event> {

    /**
     * Print the error received from the controller and restart the state in which it was
     * @param message - error message to print to the user
     */
    private void printErrorAndRestart(String message){
        System.out.println(message);
        //restart the current state from the beginning
        StateManager.restartCurrentState();
    }

    @Override
    public void update(Event event){

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        switch (command){
            case ERROR:
                //this is not really a json message: this is already the string to print
                printErrorAndRestart(jsonMessage);
                break;

            case INPUT_TIME_EXPIRED:
                ToolsView.getInputScanner().close();
                System.out.println("Time expired! You have been suspended.");
                System.out.println("But you can reconnect to the ongoing game when you want.");
                break;

            case USER_HAS_DISCONNECTED:
                String username = new Gson().fromJson(jsonMessage, MessageConnection.class).getUsername();
                if(!username.equals(InfoOnView.getMyNickname())){
                    System.out.println(username + " disconnected from the game.");
                    //set that player as disconnected in the light model (like before but with active = false)
                    for(int i=0; i < InfoOnView.getPlayers().length; i++){
                        PlayerDataLM p = InfoOnView.getPlayers()[i];
                        if(p.getNickname().equals(username)){
                         InfoOnView.getPlayers()[i] = new PlayerDataLM(p.getIdPlayer(), p.getNickname(), p.getUnloadedWeapons(),
                                    p.getnRedAmmo(), p.getnBlueAmmo(), p.getnYellowAmmo(), p.getnSkulls(),
                                    false, p.getDown(), p.getDamageLine(), p.getMarkLine(),
                                    p.getnMyLoadedWeapons(), p.getnPowerUps());
                        }
                    }
                }

                break;

            case SCORE_DEAD_PLAYERS:
                Type listType = new TypeToken<ArrayList<MessageScorePlayer>>(){}.getType();
                List<MessageScorePlayer> listInfoScores = new Gson().fromJson(jsonMessage, listType);
                //for each dead player
                for(MessageScorePlayer infoScore : listInfoScores){
                    printInfoScoringDmgLine(infoScore);
                }
                break;
            case MSG_DRAW_MY_POWERUP:
                MessageDrawMyPowerUp messageDrawMyPowerUp = new Gson().fromJson(jsonMessage, MessageDrawMyPowerUp.class);
                System.out.print("You drew ");
                for(int i=0; i < messageDrawMyPowerUp.getNamePowerUps().size(); i++){
                    System.out.print(messageDrawMyPowerUp.getNamePowerUps().get(i));
                    if(i < messageDrawMyPowerUp.getNamePowerUps().size() - 2){ //not last one or second last
                        System.out.print(", ");
                    }
                    if(i == messageDrawMyPowerUp.getNamePowerUps().size() - 2){ //second last
                        System.out.print(" and ");
                    }
                }
                System.out.print("\n");
                break;
            case MSG_ENEMY_DRAW_POWERUP:
                MessageEnemyDrawPowerUp messageDraw = new Gson().fromJson(jsonMessage, MessageEnemyDrawPowerUp.class);
                final String CARD_OR_CARDS;
                if(messageDraw.getnCards() > 1){
                    CARD_OR_CARDS = "cards";
                }
                else{
                    CARD_OR_CARDS = "card";
                }
                System.out.println(messageDraw.getNamePlayer() + "drew " + messageDraw.getnCards() + " " + CARD_OR_CARDS + ".");
                break;
            case MSG_POWERUP_DISCARDED_TO_SPAWN:
                MessagePowerUpsDiscarded pwUpsDiscardedSpawn = new Gson().fromJson(jsonMessage, MessagePowerUpsDiscarded.class);

                PowerUpLM pwDiscardedSpawn =  pwUpsDiscardedSpawn.getPowerUpsToDiscard().get(0);
                printYouOrNameOther(pwUpsDiscardedSpawn);
                System.out.println(" discarded " +  pwDiscardedSpawn.getName() + " to spawn on the " +
                        ToolsView.ammoTypeToString(pwDiscardedSpawn.getGainAmmoColor()) + " spawn point.");
                break;

            case MSG_POWERUPS_DISCARDED_AS_AMMO:
                MessagePowerUpsDiscarded pwUpsDiscardedPayment = new Gson().fromJson(jsonMessage, MessagePowerUpsDiscarded.class);
                printYouOrNameOther(pwUpsDiscardedPayment);
                System.out.print(" discarded ");
                ToolsView.printListOfPowerups(pwUpsDiscardedPayment.getPowerUpsToDiscard());
                System.out.println(" (to use it/them as ammo to spend).");
                break;

            case MSG_DOUBLEKILL:
                Message doubleKillInfo = new Gson().fromJson(jsonMessage, Message.class);
                printYouOrNameOther(doubleKillInfo);
                System.out.println(" got 1 extra point for Double Kill!");
                break;
            case MSG_NEW_TURN:
                Message infoNewTurn = new Gson().fromJson(jsonMessage, Message.class);
                if(isYou(infoNewTurn.getIdPlayer())){
                    System.out.println("It's your turn!");
                }
                else{
                   System.out.println("Turn of " + infoNewTurn.getNamePlayer() + ".");
                }
                break;
            case MSG_ALL_RELOAD_WEAPON:
                MessageWeaponPay reloadInfo = new Gson().fromJson(jsonMessage, MessageWeaponPay.class);
                printYouOrNameOther(reloadInfo);
                System.out.println(" reloaded " + reloadInfo.getNameWeapon());
                break;
            case MSG_WEAPON_BUY:
                MessageWeaponPay buyInfo = new Gson().fromJson(jsonMessage, MessageWeaponPay.class);
                printYouOrNameOther(buyInfo);
                System.out.println(" bought " + buyInfo.getNameWeapon() + ".");
                break;
            case MSG_WEAPON_SWAP:
                MessagePlayerSwapWeapons swapInfo = new Gson().fromJson(jsonMessage, MessagePlayerSwapWeapons.class);
                printYouOrNameOther(swapInfo);
                System.out.println(" left " + swapInfo.getNameWeaponDiscarded() + "on the spawn point to get " +
                        swapInfo.getNameWeapon());
                break;
            case MSG_USE_POWERUP:
                MessagePowerUpsDiscarded pwDiscardedToUseEffect = new Gson().fromJson(jsonMessage, MessagePowerUpsDiscarded.class);
                //the powerups are a list but we know they are only one in this specific context
                PowerUpLM pwUseEffect = pwDiscardedToUseEffect.getPowerUpsToDiscard().get(0);
                printYouOrNameOther(pwDiscardedToUseEffect);
                if(pwUseEffect.getName().equals(GeneralInfo.TELEPORTER) || pwUseEffect.getName().equals(GeneralInfo.NEWTON)) {
                    System.out.print(" used " + pwUseEffect.getName() + ".");
                }
                //if tagback grenade or targeting scope, there is already another message
                break;
            case MSG_TAGBACK_GRENADE:
                MessageTagBackGrenade messageTagBackGrenade = new Gson().fromJson(jsonMessage, MessageTagBackGrenade.class);
                printYouOrNameOther(messageTagBackGrenade);
                printUsedCardAgainst(GeneralInfo.TAGBACK_GRENADE);
                printYouOrOtherName(messageTagBackGrenade.getNameTarget(), false);
                System.out.println(".");
                break;
            case MSG_TARGETING_SCOPE:
                MessageTargetingScope messageTargetingScope = new Gson().fromJson(jsonMessage, MessageTargetingScope.class);
                printYouOrNameOther(messageTargetingScope);
                printUsedCardAgainst(GeneralInfo.TARGETING_SCOPE);
                printYouOrOtherName(messageTargetingScope.getNameTarget(), false);
                System.out.println(".");
                break;
            case MSG_CONCLUSION:
                System.out.println("The game got to its end!");
                System.out.println("Let's also score all the players with damage who haven't "+
                        "died and the killshot track!");
                break;
            case MSG_SCORE_ALIVE_PLAYER:
                System.out.println("Scores from the damage lines of the player with damage who are not dead: ");
                MessageScorePlayer messageScorePlayer = new Gson().fromJson(jsonMessage, MessageScorePlayer.class);
                printInfoScoringDmgLine(messageScorePlayer);
                break;
            case MSG_SCORE_KILLSHOT_TRACK:
                System.out.println("Now the scores deriving from the killshot track:");
                Type listJsonKT = new TypeToken<ArrayList<ScorePlayerWhoHit>>(){}.getType();
                List<ScorePlayerWhoHit> infoScores = new Gson().fromJson(jsonMessage, listJsonKT);
                printNamesWithScoresMade(infoScores);
                break;
            case MSG_FINAL_RESULTS:
                System.out.println("TOTAL POINTS: ");
                TotalPointsAndWinner totalPointsAndWinner = new Gson().fromJson(jsonMessage, TotalPointsAndWinner.class);
                for(int id=0; id < totalPointsAndWinner.getTotalScores().length; id++){
                    printYouOrOtherName(id);
                    System.out.print(" : " + totalPointsAndWinner.getTotalScores()[id]);
                }
                List<String> winners = totalPointsAndWinner.getWinners();
                System.out.print("\n");
                System.out.println("And the winner is...");
                if(winners.isEmpty()){
                    throw new IllegalArgumentException("The list of the winners cannot be empty.");
                }
                if(winners.size() == 1){
                    System.out.println(winners.get(0) + "!");
                }
                else{// winners > 1
                    System.out.print("Tie between: ");
                    for(int i=0; i < winners.size(); i++){
                        System.out.print(winners.get(i));
                        if(i < winners.size() - 1){//if not or last element
                            if(i  < winners.size() - 2){
                                System.out.print(", ");
                            }
                            else{
                                System.out.print(" and ");
                            }
                        }
                    }
                }
                break;
        }
    }

    private void printUsedCardAgainst(String cardName){
        System.out.print(" used " + cardName + " against ");
    }

    /**
     * Print "You" or the name of the player if it is not you, without going to the next line.
     * @param message
     */
    private void printYouOrNameOther(Message message){
        printYouOrOtherName(message.getNamePlayer(), true);
    }

    /**
     * Print "You" or the name of the player if it is not you, without going to the next line.
     * @param idPlayer
     */
    private void printYouOrOtherName(int idPlayer){
        if(isYou(idPlayer)){
            System.out.print("You");
        }
        else{
            for(PlayerDataLM p : InfoOnView.getPlayers()){
                if(p.getIdPlayer() == idPlayer){
                    System.out.print(p.getNickname());
                }
            }
        }
    }

    /**
     * Print "You" or "you"(respectively for beginning of the sentence or not) or
     * the name of the player if it is not you, without going to the next line.
     * @param namePlayer
     * @param beginningSentence
     */
    private void printYouOrOtherName(String namePlayer, boolean beginningSentence){
        if(isYou(namePlayer)){
            if(beginningSentence) {
                System.out.print("You");
            }
            else{
                System.out.print("you");
            }
        }
        else{
            System.out.print(namePlayer);
        }
    }

    /**
     * Return true if the name identifies your character, false otherwise.
     * @param name
     * @return true if the name identifies your character, false otherwise.
     */
    private boolean isYou(String name){
        return name.equals(InfoOnView.getMyNickname());

    }

    /**
     * Return true if the id identifies your character, false otherwise.
     * @param id
     * @return true if the id identifies your character, false otherwise.
     */
    private boolean isYou(int id){
        return (id == InfoOnView.getMyId());
    }

    /**
     * Print info score relative to scoring the damage line of a player.
     * @param infoScore
     */
    private void printInfoScoringDmgLine(MessageScorePlayer infoScore){
        if(isYou(infoScore.getDeadNamePlayer())){
            System.out.println("Scoring your damage line: ");
        }
        else {
            System.out.println("Scoring " + infoScore.getDeadNamePlayer() + "'s damage line: ");
        }
        printNamesWithScoresMade(infoScore.getPlayersWhoHit());

        if(isYou(infoScore.getFirstBloodNamePlayer())){
            System.out.print("You");
        }
        else{
            System.out.print(infoScore.getDeadNamePlayer());
        }
        System.out.println(" got 1 extra point for First Blood.\n");
    }

    /**
     * Print names of the player with the respective scores made
     * @param infoScores
     */
    private void printNamesWithScoresMade(List<ScorePlayerWhoHit> infoScores){
        for(ScorePlayerWhoHit elem : infoScores) {
            if (isYou(elem.getNamePlayerWhoHit())) {
                System.out.print("You");
            } else {
                System.out.print(elem.getNamePlayerWhoHit());
            }
            System.out.println("got " + elem.getnPoints() + " points");
        }
    }
}
