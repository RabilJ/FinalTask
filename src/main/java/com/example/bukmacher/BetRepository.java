package com.example.bukmacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet,Long> {

    @Query(value="SELECT * FROM BET ORDER BY MATCH_ID DESC",nativeQuery=true)
    List<Bet> orderByNumberOfBets();
}
