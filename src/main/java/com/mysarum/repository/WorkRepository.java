package com.mysarum.repository;

import com.mysarum.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkRepository extends JpaRepository<Work, Integer> {
    public Work findByPlayerId(int playerId);
//    public Work findWorkingPlayerId(int id);

}

