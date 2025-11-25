package github.AqumpusAXY.appliedenoughitems.sort;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class CreativeModeTabOrder {
    private static final Map<Item, Integer> ITEM_TO_CREATIVE_TAB_INDEX = new HashMap<>();
    private static boolean isInitialized = false;

    public static void init() {
        LocalPlayer player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;
        if (isInitialized) return;

        CreativeModeTab.ItemDisplayParameters parameters = new CreativeModeTab.ItemDisplayParameters(
                player.connection.enabledFeatures(),
                true,
                level.registryAccess()
        );

        int index = 0;
        for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
            if (tab.getType() != CreativeModeTab.Type.CATEGORY) continue;

            tab.buildContents(parameters);
            Collection<ItemStack> itemStacks = tab.getDisplayItems();
            for (ItemStack itemStack : itemStacks) {
                if (itemStack.isEmpty()) continue;

                Item item = itemStack.getItem();
                if (!ITEM_TO_CREATIVE_TAB_INDEX.containsKey(item)) {
                    ITEM_TO_CREATIVE_TAB_INDEX.put(item, index);
                    index++;
                }
            }
        }
        isInitialized = true;
    }

    public static int getIndex(Item item) {
        return ITEM_TO_CREATIVE_TAB_INDEX.getOrDefault(item, Integer.MAX_VALUE);
    }
}
