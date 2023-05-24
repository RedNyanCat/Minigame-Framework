package com.ecues;

import com.ecues.events.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

import com.ecues.helpers.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Manager extends BukkitRunnable implements Listener {

    // Manager reference
    public static Manager manager;
    private final MinigameFramework framework;

    // Configuration values
    private File minigameSaveFile;
    private FileConfiguration minigameSaveData;

    // Minigame states and values
    private States state = States.LOBBY;
    private int countdown = 20;
    private int time = 20;

    private int minPlayers = 2;
    private ArrayList<UUID> players;
    private HashMap<UUID, Kit> kits;
    private boolean dropsEnabled = false;
    private boolean spectatingEnabled = true;
    private GameMode spectatorGameMove = GameMode.SPECTATOR;
    private GameMode minigameGameMode = GameMode.SURVIVAL;

    public Manager(MinigameFramework framework) {
        this.framework = framework;
        manager = this;
    }

    // Called when stating NEW
    public static Manager getManager(MinigameFramework framework) {

        // Lazy initialization, checked if null
        if (manager == null){
            manager = new Manager(framework);
        }

        return manager;
    }

    public FileConfiguration getConfigData() { return this.minigameSaveData; }

    public void loadConfigData(MinigameFramework framework) {

        String configName = MinigameFramework.MINIGAMENAME + "_minigame_config.yml";

        // Create the minigame configuration data
        this.minigameSaveFile = new File(framework.getDataFolder(), configName);
        if (!minigameSaveFile.exists()){
            minigameSaveFile.getParentFile().mkdirs();
            framework.saveResource(configName, false);
        }

        // Load in the custom save data for the minigame
        minigameSaveData = new YamlConfiguration();
        try {
            minigameSaveData.load(this.minigameSaveFile);
        } catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }

    }

    public boolean requirementsMet(){
        // TODO implement actual requirements
        return (minPlayers <= players.size()); // could also be rank, etc
    }

    public void stop(){
        // If starting, then we need to cancel, otherwise do nothing
        if (this.state == States.STARTING)
            this.state = States.CANCELLED;
    }

    public void start(){

        this.state = States.STARTING;
        // Called when this method is called
        GameStartEvent startEvent = new GameStartEvent(players);
        Bukkit.getPluginManager().callEvent(startEvent);

        for (UUID uuid : players){
            Objects.requireNonNull(Bukkit.getPlayer(uuid)).sendMessage("Starting in " + countdown + " seconds.");
        }

        // Runs???
        this.runTaskTimer(this.framework, 0L, 20L);

    }

    // When a player dies in-game
    @EventHandler
    public void OnPlayerDeath(PlayerDeathEvent e){
        // Set game mode
        Player p = (Player) e.getEntity().getPlayer();
        assert p != null;
        p.setGameMode(spectatorGameMove);
        // Clear drops if we don't want to have item drops (i.e. Spleef)
        if (!dropsEnabled){
            e.getDrops().clear();
        }
        // Force move back to lobby server if enabled
        if (!spectatingEnabled){
            // TODO move to lobby server/kick from minigame server
        }
    }

    // We do this because each minigame server instance is its own bukkit/spigot instance
    // Joins into a lobby world inside the minigame server
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent e){
        // TODO
    }

    // Move to Countdown Class and create a InGame Class which handles the in-game timer and specific events

    @Override
    public void run(){

        // Start the game
        if (this.time == 0) {
            // Cancel the runnable
            cancel();
            // Start the game and notify all dependencies that the game is starting
            GameStartingEvent startingEvent = new GameStartingEvent(players);
            this.state = States.IN_PROGRESS;
            Bukkit.getPluginManager().callEvent(startingEvent);
            return;
        }

        // Broadcast the current time
        if (this.time % 15 == 0 || time <= 10){
            if (this.time != 1) Bukkit.broadcast(ChatColor.AQUA + "Game will start in " + this.time + " seconds.", "*");
            else Bukkit.broadcast(ChatColor.AQUA + "Game will start in " + this.time + "second.", "*");
        }

        // Listen for Cancel state/event
        if (this.state == States.CANCELLED){
            // Cancel the runnable
            cancel();
            // Notify all dependencies of the game cancellation
            GameCancelledEvent cancelEvent = new GameCancelledEvent(players);
            this.state = States.LOBBY;
            Bukkit.getPluginManager().callEvent(cancelEvent);
            Bukkit.broadcast(ChatColor.AQUA + "Countdown cancelled.", "*");
        }

        this.time--;

    }

}
