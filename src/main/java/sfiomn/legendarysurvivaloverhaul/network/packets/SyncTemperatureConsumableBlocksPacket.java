package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureConsumableBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableBlockListener;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureConsumableBlocksPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureConsumableBlocksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_consumable_blocks"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureConsumableBlocksPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureConsumableBlocksPacket::encode, SyncTemperatureConsumableBlocksPacket::decode);

	private final Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks;
	private final int size;

	public SyncTemperatureConsumableBlocksPacket(Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks)
	{
		this.temperatureConsumableBlocks = Map.copyOf(temperatureConsumableBlocks);
		this.size = temperatureConsumableBlocks.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureConsumableBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureConsumableBlock>> e : message.temperatureConsumableBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureConsumableBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureConsumableBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonTemperatureConsumableBlock> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureConsumableBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			temperatureConsumables.put(key, jtcList);
		}

		return new SyncTemperatureConsumableBlocksPacket(temperatureConsumables);
	}
	
	public static void handle(SyncTemperatureConsumableBlocksPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureConsumableBlockListener.acceptServerTemperatureConsumableBlocks(message.temperatureConsumableBlocks);
			});

		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureConsumableBlock>> temperatureConsumableBlocks) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureConsumableBlocksPacket(temperatureConsumableBlocks));
	}
}
