package com.example.bukmacher;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer money;
    private String outcome;
    private String actualOutcome;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Match match;

    public String getActualOutcome() {
        return actualOutcome;
    }

    public void setActualOutcome(String actualOutcome) {
        this.actualOutcome = actualOutcome;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bet bet = (Bet) o;
        return Objects.equals(id, bet.id) &&
                Objects.equals(money, bet.money) &&
                Objects.equals(outcome, bet.outcome) &&
                Objects.equals(actualOutcome, bet.actualOutcome) &&
                Objects.equals(match, bet.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, money, outcome, actualOutcome, match);
    }
}
