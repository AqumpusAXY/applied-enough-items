package github.AqumpusAXY.appliedenoughitems.client;

import github.AqumpusAXY.appliedenoughitems.sort.CreativeModeTabOrder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onPlayerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        CreativeModeTabOrder.init();
    }
}
