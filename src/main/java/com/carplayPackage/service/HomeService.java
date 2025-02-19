package com.carplayPackage.service;

import com.carplayPackage.model.Home;
import com.carplayPackage.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private HomeRepository HomeRepository;

    public Optional<Home> getHomeById(Integer id) {
        return HomeRepository.findById(id);
    }
}
