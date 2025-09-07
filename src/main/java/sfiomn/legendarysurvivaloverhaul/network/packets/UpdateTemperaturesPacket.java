package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureProvider;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

public class UpdateTemperaturesPacket implements CustomPacketPayload
{
	public static final Type<UpdateTemperaturesPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_temperatures"));
	public static final StreamCodec<FriendlyByteBuf, UpdateTemperaturesPacket> STREAM_CODEC =
			CustomPacketPayload.codec(UpdateTemperaturesPacket::encode, UpdateTemperaturesPacket::decode);

	private CompoundTag compound;
	
	public UpdateTemperaturesPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateTemperaturesPacket() {}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(UpdateTemperaturesPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateTemperaturesPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateTemperaturesPacket(buffer.readNbt());
	}
	
	public static void handle(UpdateTemperaturesPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> handle(message));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void handle(UpdateTemperaturesPacket message) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player != null) {
			TemperatureCapability temperature = CapabilityUtil.getTempCapability(player);
			temperature.readNBT(message.compound);
		}
	}

	public static void sendTo(ServerPlayer player, Tag compound) {
		PacketDistributor.sendToPlayer(player, new UpdateTemperaturesPacket(compound));
	}
}
