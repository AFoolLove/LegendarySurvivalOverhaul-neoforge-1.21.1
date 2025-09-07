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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessProvider;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

public class UpdateWetnessPacket implements CustomPacketPayload
{
	public static final Type<UpdateWetnessPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_wetness"));
	public static final StreamCodec<FriendlyByteBuf, UpdateWetnessPacket> STREAM_CODEC =
			CustomPacketPayload.codec(UpdateWetnessPacket::encode, UpdateWetnessPacket::decode);

	private CompoundTag compound;
	
	public UpdateWetnessPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateWetnessPacket() {}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(UpdateWetnessPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateWetnessPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateWetnessPacket(buffer.readNbt());
	}
	
	public static void handle(UpdateWetnessPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> handle(message));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void handle(UpdateWetnessPacket message) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player != null) {
			WetnessCapability wetness = CapabilityUtil.getWetnessCapability(player);
			wetness.readNBT(message.compound);
		}
	}

	public static void sendTo(ServerPlayer player, Tag compound) {
		PacketDistributor.sendToPlayer(player, new UpdateWetnessPacket(compound));
	}
}
