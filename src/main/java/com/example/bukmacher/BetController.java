package com.example.bukmacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

@Controller
public class BetController {
    private MatchRepository matchRepository;
    private BetRepository betRepository;

    public BetController(MatchRepository matchRepository, BetRepository betRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }

    @GetMapping("/bets")
    public String allBets(Model model) {

        List<Bet> listaZakladow = betRepository.findAll();
        Optional<Match> match1;
        Match match2;
        for (Bet bet : listaZakladow) {
            match1 = matchRepository.findById(bet.getMatch().getId());
            if (match1.isPresent()) {
                match2 = match1.get();
                if (match2.getOutcome() != null) {
                    if (bet.getOutcome().equals(match2.getOutcome())) {
                        bet.setActualOutcome("Wygrana!!! " + (bet.getMoney() * match2.getRate()) + "zł");
                        betRepository.save(bet);
                    } else if (!bet.getOutcome().equals(match2.getOutcome())) {
                        bet.setActualOutcome("Przegrana... ");
                        betRepository.save(bet);
                    }
                }
            }
        }
        listaZakladow = betRepository.orderByMatchId();
        model.addAttribute("betList", listaZakladow);
        return "betList";
    }

    @GetMapping("/betCreate")
    public String bets(Model model) {
        List<String> scoreList = Arrays.asList("Wygrana gospodarzy", "Wygrana gości", "Remis");
        List<Match> listaWOutcome = matchRepository.findIfOutcomeIsNull();
        Method.compareALl(listaWOutcome);
        model.addAttribute("scoreList", scoreList);
        model.addAttribute("listToBet", listaWOutcome);
        return "betForm";
    }


    @PostMapping("/betSave")
    String edit(@RequestParam(required = false, defaultValue = "1") Long matchID,
                @RequestParam(required = false, defaultValue = "Wygrana gospodarzy") String score, @RequestParam(required = false, defaultValue = "100") Integer betMoney) {
                Bet bet = new Bet();
                if (betMoney != null && score != null) {
                    bet.setMoney(betMoney);
                    bet.setOutcome(score);
                    if (matchRepository.findById(matchID).isPresent())
                        bet.setMatch(matchRepository.findById(matchID).get());
                    betRepository.save(bet);
                    System.out.println("Zakład w bazie");
                } else {
                    System.out.println("Nie udało się zapisać zakładu");
                }
        return "redirect:/";
    }
}
