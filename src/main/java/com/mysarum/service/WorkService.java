package com.mysarum.service;

import com.mysarum.model.Work;


public interface WorkService {

//    public Work findWorkingPlayerId(int id);
    public Work findByPlayerId(int playerId);
    public Work saveWork(Work work);
    public void deleteWork(int id);


}

