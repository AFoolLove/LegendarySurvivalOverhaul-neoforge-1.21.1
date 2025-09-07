package sfiomn.legendarysurvivaloverhaul.common.capabilities.temperature;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class TemperatureProvider  implements ICapabilityProvider<Player, Void, TemperatureCapability>
{
	private final TemperatureCapability capability = new TemperatureCapability();

	@Override
	public @Nullable TemperatureCapability getCapability(Player object, Void context) {
		return capability;
	}
}
