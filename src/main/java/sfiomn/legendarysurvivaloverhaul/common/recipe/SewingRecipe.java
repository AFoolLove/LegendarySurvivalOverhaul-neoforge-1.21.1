package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonObject;
import com.ibm.icu.text.CurrencyMetaInfo;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.apache.commons.io.function.IOTriConsumer;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public class SewingRecipe implements Recipe<RecipeInput> {
    private final Ingredient base;
    private final Ingredient addition;
    private final ItemStack result;

    public SewingRecipe(Ingredient base, Ingredient addition, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }
    @Override
    public boolean matches(@NotNull RecipeInput input, Level level) {
        if (level.isClientSide)
            return false;

        return base.test(input.getItem(0)) &&
               addition.test(input.getItem(1));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeInput input, @NotNull HolderLookup.Provider registries) {
        ItemStack itemstack = this.result.copy();
        CustomData customData = input.getItem(0).get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            itemstack.set(DataComponents.CUSTOM_DATA, CustomData.of(customData.copyTag()));
        }

        return itemstack;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SewingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sewing");
    }

    public static class Serializer implements RecipeSerializer<SewingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sewing");

        public static final MapCodec<SewingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(

                Ingredient.CODEC.fieldOf("base").forGetter(SewingRecipe::getBase),
                Ingredient.CODEC.fieldOf("addition").forGetter(SewingRecipe::getAddition),
                ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result)
        ).apply(instance, SewingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<SewingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SewingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

//        public @NotNull SewingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject jsonObject) {
//            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
//            Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
//            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
//
//            return new SewingRecipe(base, addition, result, recipeId);
//        }

        public static SewingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
            return new SewingRecipe(base, addition, result);
        }

        public static void toNetwork(RegistryFriendlyByteBuf friendlyByteBuf, SewingRecipe sewingRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, sewingRecipe.base);
            Ingredient.CONTENTS_STREAM_CODEC.encode(friendlyByteBuf, sewingRecipe.addition);
            ItemStack.STREAM_CODEC.encode(friendlyByteBuf, sewingRecipe.result);
        }
    }
}
