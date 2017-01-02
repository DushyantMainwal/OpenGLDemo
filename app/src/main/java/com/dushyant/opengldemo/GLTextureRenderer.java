package com.dushyant.opengldemo;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.glDeleteProgram;

/**
 * Created by Dushyant on 1/2/2017.
 */

class GLTextureRenderer {

    private int mProgram;
    private int mTexSamplerHandle;
    private int mTexCoordHandle;
    private int mPosCoordHandle;

    private FloatBuffer mTexVertices;
    private FloatBuffer mPosVertices;

    //for square
    //Vertex shaders perform operations on each vertex, and the results of these operations are used in the
    // fragment shaders which do additional calculations per pixel.
    private static final String VERTEX_SHADER =
            "attribute vec4 a_position;\n" + // Per-vertex position information we will pass in.
                    "attribute vec2 a_texcoord;\n" +
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_Position = a_position;\n" +
                    "  v_texcoord = a_texcoord;\n" +
                    "}\n";

    //FragmentShader is used to put stuff on the Screen
    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" + // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    "uniform sampler2D tex_sampler;\n" +//tex_sampler = variable
                    "varying vec2 v_texcoord;\n" +
                    "void main() {\n" +
                    "  gl_FragColor = texture2D(tex_sampler, v_texcoord);\n" +
                    "}\n";

    private static final float[] TEX_VERTICES = {
            0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f
    };

    private static final float[] POS_VERTICES = {
            -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f
    };

    /* Steps Used for initialization
    * 1. Create a program for storing fragment and vertex shader
    *  1.1. load the shaders
    *  1.2. create a program object
    *  1.3. attach the shaders
    *  1.4 link the shaders into program
    * 2. get the attributes (in this case: a_texcoord,a_position) and uniform texture Sampler tex_sampler
    * // Sampler is a uniform variable that represents an accessible texture
    * 3. initialize ByteBuffer
    *   We do our coding in Java on Android, but the underlying implementation of OpenGL ES 2 is actually written in C.
        Before we pass our data to OpenGL, we need to convert it into a form that it’s going to understand.
        Java and the native system might not store their bytes in the same order, so we use a special set of buffer classes and
        create a ByteBuffer large enough to hold our data, and tell it to store its data using the native byte order.
        We then convert it into a FloatBuffer so that we can use it to hold floating-point data. Finally,
        we copy our array into the buffer.
    * 4. call renderTexture()
    *
    */
    void initializeOpenGL() {
        // Create program
        mProgram = GLToolbox.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        // Bind attributes and uniforms
        /*A uniform(in this case 'tex_sampler') is a global GLSL(Shading Language) variable declared with the "uniform" storage
        qualifier. These act as parameters that the user of a shader program can pass to that program.
        They are stored in a program object.*/
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "tex_sampler");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texcoord");
        mPosCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_position");

        initializeByteBuffer();
    }

    private static final int FLOAT_SIZE_BYTES = 4;

    private void initializeByteBuffer() {
        // Setup coordinate buffers
        //textureBuffer
        mTexVertices = ByteBuffer.allocateDirect(TEX_VERTICES.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexVertices.put(TEX_VERTICES).position(0);

        //verticesBuffers
        mPosVertices = ByteBuffer.allocateDirect(POS_VERTICES.length * FLOAT_SIZE_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosVertices.put(POS_VERTICES).position(0);
    }

    void renderTexture(int textureId){
        //Bind default FBO
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        // Use openGL to use our shader program
        GLES20.glUseProgram(mProgram);
        GLToolbox.checkGlError("glUseProgram");

        //CheckError
        GLToolbox.checkGlError("glViewport");

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);

        // Set the vertex attributes
            //tells the openGL how to use buffer data(or how to use coordinate data)
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mTexVertices);
            //enable the vertex attribute and move on to the next attribute
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mPosCoordHandle, 2, GLES20.GL_FLOAT, false, 0, mPosVertices);
        GLES20.glEnableVertexAttribArray(mPosCoordHandle);
        GLToolbox.checkGlError("vertex attribute setup");

        // Set the input texture
            //current texture image unit
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLToolbox.checkGlError("glActiveTexture");
            //Changes the texture's stored state
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLToolbox.checkGlError("glBindTexture");
        GLES20.glUniform1i(mTexSamplerHandle, 0);

        // Draw
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//black
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);//draw the square using triangle strip
    }

}
