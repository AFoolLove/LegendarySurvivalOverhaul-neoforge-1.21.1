package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.integration.sereneseasons.SereneSeasonsModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.MountDynamicModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.TemperatureResistanceModifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TemperatureModifierRegistry
{
	public static final ResourceLocation MODIFIERS_RESOURCE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature_modifiers");
	public static final ResourceLocation DYNAMIC_MODIFIERS_RESOURCE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "dynamic_temperature_modifiers");
	public static final ResourceLocation ITEM_ATTRIBUTE_MODIFIERS_RESOURCE = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item_attribute_temperature_modifiers");

	public static final DeferredRegister<ModifierBase> MODIFIERS = DeferredRegister.create(MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = DeferredRegister.create(DYNAMIC_MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS = DeferredRegister.create(ITEM_ATTRIBUTE_MODIFIERS_RESOURCE, LegendarySurvivalOverhaul.MOD_ID);

	public static final Registry<ModifierBase> MODIFIERS_REGISTRY = MODIFIERS.makeRegistry(builder -> {});
	public static final Registry<DynamicModifierBase> DYNAMIC_MODIFIERS_REGISTRY = DYNAMIC_MODIFIERS.makeRegistry(builder -> {});
	public static final Registry<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS_REGISTRY = ITEM_ATTRIBUTE_MODIFIERS.makeRegistry(builder -> {});

	// Base Modifiers
	public static final DeferredHolder<ModifierBase, ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> ATTRIBUTE = MODIFIERS.register("attribute", AttributeModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> DIMENSION = MODIFIERS.register("dimension", DimensionModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> MOUNT = MODIFIERS.register("mount", MountModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> FREEZE = MODIFIERS.register("freeze", FreezeModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);

	public static final DeferredHolder<AttributeModifierBase, AttributeModifierBase> ITEM_ATTRIBUTE = ITEM_ATTRIBUTE_MODIFIERS.register("item_attribute", ItemAttributeModifier::new);
	public static final DeferredHolder<AttributeModifierBase, AttributeModifierBase> COAT_ATTRIBUTE = ITEM_ATTRIBUTE_MODIFIERS.register("coat_attribute", CoatAttributeModifier::new);

	public static final DeferredHolder<DynamicModifierBase, DynamicModifierBase> TEMPERATURE_RESISTANCE = DYNAMIC_MODIFIERS.register("temperature_resistance", TemperatureResistanceModifier::new);
	public static final DeferredHolder<DynamicModifierBase, DynamicModifierBase> MOUNT_DYNAMIC = DYNAMIC_MODIFIERS.register("mount_dynamic", MountDynamicModifier::new);

	// Mod Compat
	public static final DeferredHolder<ModifierBase, ModifierBase> SERENE_SEASONS = MODIFIERS.register("integration/serene_seasons", SereneSeasonsModifier::new);
	public static final DeferredHolder<ModifierBase, ModifierBase> ECLIPTIC_SEASONS = MODIFIERS.register("integration/ecliptic_seasons", EclipticSeasonsModifier::new);

//	public static final DeferredHolder<ModifierBase, ModifierBase> WORLD_TEMPERATURE_TERRA_FIRMA_CRAFT = MODIFIERS.register("integration/world_temp_tfc", TerraFirmaCraftModifier::new);
//	public static final DeferredHolder<AttributeModifierBase, AttributeModifierBase> ITEM_TEMPERATURE_TERRA_FIRMA_CRAFT = ITEM_ATTRIBUTE_MODIFIERS.register("integration/item_temp_tfc", TerraFirmaCraftHeatItemModifier::new);

//	public static final DeferredHolder<ModifierBase, ModifierBase> ORIGINS = MODIFIERS.register("integration/origins", OriginsModifier::new);
//	public static final DeferredHolder<DynamicModifierBase, DynamicModifierBase> ORIGINS_RESISTANCE = DYNAMIC_MODIFIERS.register("integration/origins_resistance", OriginsDynamicModifier::new);


	public static void register(IEventBus eventBus){
		MODIFIERS.register(eventBus);
		DYNAMIC_MODIFIERS.register(eventBus);
		ITEM_ATTRIBUTE_MODIFIERS.register(eventBus);
	}
}
