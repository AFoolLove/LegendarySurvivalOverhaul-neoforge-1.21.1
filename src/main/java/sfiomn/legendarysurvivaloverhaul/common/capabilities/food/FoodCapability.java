package sfiomn.legendarysurvivaloverhaul.common.capabilities.food;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.UnknownNullability;
import sfiomn.legendarysurvivaloverhaul.api.food.IFoodCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;


public class FoodCapability implements IFoodCapability, INBTSerializable<CompoundTag>
{
	//Unsaved data
	private Vec3 oldPos;
	private boolean wasSprinting;
	private int updateTickTimer; // Update immediately first time around

	public FoodCapability()
	{
		this.init();
	}

	public void init()
	{
		this.oldPos = null;
		this.updateTickTimer = 0;
		this.wasSprinting = false;
	}

	public void tickUpdate(Player player, Level world, PlayerTickEvent phase)
	{
		if(phase instanceof PlayerTickEvent.Pre) return;

		if (oldPos == null)
			oldPos = player.position();

		updateTickTimer++;
		if(updateTickTimer >= 10)
		{
			updateTickTimer = 0;

			// if player is not in pause, trigger the food exhaust, allowing afk player not dying from hunger
			if (oldPos.distanceTo(player.position()) > 1) {
				float foodExhausted;
				if (player.isSprinting() && this.wasSprinting)
					foodExhausted = (float) Config.Baked.sprintingFoodExhaustion;
				else if (player.isSprinting() || this.wasSprinting)
					foodExhausted = (float) ((Config.Baked.sprintingFoodExhaustion + Config.Baked.baseFoodExhaustion) / 2.0d);
				else
					foodExhausted = (float) Config.Baked.baseFoodExhaustion;

				player.getFoodData().addExhaustion(foodExhausted);
				this.oldPos = player.position();
				this.wasSprinting = player.isSprinting();
			}
		}
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

	}

	@Override
	public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
		return new CompoundTag();
	}
}
