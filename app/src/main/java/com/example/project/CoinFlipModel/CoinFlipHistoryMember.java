package com.example.project.CoinFlipModel;


/**
 * CoinFlipMember:
 *  Holds the history of a single coinflip
 *
 *  childId: (int) Unique id of the child
 *  winLoseIcon: (int) Drawable Resource
 *  HeadsTailsIcon: (int) Drawable Resource
 */
public class CoinFlipHistoryMember {

    private int childId;
    private int winLoseIcon;
    private int headsTailsIcon;

    public CoinFlipHistoryMember(int childID, int winLoseIcon, int headTailsIcon){
        this.childId = childID;
        this.winLoseIcon = winLoseIcon;
        this.headsTailsIcon = headTailsIcon;
    }

    public int getChildId() {
        return childId;
    }

    public int getWinLoseIcon(){
        return winLoseIcon;
    }

    public int getHeadsTailsIcon(){
        return headsTailsIcon;
    }
}
