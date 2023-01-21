package com.slimesquared.spellcraftarmory.block.runes;

import com.slimesquared.spellcraftarmory.block.GenericRuneBlock;
import com.slimesquared.spellcraftarmory.block.entity.RuneBlockEntity;
import com.slimesquared.spellcraftarmory.networking.ModMessages;
import com.slimesquared.spellcraftarmory.networking.packet.RuneEmitterS2CPacket;
import com.slimesquared.spellcraftarmory.util.RuneSpells;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;

public class TeleportRune extends GenericRuneBlock {
    public TeleportRune(Properties properties) {
        super(properties);
    }

    @Override
    public void activate(Player player, BlockPos pos) {
        super.activate(player, pos);
        if (player.level.getBlockEntity(pos) instanceof RuneBlockEntity) {
            double rx = pos.getX() + 0.5D;
            double ry = pos.getY();
            double rz = pos.getZ() + 0.5D;
            var startPos = player.position();

            if (player.level.getBlockState(pos).getValue(FACE) == AttachFace.CEILING
                    && !player.level.getBlockState(pos.offset(0, -1, 0)).getMaterial().blocksMotion()) {
                ry -= 1;

            }
            player.teleportTo(rx, ry, rz);

            for (var otherPlayer : player.level.players()) {
                if (otherPlayer instanceof ServerPlayer && (otherPlayer.distanceTo(player) < 50 || otherPlayer.distanceToSqr(startPos) < 2500)) {
                    var vec = new Vec3(rx, ry, rz).subtract(startPos).scale(0.1);
                    if (vec.length() > 1) {
                        vec = vec.normalize();
                    }

                    ModMessages.sendToPlayer(new RuneEmitterS2CPacket(new Vec3(rx, ry, rz), vec, RuneSpells.Teleport), (ServerPlayer) otherPlayer);
                    ModMessages.sendToPlayer(new RuneEmitterS2CPacket(startPos, vec, RuneSpells.Teleport), (ServerPlayer) otherPlayer);
                }
            }
        }
    }
}
