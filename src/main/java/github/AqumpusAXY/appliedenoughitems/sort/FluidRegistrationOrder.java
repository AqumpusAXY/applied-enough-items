package github.AqumpusAXY.appliedenoughitems.sort;

import mezz.jei.common.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidRegistrationOrder {
    private static final Map<Fluid, Integer> FLUID_TO_REGISTRATION_INDEX = new HashMap<>();
    private static boolean isInitialized = false;

    public static void init() {
        if (isInitialized) return;

        List<Fluid> fluids = Services.PLATFORM.getRegistry(Registries.FLUID).getValues()
                .filter(fluid -> fluid.isSource(fluid.defaultFluidState()))
                .toList();

        for (int i = 0; i < fluids.size(); i++) {
            FLUID_TO_REGISTRATION_INDEX.put(fluids.get(i), i);
        }

        isInitialized = true;
    }

    public static int getIndex(Fluid fluid) {
        return FLUID_TO_REGISTRATION_INDEX.getOrDefault(fluid, Integer.MAX_VALUE);
    }
}
