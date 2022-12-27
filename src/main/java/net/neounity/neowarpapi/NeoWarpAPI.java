package net.neounity.neowarpapi;

import net.neounity.neowarpapi.command.delwarpCommand;
import net.neounity.neowarpapi.command.setwarpCommand;
import net.neounity.neowarpapi.command.warpCommand;
import net.neounity.neowarpapi.command.warpsCommand;
import net.neounity.neowarpapi.listener.SignListener;
import net.neounity.neowarpapi.util.Data;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class NeoWarpAPI extends JavaPlugin {

    private static File file = new File("plugins/NeoWarpAPI", "warps.yml");
    private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    @Override
    public void onEnable() {
        getLogger().info("WarpSystem wurde erfolgreich geladen.");
        getCommand("delwarp").setExecutor(new delwarpCommand());
        getCommand("setwarp").setExecutor(new setwarpCommand());
        getCommand("warp").setExecutor(new warpCommand());
        getCommand("warps").setExecutor(new warpsCommand());
        Bukkit.getPluginManager().registerEvents(new SignListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("WarpSystem wurde erfolgreich entladen.");
    }


    private static void saveFile() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage("§e§lwarps.yml §ckonnte nicht gespeichert werden.");
        }
    }


    public String warpList() {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String warps : cfg.getKeys(false)) {
            if (sb.length() > 0) {
                sb.append("§f, §a");
            }
            sb.append("§a" + warps);
            i++;
        }
        return "§6§lWarps§8: §8(§6§l" + i + "§8)§8: §a" + sb;
    }


    public static boolean warpExists(String warpName) {
        warpName = warpName.toUpperCase();
        return cfg.contains(warpName);
    }

    public static Location warpLocation(String warpName) {
        warpName = warpName.toUpperCase();
        String world = cfg.getString(warpName + ".world");
        double x = cfg.getDouble(warpName + ".x");
        double y = cfg.getDouble(warpName + ".y");
        double z = cfg.getDouble(warpName + ".z");
        double yaw = cfg.getDouble(warpName + ".yaw");
        double pitch = cfg.getDouble(warpName + ".pitch");
        Location loc = new Location(Bukkit.getWorld(world), x, y, z);
        loc.setYaw((float) yaw);
        loc.setPitch((float) pitch);
        return loc;
    }

    public static void teleportToWarp(Player player, String warpName, Boolean silentMode) {
        warpName = warpName.toUpperCase();
        if (warpExists(warpName)) {
            String world = cfg.getString(warpName + ".world");
            double x = cfg.getDouble(warpName + ".x");
            double y = cfg.getDouble(warpName + ".y");
            double z = cfg.getDouble(warpName + ".z");
            double yaw = cfg.getDouble(warpName + ".yaw");
            double pitch = cfg.getDouble(warpName + ".pitch");
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            loc.setYaw((float) yaw);
            loc.setPitch((float) pitch);
            player.teleport(loc);

            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            player.playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 3);
            if (!silentMode) {
                player.sendMessage(Data.PREFIX + "§aErfolgreich teleportiert zu §6§l" + warpName);
            }
        } else {
            player.sendMessage(Data.PREFIX + "§6§l" + warpName + " §4existiert nicht.");
        }
    }


    public static void createWarp(Player player, String warpName) {
        warpName = warpName.toUpperCase();
        if (!warpExists(warpName)) {
            cfg.set(warpName + ".world", player.getLocation().getWorld().getName());
            cfg.set(warpName + ".x", Double.valueOf(player.getLocation().getX()));
            cfg.set(warpName + ".y", Double.valueOf(player.getLocation().getY()));
            cfg.set(warpName + ".z", Double.valueOf(player.getLocation().getZ()));
            cfg.set(warpName + ".yaw", Float.valueOf(player.getLocation().getYaw()));
            cfg.set(warpName + ".pitch", Float.valueOf(player.getLocation().getPitch()));
            saveFile();
            player.sendMessage(Data.PREFIX + "§6§l" + warpName + " §aerfolgreich §bgesetzt.");
        } else {
            player.sendMessage(Data.PREFIX + "§6§l" + warpName + " §4existiert bereits.");
        }
    }

    public static void removeWarp(Player player, String warpName) {
        warpName = warpName.toUpperCase();
        if (warpExists(warpName)) {
            cfg.set(warpName, null);
            player.sendMessage(Data.PREFIX + "§6§l" + warpName + " §aerfolgreich §cgelöscht.");
            saveFile();
        } else {
            player.sendMessage(Data.PREFIX + "§6§l" + warpName + " §4existiert nicht.");
        }
    }
}
