package me.treeofself.attributeswappingfix.mixin;

import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

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
		Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			equipment.put(slot, player.getItemBySlot(slot).copy());
		}
		((LivingEntityAccessor) player).invokeCollectEquipmentChanges(equipment);
	}
}
