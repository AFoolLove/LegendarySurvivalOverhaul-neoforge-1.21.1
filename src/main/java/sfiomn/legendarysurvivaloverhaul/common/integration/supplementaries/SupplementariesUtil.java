package sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries;

import net.mehvahdjukaar.supplementaries.client.renderers.items.LunchBoxItemRenderer;
import net.mehvahdjukaar.supplementaries.common.items.LunchBoxItem;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

//import static net.mehvahdjukaar.supplementaries.common.items.neoforge.LunchBoxItemImpl.getLunchBoxData;


public class SupplementariesUtil {

    public static ItemStack getSelectedItemInLunchBasket(ItemStack itemStack) {
//        if (LegendarySurvivalOverhaul.supplementariesLoaded && itemStack.getItem() instanceof LunchBoxItem  item)
//            return getLunchBoxData(itemStack).getSelected();
//        else
            return ItemStack.EMPTY;
    }
}
