package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

public interface IPlayerManager {
    String getOutputs() throws AbstractPlayer.TimeoutException;
    void sendData(String[] lines);
    void execute();
    void endGame(boolean won, double score);
    void addTooltip(String message);
    void addGameSummary(String message);
}
