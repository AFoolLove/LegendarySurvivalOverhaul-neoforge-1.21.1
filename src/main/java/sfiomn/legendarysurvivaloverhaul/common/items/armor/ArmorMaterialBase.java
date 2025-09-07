package sfiomn.legendarysurvivaloverhaul.common.items.armor;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialBase {
	public static final Holder<ArmorMaterial> SNOW = register("snow",
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 1);
				map.put(ArmorItem.Type.LEGGINGS, 1);
				map.put(ArmorItem.Type.CHESTPLATE, 2);
				map.put(ArmorItem.Type.HELMET, 1);
			}), 17, SoundEvents.ARMOR_EQUIP_LEATHER,
			0, 0.0f,
			() -> Ingredient.of(ItemTags.WOOL)
	);
	public static final Holder<ArmorMaterial> DESERT = register("desert",
			Util.make(new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 1);
				map.put(ArmorItem.Type.LEGGINGS, 1);
				map.put(ArmorItem.Type.CHESTPLATE, 2);
				map.put(ArmorItem.Type.HELMET, 1);
			}), 19, SoundEvents.ARMOR_EQUIP_LEATHER,
			0, 0.0f,
			() -> Ingredient.of(Items.LEATHER)
	);

	private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
	private final float maxDamageFactor = 5.75f;
	
	ArmorMaterialBase()
	{
	}

	private static Holder<ArmorMaterial> register(
			String name,
			EnumMap<ArmorItem.Type, Integer> defense,
			int enchantmentValue,
			Holder<SoundEvent> equipSound,
			float toughness,
			float knockbackResistance,
			Supplier<Ingredient> repairIngridient
	) {
		EnumMap<ArmorItem.Type, Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

		for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
			enummap.put(armoritem$type, defense.get(armoritem$type));
		}

		List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, name)));
		return Registry.registerForHolder(
				BuiltInRegistries.ARMOR_MATERIAL,
				ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, name),
				new ArmorMaterial(enummap, enchantmentValue, equipSound, repairIngridient, layers, toughness, knockbackResistance)
		);
	}
}
