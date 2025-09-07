package sfiomn.legendarysurvivaloverhaul.common.capabilities.wetness;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class WetnessProvider implements ICapabilityProvider<Player, Void, WetnessCapability>
{
	private final WetnessCapability capability = new WetnessCapability();

	@Override
	public @Nullable WetnessCapability getCapability(Player object, Void context) {
		return capability;
	}
}
