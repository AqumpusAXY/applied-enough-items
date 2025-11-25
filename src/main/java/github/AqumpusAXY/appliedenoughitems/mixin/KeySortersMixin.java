package github.AqumpusAXY.appliedenoughitems.mixin;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import github.AqumpusAXY.appliedenoughitems.config.ClientConfig;
import github.AqumpusAXY.appliedenoughitems.sort.JEISortComparator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;

@Mixin(targets = "appeng.client.gui.me.common.KeySorters", remap = false)
public abstract class KeySortersMixin {
    @Shadow @Final public static Comparator<AEKey> NAME_ASC;
    @Shadow @Final public static Comparator<AEKey> NAME_DESC;
    @Shadow @Final public static Comparator<AEKey> MOD_ASC;
    @Shadow @Final public static Comparator<AEKey> MOD_DESC;

    @Inject(method = "getComparator", at = @At("HEAD"), cancellable = true)
    private static void injectGetComparator(SortOrder order, SortDir dir, CallbackInfoReturnable<Comparator<AEKey>> cir) {
        var customSortOrder = ClientConfig.CUSTOM_SORT_ORDER.get();

        cir.setReturnValue(switch (customSortOrder) {
            case JEI -> dir == SortDir.ASCENDING ? JEISortComparator.JEI_ASC : JEISortComparator.JEI_DESC;
            case NAME -> dir == SortDir.ASCENDING ? NAME_ASC : NAME_DESC;
            case MOD -> dir == SortDir.ASCENDING ? MOD_ASC : MOD_DESC;
            case AMOUNT -> throw new UnsupportedOperationException();
        });
    }
}
