package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.endscreen.EndScreenModule;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.tooltip.TooltipModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee implements IPlayerManager {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;
    @Inject private EndScreenModule endScreenModule;
    @Inject private TooltipModule tooltipModule;
    @Inject private DebugModule debugModule;
    private Game game;
    private ViewController viewController;
    private double score = 1000;

    @Override
    public void init() {
        gameManager.setFrameDuration(100);
        gameManager.setMaxTurns(600);
        gameManager.setTurnMaxTime(50);

        String input = gameManager.getTestCaseInput().get(0);
        game = new Game(input, this);
        game.init();
        viewController = new ViewController(graphicEntityModule, game, tooltipModule, debugModule);
        graphicEntityModule.createSprite()
                .setImage(gameManager.getPlayer().getAvatarToken())
                .setX(0)
                .setY(1080)
                .setAnchorX(0)
                .setAnchorY(1)
                .setZIndex(0)
                .setAlpha(0.5)
                .setBaseHeight(100)
                .setBaseWidth(100)
        ;
        viewController.init();
    }

    @Override
    public void onEnd() {
        endScreenModule.setScores(new int[]{(int)score}, new String[]{String.valueOf(score)});
        endScreenModule.setTitleRankingsSprite("logo.png");
    }

    @Override
    public void gameTurn(int turn) {
        try{
            game.onRound();
            viewController.onRound();
        }
        catch (TimeoutException e){
            endGame(false, 1000);
            addGameSummary("Timeout");
            addTooltip("Timeout");
        }
        catch (Exception e){
            endGame(false, 1000);
            addGameSummary(e.getMessage());
            addTooltip("Provided invalid input. Check Summary for details.");
        }
    }

    @Override
    public String getOutputs() throws TimeoutException {
        return gameManager.getPlayer().getOutputs().get(0);
    }

    @Override
    public void sendData(String[] lines) {
        for(String line : lines){
            gameManager.getPlayer().sendInputLine(line);
        }
    }

    @Override
    public void execute() {
        gameManager.getPlayer().execute();
    }

    @Override
    public void endGame(boolean won, double score) {
        this.score = score;
        gameManager.putMetadata("time", String.valueOf(score));
        if(won) gameManager.winGame();
        else gameManager.loseGame();
    }

    @Override
    public void addTooltip(String message) {
        gameManager.addTooltip(gameManager.getPlayer(), message);
    }

    @Override
    public void addGameSummary(String message) {
        gameManager.addToGameSummary(message);
    }
}
