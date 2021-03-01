package com.assetBundle.asset;

import com.assetBundle.ObjectReader;
import com.common.StreamUtil;

public class NamedAsObject extends AsObject {

    private String name;

    public NamedAsObject(ObjectReader reader) throws Exception {
        super(reader);
        name = StreamUtil.readAlignedString(reader.getIs());
    }
}
