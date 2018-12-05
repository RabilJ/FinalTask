package com.example.bukmacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {

}
