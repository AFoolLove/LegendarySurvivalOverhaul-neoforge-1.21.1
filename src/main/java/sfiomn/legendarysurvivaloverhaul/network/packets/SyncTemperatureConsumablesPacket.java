package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureConsumablesPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureConsumablesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumables"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureConsumablesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureConsumablesPacket::encode, SyncTemperatureConsumablesPacket::decode);

	private final Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables;
	private final int size;

	public SyncTemperatureConsumablesPacket(Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables)
	{
		this.temperatureConsumables = Map.copyOf(temperatureConsumables);
		this.size = temperatureConsumables.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumable>> e : message.temperatureConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonTemperatureConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			temperatureConsumables.put(key, jtcList);
		}

		return new SyncTemperatureConsumablesPacket(temperatureConsumables);
	}
	
	public static void handle(SyncTemperatureConsumablesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureConsumableListener.acceptServerTemperatureConsumables(message.temperatureConsumables);
			});
		}
	}
	public static void sendTo(ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumable>> temperatureConsumables) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureConsumablesPacket(temperatureConsumables));
	}
}
