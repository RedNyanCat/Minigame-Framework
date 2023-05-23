package com.ecues;

import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.util.*;

import com.ecues.helpers.States;

public class Manager {

    // Manager reference
    public static Manager manager;

    // Configuration values
    private File minigameSaveFile;
    private FileConfiguration minigameSaveData;

    // Minigame states and values
    private States state;
    private int countdown = 20;

    private int minPlayers = 2;
    private ArrayList<UUID> players;
    private HashMap<UUID, Kit> kits;

    public static Manager getManager() {

        // Lazy initialization, checked if null
        if (manager == null){
            manager = new Manager();
        }

        return manager;
    }

    public FileConfiguration getConfigData() { return this.minigameSaveData; }

    public void createConfigData(MinigameFramework framework) {

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

}
