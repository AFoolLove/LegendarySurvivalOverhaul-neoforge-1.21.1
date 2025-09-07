package sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class ThirstProvider  implements ICapabilityProvider<Player, Void, ThirstCapability>
{
	private final ThirstCapability capability = new ThirstCapability();

	@Override
	public @Nullable ThirstCapability getCapability(Player object, Void context) {
		return capability;
	}
}
