package util;

import Components.SpriteSheet;
import Renderer.Shader;
import Renderer.Texture;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool
{
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public static Shader GetShader(String resourceName)
    {
        File file = new File(resourceName);
        if (AssetPool.shaders.containsKey(file.getAbsolutePath()))
            return AssetPool.shaders.get(file.getAbsolutePath());
        else
        {
            Shader shader = new Shader(resourceName);
            shader.Compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture GetTexture(String resourcePath)
    {
        File file = new File(resourcePath);
        if (textures.containsKey(file.getAbsolutePath()))
            return AssetPool.textures.get(file.getAbsolutePath());
        else
        {
            Texture texture = new Texture(resourcePath);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void AddSpriteSheet(String resourceName, SpriteSheet spriteSheet)
    {
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
            AssetPool.spriteSheets.put(file.getAbsolutePath(), spriteSheet);
    }

    public static SpriteSheet GetSpriteSheet(String resourceName)
    {
        File file = new File(resourceName);
        if (!AssetPool.spriteSheets.containsKey(file.getAbsolutePath()))
            assert false: "Error: Tried to access spritesheet '" + resourceName + "' and it hasn't been added to the AssetPool";

        return AssetPool.spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }

}
