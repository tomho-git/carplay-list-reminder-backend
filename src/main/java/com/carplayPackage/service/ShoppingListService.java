package com.carplayPackage.service;

import com.carplayPackage.model.ShoppingList;
import com.carplayPackage.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListService {
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    
    public List<ShoppingList> getAllShoppingLists() {
        return shoppingListRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Optional<ShoppingList> getShoppingListById(Long id) {
        return shoppingListRepository.findById(id);
    }
    
    public List<ShoppingList> getIncompleteShoppingLists() {
        return shoppingListRepository.findIncompleteLists();
    }
    
    public ShoppingList createShoppingList(ShoppingList shoppingList) {
        return shoppingListRepository.save(shoppingList);
    }
    
    public ShoppingList createShoppingList(String name) {
        ShoppingList shoppingList = new ShoppingList(name);
        return shoppingListRepository.save(shoppingList);
    }
    
    public Optional<ShoppingList> updateShoppingList(Long id, ShoppingList updatedList) {
        Optional<ShoppingList> existingList = shoppingListRepository.findById(id);
        if (existingList.isPresent()) {
            ShoppingList list = existingList.get();
            list.setName(updatedList.getName());
            if (updatedList.getIsCompleted() != null) {
                list.setIsCompleted(updatedList.getIsCompleted());
            }
            return Optional.of(shoppingListRepository.save(list));
        }
        return Optional.empty();
    }
    
    public Optional<ShoppingList> completeShoppingList(Long id) {
        Optional<ShoppingList> existingList = shoppingListRepository.findById(id);
        if (existingList.isPresent()) {
            ShoppingList list = existingList.get();
            list.setIsCompleted(true);
            return Optional.of(shoppingListRepository.save(list));
        }
        return Optional.empty();
    }
    
    public boolean deleteShoppingList(Long id) {
        if (shoppingListRepository.existsById(id)) {
            shoppingListRepository.deleteById(id);
            return true;
        }
        return false;
    }
}