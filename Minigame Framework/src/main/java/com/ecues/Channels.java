package com.ecues;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class Channels extends JavaPlugin implements PluginMessageListener {
    private final DataProcessor dataProcessor;
    private static MinigameFramework framework;

    // Constructor
    public Channels(MinigameFramework framework, DataProcessor dataProcessor) {
        this.framework = framework;
        this.dataProcessor = dataProcessor;
    }

    // Data processor interface
    public interface DataProcessor {
        void processData(Player player, String data);
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals(framework.MINIGAMENAME)) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String data = in.readUTF();
        dataProcessor.processData(player, data);
    }

    public static void sendData(JavaPlugin plugin, Player player, String data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(data);
        player.sendPluginMessage(plugin, framework.MINIGAMENAME, out.toByteArray());
    }

}
