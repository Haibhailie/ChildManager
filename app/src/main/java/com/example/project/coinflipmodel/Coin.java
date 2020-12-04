package com.example.project.coinflipmodel;

import java.util.Random;

/**
 * Coin:
 * Coin() Returns Coin Objects with headRate = 0.5.
 * Coin(double) Returns Coin Object with headRate = newRate.
 * <p>
 * headRate = chance of heads, 0.5 = 50%.
 * flipCoin() returns true if heads, false if tails.
 * setHeadRate(double) sets a new headRate.
 */
public class Coin {

    private double headRate;
    private Random randomGenerator = new Random(System.currentTimeMillis());

    public Coin() {
        headRate = 0.5;
    }

    public Coin(double newRate) {
        headRate = newRate;
    }

    public boolean flipCoin() {
        double randomNumber = randomGenerator.nextDouble();
        boolean result = randomNumber >= headRate;
        return result;
    }

    public void setHeadRate(double newRate) {
        headRate = newRate;
    }
}
