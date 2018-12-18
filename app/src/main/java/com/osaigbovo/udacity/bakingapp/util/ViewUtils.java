package com.osaigbovo.udacity.bakingapp.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Property;
import android.util.TypedValue;
import android.widget.ImageView;

import com.osaigbovo.udacity.bakingapp.R;

import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Utility methods for working with Views.
 */
public class ViewUtils {

    private ViewUtils() {
    }

    public static int getActionBarSize(@NonNull Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, value, true);
        int actionBarSize = TypedValue.complexToDimensionPixelSize(
                value.data, context.getResources().getDisplayMetrics());
        return actionBarSize;
    }

    /**
     * Determine if the navigation bar will be on the bottom of the screen, based on logic in
     * PhoneWindowManager.
     */
    public static boolean isNavBarOnBottom(@NonNull Context context) {
        final Resources res = context.getResources();
        final Configuration cfg = context.getResources().getConfiguration();
        final DisplayMetrics dm = res.getDisplayMetrics();
        boolean canMove = (dm.widthPixels != dm.heightPixels &&
                cfg.smallestScreenWidthDp < 600);
        return (!canMove || dm.widthPixels < dm.heightPixels);
    }


    public static final Property<Drawable, Integer> DRAWABLE_ALPHA
            = AnimUtils.createIntProperty(new AnimUtils.IntProp<Drawable>("alpha") {
        @Override
        public void set(Drawable drawable, int alpha) {
            drawable.setAlpha(alpha);
        }

        @Override
        public int get(Drawable drawable) {
            return drawable.getAlpha();
        }
    });

    public static final Property<ImageView, Integer> IMAGE_ALPHA
            = AnimUtils.createIntProperty(new AnimUtils.IntProp<ImageView>("imageAlpha") {
        @Override
        public void set(ImageView imageView, int alpha) {
            imageView.setImageAlpha(alpha);
        }

        @Override
        public int get(ImageView imageView) {
            return imageView.getImageAlpha();
        }
    });

    public static String capitaliseFirstLetter(String myString) {

        String[] strArray = myString.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap).append(" ");
        }
        return builder.toString();
    }

    public static String formatIngdedient(Context context, String name, double quantity, String measure) {

        String line = context.getResources().getString(R.string.recipe_details_ingredient_line);

        String quantityStr = String.format(Locale.US, "%s", quantity);
        if (quantity == (long) quantity) {
            quantityStr = String.format(Locale.US, "%d", (long) quantity);
        }

        return String.format(Locale.US, line, name, quantityStr, measure);
    }

}
