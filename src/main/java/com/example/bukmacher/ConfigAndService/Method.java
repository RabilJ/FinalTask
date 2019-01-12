package com.example.bukmacher.ConfigAndService;


import com.example.bukmacher.Model.Bet;
import com.example.bukmacher.Model.Match;

import java.util.List;

public class Method {
    public static List<Match> compareALl(List<Match> matches) {
        matches.sort(new BetsComparator());
        return matches;
    }

    public static void updateList(Bet bet, Match match2) {
        if (match2.getOutcome() != null)
            if (match2.getOutcome().equals(bet.getOutcome())) {
                bet.setActualOutcome("Wygrana!!! " + (bet.getMoney() * match2.getRate()) + "zł");
            } else if(!match2.getOutcome().equals(bet.getOutcome())){
                bet.setActualOutcome("Przegrana... ");
            }
        }

    public static void updateScore(Match match, String score) {
        match.setOutcome(score);
        System.out.println("Uaktualniono wynik meczu");
    }
}