package com.assetBundle.asset;

import com.common.ArrayInputStream;
import static com.common.StreamUtil.*;

public class GLTextureSettings {

    private int filterMode;
    private int aniso;
    private float mipBias;
    private int wrapMode;

    public GLTextureSettings(ArrayInputStream is) throws Exception {
        filterMode = readInt(is);
        aniso = readInt(is);
        mipBias = readInt(is);
        wrapMode = readInt(is);
        int wrapV = readInt(is);
        int wrapW = readInt(is);
    }

}
