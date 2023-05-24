package com.ecues.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InventoryGUI implements Listener {

    private final Inventory inventory;
    private HashMap<ItemStack, Method> methods;

    public InventoryGUI(Integer size, String title){
        this.inventory = Bukkit.createInventory(null, size, title);

    }

    public void createItem(final Material material, Integer slot, Method method, final String name, final String... lore){

        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        this.inventory.setItem(slot, item);
        this.methods.put(item, method);

    }

    public void openInventory(final @NotNull HumanEntity entity){
        entity.openInventory(this.inventory);
    }

    protected boolean invalidSelection(ItemStack item){
        return (item == null || item.getType().isAir());
    }

    @EventHandler
    public void onInventoryClick(final @NotNull InventoryClickEvent e){

        if (!e.getInventory().equals(this.inventory)) return;

        e.setCancelled(true);

        if (invalidSelection(e.getCurrentItem())) return;

        final Player p = (Player) e.getWhoClicked();

        // TODO do Inventory action here
        // TODO this will almost certainly break, fix this
        if (methods.containsKey(e.getCurrentItem())){
            try {
                methods.get(e.getCurrentItem()).invoke(p);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e){
        if(e.getInventory().equals(this.inventory)){
            e.setCancelled(true);
        }
    }

}
