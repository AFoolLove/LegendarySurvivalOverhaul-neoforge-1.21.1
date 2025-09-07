package sfiomn.legendarysurvivaloverhaul.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import sfiomn.legendarysurvivaloverhaul.network.packets.*;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static PayloadRegistrar INSTANCE;

	public static void register(RegisterPayloadHandlersEvent event)
	{
		INSTANCE = event.registrar(PROTOCOL_VERSION);

		INSTANCE.playBidirectional(UpdateTemperaturesPacket.TYPE, UpdateTemperaturesPacket.STREAM_CODEC, UpdateTemperaturesPacket::handle);
		INSTANCE.playBidirectional(UpdateWetnessPacket.TYPE, UpdateWetnessPacket.STREAM_CODEC, UpdateWetnessPacket::handle);
		INSTANCE.playBidirectional(UpdateThirstPacket.TYPE, UpdateThirstPacket.STREAM_CODEC, UpdateThirstPacket::handle);
		INSTANCE.playBidirectional(UpdateHeartsPacket.TYPE, UpdateHeartsPacket.STREAM_CODEC, UpdateHeartsPacket::handle);
		INSTANCE.playBidirectional(UpdateBodyDamagePacket.TYPE, UpdateBodyDamagePacket.STREAM_CODEC, UpdateBodyDamagePacket::handle);
		INSTANCE.playToServer(DrinkBlockFluidMessage.TYPE, DrinkBlockFluidMessage.STREAM_CODEC, DrinkBlockFluidMessage::handle);
		INSTANCE.playBidirectional(BodyPartHealingTimeMessage.TYPE, BodyPartHealingTimeMessage.STREAM_CODEC, BodyPartHealingTimeMessage::handle);

		INSTANCE.playBidirectional(SyncTemperatureConsumablesPacket.TYPE, SyncTemperatureConsumablesPacket.STREAM_CODEC, SyncTemperatureConsumablesPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureConsumableBlocksPacket.TYPE, SyncTemperatureConsumableBlocksPacket.STREAM_CODEC, SyncTemperatureConsumableBlocksPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureBlocksPacket.TYPE, SyncTemperatureBlocksPacket.STREAM_CODEC, SyncTemperatureBlocksPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureItemsPacket.TYPE, SyncTemperatureItemsPacket.STREAM_CODEC, SyncTemperatureItemsPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureBiomesPacket.TYPE, SyncTemperatureBiomesPacket.STREAM_CODEC, SyncTemperatureBiomesPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureFuelItemsPacket.TYPE, SyncTemperatureFuelItemsPacket.STREAM_CODEC, SyncTemperatureFuelItemsPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureMountsPacket.TYPE, SyncTemperatureMountsPacket.STREAM_CODEC, SyncTemperatureMountsPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureDimensionsPacket.TYPE, SyncTemperatureDimensionsPacket.STREAM_CODEC, SyncTemperatureDimensionsPacket::handle);
		INSTANCE.playBidirectional(SyncTemperatureOriginsPacket.TYPE, SyncTemperatureOriginsPacket.STREAM_CODEC, SyncTemperatureOriginsPacket::handle);

		INSTANCE.playBidirectional(SyncThirstBlocksPacket.TYPE, SyncThirstBlocksPacket.STREAM_CODEC, SyncThirstBlocksPacket::handle);
		INSTANCE.playBidirectional(SyncThirstConsumablesPacket.TYPE, SyncThirstConsumablesPacket.STREAM_CODEC, SyncThirstConsumablesPacket::handle);

		INSTANCE.playBidirectional(SyncBodyDamageHealingConsumablesPacket.TYPE, SyncBodyDamageHealingConsumablesPacket.STREAM_CODEC, SyncBodyDamageHealingConsumablesPacket::handle);
		INSTANCE.playBidirectional(SyncBodyPartsDamageSourcesPacket.TYPE, SyncBodyPartsDamageSourcesPacket.STREAM_CODEC, SyncBodyPartsDamageSourcesPacket::handle);
		INSTANCE.playBidirectional(SyncBodyPartResistanceItemsPacket.TYPE, SyncBodyPartResistanceItemsPacket.STREAM_CODEC, SyncBodyPartResistanceItemsPacket::handle);
	}
}