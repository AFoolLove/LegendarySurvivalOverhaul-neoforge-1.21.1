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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class UpdateHeartsPacket implements CustomPacketPayload
{
	public static final Type<UpdateHeartsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_hearts"));
	public static final StreamCodec<FriendlyByteBuf, UpdateHeartsPacket> STREAM_CODEC =
			CustomPacketPayload.codec(UpdateHeartsPacket::encode, UpdateHeartsPacket::decode);

	private CompoundTag compound;
	
	public UpdateHeartsPacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}
	
	public UpdateHeartsPacket() {}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(UpdateHeartsPacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateHeartsPacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateHeartsPacket(buffer.readNbt());
	}
	
	public static void handle(UpdateHeartsPacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> handle(message));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private static void handle(UpdateHeartsPacket message) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player != null) {
			HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);
			healthCapability.readNBT(message.compound);
		}
	}


	public static void sendTo(ServerPlayer player, Tag compound) {
		PacketDistributor.sendToPlayer(player, new UpdateHeartsPacket(compound));
	}
}
