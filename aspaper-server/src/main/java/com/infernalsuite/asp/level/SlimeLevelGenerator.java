package com.infernalsuite.asp.level;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SlimeLevelGenerator extends FlatLevelSource {

    public SlimeLevelGenerator(Holder<Biome> biome) {
        super(new FlatLevelGeneratorSettings(Optional.empty(), biome, List.of()), getSource(biome));
    }

    private static BiomeSource getSource(Holder<Biome> biome) {
        return new BiomeSource() {
            @Override
            protected MapCodec<? extends BiomeSource> codec() {
                return null;
            }

            @Override
            protected Stream<Holder<Biome>> collectPossibleBiomes() {
                return Stream.of(biome);
            }

            @Override
            public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler noise) {
                return biome;
            }
        };
    }
}
