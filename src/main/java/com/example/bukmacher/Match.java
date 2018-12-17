package com.example.bukmacher;


import org.springframework.core.annotation.Order;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gospodarze;
    private String goscie;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String outcome;
    private Double rate;
@OneToMany(mappedBy = "match")
private  List<Bet>bets;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGospodarze() {
        return gospodarze;
    }

    public void setGospodarze(String gospodarze) {
        this.gospodarze = gospodarze;
    }

    public String getGoscie() {
        return goscie;
    }

    public void setGoscie(String goscie) {
        this.goscie = goscie;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }

    @Override
    public String toString() {
        return gospodarze+"-"+goscie+" "+date;
    }
}
