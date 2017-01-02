package com.dushyant.opengldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.media.effect.EffectFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Dushyant on 1/2/2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    static int EFFECT_VALUE = 0;
    private int[] mTextures = new int[2];
    private EffectContext mEffectContext;
    private Effect mEffect;
    private GLTextureRenderer glTextureRenderer = new GLTextureRenderer();
    private int mImageWidth;
    private int mImageHeight;
    private Context context;

    MyGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 glNotUsed, EGLConfig config) {
        //GL10 = no need to use this in GL20,rather use static implementation of GL20
        // Set the background frame color
//        GLES20.glClearColor(0.0f, 153.0f, 54.0f, 10.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 glNotUsed, int width, int height) {
//        GLES20.glViewport(0, 0, width, height);
        loadTextures();//load the texture for generating square
        glTextureRenderer.initializeOpenGL();//initialize OpenGL feature like program,ByteBuffer etc.
    }

    private void loadTextures() {
        // Generate textures
        GLES20.glGenTextures(2, mTextures, 0);

        // Load input bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.puppy);
        mImageWidth = bitmap.getWidth();
        mImageHeight = bitmap.getHeight();

        // Upload to texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // initialize texture parameters for generating square
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        //i here indicates the type of the value you want to specify
        //go to https://open.gl/textures for more details
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mEffectContext == null) {
            //Only need to do this once
            mEffectContext = EffectContext.createWithCurrentGlContext();//binds the EffectContext with current openGL context
        }
        if (EFFECT_VALUE != 0) {
            // if no effect is chosen, just render the original bitmap
            drawEffect();
            glTextureRenderer.renderTexture(mTextures[1]);
        } else {
            // render the result of applyEffect()
            glTextureRenderer.renderTexture(mTextures[0]);
        }
    }

    private void drawEffect() {
        EffectFactory effectFactory = mEffectContext.getFactory();
        if (mEffect != null) {
            mEffect.release();
        }
        // Initialize the correct effect
        switch (EFFECT_VALUE) {
            case 0:
                break;
            case 1:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_AUTOFIX);
                mEffect.setParameter("scale", 0.5f);
                break;
            case 2:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BLACKWHITE);
                mEffect.setParameter("black", .1f);
                mEffect.setParameter("white", .7f);
                break;
            case 3:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
                mEffect.setParameter("brightness", 2.0f);
                break;
            case 4:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CONTRAST);
                mEffect.setParameter("contrast", 1.4f);
                break;
            case 5:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_CROSSPROCESS);
                break;
            case 6:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DOCUMENTARY);
                break;
            case 7:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_DUOTONE);
                mEffect.setParameter("first_color", Color.YELLOW);
                mEffect.setParameter("second_color", Color.DKGRAY);
                break;
            case 8:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FILLLIGHT);
                mEffect.setParameter("strength", .8f);
                break;
            case 9:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FISHEYE);
                mEffect.setParameter("scale", .5f);
                break;
            case 10:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("vertical", true);
                break;
            case 11:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_FLIP);
                mEffect.setParameter("horizontal", true);
                break;
            case 12:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAIN);
                mEffect.setParameter("strength", 1.0f);
                break;
            case 13:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_GRAYSCALE);
                break;
            case 14:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_LOMOISH);
                break;
            case 15:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_NEGATIVE);
                break;
            case 16:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_POSTERIZE);
                break;
            case 17:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_ROTATE);
                mEffect.setParameter("angle", 180);
                break;
            case 18:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SATURATE);
                mEffect.setParameter("scale", .5f);
                break;
            case 19:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SEPIA);
                break;
            case 20:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_SHARPEN);
                break;
            case 21:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TEMPERATURE);
                mEffect.setParameter("scale", .9f);
                break;
            case 22:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_TINT);
                mEffect.setParameter("tint", Color.MAGENTA);
                break;
            case 23:
                mEffect = effectFactory.createEffect(EffectFactory.EFFECT_VIGNETTE);
                mEffect.setParameter("scale", .5f);
                break;
            default:
                break;
        }
        mEffect.apply(mTextures[0], mImageWidth, mImageHeight, mTextures[1]);
    }
}
