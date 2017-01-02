/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dushyant.opengldemo;

import android.opengl.GLES20;

public class GLToolbox {

    //loading shaders into openGL from native os
    public static int loadShadersIntoOpenGL(int shaderType, String source) {
        int shader = GLES20.glCreateShader(shaderType);//Load the Specified shader
        if (shader != 0) {//is shader in not null
            GLES20.glShaderSource(shader, source);//pass the defined shader source
            GLES20.glCompileShader(shader);//compile the shade
            int[] compiled = new int[1];//get the compilation status using glGetShaderiv()
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {//0 means compilation failed
                String info = GLES20.glGetShaderInfoLog(shader);//log to find out Why error occured
                GLES20.glDeleteShader(shader);//delete the shader
                throw new RuntimeException("Could not compile shader " + shaderType + ":" + info);
            }
        }
        return shader;
    }

    //linking the fragment and vertex shader into a program
    public static int createProgram(String vertexSource, String fragmentSource) {
        int vertexShader = loadShadersIntoOpenGL(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vertexShader == 0) {
            return 0;
        }
        int pixelShader = loadShadersIntoOpenGL(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            return 0;
        }
        //create a program object
        int program = GLES20.glCreateProgram();
        if (program != 0) {//if not null
            GLES20.glAttachShader(program, vertexShader);//attach vertex Shader
            checkGlError("glAttachShader");//throws exception if error occurs while attaching shader
            GLES20.glAttachShader(program, pixelShader);//attach fragment Shader
            checkGlError("glAttachShader");
            GLES20.glLinkProgram(program);//links both fragment and vertex shader
            int[] linkStatus = new int[1];//check link status
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {//link failed
                String info = GLES20.glGetProgramInfoLog(program);//put error into logs
                GLES20.glDeleteProgram(program);//delete program
                throw new RuntimeException("Could not link program: " + info);
            }
        }
        return program;
    }

    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(op + ": glError " + error);
        }
    }

}
