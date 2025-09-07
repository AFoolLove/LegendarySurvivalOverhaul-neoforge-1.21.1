package sfiomn.legendarysurvivaloverhaul.common.integration.curios;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import sfiomn.legendarysurvivaloverhaul.util.AttributeBuilder;
import top.theillusivec4.curios.api.event.CurioAttributeModifierEvent;

import java.util.UUID;

public class CurioAttributeBuilder extends AttributeBuilder {

    public CurioAttributeBuilder(Holder<Attribute> attribute, String descriptionId) {
        super(attribute, descriptionId);
    }

    public void addModifier(CurioAttributeModifierEvent event, ResourceLocation id, double value) {
        event.addModifier(this.attribute, new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE));
    }
}
