package org.l2x9.l2x9core.patches;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.l2x9.l2x9core.Main;
import org.l2x9.l2x9core.util.SecondPassEvent;
import org.l2x9.l2x9core.util.Utils;

import java.util.HashMap;

public class PacketElytraFly implements Listener {
    HashMap<Player, Integer> elytraHashMap = new HashMap<>();

    @EventHandler
    public void onToggleGlide(EntityToggleGlideEvent event) {
        try {
            if (Main.getPlugin().getConfigBoolean("Elytra.PacketElytraFly-Enabled")) {
                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    if (elytraHashMap.containsKey(player)) {
                        elytraHashMap.replace(player, elytraHashMap.get(player) + 1);
                    } else {
                        elytraHashMap.put(player, 1);
                    }
                    if (elytraHashMap.get(player) > 6) {
                        elytraHashMap.remove(player);
                        ItemStack chest = player.getInventory().getChestplate();
                        if (chest != null && chest.getType() == Material.ELYTRA) {
                            player.getLocation().getWorld().dropItem(player.getLocation(), chest);
                            player.getInventory().setChestplate(null);
                            player.setGliding(false);
                            if (Main.getPlugin().getConfigBoolean("Elytra.SendMessage")) {
                                Utils.sendMessage(player, Main.getPlugin().getConfig().getString("Elytra.Message"));
                            }
                        }
                    }
                }
            }
        } catch (Error | Exception throwable) {
            Utils.reportException(throwable);
            throwable.printStackTrace();
        }
    }

    @EventHandler
    public void onSecondPass(SecondPassEvent event) {
        Utils.secondPass(elytraHashMap);
    }
}
