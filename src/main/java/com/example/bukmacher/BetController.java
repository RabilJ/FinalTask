package com.example.bukmacher;

import org.springframework.stereotype.Controller;

@Controller
public class BetController {
    private MatchRepository matchRepository;
    private BetRepository betRepository;

    public BetController(MatchRepository matchRepository, BetRepository betRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }

}
