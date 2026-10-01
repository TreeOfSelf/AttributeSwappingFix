package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla only applies a held item's attribute modifiers on the next entity tick, so swapping and attacking in the
 * same tick attacks with the new item but the old item's attributes. Same fix as Paper
 * (updateEquipmentOnPlayerActions): update equipment right after every action that can change the held item.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {
	@Shadow
	public ServerPlayer player;

	@Inject(method = "handleSetCarriedItem", at = @At("RETURN"))
	private void updateAfterSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
		((LivingEntityAccessor) this.player).invokeDetectEquipmentUpdates();
	}

	@Inject(method = "handlePlayerAction", at = @At("RETURN"))
	private void updateAfterPlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
		((LivingEntityAccessor) this.player).invokeDetectEquipmentUpdates();
	}

	@Inject(method = "handleUseItemOn", at = @At("RETURN"))
	private void updateAfterUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
		((LivingEntityAccessor) this.player).invokeDetectEquipmentUpdates();
	}

	@Inject(method = "tryPickItem", at = @At("RETURN"))
	private void updateAfterPickItem(ItemStack itemStack, CallbackInfo ci) {
		((LivingEntityAccessor) this.player).invokeDetectEquipmentUpdates();
	}
}
