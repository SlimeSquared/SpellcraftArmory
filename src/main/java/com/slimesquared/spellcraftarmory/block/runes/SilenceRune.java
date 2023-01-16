package com.slimesquared.spellcraftarmory.block.runes;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.effect.ModEffects;
import com.slimesquared.spellcraftarmory.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class SilenceRune extends GenericRuneBlock {
    public SilenceRune(Properties properties) {
        super(properties);
    }

    @Override
    public void activate(Player player, BlockPos pos) {
        super.activate(player, pos);
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity) {
            var area = new AABB(pos.offset(-12, -12, -12), pos.offset(12, 12, 12));
            var playerList = player.level.getNearbyPlayers(TargetingConditions.forNonCombat().range(12).ignoreLineOfSight(), player, area);
            if (!playerList.contains(player)) {
                playerList.add(player);
            }
            for (var victim : playerList) {
                Utils.applyEffect(victim, ModEffects.SILENCE.get(), 600, 0, true);
            }
        }
    }
}
