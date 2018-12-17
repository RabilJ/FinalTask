package com.example.bukmacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {



    @Query(value = "select m from Match m where m.outcome is null and m.bets is empty ")
    List<Match> findIfOutcomeAndBetsIsNull();

    @Query(value = "select m from Match m where m.outcome is null")
    List<Match> findIfOutcomeIsNull();


}
