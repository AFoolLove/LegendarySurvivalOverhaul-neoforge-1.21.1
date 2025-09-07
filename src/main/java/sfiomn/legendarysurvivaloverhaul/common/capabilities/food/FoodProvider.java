package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class FoodProvider implements ICapabilityProvider<Player, Void, FoodCapability>
{
	private final FoodCapability capability = new FoodCapability();

	@Override
	public @Nullable FoodCapability getCapability(Player object, Void context) {
		return capability;
	}
}
