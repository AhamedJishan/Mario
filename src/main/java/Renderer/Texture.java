package Renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture
{
    private String filepath;
    private transient int texID;
    private int width, height;

    public Texture()
    {
        this.texID = -1;
        this.width = -1;
        this.height = -1;
    }

    public Texture(int width, int height)
    {
        this.filepath = "GeneratedTexture";
        this.width = width;
        this.height = height;

        // Generate the texture on the GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

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

    public String GetFilepath()
    {
        return this.filepath;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (!(o instanceof Texture)) return false;

        Texture tex = (Texture)o;
        return tex.GetWidth() == this.width &&
                tex.GetHeight() == this.height &&
                tex.GetID() == this.texID &&
                tex.GetFilepath().equals(this.filepath);
    }
}
