package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At("TAIL"))
    private void onPlayerAction(PlayerActionC2SPacket packet, CallbackInfo ci) {
        forceEquipmentUpdate();
    }

    @Inject(method = "onUpdateSelectedSlot", at = @At("TAIL"))
    public void onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        forceEquipmentUpdate();
    }

    @Unique
    private void forceEquipmentUpdate() {
        player.resetLastAttackedTicks();
        ((LivingEntityAccessor) player).invokeGetEquipmentChanges();
    }
}