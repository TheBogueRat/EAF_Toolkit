package com.bogueratcreations.eaftoolkit;

import android.graphics.drawable.Drawable;

/**
 * Created by jodyroth on 8/28/15.
 */
public class ListViewItem {
    public final Drawable icon;
    public final String title;
    public final String description;

    public ListViewItem(Drawable icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }
}
