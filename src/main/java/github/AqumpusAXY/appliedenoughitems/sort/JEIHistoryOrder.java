package github.AqumpusAXY.appliedenoughitems.sort;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import github.AqumpusAXY.appliedenoughitems.util.BoundedLinkedList;
import mezz.jei.gui.overlay.elements.IElement;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class JEIHistoryOrder {
    private static final int MAX_HISTORY_SIZE = 18;
    private static final BoundedLinkedList<AEKey> HISTORY_AE_KEYS = new BoundedLinkedList<>(MAX_HISTORY_SIZE);
    private static boolean isInitialized = false;

    //FIXME: 初始化不会读取全部的历史记录
    public static void initOrUpdateHistoryElements(List<IElement<?>> historyElements) {
        if (!isInitialized) {
            for (IElement<?> element : historyElements) {
                HISTORY_AE_KEYS.addLast(getElementAEKey(element));
            }

            isInitialized = true;
        } else {
            HISTORY_AE_KEYS.addFirst(getElementAEKey(historyElements.get(0)));
        }
    }

    private static AEKey getElementAEKey(IElement<?> element) {
        var type = element.getTypedIngredient().getType().getIngredientClass();

        if (type == ItemStack.class) {
            return AEItemKey.of((ItemStack) element.getTypedIngredient().getIngredient());
        } else if (type == FluidStack.class) {
            return AEFluidKey.of((FluidStack) element.getTypedIngredient().getIngredient());
        } else {
            return null;
        }
    }

    public static int getAEKeyHistoryIndex(AEKey aeKey) {
        return HISTORY_AE_KEYS.getList().contains(aeKey) ? HISTORY_AE_KEYS.getList().indexOf(aeKey) : HISTORY_AE_KEYS.getMaxSize();
    }
}
