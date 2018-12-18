package com.osaigbovo.udacity.bakingapp.util;

import com.osaigbovo.udacity.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class BakingImageAssets {

    private static final List<Integer> bakingImages = new ArrayList<Integer>() {{
        add(R.drawable.a);
        add(R.drawable.b);
        add(R.drawable.yellow_cake);
        add(R.drawable.cheese_cake);
    }};

    public static List<Integer> getBakingImages() {
        return bakingImages;
    }

}
