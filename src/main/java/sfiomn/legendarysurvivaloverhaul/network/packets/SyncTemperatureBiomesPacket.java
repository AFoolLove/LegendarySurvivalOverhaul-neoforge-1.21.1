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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureBiomeOverride;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureBiomeListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureBiomesPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureBiomesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_biomes"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureBiomesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureBiomesPacket::encode, SyncTemperatureBiomesPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes;
	private final int size;

	public SyncTemperatureBiomesPacket(Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes)
	{
		this.temperatureBiomes = Map.copyOf(temperatureBiomes);
		this.size = temperatureBiomes.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureBiomesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureBiomeOverride> e : message.temperatureBiomes.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureBiomeOverride.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureBiomesPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureBiomeOverride.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureBiomes.put(key, t));
			}
		}

		return new SyncTemperatureBiomesPacket(temperatureBiomes);
	}
	
	public static void handle(SyncTemperatureBiomesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureBiomeListener.acceptServerTemperatureBiomes(message.temperatureBiomes);
			});
		}
	}
	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureBiomeOverride> temperatureBiomes) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureBiomesPacket(temperatureBiomes));
	}
}
