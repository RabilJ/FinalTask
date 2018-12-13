package com.example.bukmacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
                                bet.setActualOutcome("Wygrana!!!! "+bet.getMoney()*3);
                                betRepository.save(bet);
                            } else {
                                bet.setActualOutcome("Przegrana..");
                                betRepository.save(bet);
                            }
                        }
                    }
                }
                model.addAttribute("betList",listaZakladow);
                return "betList";
            case HOME:
                break;
        }
        return "home";
    }

    @PostMapping("/edit")
    String edit(Match match, @RequestParam(required = false, defaultValue = "HOME") Action action, @RequestParam(required = false)Long matchID,
                 @RequestParam(required = false) String score, @RequestParam(required = false) Integer betMoney){

        switch (action) {
            case ADD_MATCH:
                matchRepository.save(match);
                System.out.println("Mecz został dodany do naszej bazy");
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
                bet.setMoney(betMoney);
                bet.setOutcome(score);
                if(matchRepository.findById(matchID).isPresent())
                    bet.setMatch(matchRepository.findById(matchID).get());
                betRepository.save(bet);
                return "redirect:/";

        }
        return "redirect:/";
    }
}