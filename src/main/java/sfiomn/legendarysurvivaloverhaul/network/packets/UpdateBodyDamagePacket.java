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
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.function.Supplier;

public class UpdateBodyDamagePacket implements CustomPacketPayload
{
	public static final Type<UpdateBodyDamagePacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "update_body_damage"));
	public static final StreamCodec<FriendlyByteBuf, UpdateBodyDamagePacket> STREAM_CODEC =
			CustomPacketPayload.codec(UpdateBodyDamagePacket::encode, UpdateBodyDamagePacket::decode);

	private CompoundTag compound;

	public UpdateBodyDamagePacket(Tag compound)
	{
		this.compound = (CompoundTag) compound;
	}

	public UpdateBodyDamagePacket() {}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void encode(UpdateBodyDamagePacket message, FriendlyByteBuf buffer)
	{
		buffer.writeNbt(message.compound);
	}
	
	public static UpdateBodyDamagePacket decode(FriendlyByteBuf buffer)
	{
		return new UpdateBodyDamagePacket(buffer.readNbt());
	}
	
	public static void handle(UpdateBodyDamagePacket message, IPayloadContext context)
	{
		if (context.flow().isClientbound()) {
			context.enqueueWork(() -> handle(message));
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void handle(UpdateBodyDamagePacket message) {
		LocalPlayer player = Minecraft.getInstance().player;

		if (player != null) {
			BodyDamageCapability bodyDamageCapability = CapabilityUtil.getBodyDamageCapability(player);
			bodyDamageCapability.readNBT(message.compound);
		}
	}

	public static void sendTo(ServerPlayer player, Tag compound) {
		PacketDistributor.sendToPlayer(player, new UpdateBodyDamagePacket(compound));
	}
}
