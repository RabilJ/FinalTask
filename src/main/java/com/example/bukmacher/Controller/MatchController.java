package com.example.bukmacher.Controller;

import com.example.bukmacher.Repository.MatchRepository;
import com.example.bukmacher.ConfigAndService.Method;
import com.example.bukmacher.Model.Match;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MatchController {

    private MatchRepository matchRepository;


    public MatchController(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }




    @GetMapping("/matchEdit")
    public String edit(Model model) {
        List<String> scoreList = Arrays.asList("Wygrana gospodarzy", "Wygrana gości", "Remis");
        List<Match> lista1 = matchRepository.findIfOutcomeIsNull();
        List<Match> lista2 = matchRepository.findIfOutcomeAndBetsIsNull();
        model.addAttribute("scoreList", scoreList);
        model.addAttribute("listToRemove", lista2);
        model.addAttribute("listToUpdate", lista1);
        return "matchEditForm";
    }

    @GetMapping("/checkScores")
    public String scores(Model model) {
        List<Match> lista3 = matchRepository.findAll();
        model.addAttribute("listW", Method.compareALl(lista3));
        return "listWScores";
    }

    @GetMapping("/allMatches")
    public String all(Model model) {
        List<Match> lista3 = matchRepository.findAll();
        model.addAttribute("listWO", Method.compareALl(lista3));
        return "listWOScores";
    }

    @GetMapping("/newMatch")
    public String matchCreate(Model model) {
        Match match = new Match();
        model.addAttribute("newMatch", match);
        return "form";
    }

    @PostMapping("/removeMatch")
    String remove(@RequestParam(required = false, defaultValue = "1") Long matchID) {

        Optional<Match> optMatch = matchRepository.findById(matchID);
        if (optMatch.isPresent()) {
            Match remMatch = optMatch.get();
                matchRepository.deleteById(remMatch.getId());
                System.out.println("Mecz został poprawnie usunięty");
        }
        return "redirect:/";
    }

    @PostMapping("/updateScore")
    String update(@RequestParam(required = false, defaultValue = "1") Long matchID,
                  @RequestParam(required = false) String score) {
        Optional<Match> matchOptional = matchRepository.findById(matchID);
        if (matchOptional.isPresent()) {
            Match matchReal = matchOptional.get();
            Method.updateScore(matchReal,score);
            matchRepository.save(matchReal);
        }
        return "redirect:/";
    }

    @PostMapping("/saveMatch")
    String save(Match match) {
        matchRepository.save(match);
        System.out.println("Mecz został dodany do naszej bazy");
        return "redirect:/";

    }
}