package ru.bolshakova.shoppinglist.utils;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFont {

        private static CustomFont instance;
        private static Typeface typeface;

        public static CustomFont  getInstance(Context context) {
            synchronized (CustomFont.class) {
                if (instance == null) {
                    instance = new CustomFont();
                    typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/ds_note.ttf");
                }
                return instance;
            }
        }

        public Typeface getTypeFace() {
            return typeface;
        }
    }

