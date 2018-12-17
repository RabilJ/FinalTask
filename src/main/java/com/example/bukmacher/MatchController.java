package com.example.bukmacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class MatchController {

    private MatchRepository matchRepository;
    private BetRepository betRepository;

    public MatchController(MatchRepository matchRepository, BetRepository betRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
    }


    @GetMapping("/match")
    public String matchEdit(Model model, @RequestParam(required = false, defaultValue = "HOME") Action action) {
        List<Match> lista;
        List<Match> lista2;
        List<String> scoreList = Arrays.asList("Wygrana gospodarzy", "Wygrana gości", "Remis");
        switch (action) {
            case NEW_MATCH:
                Match match = new Match();
                model.addAttribute("newMatch", match);
                return "form";
            case ALL_MATCHES:
                lista = matchRepository.findAll();
                Collections.sort(lista, new BetsComparator());
                model.addAttribute("listWO", lista);
                return "listWOScores";
            case CHECK_SCORE:
                lista = matchRepository.findAll();
                Collections.sort(lista, new BetsComparator());
                model.addAttribute("listW", lista);
                return "listWScores";
            case EDIT_MATCHES:
                lista2 = matchRepository.findIfOutcomeAndBetsIsNull();
                Collections.sort(lista2, new BetsComparator());
                model.addAttribute("scoreList", scoreList);
                model.addAttribute("listToEdit", lista2);
                return "matchEditForm";
        }
        return "home";
    }

    @PostMapping("/edit")
    String edit(Match match, @RequestParam(required = false, defaultValue = "HOME") Action action, @RequestParam(required = false, defaultValue = "1") Long matchID,
                @RequestParam(required = false, defaultValue = "Wygrana gospodarzy") String score) {
        switch (action) {
            case ADD_MATCH:
                if (match.getDate() != null && match.getGospodarze() != null && match.getGoscie() != null && match.getRate() != null) {
                    matchRepository.save(match);
                    System.out.println("Mecz został dodany do naszej bazy");
                } else {
                    System.out.println("Nie udało się dodać meczu do bazy");
                }
                return "redirect:/";
            case SAVE_SCORE:
                Optional<Match> matchOptional = matchRepository.findById(matchID);
                if (matchOptional.isPresent()) {
                    Match matchReal = matchOptional.get();
                    if (matchReal.getOutcome() == null) {
                        matchReal.setOutcome(score);
                        matchRepository.save(matchReal);
                        System.out.println("Uaktualniono wynik meczu");
                    } else {
                        System.out.println("Ten mecz już się rozstrzygnął");
                    }
                }
                return "redirect:/";
            case REMOVE_MATCH:
                Optional<Match> optMatch = matchRepository.findById(matchID);
                List<Bet> optBetList = betRepository.findByMatchId(matchID);
                if (optMatch.isPresent()) {
                    Match remMatch = optMatch.get();
                    if (remMatch.getOutcome() == null && optBetList.isEmpty()) {
                        matchRepository.deleteById(remMatch.getId());
                        System.out.println("Mecz został poprawnie usunięty");
                    } else {
                        System.out.println("Nie można usunąć z bazy meczu, który został rozstrzygnięty\nlub na który ktoś już postawił");
                    }
                }

        }
        return "redirect:/";
        }
}