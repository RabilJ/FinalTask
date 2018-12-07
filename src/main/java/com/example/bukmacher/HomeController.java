package com.example.bukmacher;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    private MatchRepository matchRepository;
private ScoreRepository scoreRepository;

    public HomeController(MatchRepository matchRepository, ScoreRepository scoreRepository) {
        this.matchRepository = matchRepository;
        this.scoreRepository = scoreRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false, defaultValue = "HOME") Action action) {
        List<Match> lista;
        switch (action) {
            case NEW_MATCH:
                Match match = new Match();
                model.addAttribute("newMatch", match);
                return "form";
            case ALL_MATCHES:
                lista = matchRepository.findAll();
                model.addAttribute("list", lista);
                return "list";
            case ADD_SCORE:
                break;
            case REMOVE_MATCH:
                break;
            case CHECK_SCORE:
                lista = matchRepository.findAll();
                for (Match match1 : lista) {
                    match1.setScore(scoreRepository.findById(1L).get());
                matchRepository.save(match1);
                }
                return "list";
            case TO_BE_DECIDED_MATCHES:
                break;
            case HOME:
                break;
        }
        return "home";
    }

    @PostMapping("/dodaj")
    String dodaj(Match match, @RequestParam(required = false, defaultValue = "HOME") Action action) {
        switch (action) {
            case ADD_MATCH:
                matchRepository.save(match);
                return "redirect:/";
        }
        return  "redirect:/";
    }
}