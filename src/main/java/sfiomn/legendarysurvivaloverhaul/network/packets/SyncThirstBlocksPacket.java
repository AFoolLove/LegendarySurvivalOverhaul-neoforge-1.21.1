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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstBlockListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncThirstBlocksPacket implements CustomPacketPayload
{
	public static final Type<SyncThirstBlocksPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_thirst_blocks"));
	public static final StreamCodec<FriendlyByteBuf, SyncThirstBlocksPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncThirstBlocksPacket::encode, SyncThirstBlocksPacket::decode);

	private final Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks;
	private final int size;

	public SyncThirstBlocksPacket(Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks)
	{
		this.thirstBlocks = Map.copyOf(thirstBlocks);
		this.size = thirstBlocks.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncThirstBlocksPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonThirstBlock>> e : message.thirstBlocks.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonThirstBlock.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncThirstBlocksPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtbSize = buffer.readInt();
			List<JsonThirstBlock> jtbList = new ArrayList<>();
			for (int j = 0; j < jtbSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonThirstBlock.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtbList::add);
				}
			}
			thirstBlocks.put(key, jtbList);
		}

		return new SyncThirstBlocksPacket(thirstBlocks);
	}
	
	public static void handle(SyncThirstBlocksPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				ThirstBlockListener.acceptServerThirstBlocks(message.thirstBlocks);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, List<JsonThirstBlock>> thirstBlocks) {
		PacketDistributor.sendToPlayer(player, new SyncThirstBlocksPacket(thirstBlocks));
	}
}
