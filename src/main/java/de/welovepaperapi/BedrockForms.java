package de.welovepaperapi;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;

public class BedrockForms extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getCommand("farmwelt").setExecutor(this);
        getLogger().info("§aTest Plugin aktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cNur Spieler können dies tun!");
            return true;
        }

        if (isBedrockPlayer(player)) {
            sendBedrockForm(player);
        } else {
            openJavaInventory(player);
        }
        return true;
    }

    private boolean isBedrockPlayer(Player player) {
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Exception e) {
            getLogger().warning("Floodgate-API nicht verfügbar: " + e.getMessage());
            return false;
        }
    }

    private void sendBedrockForm(Player player) {
        SimpleForm form = SimpleForm.builder()
                .title("§lFarmwelt")
                .content(
                        "Du wählst eine Welt aus und danach\n" +
                                "wirst du zufällig teleportiert!\n\n" +
                                "Mit /spawn kommst du zurück!\n" +
                                "Oder zu deinem Plot mit /p h"
                )
                .button("§aOVERWORLD\n§7Custom generiert")
                .button("§cNETHER\n§7Normal generiert")
                .button("§5THE END\n§7Normal generiert")
                .validResultHandler(response -> {
                    String world = switch (response.clickedButtonId()) {
                        case 0 -> "overworld";
                        case 1 -> "nether";
                        case 2 -> "the_end";
                        default -> null;
                    };
                    if (world != null) {
                        player.performCommand("warp " + world);
                    }
                })
                .build();

        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form);
    }

    private void openJavaInventory(Player player) {
        player.openInventory(Bukkit.createInventory(
                null, 9, "§6FARMWELTEN - Wähle eine Welt"
        ));
    }
}