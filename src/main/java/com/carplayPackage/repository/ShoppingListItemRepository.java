package com.carplayPackage.repository;

import com.carplayPackage.model.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {
    
    List<ShoppingListItem> findByShoppingListIdOrderByCreatedAtAsc(Long shoppingListId);
    
    List<ShoppingListItem> findByShoppingListIdAndIsCompletedOrderByCreatedAtAsc(Long shoppingListId, Boolean isCompleted);
    
    @Query("SELECT sli FROM ShoppingListItem sli WHERE sli.shoppingList.id = :listId AND sli.isCompleted = false")
    List<ShoppingListItem> findIncompleteItemsByListId(@Param("listId") Long listId);
    
    Long countByShoppingListIdAndIsCompleted(Long shoppingListId, Boolean isCompleted);
}