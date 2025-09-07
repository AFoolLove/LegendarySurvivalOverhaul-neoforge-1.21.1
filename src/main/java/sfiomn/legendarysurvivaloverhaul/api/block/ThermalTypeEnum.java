package sfiomn.legendarysurvivaloverhaul.api.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ThermalTypeEnum implements StringRepresentable {
    COOLING("cooling", -1.0f),
    HEATING("heating", 1.0f),
    BROKEN("broken", 0f);

    public static final Codec<ThermalTypeEnum> CODEC = StringRepresentable.fromValues(ThermalTypeEnum::values);

    private final String name;
    private final float temperature;

    ThermalTypeEnum(String name, float temperature) {
        this.name = name;
        this.temperature = temperature;
    }

    public float getTemperatureLevel() {
        return temperature;
    }

    public static ThermalTypeEnum get(String name) {
        for(ThermalTypeEnum t : values())
            if(t.name().equalsIgnoreCase(name)) return t;
        throw new IllegalArgumentException();
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
