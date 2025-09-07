package sfiomn.legendarysurvivaloverhaul.common.events;


import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.registry.AttributeRegistry;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModBusEvents {

    @SubscribeEvent
    public static void onEntityAttributesChange(EntityAttributeModificationEvent event) {
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEATING_TEMPERATURE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEATING_TEMPERATURE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COOLING_TEMPERATURE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COOLING_TEMPERATURE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAT_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAT_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.COLD_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.COLD_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.THERMAL_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.THERMAL_RESISTANCE
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BODY_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BODY_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.HEAD_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.HEAD_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.CHEST_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.CHEST_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.RIGHT_ARM_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.RIGHT_ARM_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEFT_ARM_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEFT_ARM_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.LEGS_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.LEGS_RESISTANCE
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.FEET_RESISTANCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.FEET_RESISTANCE
            );
        }

        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.PERMANENT_HEART)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.PERMANENT_HEART
            );
        }
        if (!event.has(EntityType.PLAYER, AttributeRegistry.BROKEN_HEART_RESILIENCE)) {
            event.add(EntityType.PLAYER,
                    AttributeRegistry.BROKEN_HEART_RESILIENCE
            );
        }
    }
}
