package com.example.project.CoinFlipModel;

public class CoinFlipHistoryMember {

    private int childID;
    private int winLoseIcon;
    private int headsTailsIcon;

    public CoinFlipHistoryMember(int childID, int winLoseIcon, int headTailsIcon){
        this.childID = childID;
        this.winLoseIcon = winLoseIcon;
        this.headsTailsIcon = headTailsIcon;
    }

    public int getChildID() {
        return childID;
    }

    public int getWinLoseIcon(){
        return winLoseIcon;
    }

    public int getHeadsTailsIcon(){
        return headsTailsIcon;
    }
}
