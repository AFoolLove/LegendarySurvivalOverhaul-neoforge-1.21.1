package sfiomn.legendarysurvivaloverhaul.common.effects;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.function.BiConsumer;

// TODO amplifierMultiplier
public class SimpleAttributeEffect extends MobEffect {
    public static final String HOT_FOOD_ATTRIBUTE_UUID = "dbac2b95-f979-4104-a4ba-3039c1015ae7";
    public static final ResourceLocation HOT_FOOD_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "hot_food_attribute");
    public static final String HOT_DRINK_ATTRIBUTE_UUID = "5f1f294d-86f4-42e8-ae78-67223f29e9e8";
    public static final ResourceLocation HOT_DRINK_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "hot_drink_attribute");
    public static final String COLD_FOOD_ATTRIBUTE_UUID = "344cffd9-9a50-4bd5-9ac1-aa4830c3128a";
    public static final ResourceLocation COLD_FOOD_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_food_attribute");
    public static final String COLD_DRINK_ATTRIBUTE_UUID = "10db1b22-eda8-4cc2-90df-c1e3b06fa460";
    public static final ResourceLocation COLD_DRINK_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_drink_attribute");
    public static final String HEAT_RESISTANCE_ATTRIBUTE_UUID = "fc0998d2-a273-4d8c-aaf4-39f8a25a3c8e";
    public static final ResourceLocation HEAT_RESISTANCE_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "heat_resistance_attribute");
    public static final String COLD_RESISTANCE_ATTRIBUTE_UUID = "cc64e9cb-10dd-4896-8ff6-5f6a21b949ff";
    public static final ResourceLocation COLD_RESISTANCE_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_resistance_attribute");

    protected final double amplifierMultiplier;

    public SimpleAttributeEffect(MobEffectCategory pCategory, int pColor, double amplifierMultiplier) {
        super(pCategory, pColor);
        this.amplifierMultiplier = amplifierMultiplier;
    }

//    @Override
//    public void createModifiers(int amplifier, BiConsumer<Holder<Attribute>, AttributeModifier> output) {
//        super.createModifiers(amplifier, output);
//    }
}
