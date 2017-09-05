package net.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

public interface CommandExecutor {

    void onCommand(Player Player, String[] args);

    class CommandRegister implements Listener { 

        private static HashMap<String[], CommandExecutor> registerCommands = new HashMap<String[], CommandExecutor>();

        public static void registerCommand(String[] Command, CommandExecutor commandExecutor) {
            if(!registerCommands.containsKey(Command))
                registerCommands.put(Command, commandExecutor);
        }

        @EventHandler
        public void on(PlayerCommandPreprocessEvent Event) {
            if(isCommand(Event.getMessage().split(" ")[0])) {
                Event.setCancelled(true);

                String[] OriginalArguments = Event.getMessage().split(" ");
                String CommandArguments = "";

                for(int i = 1; i < OriginalArguments.length; i++) {
                    CommandArguments = (CommandArguments != "" ? CommandArguments + " " + OriginalArguments[i] : OriginalArguments[i]);
                }
                String[] CMD = toCommand(Event.getMessage().split(" ")[0]);
                registerCommands.get(CMD).onCommand(Event.getPlayer(), CommandArguments.split(" "));
            }
        }

        public Boolean isCommand(String Message) {
            for(String[] CommandsByte :  registerCommands.keySet()) {
                for(String Commands : CommandsByte) {
                    String CurrentCommand = Commands;
                    String CurrentMessage = Message;

                    if (Commands.startsWith("/"))
                        CurrentCommand = Commands.substring(1);
                    if (Message.startsWith("/"))
                        CurrentMessage = Message.substring(1);

                    if (CurrentMessage.equalsIgnoreCase(CurrentCommand))
                        return true;

                }
            }
            return false;
        }

        public String[] toCommand(String Message) {
            if(Message.startsWith("/"))
                Message = Message.substring(1);

            for(String[] CommandByte : registerCommands.keySet()) {
                for(String Command : CommandByte) {
                    if(Command.equalsIgnoreCase(Message)) {
                        return CommandByte;
                    }
                }
            }
            return null;
        }
    }

}

