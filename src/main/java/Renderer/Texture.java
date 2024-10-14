package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private String filepath;
    private int texID;
    private int width, height;

    public void Load(String filepath)
    {
        this.filepath = filepath;

        // Generate and Bind texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // Set texture parameters
        // Repeat image in both directions while wrapping
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When shrinking or strecthing the image pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        stbi_set_flip_vertically_on_load(true);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer imageData = stbi_load(filepath, width, height, channels, 0);

        if (imageData != null)
        {
            this.width = width.get(0);
            this.height = height.get(0);
            int format = 0;

            if      (channels.get(0) == 3) format = GL_RGB;
            else if (channels.get(0) == 4) format = GL_RGBA;
            else assert false: "Error: (Texture) Invalid number of channels '" + channels.get() + "'";

            glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, imageData);
        }
        else
        {
            assert false: "Error: (Texture) Couldn't load image at '" + filepath + "'";
        }

        stbi_image_free(imageData);
    }

    public void Bind()
    {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void Unbind()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int GetWidth()
    {
        return this.width;
    }

    public int GetHeight()
    {
        return this.height;
    }

    public int GetID()
    {
        return this.texID;
    }
}
