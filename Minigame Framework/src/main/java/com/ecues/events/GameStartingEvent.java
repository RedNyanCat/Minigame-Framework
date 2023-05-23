package com.ecues.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class GameStartingEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final ArrayList<UUID> players;

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers(){
        return HANDLERS;
    }

    public GameStartingEvent(ArrayList<UUID> players){

        this.players = players;

    }

}
