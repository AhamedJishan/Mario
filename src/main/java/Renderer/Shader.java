package Renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader
{
    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader (String filepath)
    {
        this.filepath = filepath;

        try
        {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex"))            {
                vertexSource = splitString[1];
            }
            else if (firstPattern.equals("fragment"))            {
                fragmentSource = splitString[1];
            }
            else{
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex"))            {
                vertexSource = splitString[2];
            }
            else if (secondPattern.equals("fragment"))            {
                fragmentSource = splitString[2];
            }
            else{
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("Erorr: Could not open file for shader at path: '" + filepath + "'");
        }
    }

    public void Compile()
    {
        // ========================
        // Compile and link Shaders
        // ========================
        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
        // Error check
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex Shader Compilation failed!");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false: "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
        // Error check
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment Shader Compilation failed!");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false: "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);
        // Error check
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tShader Program Linking failed!");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false: "";
        }
    }

    public void Use()
    {
        // Bind the shader program
        if (!beingUsed)
        {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void Detach()
    {
        // Bind the shader program
        if (beingUsed)
        {
            glUseProgram(0);
            beingUsed = false;
        }
    }

    public void UploadMat4f(String varName, Matrix4f mat)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void UploadMat3f(String varName, Matrix3f mat)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void UploadVec4f(String varName, Vector4f vec)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void UploadVec3f(String varName, Vector3f vec)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void UploadVec2f(String varName, Vector2f vec)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void UploadFloat(String varName, float val)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1f(varLocation, val);
    }

    public void UploadInt(String varName, int val)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, val);
    }

    public void UploadTexture(String varName, int slot)
    {
        Use();
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        glUniform1i(varLocation, slot);
    }
}
