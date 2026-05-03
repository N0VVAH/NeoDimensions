package net.torchednova.neodimensions.saving;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;


public class SaveData {
	public static Path path;
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void init(MinecraftServer server)
	{
		path = server.getWorldPath(LevelResource.ROOT).resolve("data").resolve("neodimensions");
	}

	public static <T> Object load(Type type, String name)
	{
		try{
			Path file = path.resolve(name);

			if (Files.exists(file) == false)
			{
				save(null, name);
			}

			String json = Files.readString(file);

			return GSON.<T>fromJson(json, type);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T> void save(T obj, String name)
	{
		try{
			Path file = path.resolve(name);

			Path parent = file.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}

			String json = GSON.toJson(obj);
			Files.writeString(file, json);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}



}
