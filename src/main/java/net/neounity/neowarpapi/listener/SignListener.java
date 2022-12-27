package net.neounity.neowarpapi.listener;

import net.neounity.neowarpapi.NeoWarpAPI;
import net.neounity.neowarpapi.util.Data;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onRightClickOnSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getLine(0).equalsIgnoreCase("§8* §b§lWarp §8*")) {
                NeoWarpAPI.teleportToWarp(player, sign.getLine(1).toUpperCase(), false);
            }
        }
    }


    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getLine(0).equalsIgnoreCase("[warp]")) {
            if (player.hasPermission("warpsystem.addwarpsign")) {
                String name = event.getLine(1).toUpperCase();
                if (NeoWarpAPI.warpExists(event.getLine(1)) && event.getLine(1) != null) {
                    event.setLine(0, "§8* §b§lWarp §8*");
                    event.setLine(1,  name);
                    event.setLine(2, "§e§lHier klicken!");
                    event.setLine(3, "§c§m-------");
                } else {
                    player.sendMessage(Data.PREFIX + "§6§l" + event.getLine(1) + " §4does not exist.");
                    event.getBlock().breakNaturally();
                }
            } else {
                player.sendMessage(Data.NO_PERMISSIONS);
            }
        }
    }
}