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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureFuelItem;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureFuelItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureFuelItemsPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureFuelItemsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_fuel_items"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureFuelItemsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureFuelItemsPacket::encode, SyncTemperatureFuelItemsPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems;
	private final int size;

	public SyncTemperatureFuelItemsPacket(Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems)
	{
		this.temperatureFuelItems = Map.copyOf(temperatureFuelItems);
		this.size = temperatureFuelItems.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureFuelItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureFuelItem> e : message.temperatureFuelItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureFuelItem.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureFuelItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureFuelItem.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureFuelItems.put(key, t));
			}
		}

		return new SyncTemperatureFuelItemsPacket(temperatureFuelItems);
	}
	
	public static void handle(SyncTemperatureFuelItemsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureFuelItemListener.acceptServerTemperatureFuelItems(message.temperatureFuelItems);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureFuelItem> temperatureFuelItems) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureFuelItemsPacket(temperatureFuelItems));
	}
}
