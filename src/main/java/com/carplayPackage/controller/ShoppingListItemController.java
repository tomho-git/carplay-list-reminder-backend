package com.carplayPackage.controller;

import com.carplayPackage.model.ShoppingListItem;
import com.carplayPackage.service.ShoppingListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping-list-items")
@CrossOrigin(origins = "*")
public class ShoppingListItemController {
    
    @Autowired
    private ShoppingListItemService shoppingListItemService;
    
    @GetMapping("/list/{listId}")
    public ResponseEntity<List<ShoppingListItem>> getItemsByShoppingListId(@PathVariable Long listId) {
        List<ShoppingListItem> items = shoppingListItemService.getItemsByShoppingListId(listId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/list/{listId}/incomplete")
    public ResponseEntity<List<ShoppingListItem>> getIncompleteItemsByShoppingListId(@PathVariable Long listId) {
        List<ShoppingListItem> items = shoppingListItemService.getIncompleteItemsByShoppingListId(listId);
        return ResponseEntity.ok(items);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListItem> getShoppingListItemById(@PathVariable Long id) {
        Optional<ShoppingListItem> item = shoppingListItemService.getShoppingListItemById(id);
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/list/{listId}")
    public ResponseEntity<ShoppingListItem> createShoppingListItem(@PathVariable Long listId, @RequestBody ShoppingListItem item) {
        Optional<ShoppingListItem> createdItem = shoppingListItemService.createShoppingListItem(listId, item);
        return createdItem.map(item1 -> ResponseEntity.status(HttpStatus.CREATED).body(item1))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PostMapping("/list/{listId}/create")
    public ResponseEntity<ShoppingListItem> createShoppingListItemWithName(@PathVariable Long listId, @RequestParam String name) {
        Optional<ShoppingListItem> createdItem = shoppingListItemService.createShoppingListItem(listId, name);
        return createdItem.map(item -> ResponseEntity.status(HttpStatus.CREATED).body(item))
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListItem> updateShoppingListItem(@PathVariable Long id, @RequestBody ShoppingListItem item) {
        Optional<ShoppingListItem> updatedItem = shoppingListItemService.updateShoppingListItem(id, item);
        return updatedItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/toggle")
    public ResponseEntity<ShoppingListItem> toggleItemCompletion(@PathVariable Long id) {
        Optional<ShoppingListItem> toggledItem = shoppingListItemService.toggleItemCompletion(id);
        return toggledItem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingListItem(@PathVariable Long id) {
        boolean deleted = shoppingListItemService.deleteShoppingListItem(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/list/{listId}/stats")
    public ResponseEntity<Object> getListStats(@PathVariable Long listId) {
        Long completedCount = shoppingListItemService.getCompletedItemCount(listId);
        Long incompleteCount = shoppingListItemService.getIncompleteItemCount(listId);
        
        return ResponseEntity.ok(new Object() {
            public final Long completed = completedCount;
            public final Long incomplete = incompleteCount;
            public final Long total = completedCount + incompleteCount;
        });
    }
}