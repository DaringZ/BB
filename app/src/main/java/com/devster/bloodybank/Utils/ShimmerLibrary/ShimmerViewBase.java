package com.devster.bloodybank.Utils.ShimmerLibrary;

/**
 * Created by MOD on 6/27/2018.
 */

public interface ShimmerViewBase {

    public float getGradientX();
    public void setGradientX(float gradientX);
    public boolean isShimmering();
    public void setShimmering(boolean isShimmering);
    public boolean isSetUp();
    public void setAnimationSetupCallback(ShimmerViewHelper.AnimationSetupCallback callback);
    public int getPrimaryColor();
    public void setPrimaryColor(int primaryColor);
    public int getReflectionColor();
    public void setReflectionColor(int reflectionColor);
}
