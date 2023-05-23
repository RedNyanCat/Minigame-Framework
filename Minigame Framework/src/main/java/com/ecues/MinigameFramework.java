package com.ecues;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public final class MinigameFramework extends JavaPlugin implements Channels.DataProcessor {

    public static String MINIGAMENAME = "TEST";

    @Override
    public void processData(Player player, String data) {
        // Process the received data here
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new Manager(this), this);

        // Create an instance of the Channels class and register it as an incoming plugin channel
        Channels channels = new Channels(this,this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, MINIGAMENAME);
        getServer().getMessenger().registerIncomingPluginChannel(this, MINIGAMENAME, channels);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
