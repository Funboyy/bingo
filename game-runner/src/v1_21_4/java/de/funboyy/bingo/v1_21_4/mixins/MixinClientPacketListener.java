package de.funboyy.bingo.v1_21_4.mixins;

import de.funboyy.bingo.api.Bingo;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

  @Inject(method = "handleRespawn", at = @At("HEAD"))
  public void bingo$handleRespawn(final ClientboundRespawnPacket packet, final CallbackInfo callbackInfo) {
    Bingo.get().getGame().handleDeath();
  }

}
