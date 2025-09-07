package sfiomn.legendarysurvivaloverhaul.common.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.api.thirst.HydrationEnum;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.drink.CanteenItem;

public class PurificationBlastingRecipe extends BlastingRecipe {
    public PurificationBlastingRecipe(String group, CookingBookCategory cookingBookCategory, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(group, cookingBookCategory, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(@NotNull SingleRecipeInput inventory, @NotNull Level level) {
        return this.ingredient.test(inventory.getItem(0)) && ThirstUtil.getCapacityTag(inventory.getItem(0)) > 0;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SingleRecipeInput inventory, HolderLookup.@NotNull Provider registries) {
        int hydrationCapacity = ThirstUtil.getCapacityTag(inventory.getItem(0));
        ItemStack result = this.result.copy();
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, hydrationCapacity);
        return result;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        ItemStack result = this.result.copy();
        int maxHydrationCapacity = 0;
        if (this.result.getItem() instanceof CanteenItem resultItem) {
            maxHydrationCapacity = resultItem.getMaxCapacity();
        }
        ThirstUtil.setHydrationEnumTag(result, HydrationEnum.PURIFIED);
        ThirstUtil.setCapacityTag(result, maxHydrationCapacity);
        return result;
    }


    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeType.BLASTING;
    }

    public static class Serializer implements RecipeSerializer<PurificationBlastingRecipe> {
        private final MapCodec<PurificationBlastingRecipe> codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.fieldOf("group").forGetter(PurificationBlastingRecipe::getGroup),
                CookingBookCategory.CODEC.fieldOf("category").forGetter(PurificationBlastingRecipe::category),
                Ingredient.CODEC.fieldOf("ingredient").forGetter(o -> o.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
                Codec.FLOAT.fieldOf("experience").forGetter(PurificationBlastingRecipe::getExperience),
                Codec.INT.fieldOf("cookingtime").forGetter(PurificationBlastingRecipe::getCookingTime)
        ).apply(instance, PurificationBlastingRecipe::new));

        private final StreamCodec<RegistryFriendlyByteBuf, PurificationBlastingRecipe> streamCodec = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        public static final Serializer INSTANCE = new Serializer(100);
        private final int defaultCookingTime;

        public Serializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
        }

        @Override
        public MapCodec<PurificationBlastingRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PurificationBlastingRecipe> streamCodec() {
            return streamCodec;
        }

//        public PurificationBlastingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
//            String s = GsonHelper.getAsString(pJson, "group", "");
//            CookingBookCategory cookingbookcategory = (CookingBookCategory)CookingBookCategory.CODEC.byName(GsonHelper.getAsString(pJson, "category", (String)null), CookingBookCategory.MISC);
//            JsonElement jsonelement = GsonHelper.isArrayNode(pJson, "ingredient") ? GsonHelper.getAsJsonArray(pJson, "ingredient") : GsonHelper.getAsJsonObject(pJson, "ingredient");
//            Ingredient ingredient = Ingredient.fromJson((JsonElement)jsonelement, false);
//            if (!pJson.has("result")) {
//                throw new JsonSyntaxException("Missing result, expected to find a string or object");
//            } else {
//                ItemStack itemstack;
//                if (pJson.get("result").isJsonObject()) {
//                    itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pJson, "result"));
//                } else {
//                    String s1 = GsonHelper.getAsString(pJson, "result");
//                    ResourceLocation resourcelocation = new ResourceLocation(s1);
//                    itemstack = new ItemStack((ItemLike) BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
//                        return new IllegalStateException("Item: " + s1 + " does not exist");
//                    }));
//                }
//
//                float f = GsonHelper.getAsFloat(pJson, "experience", 0.0F);
//                int i = GsonHelper.getAsInt(pJson, "cookingtime", this.defaultCookingTime);
//                return new PurificationBlastingRecipe(pRecipeId, s, cookingbookcategory, ingredient, itemstack, f, i);
//            }
//        }

        public static PurificationBlastingRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            String s = pBuffer.readUtf();
            CookingBookCategory cookingbookcategory = pBuffer.readEnum(CookingBookCategory.class);

            Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(pBuffer);
            float f = pBuffer.readFloat();
            int i = pBuffer.readVarInt();
            return new PurificationBlastingRecipe(s, cookingbookcategory, ingredient, itemstack, f, i);
        }

        public static void toNetwork(RegistryFriendlyByteBuf pBuffer, PurificationBlastingRecipe pRecipe) {
            pBuffer.writeUtf(pRecipe.getGroup());
            pBuffer.writeEnum(pRecipe.category());
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.ingredient);
            ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
            pBuffer.writeFloat(pRecipe.experience);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }
    }
}
