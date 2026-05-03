package net.torchednova.neodimensions.voiddimension;

import net.minecraft.world.phys.Vec3;

public class Parts {
	public Parts(Vec3 pos, String uuid, Vec3 size)
	{
		this.pos = pos;
		this.uuid = uuid;
		this.size = size;
		this.center = new Vec3(pos.x + (size.x / 2), pos.y, pos.z + (size.z / 2));
	}
	public Vec3 pos;
	public Vec3 center;
	public String uuid;
	public Vec3 size;
}
