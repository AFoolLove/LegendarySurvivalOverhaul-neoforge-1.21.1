package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.common.effects.*;

public class MobEffectRegistry {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> TEMPERATURE_POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, LegendarySurvivalOverhaul.MOD_ID);
	public static final DeferredRegister<Potion> THIRST_POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, LegendarySurvivalOverhaul.MOD_ID);

	public static final DeferredHolder<MobEffect, MobEffect> THIRST = EFFECTS.register("thirst", ThirstEffect::new);
	public static final DeferredHolder<Potion, Potion> THIRST_POTION = THIRST_POTIONS.register("thirst", () -> new Potion("thirst", new MobEffectInstance(THIRST, 3600, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> THIRST_POTION_LONG = THIRST_POTIONS.register("thirst_long", () -> new Potion("thirst_long", new MobEffectInstance(THIRST, 9600, 0, false, true, true)));
	public static final DeferredHolder<MobEffect, MobEffect> HYDRATION_FILL = EFFECTS.register("hydration_fill", HydrationFillEffect::new);
	public static final DeferredHolder<Potion, Potion> HYDRATION_FILL_POTION = THIRST_POTIONS.register("hydration_fill", () -> new Potion("hydration_fill", new MobEffectInstance(HYDRATION_FILL, 3600, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> HYDRATION_FILL_POTION_LONG = THIRST_POTIONS.register("hydration_fill_long", () -> new Potion("hydration_fill_long", new MobEffectInstance(HYDRATION_FILL, 9600, 0, false, true, true)));

	public static final DeferredHolder<MobEffect, MobEffect> HOT_FOOD = EFFECTS.register("hot_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE, SimpleAttributeEffect.HOT_FOOD_ATTRIBUTE, 1.0, AttributeModifier.Operation.ADD_VALUE));
	public static final DeferredHolder<MobEffect, MobEffect> HOT_DRINk = EFFECTS.register("hot_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16714764, 1).addAttributeModifier(AttributeRegistry.HEATING_TEMPERATURE, SimpleAttributeEffect.HOT_DRINK_ATTRIBUTE, 1.0, AttributeModifier.Operation.ADD_VALUE));
	public static final DeferredHolder<MobEffect, MobEffect> COLD_FOOD = EFFECTS.register("cold_food", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, -1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE, SimpleAttributeEffect.COLD_FOOD_ATTRIBUTE, -1.0, AttributeModifier.Operation.ADD_VALUE));
	public static final DeferredHolder<MobEffect, MobEffect> COLD_DRINK = EFFECTS.register("cold_drink", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 1166574, -1).addAttributeModifier(AttributeRegistry.COOLING_TEMPERATURE, SimpleAttributeEffect.COLD_DRINK_ATTRIBUTE, -1.0, AttributeModifier.Operation.ADD_VALUE));
	public static final DeferredHolder<MobEffect, MobEffect> HEAT_RESISTANCE = EFFECTS.register("heat_resistance", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 16420407, 1).addAttributeModifier(AttributeRegistry.HEAT_RESISTANCE, SimpleAttributeEffect.HEAT_RESISTANCE_ATTRIBUTE, 1.0, AttributeModifier.Operation.ADD_VALUE));
	public static final DeferredHolder<MobEffect, MobEffect> COLD_RESISTANCE = EFFECTS.register("cold_resistance", () -> new SimpleAttributeEffect(MobEffectCategory.BENEFICIAL, 6466303, 1).addAttributeModifier(AttributeRegistry.COLD_RESISTANCE, SimpleAttributeEffect.COLD_RESISTANCE_ATTRIBUTE, 1.0, AttributeModifier.Operation.ADD_VALUE));

	public static final DeferredHolder<MobEffect, MobEffect> FROSTBITE = EFFECTS.register("frostbite", FrostbiteEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> COLD_HUNGER = EFFECTS.register("cold_hunger", ColdHungerEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> HEAT_STROKE = EFFECTS.register("heat_stroke", HeatStrokeEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> HEAT_THIRST = EFFECTS.register("heat_thirst", HeatThirstEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> COLD_IMMUNITY = EFFECTS.register("cold_immunity", ColdImmunityEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> HEAT_IMMUNITY = EFFECTS.register("heat_immunity", HeatImmunityEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> TEMPERATURE_IMMUNITY = EFFECTS.register("temperature_immunity", TemperatureImmunityEffect::new);

	public static final DeferredHolder<Potion, Potion> HEAT_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("heat_immunity", () -> new Potion("heat_immunity", new MobEffectInstance(HEAT_IMMUNITY, 1800, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> HEAT_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("heat_immunity_long", () -> new Potion("heat_immunity_long", new MobEffectInstance(HEAT_IMMUNITY, 2400, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> COLD_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("cold_immunity", () -> new Potion("cold_immunity", new MobEffectInstance(COLD_IMMUNITY, 1800, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> COLD_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("cold_immunity_long", () -> new Potion("cold_immunity_long", new MobEffectInstance(COLD_IMMUNITY, 2400, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> HEAT_RESISTANCE_POTION = TEMPERATURE_POTIONS.register("heat_resistance", () -> new Potion("heat_resistance", new MobEffectInstance(HEAT_RESISTANCE, 1800, 1, false, true, true)));
	public static final DeferredHolder<Potion, Potion> HEAT_RESISTANCE_POTION_LONG = TEMPERATURE_POTIONS.register("heat_resistance_long", () -> new Potion("heat_resistance_long", new MobEffectInstance(HEAT_RESISTANCE, 2400, 1, false, true, true)));
	public static final DeferredHolder<Potion, Potion> COLD_RESISTANCE_POTION = TEMPERATURE_POTIONS.register("cold_resistance", () -> new Potion("cold_resistance", new MobEffectInstance(COLD_RESISTANCE, 1800, 1, false, true, true)));
	public static final DeferredHolder<Potion, Potion> COLD_RESISTANCE_POTION_LONG = TEMPERATURE_POTIONS.register("cold_resistance_long", () -> new Potion("cold_resistance_long", new MobEffectInstance(COLD_RESISTANCE, 2400, 1, false, true, true)));
	public static final DeferredHolder<Potion, Potion> TEMPERATURE_IMMUNITY_POTION = TEMPERATURE_POTIONS.register("temperature_immunity", () -> new Potion("temperature_immunity", new MobEffectInstance(TEMPERATURE_IMMUNITY, 1800, 0, false, true, true)));
	public static final DeferredHolder<Potion, Potion> TEMPERATURE_IMMUNITY_POTION_LONG = TEMPERATURE_POTIONS.register("temperature_immunity_long", () -> new Potion("temperature_immunity_long", new MobEffectInstance(TEMPERATURE_IMMUNITY, 2400, 0, false, true, true)));

	public static final DeferredHolder<MobEffect, MobEffect> PAINKILLER = EFFECTS.register("painkiller", PainKillerEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> PAINKILLER_ADDICTION = EFFECTS.register("painkiller_addiction", PainkillerAddictionEffect::new);

	public static final DeferredHolder<MobEffect, MobEffect> HARD_FALLING = EFFECTS.register("hard_falling", HardFallingEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> VULNERABILITY = EFFECTS.register("vulnerability", VulnerabilityEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> HEADACHE = EFFECTS.register("headache", HeadacheEffect::new);
	public static final DeferredHolder<MobEffect, MobEffect> RECOVERY = EFFECTS.register("recovery", RecoveryEffect::new);

	public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event)
	{
		addBrewingRecipe(event, Potions.AWKWARD, ItemRegistry.SUN_FERN.get(), HEAT_RESISTANCE_POTION);
		addBrewingRecipe(event, HEAT_RESISTANCE_POTION, Items.REDSTONE, HEAT_RESISTANCE_POTION_LONG);

		addBrewingRecipe(event, Potions.AWKWARD, ItemRegistry.ICE_FERN.get(), COLD_RESISTANCE_POTION);
		addBrewingRecipe(event, COLD_RESISTANCE_POTION, Items.REDSTONE, COLD_RESISTANCE_POTION_LONG);

		addBrewingRecipe(event, Potions.AWKWARD, ItemRegistry.SUN_FERN_GOLD.get(), HEAT_IMMUNITY_POTION);
		addBrewingRecipe(event, HEAT_IMMUNITY_POTION, Items.REDSTONE, HEAT_IMMUNITY_POTION_LONG);

		addBrewingRecipe(event, Potions.AWKWARD, ItemRegistry.ICE_FERN_GOLD.get(), COLD_IMMUNITY_POTION);
		addBrewingRecipe(event, COLD_IMMUNITY_POTION, Items.REDSTONE, COLD_IMMUNITY_POTION_LONG);

		addBrewingRecipe(event, HEAT_IMMUNITY_POTION, ItemRegistry.ICE_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION);
		addBrewingRecipe(event, HEAT_IMMUNITY_POTION_LONG, ItemRegistry.ICE_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION_LONG);

		addBrewingRecipe(event, COLD_IMMUNITY_POTION, ItemRegistry.SUN_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION);
		addBrewingRecipe(event, COLD_IMMUNITY_POTION_LONG, ItemRegistry.SUN_FERN_GOLD.get(), TEMPERATURE_IMMUNITY_POTION_LONG);
		addBrewingRecipe(event, TEMPERATURE_IMMUNITY_POTION, Items.REDSTONE, TEMPERATURE_IMMUNITY_POTION_LONG);
	}

	private static void addBrewingRecipe(RegisterBrewingRecipesEvent event, Holder<Potion> potionInput, Item ingredient, Holder<Potion> potionResult)
	{
		event.getBuilder().addMix(potionInput, ingredient, potionResult);
	}

	private static void addBrewingRecipe(RegisterBrewingRecipesEvent event, Holder<Potion> potionInput, Item ingredient, Item itemResult)
	{
//		BrewingRecipeRegistry.addRecipe(StrictNBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potionInput)), StrictNBTIngredient.of(new ItemStack(ingredient)), new ItemStack(itemResult));
	}
	
	public static void register(IEventBus eventBus){
		EFFECTS.register(eventBus);
		TEMPERATURE_POTIONS.register(eventBus);
		THIRST_POTIONS.register(eventBus);
	}
}
