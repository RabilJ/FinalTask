package com.example.bukmacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class HomeController {
    private MatchRepository matchRepository;
    private BetRepository betRepository;

    public HomeController(MatchRepository matchRepository, BetRepository betRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false, defaultValue = "HOME") Action action) {
        List<Match> lista;
        List<Bet> listaZakladow;
        List<String> scoreList = Arrays.asList("Wygrana gospodarzy", "Wygrana gości", "Remis");
        switch (action) {
            case NEW_MATCH:
                Match match = new Match();
                model.addAttribute("newMatch", match);
                return "form";
            case ALL_MATCHES:
                lista = matchRepository.findAll();
                model.addAttribute("listWO", lista);
                return "listWOScores";
            case CHECK_SCORE:
                lista = matchRepository.findAll();
                model.addAttribute("listW", lista);
                return "listWScores";
            case EDIT_MATCHES:
                Match matchToEdit = new Match();
                model.addAttribute("matchToEdit", matchToEdit);
                lista = matchRepository.findAll();
                model.addAttribute("scoreList", scoreList);
                model.addAttribute("listToEdit", lista);
                return "matchEditForm";
            case PLACE_BET:
                List<Match> listaWOutcome = matchRepository.findIfOutcomeIsNull();
                model.addAttribute("scoreList", scoreList);
                model.addAttribute("listToBet", listaWOutcome);
                return "betForm";

            case ALL_BETS:
                listaZakladow = betRepository.findAll();
                lista = matchRepository.findAll();
                for (Bet bet : listaZakladow) {
                    for (Match match1 : lista) {
                        if (match1.getOutcome() != null) {
                            if (bet.getOutcome().equals(match1.getOutcome())) {
                                bet.setActualOutcome("Wygrana!!!! " + bet.getMoney() * 3 + "zł");
                                betRepository.save(bet);
                            } else {
                                bet.setActualOutcome("Przegrana..");
                                betRepository.save(bet);
                            }
                        }
                    }
                }
                 betRepository.orderByNumberOfBets();
                listaZakladow = betRepository.orderByNumberOfBets();
                model.addAttribute("betList", listaZakladow);
                return "betList";
            case HOME:
                break;
        }
        return "home";
    }

    @PostMapping("/edit")
    String edit(Match match, @RequestParam(required = false, defaultValue = "HOME") Action action, @RequestParam(required = false, defaultValue = "1") Long matchID,
                @RequestParam(required = false, defaultValue = "Wygrana gospodarzy") String score, @RequestParam(required = false, defaultValue = "100") Integer betMoney) {

        switch (action) {
            case ADD_MATCH:
                if (match.getDate() != null && match.getGospodarze() != null && match.getGoscie() != null) {
                    matchRepository.save(match);
                    System.out.println("Mecz został dodany do naszej bazy");
                } else {
                    System.out.println("Nie udało się dodać meczu do bazy");
                }
                return "redirect:/";
            case SAVE_SCORE:
                Optional<Match> matchOptional = matchRepository.findById(match.getId());
                if (matchOptional.isPresent()) {
                    Match matchReal = matchOptional.get();
                    matchReal.setOutcome(match.getOutcome());
                    matchRepository.save(matchReal);
                    System.out.println("Uaktualniono wynik meczu");
                    return "redirect:/";
                }
                return "redirect:/";
            case REMOVE_MATCH:
                Long id = match.getId();
                if (matchRepository.findById(id).isPresent()) {
                    matchRepository.deleteById(id);
                    System.out.println("Mecz został poprawnie usunięty");
                }
                return "redirect:/";
            case SAVE_BET:
                Bet bet = new Bet();
                if (betMoney!= null &&score!= null) {
                    bet.setMoney(betMoney);
                    bet.setOutcome(score);
                    if (matchRepository.findById(matchID).isPresent())
                        bet.setMatch(matchRepository.findById(matchID).get());
                    betRepository.save(bet);
                    System.out.println("Zakład w bazie");
                }else{
                    System.out.println("Nie udało się zapisać zakładu");
                }
                    return "redirect:/";

        }
        return "redirect:/";
    }
}