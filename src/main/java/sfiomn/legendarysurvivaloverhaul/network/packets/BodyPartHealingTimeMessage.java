package sfiomn.legendarysurvivaloverhaul.network.packets;

import com.google.common.graph.Network;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonHealingConsumable;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.common.integration.supplementaries.SupplementariesUtil;
import sfiomn.legendarysurvivaloverhaul.common.items.heal.BodyHealingItem;
import sfiomn.legendarysurvivaloverhaul.network.NetworkHandler;
import sfiomn.legendarysurvivaloverhaul.registry.MobEffectRegistry;
import sfiomn.legendarysurvivaloverhaul.registry.SoundRegistry;

import java.util.function.Supplier;

public class BodyPartHealingTimeMessage implements CustomPacketPayload
{
    public static final Type<BodyPartHealingTimeMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_part_healing_time"));
    public static final StreamCodec<FriendlyByteBuf, BodyPartHealingTimeMessage> STREAM_CODEC =
            CustomPacketPayload.codec(BodyPartHealingTimeMessage::encode, BodyPartHealingTimeMessage::decode);

    private CompoundTag compound;
    // CLIENT to SERVER side message

    public BodyPartHealingTimeMessage(BodyPartEnum bodyPart, String healingItem, InteractionHand hand, boolean consumeItem, boolean applyEffect)
    {
        CompoundTag bodyPartHealNbt = new CompoundTag();
        bodyPartHealNbt.putString("bodyPartEnum", bodyPart.name());
        bodyPartHealNbt.putString("healingItem", healingItem);
        bodyPartHealNbt.putBoolean("mainHand", hand == InteractionHand.MAIN_HAND);
        bodyPartHealNbt.putBoolean("consumeItem", consumeItem);
        bodyPartHealNbt.putBoolean("applyEffect", applyEffect);
        this.compound = bodyPartHealNbt;
    }

    public BodyPartHealingTimeMessage(Tag nbt) {
        this.compound = (CompoundTag) nbt;
    }

    public BodyPartHealingTimeMessage() {}

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void encode(BodyPartHealingTimeMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.compound);
    }

    public static BodyPartHealingTimeMessage decode(FriendlyByteBuf buffer)
    {
        return new BodyPartHealingTimeMessage(buffer.readNbt());
    }

    public static void handle(BodyPartHealingTimeMessage message, IPayloadContext context)
    {
        if (context.flow().isServerbound() && context.player() instanceof ServerPlayer player) {
            context.enqueueWork(() -> applyHealingItemOnServer(player, message.compound));
        }
    }

    public static void applyHealingItemOnServer(ServerPlayer player, CompoundTag nbt) {
        BodyPartEnum bodyPartEnum = BodyPartEnum.valueOf(nbt.getString("bodyPartEnum"));
        String healingItem = nbt.getString("healingItem");
        InteractionHand hand = nbt.getBoolean("mainHand") ? InteractionHand.MAIN_HAND: InteractionHand.OFF_HAND;
        boolean shouldConsume = nbt.getBoolean("consumeItem");
        boolean shouldApplyEffect = nbt.getBoolean("applyEffect");

        ItemStack usedItemStack = player.getItemInHand(hand);
        if (LegendarySurvivalOverhaul.supplementariesLoaded) {
            ItemStack itemStackInBasket = SupplementariesUtil.getSelectedItemInLunchBasket(player.getItemInHand(hand));
            if (itemStackInBasket != ItemStack.EMPTY)
                usedItemStack = itemStackInBasket;
        }

        ResourceLocation itemStackRegistryName = ResourceLocation.parse(healingItem);
        JsonHealingConsumable jhc = BodyDamageDataManager.getHealingItem(itemStackRegistryName);

        player.serverLevel().playSound(null, player, SoundRegistry.HEAL_BODY_PART.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        if (shouldConsume && !player.isCreative())
            usedItemStack.shrink(1);

        if (jhc != null) {
            if (shouldApplyEffect)
                player.addEffect(new MobEffectInstance(MobEffectRegistry.RECOVERY, jhc.recoveryEffectDuration, jhc.recoveryEffectAmplifier, false, false, true));
            BodyDamageUtil.applyHealingTimeBodyPart(player, bodyPartEnum, jhc.healingValue, jhc.healingTime);
        }
    }

    public static void sendToServer(BodyPartEnum bodyPart, String healingItem, InteractionHand hand, boolean consumeItem, boolean applyEffect) {
        BodyPartHealingTimeMessage bodyPartHealingTimeMessageToServer = new BodyPartHealingTimeMessage(bodyPart, healingItem, hand, consumeItem, applyEffect);
        PacketDistributor.sendToServer(bodyPartHealingTimeMessageToServer);
    }
}
