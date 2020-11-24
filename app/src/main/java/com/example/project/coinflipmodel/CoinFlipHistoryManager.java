package com.example.project.coinflipmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * CoinFlipManager: (Singleton)
 *  Manages CoinFlipMemebers in a List
 *  Contains the history of a coinflips
 *
 *  getFlipWinLoseIcon(index) Returns an int to R.drawable.
 *  getFlipHeadsTailsIcon(index) Returns an int to R.drawable.
 *  getFlipChildId(index) Returns Id of a child
 *  getFlipList() Returns the CoinFlipHistoryMember list
 *
 *  setFlipList(List<CoinFlipHistoryMember>) Sets the CoinFlipHistory list
 *
 *  add(CoinFlipHistoryMember) adds an item to the beginning of the list
 */
public class CoinFlipHistoryManager {
    private static CoinFlipHistoryManager instance;
    private List<CoinFlipHistoryMember> flipList = new ArrayList<>();
    private CoinFlipHistoryManager() {}

    public static CoinFlipHistoryManager getInstance() {
        if (instance == null) {
            instance = new CoinFlipHistoryManager();
        }
        return instance;
    }

    public List<CoinFlipHistoryMember> getFlipList() {
        return flipList;
    }

    public void add(CoinFlipHistoryMember newFlip){
        flipList.add(0, newFlip);
    }

    public int getFlipWinLoseIcon(int index){
        return flipList.get(index).getWinLoseIcon();
    }

    public int getFlipHeadsTailsIcon(int index){
        return flipList.get(index).getHeadsTailsIcon();
    }

    public int getFlipChildId(int index){
        return flipList.get(index).getChildId();
    }

    public int getLength(){
        return flipList.size();
    }

    public String getDateTime(int index){
        return flipList.get(index).getDateTimeFlip();
    }

    public void setFlipList(List<CoinFlipHistoryMember> flipList) {
        this.flipList = flipList;
    }
}
