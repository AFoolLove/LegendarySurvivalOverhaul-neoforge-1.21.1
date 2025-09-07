package sfiomn.legendarysurvivaloverhaul.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

import java.util.UUID;

public class AttributeBuilder {

    protected final Holder<Attribute> attribute;
    protected final ResourceLocation descriptionId;

    public AttributeBuilder(Holder<Attribute> attribute, String descriptionId) {
        this.attribute = attribute;
        this.descriptionId = ResourceLocation.fromNamespaceAndPath(LegendarySurvivalOverhaul.MOD_ID, descriptionId);
    }

    public AttributeBuilder(Holder<Attribute> attribute, ResourceLocation descriptionId) {
        this.attribute = attribute;
        this.descriptionId = descriptionId;
    }

    public void addModifier(ItemAttributeModifierEvent event, ResourceLocation id, EquipmentSlotGroup slot, double value) {
        event.addModifier(attribute, new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE), slot);
    }

    public void addModifier(Player player, ResourceLocation id, double value) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
            instance.addPermanentModifier(new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    public AttributeInstance getAttribute(Player player) {
        return player.getAttribute(attribute);
    }

    public ResourceLocation getDescriptionId() {
        return descriptionId;
    }
}
