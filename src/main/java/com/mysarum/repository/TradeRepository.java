package com.mysarum.repository;

import com.mysarum.model.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    public Trade findById(int id);
    Page<Trade> findAll(Pageable pageable);
}
