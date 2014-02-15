package no.runsafe.dergons;

import net.minecraft.server.v1_7_R1.*;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.internal.wrapper.ObjectUnwrapper;

import java.util.Iterator;
import java.util.List;

public class CustomDergonEntity extends EntityEnderDragon
{
	public CustomDergonEntity(IWorld world)
	{
		super(ObjectUnwrapper.getMinecraft(world));
	}

	private void bN()
	{
		if (bC != null)
		{
			if (bC.dead)
			{
				if (!world.isStatic)
					a(bq, DamageSource.explosion(null), 10.0F);

				bC = null;
			}
			else if (ticksLived % 10 == 0 && getHealth() < getMaxHealth())
			{
				setHealth(getHealth() + 1.0F);
			}
		}

		if (random.nextInt(10) == 0)
		{
			float f = 32.0F;
			List list = world.a(EntityEnderCrystal.class, boundingBox.grow((double) f, (double) f, (double) f));
			EntityEnderCrystal entityendercrystal = null;
			double d0 = Double.MAX_VALUE;
			Iterator iterator = list.iterator();

			while (iterator.hasNext())
			{
				EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
				double d1 = entityendercrystal1.e(this);

				if (d1 < d0)
				{
					d0 = d1;
					entityendercrystal = entityendercrystal1;
				}
			}

			bC = entityendercrystal;
		}
	}

	private void bO()
	{
		bz = false;
		if (random.nextInt(2) == 0 && !world.players.isEmpty())
		{
			targetEntity = (Entity) world.players.get(random.nextInt(world.players.size()));
		}
		else
		{
			boolean flag = false;

			do {
				h = 0.0D;
				i = (double) (70.0F + random.nextFloat() * 50.0F);
				j = 0.0D;
				h += (double) (random.nextFloat() * 120.0F - 60.0F);
				j += (double) (random.nextFloat() * 120.0F - 60.0F);
				double d0 = locX - h;
				double d1 = locY - i;
				double d2 = locZ - j;

				flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
			} while (!flag);

			targetEntity = null;
		}
	}

	@Override
	public void e()
	{
		float f;
		float f1;

		if (world.isStatic)
		{
			f = MathHelper.cos(by * 3.1415927F * 2.0F);
			f1 = MathHelper.cos(bx * 3.1415927F * 2.0F);
			if (f1 <= -0.3F && f >= -0.3F)
				world.a(locX, locY, locZ, "mob.enderdragon.wings", 5.0F, 0.8F + random.nextFloat() * 0.3F, false);
		}

		bx = by;
		float f2;

		if (getHealth() <= 0.0F)
		{
			f = (random.nextFloat() - 0.5F) * 8.0F;
			f1 = (random.nextFloat() - 0.5F) * 4.0F;
			f2 = (random.nextFloat() - 0.5F) * 8.0F;
			world.addParticle("largeexplode", locX + (double) f, locY + 2.0D + (double) f1, locZ + (double) f2, 0.0D, 0.0D, 0.0D);
		}
		else
		{
			this.bN();
			f = 0.2F / (MathHelper.sqrt(motX * motX + motZ * motZ) * 10.0F + 1.0F);
			f *= (float) Math.pow(2.0D, motY);
			by += (bA ? f * 0.5F : f);

			yaw = MathHelper.g(yaw);
			if (bo < 0)
			{
				for (int d05 = 0; d05 < bn.length; ++d05)
				{
					bn[d05][0] = (double) yaw;
					bn[d05][1] = locY;
				}
			}

			if (++bo == bn.length)
				bo = 0;

			bn[bo][0] = (double) yaw;
			bn[bo][1] = locY;
			double d0;
			double d1;
			double d2;
			double d3;
			float f3;

			if (world.isStatic)
			{
				if (bh > 0)
				{
					d0 = locX + (bi - locX) / (double) bh;
					d1 = locY + (bj - locY) / (double) bh;
					d2 = locZ + (bk - locZ) / (double) bh;
					d3 = MathHelper.g(bl - (double) yaw);
					yaw = (float) ((double) yaw + d3 / (double) bh);
					pitch = (float) ((double) pitch + (bm - (double) pitch) / (double) bh);
					--bh;
					setPosition(d0, d1, d2);
					b(yaw, pitch);
				}
			}
			else
			{
				d0 = h - locX;
				d1 = i - locY;
				d2 = j - locZ;
				d3 = d0 * d0 + d1 * d1 + d2 * d2;
				if (targetEntity != null)
				{
					h = targetEntity.locX;
					j = targetEntity.locZ;
					double d4 = h - locX;
					double d5 = j - locZ;
					double d6 = Math.sqrt(d4 * d4 + d5 * d5);
					double d7 = 0.4000000059604645D + d6 / 80.0D - 1.0D;

					if (d7 > 10.0D)
						d7 = 10.0D;

					i = targetEntity.boundingBox.b + d7;
				}
				else
				{
					h += random.nextGaussian() * 2.0D;
					j += random.nextGaussian() * 2.0D;
				}

				if (bz || d3 < 100.0D || d3 > 22500.0D || positionChanged || G)
					bO();

				d1 /= (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
				f3 = 0.6F;
				if (d1 < (double) (-f3))
					d1 = (double) (-f3);

				if (d1 > (double) f3)
					d1 = (double) f3;

				motY += d1 * 0.10000000149011612D;
				yaw = MathHelper.g(yaw);
				double d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D;
				double d9 = MathHelper.g(d8 - (double) yaw);

				if (d9 > 50.0D)
					d9 = 50.0D;

				if (d9 < -50.0D)
					d9 = -50.0D;

				Vec3D vec3d = world.getVec3DPool().create(h - locX, i - locY, j - locZ).a();
				Vec3D vec3d1 = world.getVec3DPool().create((double) MathHelper.sin(yaw * 3.1415927F / 180.0F), motY, (double) (-MathHelper.cos(yaw * 3.1415927F / 180.0F))).a();
				float f4 = (float) (vec3d1.b(vec3d) + 0.5D) / 1.5F;

				if (f4 < 0.0F)
					f4 = 0.0F;

				bg *= 0.8F;
				float f5 = MathHelper.sqrt(motX * motX + motZ * motZ) * 1.0F + 1.0F;
				double d10 = Math.sqrt(motX * motX + motZ * motZ) * 1.0D + 1.0D;

				if (d10 > 40.0D)
					d10 = 40.0D;

				bg = (float) ((double) bg + d9 * (0.699999988079071D / d10 / (double) f5));
				yaw += bg * 0.1F;
				float f6 = (float) (2.0D / (d10 + 1.0D));
				float f7 = 0.06F;

				a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
				if (bA)
					move(motX * 0.800000011920929D, motY * 0.800000011920929D, motZ * 0.800000011920929D);
				else
					move(motX, motY, motZ);

				Vec3D vec3d2 = world.getVec3DPool().create(motX, motY, motZ).a();
				float f8 = (float) (vec3d2.b(vec3d1) + 1.0D) / 2.0F;

				f8 = 0.8F + 0.15F * f8;
				motX *= (double) f8;
				motZ *= (double) f8;
				motY *= 0.9100000262260437D;
			}

			aN = yaw;
			bq.width = bq.length = 3.0F;
			bs.width = bs.length = 2.0F;
			bt.width = bt.length = 2.0F;
			bu.width = bu.length = 2.0F;
			br.length = 3.0F;
			br.width = 5.0F;
			bv.length = 2.0F;
			bv.width = 4.0F;
			bw.length = 3.0F;
			bw.width = 4.0F;
			f1 = (float) (b(5, 1.0F)[1] - b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
			f2 = MathHelper.cos(f1);
			float f9 = -MathHelper.sin(f1);
			float f10 = yaw * 3.1415927F / 180.0F;
			float f11 = MathHelper.sin(f10);
			float f12 = MathHelper.cos(f10);

			br.h();
			br.setPositionRotation(locX + (double) (f11 * 0.5F), locY, locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
			bv.h();
			bv.setPositionRotation(locX + (double) (f12 * 4.5F), locY + 2.0D, locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
			bw.h();
			bw.setPositionRotation(locX - (double) (f12 * 4.5F), locY + 2.0D, locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);

			if (!world.isStatic && hurtTicks == 0)
			{
				a(world.getEntities(this, bv.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
				a(world.getEntities(this, bw.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
				b(world.getEntities(this, bq.boundingBox.grow(1.0D, 1.0D, 1.0D)));
			}

			double[] adouble = b(5, 1.0F);
			double[] adouble1 = b(0, 1.0F);

			f3 = MathHelper.sin(yaw * 3.1415927F / 180.0F - bg * 0.01F);
			float f13 = MathHelper.cos(yaw * 3.1415927F / 180.0F - bg * 0.01F);

			bq.h();
			bq.setPositionRotation(locX + (double) (f3 * 5.5F * f2), locY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F), locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j)
			{
				EntityComplexPart entitycomplexpart = null;

				if (j == 0)
					entitycomplexpart = bs;

				if (j == 1)
					entitycomplexpart = bt;

				if (j == 2)
					entitycomplexpart = bu;

				double[] adouble2 = b(12 + j * 2, 1.0F);
				float f14 = yaw * 3.1415927F / 180.0F + b(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (float) (j + 1) * 2.0F;

				entitycomplexpart.h();
				entitycomplexpart.setPositionRotation(locX - (double) ((f11 * f17 + f15 * f18) * f2), locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
			}

			if (!world.isStatic)
				bA = a(bq.boundingBox) | a(br.boundingBox);
		}
	}

	private void a(List list)
	{
		double d0 = (br.boundingBox.a + br.boundingBox.d) / 2.0D;
		double d1 = (br.boundingBox.c + br.boundingBox.f) / 2.0D;
		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			Entity entity = (Entity) iterator.next();

			if (entity instanceof EntityLiving)
			{
				double d2 = entity.locX - d0;
				double d3 = entity.locZ - d1;
				double d4 = d2 * d2 + d3 * d3;

				entity.g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
			}
		}
	}

	private void b(List list)
	{
		for (int i = 0; i < list.size(); ++i)
		{
			Entity entity = (Entity) list.get(i);

			if (entity instanceof EntityLiving)
				entity.damageEntity(DamageSource.mobAttack(this), 10.0F);
		}
	}

	private boolean a(AxisAlignedBB axisalignedbb)
	{
		int i = MathHelper.floor(axisalignedbb.a);
		int j = MathHelper.floor(axisalignedbb.b);
		int k = MathHelper.floor(axisalignedbb.c);
		int l = MathHelper.floor(axisalignedbb.d);
		int i1 = MathHelper.floor(axisalignedbb.e);
		int j1 = MathHelper.floor(axisalignedbb.f);
		boolean flag = false;
		boolean flag1 = false;

		for (int k1 = i; k1 <= l; ++k1)
		{
			for (int l1 = j; l1 <= i1; ++l1)
			{
				for (int i2 = k; i2 <= j1; ++i2)
				{
					Block block = world.getType(k1, l1, i2);

					if (block.getMaterial() != Material.AIR)
					{
						if (block != Blocks.OBSIDIAN && block != Blocks.WHITESTONE && block != Blocks.BEDROCK && this.world.getGameRules().getBoolean("mobGriefing"))
							flag1 = world.setAir(k1, l1, i2) || flag1;
						else
							flag = true;
					}
				}
			}
		}

		if (flag1)
		{
			double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) random.nextFloat();
			double d1 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) random.nextFloat();
			double d2 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) random.nextFloat();

			world.addParticle("largeexplode", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}

		return flag;
	}

	private float b(double d0)
	{
		return (float) MathHelper.g(d0);
	}

	private Entity targetEntity;
}