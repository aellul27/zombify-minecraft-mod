package com.aellul27.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {

	@Inject(
			method = "onKilledOther",
			at = @At("HEAD"),
			cancellable = true
	)
	private void alwaysInfectVillagers(
			ServerWorld world,
			LivingEntity other,
			DamageSource damageSource,
			CallbackInfoReturnable<Boolean> cir
	) {
		if (other instanceof VillagerEntity villager) {
			ZombieEntity zombie = (ZombieEntity) (Object) this;

			// Force infection regardless of difficulty
			if (zombie.infectVillager(world, villager)) {
				cir.setReturnValue(false); // vanilla sets bl = false on success
				return;
			}
		}
		// otherwise let vanilla logic run
	}
}