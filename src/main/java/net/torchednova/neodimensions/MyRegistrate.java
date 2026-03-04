package net.torchednova.neodimensions;

import com.tterrag.registrate.AbstractRegistrate;

public class MyRegistrate extends AbstractRegistrate<MyRegistrate> {
    protected MyRegistrate(String modId) {
        super(modId);
    }

    public static MyRegistrate create(String modId)
    {
        return new MyRegistrate(modId);
    }


}
