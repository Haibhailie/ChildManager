package com.example.project.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

public class Utils {
    /**
     * Acquire Bitmap object from an Uri
     * Scale the photo to 1:1
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        Bitmap photo = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        Bitmap resizedPhoto = getResizedBitmap(photo, 100, 100);
        return resizedPhoto;
    }

    /**
     * Scale bitmap to 1:1
     */
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
