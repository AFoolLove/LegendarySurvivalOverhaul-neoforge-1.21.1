package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.block.ThermalTypeEnum;
import sfiomn.legendarysurvivaloverhaul.common.blocks.*;

import java.util.function.Supplier;

public class BlockRegistry {

	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LegendarySurvivalOverhaul.MOD_ID);

	public static final DeferredBlock<HeaterBaseBlock> HEATER = registerBlock("heater", () -> new HeaterBaseBlock(ThermalTypeEnum.HEATING));
	public static final DeferredBlock<HeaterTopBlock> HEATER_TOP = BLOCKS.register("heater_top", () -> new HeaterTopBlock(null));
	public static final DeferredBlock<CoolerBlock> COOLER = registerBlock("cooler", () -> new CoolerBlock(ThermalTypeEnum.COOLING));
	public static final DeferredBlock<SewingTableBlock> SEWING_TABLE = registerBlock("sewing_table", () -> new SewingTableBlock(null));
	public static final DeferredBlock<SunFernBlock> SUN_FERN_CROP = BLOCKS.register("sun_fern_crop", SunFernBlock::new);
	public static final DeferredBlock<SunFernGoldBlock> SUN_FERN_GOLD = registerBlock("sun_fern_gold", () -> new SunFernGoldBlock(null));
	public static final DeferredBlock<IceFernBlock> ICE_FERN_CROP = BLOCKS.register("ice_fern_crop", IceFernBlock::new);
	public static final DeferredBlock<IceFernGoldBlock> ICE_FERN_GOLD = registerBlock("ice_fern_gold", () -> new IceFernGoldBlock(null));
	public static final DeferredBlock<WaterPlantBlock> WATER_PLANT_CROP = BLOCKS.register("water_plant_crop", WaterPlantBlock::new);

	private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
		DeferredBlock<T> newBlock = BLOCKS.register(name, block);
		registerBlockItem(name, newBlock);
		return newBlock;
	}

	private static <T extends Block, R extends T> void registerBlockItem(String name, DeferredHolder<T, R> block) {
		ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
