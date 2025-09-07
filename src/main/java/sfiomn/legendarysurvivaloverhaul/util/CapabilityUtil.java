package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.IBodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureItemCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstProvider;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeTypeRegistry;

/**
 * Helper functions for quickly getting player capabilities.
 * @author Icey
 */
public final class CapabilityUtil
{
	private CapabilityUtil() {}
	
	/**
	 * Gets the temperature capability of the given player.
	 * @param player Player
	 * @return The temperature capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static TemperatureCapability getTempCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.TEMPERATURE);
	}

	/**
	 * Gets the temperature item capability of the given itemstack.
	 * @param itemStack ItemStack
	 * @return The temperature item capability of the given itemstack if it exists, or a new dummy capability if it doesn't.
	 */
	public static TemperatureItemCapability getTempItemCapability(ItemStack itemStack)
	{
		return itemStack.getCapability(TemperatureItemCapability.TemperatureItemProvider.TEMPERATURE_ITEM_CAPABILITY);
	}

	/**
	 * Gets the health capability of the given player.
	 * @param player Player
	 * @return The health capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static HealthCapability getHealthCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.HEALTH);
	}

	/**
	 * Gets the wetness capability of the given player.
	 * @param player Player
	 * @return The wetness capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static WetnessCapability getWetnessCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.WETNESS);
	}

	/**
	 * Gets the thirst capability of the given player.
	 * @param player Player
	 * @return The thirst capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static ThirstCapability getThirstCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.THIRST);
	}

	/**
	 * Gets the Food capability of the given player.
	 * @param player Player
	 * @return The food capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static FoodCapability getFoodCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.FOOD);
	}

	/**
	 * Gets the Body Damage capability of the given player.
	 * @param player Player
	 * @return The body damage capability of the given player if it exists, or a new dummy capability if it doesn't.
	 */
	public static BodyDamageCapability getBodyDamageCapability(Player player)
	{
		return player.getData(AttributeTypeRegistry.BODY_DAMAGE);
	}
}
