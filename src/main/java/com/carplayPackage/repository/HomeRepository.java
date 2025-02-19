package com.carplayPackage.repository;

import com.carplayPackage.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<Home, Integer> {
    // ...existing code...
}
