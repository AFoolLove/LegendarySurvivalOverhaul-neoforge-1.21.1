package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ITemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureEnum;
import sfiomn.legendarysurvivaloverhaul.common.items.ThermometerItem;
import sfiomn.legendarysurvivaloverhaul.util.WorldUtil;

import java.util.Optional;

public class TemperatureItemCapability implements ITemperatureItemCapability, INBTSerializable<CompoundTag> {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature_item");

    private float temperature;
    private long updateTick;

    public TemperatureItemCapability() {
        this.init();
    }

    private void init() {
        this.temperature = TemperatureEnum.NORMAL.getMiddle();
        this.updateTick = 0;
    }

    @Override
    public boolean shouldUpdate(long currentTick) {
        return (currentTick - this.updateTick) > 10;
    }

    @Override
    public void updateWorldTemperature(Level world, Entity holder, long currentTick) {
        this.updateTick = currentTick;
        this.temperature = WorldUtil.calculateClientWorldEntityTemperature(world, holder);
    }

    @Override
    public float getWorldTemperatureLevel() {
        return this.temperature;
    }

    @Override
    public void setWorldTemperatureLevel(float temperature) {
        this.temperature = temperature;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        readNBT(nbt);
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        return writeNBT();
    }

    public CompoundTag writeNBT()
    {
        CompoundTag compound = new CompoundTag();

        compound.putFloat("temperature", this.temperature);

        return compound;
    }

    public void readNBT(CompoundTag compound)
    {
        this.init();
        if (compound.contains("temperature"))
            this.setWorldTemperatureLevel(compound.getFloat("temperature"));
    }

    public static class TemperatureItemProvider implements ICapabilityProvider<ItemStack, Void, TemperatureItemCapability>
    {
        public static ItemCapability<TemperatureItemCapability, Void> TEMPERATURE_ITEM_CAPABILITY = ItemCapability.createVoid(TemperatureItemCapability.ID, TemperatureItemCapability.class);

        private final TemperatureItemCapability capability = new TemperatureItemCapability();

        @Override
        public @Nullable TemperatureItemCapability getCapability(ItemStack object, Void context) {
            return capability;
        }
    }
}
