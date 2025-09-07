package sfiomn.legendarysurvivaloverhaul.data.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.loot_modifiers.AdditionalLootTable;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static sfiomn.legendarysurvivaloverhaul.data.loot.ModChestLootTables.chestInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModEntityLootTables.entityInjectedLootTables;
import static sfiomn.legendarysurvivaloverhaul.data.loot.ModFishingLootTables.fishingInjectedLootTables;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, LegendarySurvivalOverhaul.MOD_ID);
    }

    @Override
    protected void start() {
        for (ResourceKey<LootTable> lootTable: chestInjectedLootTables.keySet()) {
            this.add(lootTable.location().getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable.location()).build()},
                    ResourceKey.create(Registries.LOOT_TABLE,
                            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID,
                                    "inject/" + lootTable.location().getPath())
                    ),
                    false));
        }

        for (Map.Entry<ResourceKey<LootTable>, ResourceLocation> lootTable: entityInjectedLootTables.entrySet()) {
            this.add(lootTable.getKey().location().getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable.getKey().location()).build()},
                    ResourceKey.create(Registries.LOOT_TABLE,
                            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID,
                                    "inject/" + lootTable.getKey().location().getPath())
                    ),
                    false));
        }

        for (Map.Entry<ResourceKey<LootTable>, ResourceLocation> lootTable: fishingInjectedLootTables.entrySet()) {
            this.add(lootTable.getKey().location().getPath(), new AdditionalLootTable(
                    new LootItemCondition[]{LootTableIdCondition.builder(lootTable.getKey().location()).build()},
                    ResourceKey.create(Registries.LOOT_TABLE,
                            ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID,
                                    "inject/" + lootTable.getKey().location().getPath())
                    ),
                    false));
        }
    }
}
