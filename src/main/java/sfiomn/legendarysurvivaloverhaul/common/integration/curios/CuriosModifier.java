package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.UUID;

public class CuriosModifier
{
	public static final CurioAttributeBuilder HEATING_TEMPERATURE = new CurioAttributeBuilder(AttributeRegistry.HEATING_TEMPERATURE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heating_temperature");
	public static final CurioAttributeBuilder COOLING_TEMPERATURE = new CurioAttributeBuilder(AttributeRegistry.COOLING_TEMPERATURE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cooling_temperature");
	public static final CurioAttributeBuilder HEAT_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.HEAT_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".heat_resistance");
	public static final CurioAttributeBuilder COLD_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.COLD_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".cold_resistance");
	public static final CurioAttributeBuilder THERMAL_RESISTANCE = new CurioAttributeBuilder(AttributeRegistry.THERMAL_RESISTANCE, "attribute." + LegendarySurvivalOverhaul.MOD_ID + ".thermal_resistance");

	public CuriosModifier() {}

	public static void addAttribute(CurioAttributeModifierEvent event) {
		if (!LegendarySurvivalOverhaul.curiosLoaded)
			return;

		ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
		JsonTemperatureResistance tempConfig = TemperatureDataManager.getItem(itemRegistryName);

		if (itemRegistryName != null && tempConfig != null) {
			for (ISlotType slot : CuriosApi.getItemStackSlots(event.getItemStack(), FMLLoader.getDist() == Dist.CLIENT).values()) {
				if (slot.getIdentifier().equals(event.getSlotContext().identifier())) {
					HEATING_TEMPERATURE.addModifier(event, itemRegistryName, Math.max(tempConfig.temperature, 0));
					COOLING_TEMPERATURE.addModifier(event, itemRegistryName, Math.min(tempConfig.temperature, 0));
					HEAT_RESISTANCE.addModifier(event, itemRegistryName, tempConfig.heatResistance);
					COLD_RESISTANCE.addModifier(event, itemRegistryName, tempConfig.coldResistance);
					THERMAL_RESISTANCE.addModifier(event, itemRegistryName, tempConfig.thermalResistance);
				}
			}
		}
	}
}
