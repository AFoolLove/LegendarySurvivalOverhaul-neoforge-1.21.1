package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureOriginListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureOriginsPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureOriginsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_origins"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureOriginsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureOriginsPacket::encode, SyncTemperatureOriginsPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins;
	private final int size;

	public SyncTemperatureOriginsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins)
	{
		this.temperatureOrigins = Map.copyOf(temperatureOrigins);
		this.size = temperatureOrigins.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureOriginsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureOrigins.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureOriginsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureOrigins.put(key, t));
			}
		}

		return new SyncTemperatureOriginsPacket(temperatureOrigins);
	}
	
	public static void handle(SyncTemperatureOriginsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureOriginListener.acceptServerTemperatureOrigins(message.temperatureOrigins);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureOrigins) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureOriginsPacket(temperatureOrigins));
	}
}
