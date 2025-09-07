package sfiomn.legendarysurvivaloverhaul.common.capabilities.health;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;
public class HealthProvider  implements ICapabilityProvider<Player, Void, HealthCapability>
{
	private final HealthCapability capability = new HealthCapability();

	@Override
	public @Nullable HealthCapability getCapability(Player object, Void context) {
		return capability;
	}
}
