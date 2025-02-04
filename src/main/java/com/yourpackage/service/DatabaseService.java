package com.yourpackage.service;

import com.yourpackage.repository.DatabaseRepository;
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
