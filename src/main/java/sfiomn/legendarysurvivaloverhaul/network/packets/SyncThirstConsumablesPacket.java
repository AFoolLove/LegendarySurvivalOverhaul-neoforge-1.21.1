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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.ThirstConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SyncThirstConsumablesPacket implements CustomPacketPayload
{
	public static final Type<SyncThirstConsumablesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_thirst_consumables"));
	public static final StreamCodec<FriendlyByteBuf, SyncThirstConsumablesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncThirstConsumablesPacket::encode, SyncThirstConsumablesPacket::decode);

	private final Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables;
	private final int size;

	public SyncThirstConsumablesPacket(Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables)
	{
		this.thirstConsumables = Map.copyOf(thirstConsumables);
		this.size = thirstConsumables.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncThirstConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, List<JsonThirstConsumable>> e : message.thirstConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			buffer.writeInt(e.getValue().size());
			var r = JsonThirstConsumable.LIST_CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> ((ListTag) j).forEach(k -> buffer.writeNbt((CompoundTag) k)));
		}
	}
	
	public static SyncThirstConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			int jtcSize = buffer.readInt();
			List<JsonThirstConsumable> jtcList = new ArrayList<>();
			for (int j = 0; j < jtcSize; j++) {
				CompoundTag tag = buffer.readNbt();
				if (tag != null) {
					var r = JsonThirstConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
					r.result().ifPresent(jtcList::add);
				}
			}
			thirstConsumables.put(key, jtcList);
		}

		return new SyncThirstConsumablesPacket(thirstConsumables);
	}
	
	public static void handle(SyncThirstConsumablesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				ThirstConsumableListener.acceptServerThirstConsumables(message.thirstConsumables);
			});
		}
	}


	public static void sendTo(ServerPlayer player, Map<ResourceLocation, List<JsonThirstConsumable>> thirstConsumables) {
		PacketDistributor.sendToPlayer(player, new SyncThirstConsumablesPacket(thirstConsumables));
	}
}
