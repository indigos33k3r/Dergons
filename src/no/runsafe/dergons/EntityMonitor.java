package no.runsafe.dergons;

import net.minecraft.server.v1_7_R1.Entity;
import no.runsafe.framework.api.event.entity.IEntityDamageByEntityEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.wrapper.ObjectUnwrapper;
import no.runsafe.framework.minecraft.entity.ProjectileEntity;
import no.runsafe.framework.minecraft.entity.RunsafeEntity;
import no.runsafe.framework.minecraft.entity.RunsafeProjectile;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageByEntityEvent;

public class EntityMonitor implements IEntityDamageByEntityEvent
{
	public EntityMonitor(DergonHandler handler)
	{
		this.handler = handler;
	}

	@Override
	public void OnEntityDamageByEntity(RunsafeEntityDamageByEntityEvent event)
	{
		RunsafeEntity entity = event.getEntity();
		if (entity == null)
			return;

		Entity rawEntity = ObjectUnwrapper.getMinecraft(entity);
		if (rawEntity instanceof CustomDergonEntity)
		{
			for (Dergon dergon : handler.getDergons())
			{
				RunsafeEntity attacker = event.getDamageActor();
				if (dergon.isDergon(entity))
				{
					IPlayer attackingPlayer = null;

					if (attacker instanceof RunsafeProjectile)
					{
						if (attacker.getEntityType() == ProjectileEntity.Arrow)
						{
							event.setDamage(7.0D);
							attacker.remove();
							dergon.getLivingEntity().damage(7.0D);
							event.cancel();
						}

						IPlayer shooter = ((RunsafeProjectile) attacker).getShooterPlayer();
						if (shooter != null)
							attackingPlayer = shooter;
					}
					else if (attacker instanceof IPlayer)
					{
						attackingPlayer = (IPlayer) attacker;
					}

					if (attackingPlayer != null)
					{
						dergon.registerAttack(attackingPlayer, event.getDamage());
						if (attacker.getEntityType() == ProjectileEntity.Snowball)
							new DergonSnowballEvent(attackingPlayer).Fire();
					}

					break;
				}
			}
		}
	}

	private final DergonHandler handler;
}
