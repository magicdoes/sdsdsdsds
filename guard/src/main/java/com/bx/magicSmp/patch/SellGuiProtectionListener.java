package com.bx.magicSmp.patch;

import com.bx.magicSmp.menus.SellMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Protects the control row (45-53) of MagicSMP's SellMenu.
 * Runs before the normal MagicSMP listener so Bukkit knows the click is
 * cancelled before MagicSMP opens a progress/worth menu.
 */
public final class SellGuiProtectionListener implements Listener {
    private boolean isSellMenu(Inventory top) {
        return top != null && top.getHolder() instanceof SellMenu;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        if (!isSellMenu(top)) return;

        int raw = event.getRawSlot();
        if (raw >= 45 && raw <= 53) {
            // Snapshot the cursor. The MagicSMP handler is still allowed to run
            // and perform the button action, but vanilla inventory transfer is not.
            ItemStack cursorBefore = event.getCursor() == null ? null : event.getCursor().clone();
            event.setCancelled(true);

            HumanEntity who = event.getWhoClicked();
            if (who instanceof Player player) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin("MagicSMP");
                if (plugin != null) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        // Never let the GUI icon become the player's cursor item.
                        // Restore exactly what they were holding before the click.
                        player.setItemOnCursor(cursorBefore);
                        player.updateInventory();
                    });
                }
            }
            return;
        }

        // Block actions from the player's inventory that can pull/collect items
        // from the top inventory in unusual ways.
        switch (event.getAction()) {
            case COLLECT_TO_CURSOR, MOVE_TO_OTHER_INVENTORY, HOTBAR_SWAP, HOTBAR_MOVE_AND_READD -> event.setCancelled(true);
            default -> { }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onDrag(InventoryDragEvent event) {
        Inventory top = event.getView().getTopInventory();
        if (!isSellMenu(top)) return;
        for (int raw : event.getRawSlots()) {
            if (raw >= 45 && raw <= 53) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
