package com.example.bukmacher.Controller;

import com.example.bukmacher.Model.Match;
import com.example.bukmacher.Repository.BetRepository;
import com.example.bukmacher.Repository.MatchRepository;
import com.example.bukmacher.Repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RestMatchController {
    private MatchRepository matchRepository;
    private BetRepository betRepository;
    private UserRepository userRepository;

    public RestMatchController(MatchRepository matchRepository, BetRepository betRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.betRepository = betRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/match/{id}")
    public List<Match> getMatch(@PathVariable Long id) {
        List<Match> matches = matchRepository.findyWithId(id);
        for (Match match : matches) {
            match.setBets(null);
        }
        return matches;
    }

    @PostMapping("/api/match")
    public Match putMatch(@RequestBody Match match) {
        matchRepository.save(match);
        return match;
    }

    @DeleteMapping("/api/match/{id}")
    public void update(@PathVariable Long id) {
        matchRepository.deleteById(id);
    }

    @PutMapping("/api/match/{id}")
    public void update(@PathVariable Long id, @RequestBody Match match) {
        Optional<Match> match1 = matchRepository.findById(id);
        if (match1.isPresent()) {
            Match matchToChange = match1.get();
            matchToChange.setGospodarze(match.getGospodarze());
            matchToChange.setGoscie(match.getGoscie());
            matchToChange.setRate(match.getRate());
            matchToChange.setDate(match.getDate());
            matchToChange.setBets(match.getBets());
            matchToChange.setOutcome(match.getOutcome());
            matchRepository.save(matchToChange);
        }
    }
}