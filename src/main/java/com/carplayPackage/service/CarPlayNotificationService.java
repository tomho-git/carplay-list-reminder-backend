package com.carplayPackage.service;

import com.carplayPackage.model.ShoppingList;
import com.carplayPackage.model.ShoppingListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarPlayNotificationService {
    
    @Autowired
    private ShoppingListService shoppingListService;
    
    @Autowired
    private ShoppingListItemService shoppingListItemService;
    
    public NotificationResponse getCarPlayNotification() {
        List<ShoppingList> incompleteLists = shoppingListService.getIncompleteShoppingLists();
        
        if (incompleteLists.isEmpty()) {
            return new NotificationResponse("No shopping lists pending", null);
        }
        
        StringBuilder message = new StringBuilder();
        message.append("You have ").append(incompleteLists.size()).append(" shopping list(s) to complete:\n");
        
        for (ShoppingList list : incompleteLists) {
            List<ShoppingListItem> incompleteItems = shoppingListItemService.getIncompleteItemsByShoppingListId(list.getId());
            message.append("• ").append(list.getName()).append(" (").append(incompleteItems.size()).append(" items)\n");
        }
        
        return new NotificationResponse(message.toString(), incompleteLists);
    }
    
    public NotificationResponse getShoppingListSummary(Long listId) {
        var shoppingList = shoppingListService.getShoppingListById(listId);
        
        if (shoppingList.isEmpty()) {
            return new NotificationResponse("Shopping list not found", null);
        }
        
        List<ShoppingListItem> incompleteItems = shoppingListItemService.getIncompleteItemsByShoppingListId(listId);
        
        if (incompleteItems.isEmpty()) {
            return new NotificationResponse("All items completed in " + shoppingList.get().getName(), null);
        }
        
        StringBuilder message = new StringBuilder();
        message.append("Shopping List: ").append(shoppingList.get().getName()).append("\n");
        message.append("Items to buy:\n");
        
        for (ShoppingListItem item : incompleteItems) {
            message.append("• ").append(item.getName());
            if (item.getQuantity() > 1) {
                message.append(" (").append(item.getQuantity()).append(")");
            }
            message.append("\n");
        }
        
        return new NotificationResponse(message.toString(), incompleteItems);
    }
    
    public static class NotificationResponse {
        private String message;
        private Object data;
        
        public NotificationResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public Object getData() {
            return data;
        }
        
        public void setData(Object data) {
            this.data = data;
        }
    }
}