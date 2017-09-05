package net.command;

import org.bukkit.entity.Player;

public interface CommandExecutor {

    void onCommand(Player Player, String[] args);

}

