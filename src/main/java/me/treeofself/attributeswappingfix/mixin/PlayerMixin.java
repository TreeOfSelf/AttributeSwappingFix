package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla applies held item changes on the next player tick, so swapping and attacking in the same tick mixes one
 * item's attack charge with another item's attributes. Run the vanilla held item check right before attacking, using
 * the real equipment tracking so attribute modifiers never get out of sync.
 */
@Mixin(Player.class)
public abstract class PlayerMixin {
	@Shadow
	private ItemStack lastItemInMainHand;

	@Shadow
	public abstract void resetAttackStrengthTicker();

	@Inject(method = "attack", at = @At("HEAD"))
	private void syncHeldItemBeforeAttack(Entity entity, CallbackInfo ci) {
		Player self = (Player) (Object) this;
		if (self.level().isClientSide()) {
			return;
		}

		ItemStack mainHand = self.getMainHandItem();
		if (!ItemStack.matches(this.lastItemInMainHand, mainHand)) {
			if (!ItemStack.isSameItem(this.lastItemInMainHand, mainHand)) {
				this.resetAttackStrengthTicker();
			}
			this.lastItemInMainHand = mainHand.copy();
		}
		((LivingEntityAccessor) self).invokeDetectEquipmentUpdates();
	}
}
