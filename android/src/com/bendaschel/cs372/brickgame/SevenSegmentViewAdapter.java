package com.bendaschel.cs372.brickgame;

import com.bendaschel.sevensegmentview.SevenSegmentView;

import java.util.Arrays;
import java.util.List;

public class SevenSegmentViewAdapter {

    private List<SevenSegmentView> mViews;

    /**
     * Views should be in order of  least significant to most significant digit
     * with the least significant starting at index 0.
     * @param views
     */
    public SevenSegmentViewAdapter(SevenSegmentView ... views) {
        mViews = Arrays.asList(views);
    }

    /**
     * Set the current value over the array of displays held by this adapter
     * @param value
     * @return true if entire value can be displayed by displays
     */
    public boolean setCurrentValue(int value) {
        int factor = 1;
        for (int i = 0; i < mViews.size(); i++ ){
            int displayValue = (value / factor) % 10;
            mViews.get(i).setCurrentValue(displayValue);
            factor *= 10;
        }
        return value / factor != 0;
    }

}
