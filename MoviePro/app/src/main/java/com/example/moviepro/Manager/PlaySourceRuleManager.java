package com.example.moviepro.Manager;

import com.example.moviepro.bean.PlaySourceRule;

import java.util.ArrayList;

public class PlaySourceRuleManager {
    private ArrayList<PlaySourceRule> playSourceRules=new ArrayList<PlaySourceRule>();

    public PlaySourceRuleManager() {
        PlaySourceRule playSourceRule_zycj=new PlaySourceRule();
    }

    public ArrayList<PlaySourceRule> getPlaySourceRules() {
        return playSourceRules;
    }

    public void setPlaySourceRules(ArrayList<PlaySourceRule> playSourceRules) {
        this.playSourceRules = playSourceRules;
    }
}
