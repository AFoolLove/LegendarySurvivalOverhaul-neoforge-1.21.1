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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonBodyPartsDamageSource;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyPartsDamageSourceListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyPartsDamageSourcesPacket implements CustomPacketPayload
{
	public static final Type<SyncBodyPartsDamageSourcesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_parts_damage_sources"));
	public static final StreamCodec<FriendlyByteBuf, SyncBodyPartsDamageSourcesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncBodyPartsDamageSourcesPacket::encode, SyncBodyPartsDamageSourcesPacket::decode);

	private final Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources;
	private final int size;

	public SyncBodyPartsDamageSourcesPacket(Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources)
	{
		this.damageSources = Map.copyOf(damageSources);
		this.size = damageSources.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncBodyPartsDamageSourcesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonBodyPartsDamageSource> e : message.damageSources.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonBodyPartsDamageSource.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyPartsDamageSourcesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonBodyPartsDamageSource.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> damageSources.put(key, t));
			}
		}

		return new SyncBodyPartsDamageSourcesPacket(damageSources);
	}
	
	public static void handle(SyncBodyPartsDamageSourcesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				BodyPartsDamageSourceListener.acceptServerDamageSources(message.damageSources);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonBodyPartsDamageSource> damageSources) {
		PacketDistributor.sendToPlayer(player, new SyncBodyPartsDamageSourcesPacket(damageSources));
	}
}
