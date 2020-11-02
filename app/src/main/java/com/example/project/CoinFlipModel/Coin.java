package com.example.project.CoinFlipModel;

/**
 * Coin:
 *  Coin() Returns Coin Objects with headRate = 0.5.
 *  Coin(double) Returns Coin Object with headRate = newRate.
 *
 *  headRate = chance of heads, 0.5 = 50%.
 *  flipCoin() returns 1 if heads, 0 if tails.
 *  setHeadRate(double) sets a new headRate.
 */
public class Coin {

    private double headRate;

    public Coin(){
        headRate = 0.5;
    }

    public Coin(double newRate){
        headRate = newRate;
    }

    public int flipCoin(){
        double randomNumber = Math.random();
        boolean result = randomNumber >= headRate;

        return result ? 1 : 0;
    }

    public void setHeadRate(double newRate){
        headRate = newRate;
    }
}
