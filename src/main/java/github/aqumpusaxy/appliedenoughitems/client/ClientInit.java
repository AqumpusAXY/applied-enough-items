package github.aqumpusaxy.appliedenoughitems.client;

import github.aqumpusaxy.appliedenoughitems.sort.FluidRegistrationOrder;
import github.aqumpusaxy.appliedenoughitems.sort.ItemCreativeModeTabOrder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        ItemCreativeModeTabOrder.init();
        FluidRegistrationOrder.init();
    }
}
