package sfiomn.legendarysurvivaloverhaul.network.packets;

import com.mojang.serialization.Codec;
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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartResistanceItemListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyPartResistanceItemsPacket implements CustomPacketPayload
{
	public static final Type<SyncBodyPartResistanceItemsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_part_resistance_items"));
	public static final StreamCodec<FriendlyByteBuf, SyncBodyPartResistanceItemsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncBodyPartResistanceItemsPacket::encode, SyncBodyPartResistanceItemsPacket::decode);

	private final Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems;
	private final int size;

	public SyncBodyPartResistanceItemsPacket(Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems)
	{
		this.bodyPartResistanceItems = Map.copyOf(bodyPartResistanceItems);
		this.size = bodyPartResistanceItems.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncBodyPartResistanceItemsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartResistance> e : message.bodyPartResistanceItems.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartResistanceItemsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> bodyPartResistanceItems.put(key, t));
			}
		}

		return new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems);
	}
	
	public static void handle(SyncBodyPartResistanceItemsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				BodyPartResistanceItemListener.acceptServerBodyPartResistanceItems(message.bodyPartResistanceItems);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonBodyPartResistance> bodyPartResistanceItems) {
		PacketDistributor.sendToPlayer(player, new SyncBodyPartResistanceItemsPacket(bodyPartResistanceItems));
	}
}
