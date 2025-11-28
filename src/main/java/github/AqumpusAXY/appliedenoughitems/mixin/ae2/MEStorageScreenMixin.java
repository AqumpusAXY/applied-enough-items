package github.AqumpusAXY.appliedenoughitems.mixin.ae2;

import appeng.api.config.SortOrder;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.me.common.Repo;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.TerminalStyle;
import appeng.client.gui.widgets.ISortSource;
import appeng.client.gui.widgets.SettingToggleButton;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.menu.me.common.MEStorageMenu;
import appeng.util.IConfigManagerListener;
import github.AqumpusAXY.appliedenoughitems.config.CustomSortOrder;
import github.AqumpusAXY.appliedenoughitems.gui.widget.CustomSettingToggleButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static github.AqumpusAXY.appliedenoughitems.config.ClientConfig.CUSTOM_SORT_ORDER;

@Mixin(value = MEStorageScreen.class, remap = false)
public abstract class MEStorageScreenMixin<C extends MEStorageMenu>
        extends AEBaseScreen<C> implements ISortSource, IConfigManagerListener {
    @Shadow @Final private TerminalStyle style;
    @Shadow @Final protected Repo repo;
    @Shadow private SettingToggleButton<SortOrder> sortByToggle;
    @Unique private CustomSettingToggleButton<CustomSortOrder> customSortByToggle;

    public MEStorageScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addButton(MEStorageMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        if (!this.style.isSortable()) return;

        this.customSortByToggle = new CustomSettingToggleButton<>(CUSTOM_SORT_ORDER, ((button, backwards) -> {
            button.toggleSetting(backwards);
            repo.updateView();
        }));

        VerticalButtonBar buttonBar = ((AEBaseScreenAccessor) this).getVerticalToolbar();
        List<Button> buttons = ((VerticalButtonBarAccessor) buttonBar).getButtons();

        int index = buttons.indexOf(sortByToggle);
        buttons.set(index, customSortByToggle);
    }
}
