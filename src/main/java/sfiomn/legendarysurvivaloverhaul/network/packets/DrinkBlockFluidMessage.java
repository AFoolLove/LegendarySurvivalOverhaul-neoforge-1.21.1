package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;

import java.util.function.Supplier;

public class DrinkBlockFluidMessage implements CustomPacketPayload
{
    // CLIENT to SERVER side message
    public static final Type<DrinkBlockFluidMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "drink_block_fluid"));
    public static final StreamCodec<FriendlyByteBuf, DrinkBlockFluidMessage> STREAM_CODEC =
            CustomPacketPayload.codec(DrinkBlockFluidMessage::encode, DrinkBlockFluidMessage::decode);

    public DrinkBlockFluidMessage()
    {
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(DrinkBlockFluidMessage message, FriendlyByteBuf buffer)
    {
    }

    public static DrinkBlockFluidMessage decode(FriendlyByteBuf buffer)
    {
        return new DrinkBlockFluidMessage();
    }

    public static void handle(DrinkBlockFluidMessage message, IPayloadContext context)
    {
        if (context.flow().isServerbound() && context.player() instanceof ServerPlayer player){
            context.enqueueWork(() -> DrinkWaterOnServer(player));
        }
    }

    public static void DrinkWaterOnServer(ServerPlayer player) {
        JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, player.blockInteractionRange() / 2);

        if (jsonFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
    }

    public static void sendToServer() {
        DrinkBlockFluidMessage messageDrinkToServer = new DrinkBlockFluidMessage();
        PacketDistributor.sendToServer(messageDrinkToServer);
    }
}
