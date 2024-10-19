package Renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
    private int fboID;
    private Texture texture = null;

    public Framebuffer(int width, int height)
    {
        // Generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // create and attach the texture to render the data to
        texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.GetID(), 0);

        // create renderbufferobject to store depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            assert false: "ERROR: Framebuffer not complete";

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void Bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void Unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int GetFboID() {
        return fboID;
    }

    public int GetTextureId() {
        return texture.GetID();
    }
}
