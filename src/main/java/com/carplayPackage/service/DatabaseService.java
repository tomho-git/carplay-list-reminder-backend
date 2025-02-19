package com.carplayPackage.service;

import com.carplayPackage.repository.DatabaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    public String getCurrentTime() {
        return databaseRepository.getCurrentTime();
    }
}
