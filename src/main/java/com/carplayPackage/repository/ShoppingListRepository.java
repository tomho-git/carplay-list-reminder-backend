package com.carplayPackage.repository;

import com.carplayPackage.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {
    
    List<ShoppingList> findByIsCompletedOrderByCreatedAtDesc(Boolean isCompleted);
    
    List<ShoppingList> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT sl FROM ShoppingList sl WHERE sl.isCompleted = false")
    List<ShoppingList> findIncompleteLists();
}