package com.mysarum.service;

import com.mysarum.model.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TradeService {

    public Trade findDealById(int id);

    public List<Trade> findAll();

    public Trade saveDeal(Trade trade);

    public void deleteDeal(int id);

    public Page<Trade> getPaginatedItems(Pageable pageable);


}
