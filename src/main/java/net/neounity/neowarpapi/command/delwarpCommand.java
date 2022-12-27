package net.neounity.neowarpapi.command;

import net.neounity.neowarpapi.NeoWarpAPI;
import net.neounity.neowarpapi.util.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class delwarpCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.ONLY_INGAME);
            return true;
        }
        Player player = (Player) sender;

        if (player.hasPermission("neowarpapi.delwarp")) {
            if (args.length == 1) {
                NeoWarpAPI.removeWarp(player, args[0]);
            } else {
                player.sendMessage(Data.USAGE + "/delwarp [Warp]");
            }
        } else {
            player.sendMessage(Data.NO_PERMISSIONS);
        }
        return true;
    }
}