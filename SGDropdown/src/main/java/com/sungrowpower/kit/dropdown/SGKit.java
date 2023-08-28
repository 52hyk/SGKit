package com.sungrowpower.kit.dropdown;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class SGKit {
    private Application context;

    public void setContext(Application context) {
        this.context = context;
    }

    public Resources getResources(){
        return context.getResources();
    }

    public Application getContext(){
        return context;
    }

    private static class Holder {
        private static final SGKit INSTANCE = new SGKit();
    }

    public static SGKit getInstance() {
        return Holder.INSTANCE;
    }

}
