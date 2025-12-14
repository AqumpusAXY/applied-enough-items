package github.aqumpusaxy.appliedenoughitems;

import github.aqumpusaxy.appliedenoughitems.config.ClientConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AppliedEnoughItems.MODID)
public class AppliedEnoughItems {
    public static final String MODID = "appliedenoughitems";

    public AppliedEnoughItems(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }
}
