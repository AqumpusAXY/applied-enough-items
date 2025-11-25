package github.AqumpusAXY.appliedenoughitems.sort;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import net.minecraft.world.item.Item;

import java.util.Comparator;
import java.util.List;

public class JEISortComparator {
    private static final List<String> MOD_PRIORITY = List.of("minecraft", "ae2");
    public static final Comparator<AEKey> JEI_ASC = getModNameComparator()
            .thenComparing(getTypeComparator())
            .thenComparing(getCreativeModeTabComparator());
    public static final Comparator<AEKey> JEI_DESC = JEI_ASC.reversed();

    private static int getTypePriority(AEKey aeKey) {
        Class<? extends AEKey> typeClass = aeKey.getClass();

        if (typeClass == AEItemKey.class) {
            return 0;
        } else if (typeClass == AEFluidKey.class) {
            return 1;
        } else {
            return 2;
        }
    }

    public static Comparator<AEKey> getModNameComparator() {
        return Comparator.comparing(AEKey::getModId,
                Comparator.comparingInt(modId -> {
                    int index = MOD_PRIORITY.indexOf(modId);
                    return index >= 0 ? index : (modId.hashCode() & 0x7fffffff) + MOD_PRIORITY.size();
                })
        );
    }

    public static Comparator<AEKey> getTypeComparator() {
        return Comparator.comparingInt(JEISortComparator::getTypePriority);
    }

    public static Comparator<AEKey> getCreativeModeTabComparator() {
        return (a, b) -> {
            if (a.getClass() == AEItemKey.class && b.getClass() == AEItemKey.class) {
                Item itemA = ((AEItemKey) a).getItem();
                Item itemB = ((AEItemKey) b).getItem();
                return Integer.compare(CreativeModeTabOrder.getIndex(itemA), CreativeModeTabOrder.getIndex(itemB));
            }
            return 0;
        };
    }
}
