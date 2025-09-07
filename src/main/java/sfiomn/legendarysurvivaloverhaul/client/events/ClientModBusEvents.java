package sfiomn.legendarysurvivaloverhaul.client.events;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.client.particles.BreathParticle;
import sfiomn.legendarysurvivaloverhaul.client.particles.FernBlossomParticle;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationClientTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.client.tooltips.HydrationTooltipComponent;
import sfiomn.legendarysurvivaloverhaul.registry.ParticleTypeRegistry;

import static sfiomn.legendarysurvivaloverhaul.client.integration.sereneseasons.RenderSeasonCards.SEASON_CARD_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderBodyDamageGui.BODY_DAMAGE_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderHealthGui.HEALTH_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui.FOOD_BAR_COLD_EFFECT_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureGui.TEMPERATURE_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTemperatureOverlay.TEMPERATURE_OVERLAY;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderThirstGui.THIRST_GUI;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderTooltipFrame.TOOLTIP_ITEM_FRAME;
import static sfiomn.legendarysurvivaloverhaul.client.render.RenderWetnessGui.WETNESS_GUI;

@EventBusSubscriber(modid = LegendarySurvivalOverhaul.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {
    public static final ResourceLocation HEALTH_OVERHAUL_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "health_overhaul");
    public static final ResourceLocation COLD_HUNGER_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "cold_hunger");
    public static final ResourceLocation THIRST_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "thirst");
    public static final ResourceLocation TEMPERATURE_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature");
    public static final ResourceLocation WETNESS_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "wetness");
    public static final ResourceLocation BODY_DAMAGE_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "body_damage");

    public static final ResourceLocation TEMPERATURE_OVERLAY_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "temperature_overlay");
    public static final ResourceLocation ITEM_FRAME_TOOLTIP_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "item_frame_tooltip");
    public static final ResourceLocation SEASON_CARD_ID = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, "season_card");



    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, HEALTH_OVERHAUL_ID, HEALTH_GUI);

        event.registerBelow(VanillaGuiLayers.FOOD_LEVEL, COLD_HUNGER_ID, FOOD_BAR_COLD_EFFECT_GUI);

        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, THIRST_ID, THIRST_GUI);

        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, TEMPERATURE_ID, TEMPERATURE_GUI);

        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, WETNESS_ID, WETNESS_GUI);

        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, BODY_DAMAGE_ID, BODY_DAMAGE_GUI);

        event.registerAbove(VanillaGuiLayers.EFFECTS, TEMPERATURE_OVERLAY_ID, TEMPERATURE_OVERLAY);

        event.registerAbove(VanillaGuiLayers.SELECTED_ITEM_NAME, ITEM_FRAME_TOOLTIP_ID, TOOLTIP_ITEM_FRAME);

        event.registerAbove(VanillaGuiLayers.SELECTED_ITEM_NAME, SEASON_CARD_ID, SEASON_CARD_GUI);

    }

    @SubscribeEvent
    public static void onTooltipRegistration(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(HydrationTooltipComponent.class, component -> new HydrationClientTooltipComponent(component.hydration, component.saturation));
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleTypeRegistry.SUN_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        event.registerSpriteSet(ParticleTypeRegistry.ICE_FERN_BLOSSOM.get(), FernBlossomParticle.Factory::new);
        event.registerSpriteSet(ParticleTypeRegistry.COLD_BREATH.get(), BreathParticle.Factory::new);
    }
}
