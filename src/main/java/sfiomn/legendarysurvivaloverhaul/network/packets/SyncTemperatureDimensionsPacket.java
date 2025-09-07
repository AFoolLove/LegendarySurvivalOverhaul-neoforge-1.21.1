package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureDimension;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureDimensionListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureDimensionsPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureDimensionsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_dimensions"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureDimensionsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureDimensionsPacket::encode, SyncTemperatureDimensionsPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions;
	private final int size;

	public SyncTemperatureDimensionsPacket(Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions)
	{
		this.temperatureDimensions = Map.copyOf(temperatureDimensions);
		this.size = temperatureDimensions.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureDimensionsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureDimension> e : message.temperatureDimensions.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureDimension.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureDimensionsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureDimension.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureDimensions.put(key, t));
			}
		}

		return new SyncTemperatureDimensionsPacket(temperatureDimensions);
	}
	
	public static void handle(SyncTemperatureDimensionsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureDimensionListener.acceptServerTemperatureDimensions(message.temperatureDimensions);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureDimension> temperatureDimensions) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureDimensionsPacket(temperatureDimensions));
	}
}
