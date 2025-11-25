package github.AqumpusAXY.appliedenoughitems.mixin;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import appeng.client.gui.me.common.Repo;
import appeng.menu.me.common.GridInventoryEntry;
import github.AqumpusAXY.appliedenoughitems.config.ClientConfig;
import github.AqumpusAXY.appliedenoughitems.config.CustomSortOrder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;


@Mixin(value = Repo.class, remap = false)
public abstract class RepoMixin {
    @Shadow @Final public static Comparator<GridInventoryEntry> AMOUNT_ASC;
    @Shadow @Final public static Comparator<GridInventoryEntry> AMOUNT_DESC;
    @Shadow protected abstract Comparator<AEKey> getKeyComparator(SortOrder sortBy, SortDir sortDir);

    @Inject(method = "getComparator", at = @At("HEAD"), cancellable = true)
    private void injectGetComparator(SortOrder sortOrder, SortDir sortDir, CallbackInfoReturnable<Comparator<? super GridInventoryEntry>> cir) {
        var customSortOrder = ClientConfig.CUSTOM_SORT_ORDER.get();

        if (customSortOrder == CustomSortOrder.AMOUNT) {
            cir.setReturnValue(sortDir == SortDir.ASCENDING ? AMOUNT_ASC : AMOUNT_DESC);
        } else {
            cir.setReturnValue(Comparator.comparing(GridInventoryEntry::getWhat, getKeyComparator(sortOrder, sortDir)));
        }
    }
}
