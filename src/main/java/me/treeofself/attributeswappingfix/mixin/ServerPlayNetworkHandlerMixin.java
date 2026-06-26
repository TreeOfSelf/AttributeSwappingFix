package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {

	@Shadow
	public ServerPlayer player;

	@Inject(method = "handlePlayerAction", at = @At("TAIL"))
	private void onHandlePlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
		forceEquipmentUpdate();
	}

	@Inject(method = "handleSetCarriedItem", at = @At("TAIL"))
	private void onHandleSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
		forceEquipmentUpdate();
	}

	@Unique
	private void forceEquipmentUpdate() {
		Player playerEntity = player;
		playerEntity.resetAttackStrengthTicker();
		((LivingEntityAccessor) player).invokeCollectEquipmentChanges(new java.util.HashMap<>());
	}
}
