package com.osaigbovo.udacity.bakingapp.util;

import android.graphics.Bitmap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

/**
 * Utility methods for working with colors.
 */
public class ColorUtils {

    private ColorUtils() {
    }

    public static final int IS_LIGHT = 0;
    public static final int IS_DARK = 1;
    public static final int LIGHTNESS_UNKNOWN = 2;

    /**
     * Set the alpha component of {@code color} to be {@code alpha}.
     */
    public static @CheckResult
    @ColorInt
    int modifyAlpha(@ColorInt int color,
                    @IntRange(from = 0, to = 255) int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

}
