package com.carplayPackage.service;

import com.carplayPackage.model.ShoppingList;
import com.carplayPackage.model.ShoppingListItem;
import com.carplayPackage.repository.ShoppingListItemRepository;
import com.carplayPackage.repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingListItemService {
    
    @Autowired
    private ShoppingListItemRepository shoppingListItemRepository;
    
    @Autowired
    private ShoppingListRepository shoppingListRepository;
    
    public List<ShoppingListItem> getItemsByShoppingListId(Long shoppingListId) {
        return shoppingListItemRepository.findByShoppingListIdOrderByCreatedAtAsc(shoppingListId);
    }
    
    public List<ShoppingListItem> getIncompleteItemsByShoppingListId(Long shoppingListId) {
        return shoppingListItemRepository.findIncompleteItemsByListId(shoppingListId);
    }
    
    public Optional<ShoppingListItem> getShoppingListItemById(Long id) {
        return shoppingListItemRepository.findById(id);
    }
    
    public Optional<ShoppingListItem> createShoppingListItem(Long shoppingListId, ShoppingListItem item) {
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(shoppingListId);
        if (shoppingList.isPresent()) {
            item.setShoppingList(shoppingList.get());
            return Optional.of(shoppingListItemRepository.save(item));
        }
        return Optional.empty();
    }
    
    public Optional<ShoppingListItem> createShoppingListItem(Long shoppingListId, String name) {
        Optional<ShoppingList> shoppingList = shoppingListRepository.findById(shoppingListId);
        if (shoppingList.isPresent()) {
            ShoppingListItem item = new ShoppingListItem(name, shoppingList.get());
            return Optional.of(shoppingListItemRepository.save(item));
        }
        return Optional.empty();
    }
    
    public Optional<ShoppingListItem> updateShoppingListItem(Long id, ShoppingListItem updatedItem) {
        Optional<ShoppingListItem> existingItem = shoppingListItemRepository.findById(id);
        if (existingItem.isPresent()) {
            ShoppingListItem item = existingItem.get();
            item.setName(updatedItem.getName());
            if (updatedItem.getDescription() != null) {
                item.setDescription(updatedItem.getDescription());
            }
            if (updatedItem.getQuantity() != null) {
                item.setQuantity(updatedItem.getQuantity());
            }
            if (updatedItem.getIsCompleted() != null) {
                item.setIsCompleted(updatedItem.getIsCompleted());
            }
            return Optional.of(shoppingListItemRepository.save(item));
        }
        return Optional.empty();
    }
    
    public Optional<ShoppingListItem> toggleItemCompletion(Long id) {
        Optional<ShoppingListItem> existingItem = shoppingListItemRepository.findById(id);
        if (existingItem.isPresent()) {
            ShoppingListItem item = existingItem.get();
            item.setIsCompleted(!item.getIsCompleted());
            return Optional.of(shoppingListItemRepository.save(item));
        }
        return Optional.empty();
    }
    
    public boolean deleteShoppingListItem(Long id) {
        if (shoppingListItemRepository.existsById(id)) {
            shoppingListItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Long getCompletedItemCount(Long shoppingListId) {
        return shoppingListItemRepository.countByShoppingListIdAndIsCompleted(shoppingListId, true);
    }
    
    public Long getIncompleteItemCount(Long shoppingListId) {
        return shoppingListItemRepository.countByShoppingListIdAndIsCompleted(shoppingListId, false);
    }
}