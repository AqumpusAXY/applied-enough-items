package github.aqumpusaxy.appliedenoughitems.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.EnumValue<CustomSortOrder> CUSTOM_SORT_ORDER = BUILDER
            .comment("Item sort order in ae2 terminals")
            .defineEnum("sortOrder", CustomSortOrder.JEI);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
