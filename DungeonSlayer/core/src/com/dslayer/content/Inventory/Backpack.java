/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dslayer.content.Inventory;

import com.dslayer.content.Inventory.Items.BossKey;
import com.dslayer.content.Inventory.Items.Items;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ARustedKnight
 */
public class Backpack {
    
    private List<Items> _items;
    
    public Backpack(){
        _items = new ArrayList();
    }
    
    public boolean addItem(Items item){
        return _items.add(item);
    }
    public boolean removeItem(Items item){
        return _items.remove(item);
    }
    
    public boolean containsItem(Class<? extends Items> cls){
        for(Items item: _items){
            if(item.getClass() == cls){
                return true;
            }
        }
        return false;
    }
    public List<Items> getItems(){
        return _items;
    }
}
