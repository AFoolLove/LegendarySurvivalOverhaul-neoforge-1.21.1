package sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BodyDamageProvider implements ICapabilityProvider<Player, Void, BodyDamageCapability> {
    private final BodyDamageCapability capability = new BodyDamageCapability();

    @Override
    public @Nullable BodyDamageCapability getCapability(@NotNull Player object, Void context) {
        return capability;
    }
}
