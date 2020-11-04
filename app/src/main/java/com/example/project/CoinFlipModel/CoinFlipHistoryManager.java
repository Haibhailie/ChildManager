package com.example.project.CoinFlipModel;

import java.util.ArrayList;
import java.util.List;

public class CoinFlipHistoryManager {

    private static CoinFlipHistoryManager instance;

    private List<CoinFlipHistoryMember> FlipList = new ArrayList<>();
    private CoinFlipHistoryManager() {}

    public static CoinFlipHistoryManager getInstance() {
        if (instance == null) {
            instance = new CoinFlipHistoryManager();
        }
        return instance;
    }

    public List<CoinFlipHistoryMember> getFlipList() {
        return FlipList;
    }


}
