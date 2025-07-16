package com.carplayPackage.controller;

import com.carplayPackage.model.ShoppingList;
import com.carplayPackage.service.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shopping-lists")
@CrossOrigin(origins = "*")
public class ShoppingListController {
    
    @Autowired
    private ShoppingListService shoppingListService;
    
    @GetMapping
    public ResponseEntity<List<ShoppingList>> getAllShoppingLists() {
        List<ShoppingList> lists = shoppingListService.getAllShoppingLists();
        return ResponseEntity.ok(lists);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ShoppingList> getShoppingListById(@PathVariable Long id) {
        Optional<ShoppingList> shoppingList = shoppingListService.getShoppingListById(id);
        return shoppingList.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/incomplete")
    public ResponseEntity<List<ShoppingList>> getIncompleteShoppingLists() {
        List<ShoppingList> lists = shoppingListService.getIncompleteShoppingLists();
        return ResponseEntity.ok(lists);
    }
    
    @PostMapping
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingList shoppingList) {
        ShoppingList createdList = shoppingListService.createShoppingList(shoppingList);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }
    
    @PostMapping("/create")
    public ResponseEntity<ShoppingList> createShoppingListWithName(@RequestParam String name) {
        ShoppingList createdList = shoppingListService.createShoppingList(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdList);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ShoppingList> updateShoppingList(@PathVariable Long id, @RequestBody ShoppingList shoppingList) {
        Optional<ShoppingList> updatedList = shoppingListService.updateShoppingList(id, shoppingList);
        return updatedList.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/complete")
    public ResponseEntity<ShoppingList> completeShoppingList(@PathVariable Long id) {
        Optional<ShoppingList> completedList = shoppingListService.completeShoppingList(id);
        return completedList.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingList(@PathVariable Long id) {
        boolean deleted = shoppingListService.deleteShoppingList(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}