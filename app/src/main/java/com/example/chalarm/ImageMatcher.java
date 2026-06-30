package com.example.chalarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageMatcher {
    private static final int THUMBNAIL_SIZE = 100;
    private static final float SIMILARITY_THRESHOLD = 0.75f;

    public static boolean matchImages(Bitmap capturedImage, Bitmap referenceImage) {
        Bitmap capturedThumb = Bitmap.createScaledBitmap(capturedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE, true);
        Bitmap referenceThumb = Bitmap.createScaledBitmap(referenceImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE, true);

        int matchCount = 0;
        int totalPixels = THUMBNAIL_SIZE * THUMBNAIL_SIZE;

        for (int y = 0; y < THUMBNAIL_SIZE; y++) {
            for (int x = 0; x < THUMBNAIL_SIZE; x++) {
                int capturedPixel = capturedThumb.getPixel(x, y);
                int referencePixel = referenceThumb.getPixel(x, y);

                if (arePixelsSimilar(capturedPixel, referencePixel)) {
                    matchCount++;
                }
            }
        }

        float similarity = (float) matchCount / totalPixels;
        return similarity >= SIMILARITY_THRESHOLD;
    }

    private static boolean arePixelsSimilar(int pixel1, int pixel2) {
        int r1 = (pixel1 >> 16) & 0xFF;
        int g1 = (pixel1 >> 8) & 0xFF;
        int b1 = pixel1 & 0xFF;

        int r2 = (pixel2 >> 16) & 0xFF;
        int g2 = (pixel2 >> 8) & 0xFF;
        int b2 = pixel2 & 0xFF;

        int diff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
        return diff < 100; // Adjust threshold as needed
    }
}