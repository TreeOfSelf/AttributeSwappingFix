package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla only notices a held item change on the next player tick, so swapping and attacking in the same tick attacks
 * with the new item but the old item's attributes and attack charge. Right after every action that can change the
 * held item, run that same tick check (reset the attack charge when the item changed) and update the equipment
 * attributes, like Paper's updateEquipmentOnPlayerActions. A same tick swap and hit then works exactly like swapping
 * and hitting a tick later.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {
	@Shadow
	public ServerPlayer player;

	@Inject(method = "handleSetCarriedItem", at = @At("RETURN"))
	private void updateAfterSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
		syncHeldItem();
	}

	@Inject(method = "handlePlayerAction", at = @At("RETURN"))
	private void updateAfterPlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
		syncHeldItem();
	}

	@Inject(method = "handleUseItemOn", at = @At("RETURN"))
	private void updateAfterUseItemOn(ServerboundUseItemOnPacket packet, CallbackInfo ci) {
		syncHeldItem();
	}

	@Inject(method = "tryPickItem", at = @At("RETURN"))
	private void updateAfterPickItem(ItemStack itemStack, CallbackInfo ci) {
		syncHeldItem();
	}

	@Unique
	private void syncHeldItem() {
		// Same as Player.tick
		PlayerAccessor accessor = (PlayerAccessor) this.player;
		ItemStack mainHand = this.player.getMainHandItem();
		if (!ItemStack.matches(accessor.getLastItemInMainHand(), mainHand)) {
			if (!ItemStack.isSameItem(accessor.getLastItemInMainHand(), mainHand)) {
				this.player.resetAttackStrengthTicker();
			}
			accessor.setLastItemInMainHand(mainHand.copy());
		}
		((LivingEntityAccessor) this.player).invokeDetectEquipmentUpdates();
	}
}
