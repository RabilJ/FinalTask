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

    public HomeController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false, defaultValue = "HOME") Action action) {
        List<Match> lista;
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
                Match matchToBet = new Match();
                List<Match> listaWOutcome = matchRepository.findIfOutcomeIsNotNull();
                model.addAttribute("scoreList", scoreList);
                model.addAttribute("listToBet", listaWOutcome);
                model.addAttribute("matchToBet", matchToBet);
                return "betForm";
            case HOME:
                break;
        }
        return "home";
    }

    @PostMapping("/edit")
    String dodaj(Model model, Match match, @RequestParam(required = false, defaultValue = "HOME") Action action, HttpServletResponse response, Integer thisbet) throws IOException {

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
            case CHECK_BET:
                Optional<Match> matchForBet = matchRepository.findById(match.getId());
                if (matchForBet.isPresent()) {
                    Match matchCheck = matchForBet.get();
                    if (matchCheck.equals(matchRepository.findByIdAndOutcome(match.getId(), match.getOutcome()))) {
                        return "redirect:/successBet";
                    } else {
                        return "redirect:/failedBet";
                    }

                }
        }
        return "redirect:/";
    }
}