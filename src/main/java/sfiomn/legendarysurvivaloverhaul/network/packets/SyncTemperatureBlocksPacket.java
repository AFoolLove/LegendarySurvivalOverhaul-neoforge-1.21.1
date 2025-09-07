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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBlockListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureBlocksPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureBlocksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_blocks"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureBlocksPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureBlocksPacket::encode, SyncTemperatureBlocksPacket::decode);

	private final Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks;
	private final int size;

	public SyncTemperatureBlocksPacket(Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks)
	{
		this.temperatureBlocks = Map.copyOf(temperatureBlocks);
		this.size = temperatureBlocks.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonTemperatureBlock>> e : message.temperatureBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonTemperatureBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncTemperatureBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtbSize = buffer.readInt();
			List<JsonTemperatureBlock> jtbList = new ArrayList<>();
			for (int j = 0; j < jtbSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonTemperatureBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtbList::add);
				}
			}
			temperatureBlocks.put(key, jtbList);
		}
		return new SyncTemperatureBlocksPacket(temperatureBlocks);
	}
	
	public static void handle(SyncTemperatureBlocksPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureBlockListener.acceptServerTemperatureBlocks(message.temperatureBlocks);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, List<JsonTemperatureBlock>> temperatureBlocks) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureBlocksPacket(temperatureBlocks));
	}
}
