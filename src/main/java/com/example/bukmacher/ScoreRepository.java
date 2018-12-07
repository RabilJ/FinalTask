package com.example.bukmacher;


import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
