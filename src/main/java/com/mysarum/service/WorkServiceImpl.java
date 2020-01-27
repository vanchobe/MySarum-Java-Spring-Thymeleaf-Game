package com.mysarum.service;

import com.mysarum.model.Work;
import com.mysarum.repository.WorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WorkServiceImpl implements WorkService {

    private WorkRepository workRepository;

    @Autowired
    public WorkServiceImpl(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    @Override
    public Work findByPlayerId(int playerId) {
        return workRepository.findByPlayerId(playerId);
    }

//    @Override
//    public Work findWorkingPlayerId(int id) {
//        return workRepository.findWorkingPlayerId(id);
//    }

    @Override
    public Work saveWork(Work work) {
        return workRepository.save(work);
    }

    @Override
    public void deleteWork(int id) {
        workRepository.delete(findByPlayerId(id));
    }


}

