package com.example.project.CoinFlipModel;

public class CoinFlipHistoryMember {

    private int childID;
    private boolean winLose;

    public CoinFlipHistoryMember(int childID, boolean winLose){
        this.childID = childID;
        this.winLose = winLose;
    }

    public int getChildID() {
        return childID;
    }

    public boolean getWinLose(){
        return winLose;
    }
}
