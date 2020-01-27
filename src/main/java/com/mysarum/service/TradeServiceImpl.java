package com.mysarum.service;

import com.mysarum.model.Trade;
import com.mysarum.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {
    private TradeRepository tradeRepository;



    @Autowired
    public TradeServiceImpl(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Trade findDealById(int id) {
        return tradeRepository.findById(id);
    }

    @Override
    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    @Override
    public Trade saveDeal(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public void deleteDeal(int id) {
        tradeRepository.delete(findDealById(id));
    }

    public Page<Trade> getPaginatedItems(Pageable pageable) {
        return tradeRepository.findAll(pageable);
    }


}
