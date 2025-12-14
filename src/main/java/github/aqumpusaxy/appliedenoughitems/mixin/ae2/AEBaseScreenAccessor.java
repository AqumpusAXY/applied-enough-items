package github.aqumpusaxy.appliedenoughitems.mixin.ae2;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.widgets.VerticalButtonBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AEBaseScreen.class, remap = false)
public interface AEBaseScreenAccessor {
    @Accessor("verticalToolbar")
    VerticalButtonBar getVerticalToolbar();
}
