package com.example.project.coinflipmodel;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * CoinFlipMember:
 * Holds the history of a single coinflip
 * <p>
 * childId: (int) Unique id of the child
 * winLoseIcon: (int) Drawable Resource
 * HeadsTailsIcon: (int) Drawable Resource
 */
public class CoinFlipHistoryMember {
    private int childId;
    private int winLoseIcon;
    private int headsTailsIcon;
    private String dateTimeFlip;

    public CoinFlipHistoryMember(int childID, int winLoseIcon, int headTailsIcon) {
        this.childId = childID;
        this.winLoseIcon = winLoseIcon;
        this.headsTailsIcon = headTailsIcon;
        setDateTime();
    }

    private void setDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a, yyyy");
        dateTimeFlip = dateFormat.format(currentDate);
    }

    public int getChildId() {
        return childId;
    }

    public int getWinLoseIcon() {
        return winLoseIcon;
    }

    public int getHeadsTailsIcon() {
        return headsTailsIcon;
    }

    public String getDateTimeFlip() {
        return dateTimeFlip;
    }
}
