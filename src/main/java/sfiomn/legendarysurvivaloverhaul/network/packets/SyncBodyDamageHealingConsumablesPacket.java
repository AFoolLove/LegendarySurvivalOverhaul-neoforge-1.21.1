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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.common.listeners.BodyDamageHealingConsumableListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncBodyDamageHealingConsumablesPacket implements CustomPacketPayload
{
	public static final Type<SyncBodyDamageHealingConsumablesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_body_damage_healing_consumables"));
	public static final StreamCodec<FriendlyByteBuf, SyncBodyDamageHealingConsumablesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncBodyDamageHealingConsumablesPacket::encode, SyncBodyDamageHealingConsumablesPacket::decode);

	private final Map<ResourceLocation, JsonHealingConsumable> healingConsumables;
	private final int size;

	public SyncBodyDamageHealingConsumablesPacket(Map<ResourceLocation, JsonHealingConsumable> healingConsumables)
	{
		this.healingConsumables = Map.copyOf(healingConsumables);
		this.size = healingConsumables.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncBodyDamageHealingConsumablesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonHealingConsumable> e : message.healingConsumables.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonHealingConsumable.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncBodyDamageHealingConsumablesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonHealingConsumable> healingConsumables = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonHealingConsumable.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> healingConsumables.put(key, t));
			}
		}

		return new SyncBodyDamageHealingConsumablesPacket(healingConsumables);
	}
	
	public static void handle(SyncBodyDamageHealingConsumablesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				BodyDamageHealingConsumableListener.acceptServerHealingConsumables(message.healingConsumables);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonHealingConsumable> healingConsumables) {
		PacketDistributor.sendToPlayer(player, new SyncBodyDamageHealingConsumablesPacket(healingConsumables));
	}
}
