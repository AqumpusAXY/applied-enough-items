package github.AqumpusAXY.appliedenoughitems.mixin.ae2;

import appeng.client.gui.widgets.VerticalButtonBar;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = VerticalButtonBar.class, remap = false)
public interface VerticalButtonBarAccessor {
    @Accessor("buttons")
    List<Button> getButtons();
}
