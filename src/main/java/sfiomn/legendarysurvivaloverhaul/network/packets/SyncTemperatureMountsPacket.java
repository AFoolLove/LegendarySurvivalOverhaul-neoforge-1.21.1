package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.nbt.CompoundTag;
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
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperature;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonTemperatureResistance;
import sfiomn.legendarysurvivaloverhaul.common.listeners.TemperatureMountListener;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SyncTemperatureMountsPacket implements CustomPacketPayload
{
	public static final Type<SyncTemperatureMountsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "sync_temperature_mounts"));
	public static final StreamCodec<FriendlyByteBuf, SyncTemperatureMountsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(SyncTemperatureMountsPacket::encode, SyncTemperatureMountsPacket::decode);

	private final Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts;
	private final int size;

	public SyncTemperatureMountsPacket(Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts)
	{
		this.temperatureMounts = Map.copyOf(temperatureMounts);
		this.size = temperatureMounts.size();
	}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(SyncTemperatureMountsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeInt(message.size);
		for (Map.Entry<ResourceLocation, JsonTemperatureResistance> e : message.temperatureMounts.entrySet()) {
			buffer.writeResourceLocation(e.getKey());
			var r = JsonTemperatureResistance.CODEC.encodeStart(NbtOps.INSTANCE, e.getValue());
			r.result().ifPresent(j -> buffer.writeNbt((CompoundTag) j));
		}
	}
	
	public static SyncTemperatureMountsPacket decode(FriendlyByteBuf buffer)
	{
		int size = buffer.readInt();
		Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts = new HashMap<>();
		for (int i = 0; i < size; i++) {
			ResourceLocation key = buffer.readResourceLocation();
			CompoundTag tag = buffer.readNbt();
			if (tag != null) {
				var r = JsonTemperatureResistance.CODEC.parse(NbtOps.INSTANCE, tag);
				r.result().ifPresent(t -> temperatureMounts.put(key, t));
			}
		}

		return new SyncTemperatureMountsPacket(temperatureMounts);
	}

	public static void handle(SyncTemperatureMountsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> {
				TemperatureMountListener.acceptServerTemperatureMounts(message.temperatureMounts);
			});
		}
	}

	public static void sendTo(ServerPlayer player, Map<ResourceLocation, JsonTemperatureResistance> temperatureMounts) {
		PacketDistributor.sendToPlayer(player, new SyncTemperatureMountsPacket(temperatureMounts));
	}
}
