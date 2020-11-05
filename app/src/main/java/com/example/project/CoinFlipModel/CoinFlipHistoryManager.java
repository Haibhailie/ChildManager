package com.example.project.CoinFlipModel;

import com.example.project.ChildModel.Child;

import java.util.ArrayList;
import java.util.List;

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

    public int getFlipChildID(int index){
        return flipList.get(index).getChildID();
    }

    public int getLength(){
        return flipList.size();
    }

    public void setFlipList(List<CoinFlipHistoryMember> flipList) {
        this.flipList = flipList;
    }


}
