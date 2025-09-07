package sfiomn.legendarysurvivaloverhaul.common.loot_modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.LootTable.createStackSplitter;

public class AdditionalLootTable extends LootModifier {

    public static final Supplier<MapCodec<AdditionalLootTable>> MAP_CODEC = Suppliers.memoize(
            () -> RecordCodecBuilder.mapCodec(instance -> codecStart(instance)
                    .and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTable").forGetter(m -> m.lootTable))
                    .and(Codec.BOOL.optionalFieldOf("replace", false).forGetter(m -> m.replace))
                    .apply(instance, AdditionalLootTable::new)
            )
    );

    private final ResourceKey<LootTable> lootTable;
    private final boolean replace;

    public AdditionalLootTable(LootItemCondition[] conditions, ResourceKey<LootTable> lootTable, boolean replace) {
        super(conditions);
        this.lootTable = lootTable;
        this.replace = replace;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (replace) {
            generatedLoot.clear();
        }

        ServerLevel level = context.getLevel();
        // noinspection deprecation
        context.getResolver().get(Registries.LOOT_TABLE, this.lootTable).ifPresent(extraTable -> {
            extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add));
            LegendarySurvivalOverhaul.LOGGER.debug(extraTable.value().getLootTableId());
            LegendarySurvivalOverhaul.LOGGER.debug("gen loot : " + generatedLoot);
        });
        // noinspection deprecation
        //context.getResolver().getLootTable(lootTable).getRandomItemsRaw(context, generatedLoot::add);

        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return MAP_CODEC.get();
    }
}
