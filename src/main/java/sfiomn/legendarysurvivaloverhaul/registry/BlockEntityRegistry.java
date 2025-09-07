package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.CoolerBlockEntity;
import sfiomn.legendarysurvivaloverhaul.common.blockentities.HeaterBlockEntity;

public class BlockEntityRegistry {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LegendarySurvivalOverhaul.MOD_ID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<HeaterBlockEntity>> HEATER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "heater_block_entity", () -> BlockEntityType.Builder
                    .of(HeaterBlockEntity::new, BlockRegistry.HEATER.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CoolerBlockEntity>> COOLER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendarySurvivalOverhaul.MOD_ID + "cooler_block_entity", () -> BlockEntityType.Builder
                    .of(CoolerBlockEntity::new, BlockRegistry.COOLER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
