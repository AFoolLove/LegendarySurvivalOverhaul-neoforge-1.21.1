package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.capabilities.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
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
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;
import sfiomn.legendarysurvivaloverhaul.registry.BlockEntityRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.ItemRegistry;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModCapabilities
{

	public static final ResourceLocation TEMPERATURE_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature");
	public static final ResourceLocation WETNESS_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "wetness");
	public static final ResourceLocation THIRST_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirst");
	public static final ResourceLocation HEALTH_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "health");
	public static final ResourceLocation FOOD_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "food");
	public static final ResourceLocation BODY_DAMAGE_RES = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_damage");

	public static final EntityCapability<BodyDamageCapability, Void> BODY_DAMAGE_CAPABILITY = EntityCapability.createVoid(BODY_DAMAGE_RES, BodyDamageCapability.class);
	public static final EntityCapability<FoodCapability, Void> FOOD_CAPABILITY = EntityCapability.createVoid(FOOD_RES, FoodCapability.class);
	public static final EntityCapability<ThirstCapability, Void> THIRST_CAPABILITY = EntityCapability.createVoid(THIRST_RES, ThirstCapability.class);
	public static final EntityCapability<WetnessCapability, Void> WETNESS_CAPABILITY = EntityCapability.createVoid(WETNESS_RES, WetnessCapability.class);
	public static final EntityCapability<HealthCapability, Void> HEALTH_CAPABILITY = EntityCapability.createVoid(HEALTH_RES, HealthCapability.class);
	public static final EntityCapability<TemperatureCapability, Void> TEMPERATURE_CAPABILITY = EntityCapability.createVoid(TEMPERATURE_RES, TemperatureCapability.class);

	public static void attachCapabilityPlayer(RegisterCapabilitiesEvent event)
	{
		event.registerEntity(BODY_DAMAGE_CAPABILITY, EntityType.PLAYER, new BodyDamageProvider());
		event.registerEntity(FOOD_CAPABILITY, EntityType.PLAYER, new FoodProvider());
		event.registerEntity(THIRST_CAPABILITY, EntityType.PLAYER, new ThirstProvider());
		event.registerEntity(WETNESS_CAPABILITY, EntityType.PLAYER, new WetnessProvider());
		event.registerEntity(HEALTH_CAPABILITY, EntityType.PLAYER, new HealthProvider());
		event.registerEntity(TEMPERATURE_CAPABILITY, EntityType.PLAYER, new TemperatureProvider());

		// Must register at least one item
		event.registerItem(TemperatureItemCapability.TemperatureItemProvider.TEMPERATURE_ITEM_CAPABILITY, new TemperatureItemCapability.TemperatureItemProvider(), ItemRegistry.THERMOMETER);

		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityRegistry.HEATER_BLOCK_ENTITY.get(), (entity, context) -> new InvWrapper(entity));
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityRegistry.COOLER_BLOCK_ENTITY.get(), (entity, context) -> new InvWrapper(entity));
	}

	@SubscribeEvent
	public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
		onPlayerTick(event);
	}

	@SubscribeEvent
	public static void onPlayerTickPost(PlayerTickEvent.Post event) {
		onPlayerTick(event);
	}

	public static void onPlayerTick(PlayerTickEvent event)
	{
		Player player = event.getEntity();
		Level level = player.level();

		if (level.isClientSide())
		{
			// Client Side
			if (shouldSkipTick(player)) return;

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);

				tempCap.tickClient(player, event);
			}
		}
		else
		{
			// Server Side

			if (shouldSkipTick(player)) return;

			if (!Config.Baked.vanillaFreezeEnabled) {
				if (player.getTicksFrozen() > 0)
					player.setTicksFrozen(0);
			}

			if (Config.Baked.temperatureEnabled) {
				TemperatureCapability tempCap = CapabilityUtil.getTempCapability(player);

				tempCap.tickUpdate(player, level, event);

				if(event instanceof PlayerTickEvent.Pre && (tempCap.isDirty() || tempCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					tempCap.setClean();
					sendTemperatureUpdate(player);
				}
			}

			if (Config.Baked.wetnessEnabled) {
				WetnessCapability wetCap = CapabilityUtil.getWetnessCapability(player);

				wetCap.tickUpdate(player, level, event);

				/**
				 * Because of the way wetness is ticked, if it's dirty, it's probably going to be dirty next tick,
				 * and if it's clean, it's probably going to be clean the next tick
				 * Thus, we don't want to clean up the wetness capability every single tick
				 * just because the player is standing out in the rain
				 * since it's not good for performance
				 */
				if (event instanceof PlayerTickEvent.Pre && (wetCap.getPacketTimer() % Config.Baked.routinePacketSync == 0 || wetCap.isDirty()))
				{
					wetCap.setClean();
					sendWetnessUpdate(player);
				}
			}

			if (Config.Baked.thirstEnabled) {
				ThirstCapability thirstCap = CapabilityUtil.getThirstCapability(player);

				thirstCap.tickUpdate(player, level, event);

				if (event instanceof PlayerTickEvent.Pre && (thirstCap.isDirty() || thirstCap.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					thirstCap.setClean();
					sendThirstUpdate(player);
				}
			}

			if (Config.Baked.baseFoodExhaustion > 0) {
				FoodCapability foodCapability = CapabilityUtil.getFoodCapability(player);

				foodCapability.tickUpdate(player, level, event);
			}

			if (Config.Baked.localizedBodyDamageEnabled) {
				BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);

				bodyDamageCapability.tickUpdate(player, level, event);

				if(event instanceof PlayerTickEvent.Pre && (bodyDamageCapability.isDirty() || bodyDamageCapability.getPacketTimer() % Config.Baked.routinePacketSync == 0))
				{
					bodyDamageCapability.setClean();
					sendBodyDamageUpdate(player);
				}
			}

			if (Config.Baked.healthOverhaulEnabled) {
				HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

				if(event instanceof PlayerTickEvent.Pre && healthCapability.isDirty())
				{
					healthCapability.setClean();
					sendHealthUpdate(player);
				}
			}
		}
	}

	@SubscribeEvent
	public static void deathHandler(PlayerEvent.Clone event)
	{
		Player orig = event.getOriginal();
		Player player = event.getEntity();

		if (event.isWasDeath())
		{
            if (Config.Baked.localizedBodyDamageEnabled && Config.Baked.healthOverhaulEnabled) {
                BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
            }

			if (Config.Baked.temperatureEnabled)
				player.getPersistentData().putBoolean("tempImmuneOnSpawn", orig.getPersistentData().getBoolean("tempImmuneOnSpawn"));

			if (Config.Baked.healthOverhaulEnabled)
			{
//				orig.reviveCaps();
				HealthCapability oldCap = CapabilityUtil.getHealthCapability(orig);
//				orig.invalidateCaps();

				HealthCapability newCap = CapabilityUtil.getHealthCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				HealthUtil.initializeHealthAttributes(player);

				if (Config.Baked.heartsLostOnDeath > 0)
					HealthUtil.loseHearth(player, Config.Baked.heartsLostOnDeath);
				else
					HealthUtil.updatePlayerMaxHealthAttribute(player);

				player.setHealth(player.getMaxHealth());
			}
		}
		else
		{
			if (Config.Baked.temperatureEnabled)
			{
//				orig.reviveCaps();
				TemperatureCapability oldCap = CapabilityUtil.getTempCapability(orig);
//				orig.invalidateCaps();

				TemperatureCapability newCap = CapabilityUtil.getTempCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendTemperatureUpdate(player);
			}

			if (Config.Baked.wetnessEnabled)
			{
//				orig.reviveCaps();
				WetnessCapability oldCap = CapabilityUtil.getWetnessCapability(orig);
//				orig.invalidateCaps();

				WetnessCapability newCap = CapabilityUtil.getWetnessCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendWetnessUpdate(player);
			}

			if (Config.Baked.thirstEnabled)
			{
//				orig.reviveCaps();
				ThirstCapability oldCap = CapabilityUtil.getThirstCapability(orig);
//				orig.invalidateCaps();

				ThirstCapability newCap = CapabilityUtil.getThirstCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				sendThirstUpdate(player);
			}
			
			if (Config.Baked.healthOverhaulEnabled)
			{
//				orig.reviveCaps();
				HealthCapability oldCap = CapabilityUtil.getHealthCapability(orig);
//				orig.invalidateCaps();

				HealthCapability newCap = CapabilityUtil.getHealthCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				HealthUtil.initializeHealthAttributes(player);
				HealthUtil.updatePlayerMaxHealthAttribute(player);
				player.setHealth(player.getMaxHealth());
				sendHealthUpdate(player);
			}

			if (Config.Baked.localizedBodyDamageEnabled)
			{
//				orig.reviveCaps();
				BodyDamageCapability oldCap = CapabilityUtil.getBodyDamageCapability(orig);
//				orig.invalidateCaps();

				BodyDamageCapability newCap = CapabilityUtil.getBodyDamageCapability(player);
				newCap.readNBT(oldCap.writeNBT());

				BodyDamageUtil.updatePlayerBrokenHeartAttribute(player);
				sendBodyDamageUpdate(player);
			}
		}
	}

	private static void sendTemperatureUpdate(Player player)
	{
		if (!player.level().isClientSide())
		{
			UpdateTemperaturesPacket.sendTo((ServerPlayer) player,
					CapabilityUtil.getTempCapability(player).writeNBT());
		}
	}

	private static void sendWetnessUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateWetnessPacket.sendTo((ServerPlayer) player,
					CapabilityUtil.getWetnessCapability(player).writeNBT());
		}
	}

	private static void sendThirstUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateThirstPacket.sendTo((ServerPlayer) player,
					CapabilityUtil.getThirstCapability(player).writeNBT());
		}
	}

	private static void sendBodyDamageUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateBodyDamagePacket.sendTo((ServerPlayer) player,
					CapabilityUtil.getBodyDamageCapability(player).writeNBT());
		}
	}

	private static void sendHealthUpdate(Player player)
	{
		if (!player.level().isClientSide)
		{
			UpdateHeartsPacket.sendTo((ServerPlayer) player,
					CapabilityUtil.getHealthCapability(player).writeNBT());
		}
	}

	@SubscribeEvent
	public static void syncCapsOnDimensionChange(PlayerChangedDimensionEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.healthOverhaulEnabled)
			sendHealthUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}

	@SubscribeEvent
	public static void syncCapsOnLogin(PlayerLoggedInEvent event)
	{
		Player player = event.getEntity();
		if (Config.Baked.temperatureEnabled)
			sendTemperatureUpdate(player);
		if (Config.Baked.wetnessEnabled)
			sendWetnessUpdate(player);
		if (Config.Baked.thirstEnabled)
			sendThirstUpdate(player);
		if (Config.Baked.healthOverhaulEnabled)
			sendHealthUpdate(player);
		if (Config.Baked.localizedBodyDamageEnabled)
			sendBodyDamageUpdate(player);
	}
	
	protected static boolean shouldSkipTick(Player player)
	{
		return player.isCreative() || player.isSpectator();
	}
}
