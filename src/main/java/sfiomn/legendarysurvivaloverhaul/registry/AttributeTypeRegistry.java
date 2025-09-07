package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.food.FoodCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature.TemperatureCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness.WetnessCapability;

import java.util.function.Supplier;

public class AttributeTypeRegistry {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LegendarySurvivalOverhaul.MOD_ID);

    public static final  Supplier<AttachmentType<BodyDamageCapability>> BODY_DAMAGE = register(ModCapabilities.BODY_DAMAGE_RES, BodyDamageCapability::new);
    public static final Supplier<AttachmentType<FoodCapability>> FOOD = register(ModCapabilities.FOOD_RES, FoodCapability::new);
    public static final Supplier<AttachmentType<ThirstCapability>> THIRST = register(ModCapabilities.THIRST_RES, ThirstCapability::new);
    public static final Supplier<AttachmentType<WetnessCapability>> WETNESS = register(ModCapabilities.WETNESS_RES, WetnessCapability::new);
    public static final Supplier<AttachmentType<HealthCapability>> HEALTH = register(ModCapabilities.HEALTH_RES, HealthCapability::new);
    public static final Supplier<AttachmentType<TemperatureCapability>> TEMPERATURE = register(ModCapabilities.TEMPERATURE_RES, TemperatureCapability::new);

    private static <S extends Tag, T extends INBTSerializable<S>> Supplier<AttachmentType<T>> register(ResourceLocation name, Supplier<T> defaultValueSupplier){
        return ATTACHMENT_TYPES.register(name.getPath(), () -> AttachmentType.serializable(defaultValueSupplier).build());
    }

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
