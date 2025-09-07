package sfiomn.legendarysurvivaloverhaul.common.items.heal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.config.Config;

public class PlasterItem extends BodyHealingItem {
    public PlasterItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return Config.Baked.plasterUseTime;
    }
}
