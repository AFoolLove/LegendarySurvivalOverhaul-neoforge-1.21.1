package sfiomn.legendarysurvivaloverhaul.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModFishingLootTables implements LootTableSubProvider {

    public static Map<ResourceKey<LootTable>, ResourceLocation> fishingInjectedLootTables = Map.ofEntries(
            Map.entry(BuiltInLootTables.FISHING_TREASURE, ResourceLocation.withDefaultNamespace("gameplay/fishing/treasure"))
    );

    public ModFishingLootTables(HolderLookup.Provider provider) {
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {

        for (Map.Entry<ResourceKey<LootTable>, ResourceLocation> entry : fishingInjectedLootTables.entrySet()) {
            biConsumer.accept(ResourceKey.create(entry.getKey().registryKey(),
                    ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "inject/" + entry.getKey().location().getPath())
            ), LootTable.lootTable().withPool(
                    LootPool.lootPool()
                            .add(LootItem.lootTableItem(ItemRegistry.SPONGE.get()).setWeight(20))
                            .add(EmptyLootItem.emptyItem().setWeight(80))
            ));
        }
    }
}
