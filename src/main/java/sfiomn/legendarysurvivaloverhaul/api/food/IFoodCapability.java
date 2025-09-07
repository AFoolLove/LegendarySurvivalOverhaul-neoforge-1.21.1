package sfiomn.legendarysurvivaloverhaul.api.food;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public interface IFoodCapability
{
	/**
	 * (Don't use this!) <br>
	 * Runs a tick update for the player's food capability
	 * @param player
	 * @param world
	 * @param phase
	 */
	public void tickUpdate(Player player, Level world, PlayerTickEvent phase);
}
