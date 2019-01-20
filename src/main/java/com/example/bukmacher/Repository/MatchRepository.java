package com.example.bukmacher.Repository;

import com.example.bukmacher.Model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {



    @Query(value = "select m from Match m where m.outcome is null and m.bets is empty ")
    List<Match> findIfOutcomeAndBetsIsNull();

    @Query(value = "select m from Match m where m.outcome is null")
    List<Match> findIfOutcomeIsNull();

@Query(value = "select m from Match m where m.id = :idGiven")
    List<Match>findyWithId(@Param ("idGiven")Long id);

    public void deleteById(Long id);
}
