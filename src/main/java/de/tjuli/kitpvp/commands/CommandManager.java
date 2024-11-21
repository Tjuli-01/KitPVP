//https://github.com/NikV2/AnticheatBase/blob/master/src/main/java/me/nik/anticheatbase/commands/CommandManager.java
package de.tjuli.kitpvp.commands;

import de.tjuli.kitpvp.KitPVP;
import de.tjuli.kitpvp.commands.subcommands.*;
import de.tjuli.kitpvp.enums.MsgType;
import de.tjuli.kitpvp.kit.Kit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager implements TabExecutor {
    private final JavaPlugin plugin;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(KitPVP plugin) {
        this.plugin = plugin;
        subCommands.add(new EditKitCommand(plugin));
        subCommands.add(new CreateKitCommand(plugin));
        subCommands.add(new DeleteKitCommand(plugin));
        subCommands.add(new SpawnNPCCommand(plugin));
        subCommands.add(new OpenGUICommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(MsgType.NO_CONSOLE.getMessage());
            return true;
        }
        if (args.length > 0) {
            String subCommandName = args[0];

            for (SubCommand subCommand : subCommands) {
                if (subCommandName.equalsIgnoreCase(subCommand.getName())) {
                    if (!sender.hasPermission(subCommand.getPermission())) {
                        sender.sendMessage(MsgType.NO_PERMISSION.getMessage());
                        return true;
                    }
                    if(args.length - 1 < subCommand.minArgs()) {
                        sender.sendMessage(MsgType.INVALID_SYNTAX.getMessage());
                        return true;
                    }
                    if(subCommand.needsEditorMode() && !KitPVP.INSTANCE.getKitEditor().isEditorModeEnabled((Player) sender)) {
                        sender.sendMessage(MsgType.NOT_IN_EDITOR_MODE.getMessage());
                        return true;
                    }

                    subCommand.execute((Player) sender, args);
                    return true;
                }
            }
        }


        List<String> authors = plugin.getDescription().getAuthors();
        int numAuthors = authors.size();

        StringBuilder authorString = new StringBuilder();
        for (int i = 0; i < numAuthors; i++) {
            authorString.append(authors.get(i));

            if (i < numAuthors - 2) {
                authorString.append(", ");
            } else if (i == numAuthors - 2) {
                authorString.append(" and ");
            }
        }

        String authorsString = authorString.toString();


        sender.sendMessage(MsgType.PREFIX.getMessage() + ChatColor.GRAY + "Kitpvp v" + plugin.getDescription().getVersion() + " by " + authorsString);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            return subCommands.stream()
                    .filter(subCommand -> sender.hasPermission(subCommand.getPermission()))
                    .map(SubCommand::getName)
                    .filter(name -> name.toLowerCase().startsWith(input))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            SubCommand subCommand = getSubCommandByName(args[0]);
            if(!sender.hasPermission(subCommand.getPermission())) {
                return null;
            }
            String input = args[1].toLowerCase();
            if (subCommand != null && subCommand.tabKits()) {
                Player player = (Player) sender;
                return KitPVP.INSTANCE.getKitManager().getKits(player.getUniqueId()).stream()
                        .map(Kit::getName)
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .collect(Collectors.toList());
            }
        }

        return null;
    }

    private SubCommand getSubCommandByName(String name) {
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(name)) {
                return subCommand;
            }
        }
        return null;
    }

}
