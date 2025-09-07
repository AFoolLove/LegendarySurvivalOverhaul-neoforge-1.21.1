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
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureItemsPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureItemsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_items"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureItemsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureItemsPacket::encode, SyncTemperatureItemsPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureItems;
	private final int size;

	public SyncTemperatureItemsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureItems)
	{
		this.temperatureItems = Map.copyOf(temperatureItems);
		this.size = temperatureItems.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureItems.put(key, t));
			}
		}

		return new SyncTemperatureItemsPacket(temperatureItems);
	}
	
	public static void handle(SyncTemperatureItemsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureItemListener.acceptServerTemperatureItems(message.temperatureItems);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureItems) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureItemsPacket(temperatureItems));
	}
}
