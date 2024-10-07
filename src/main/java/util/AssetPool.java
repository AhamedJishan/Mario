package util;

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
}
