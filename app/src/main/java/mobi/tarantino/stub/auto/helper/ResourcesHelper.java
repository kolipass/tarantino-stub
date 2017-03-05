package mobi.tarantino.stub.auto.helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.TypedValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import mobi.tarantino.stub.auto.R;

/**

 */

public class ResourcesHelper {

    private Context context;
    private Resources resources;

    @Inject
    public ResourcesHelper(@NonNull Context context) {
        this.context = context;
        resources = context.getResources();
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     *
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static Bitmap storeThumbnail(
            @NonNull ContentResolver cr,
            @NonNull Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND, kind);
        values.put(Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public static int dpToPx(int sizeInDP, @NonNull Resources resources) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, sizeInDP, resources
                        .getDisplayMetrics());
    }

    @Nullable
    public Drawable getVectorDrawable(@DrawableRes int drawableId) {
        return VectorDrawableCompat.create(context.getResources(), drawableId, null);
    }

    @ColorInt
    public int getColor(@ColorRes int colorId, Resources.Theme theme) {
        return ResourcesCompat.getColor(resources, colorId, theme);
    }

    @NonNull
    @ArrayRes
    public int[] getColorArray(@ArrayRes int arrayId) {
        return resources.getIntArray(arrayId);
    }

    public Bitmap generatePaymentStamp(String dateString) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                .payment_stamp);
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }

        Bitmap stampBitmap = bitmap.copy(bitmapConfig, true);

        Canvas stampCanvas = new Canvas(stampBitmap);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(18 * context.getResources().getDisplayMetrics().density);

        Rect textBound = new Rect();
        dateString = dateString.toUpperCase();
        textPaint.getTextBounds(dateString, 0, dateString.length(), textBound);

        Bitmap textBitmap = Bitmap.createBitmap(textBound.width() + 10, textBound.height() + 10,
                Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        textCanvas.drawColor(Color.TRANSPARENT);
        textCanvas.drawText(dateString, 5, textBound.height() + 5, textPaint);

        RenderScript renderScript = RenderScript.create(context);
        Allocation allocation = Allocation.createFromBitmap(renderScript, textBitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript, allocation.getElement
                ());
        blur.setInput(allocation);
        blur.setRadius(3.5f);
        blur.forEach(allocation);
        allocation.copyTo(textBitmap);

        stampCanvas.drawBitmap(textBitmap,
                (stampBitmap.getWidth() - textBound.width()) / 2,
                (stampBitmap.getHeight() - textBound.height() - 35) / 2,
                textPaint);

        return stampBitmap;
    }

    /**
     * A copy of the Android internals  insertImage method, this method populates the
     * meta data with DATE_ADDED and DATE_TAKEN. This fixes a common problem where media
     * that is inserted manually gets saved at the end of the gallery (because date is not
     * populated).
     *
     * @see android.provider.MediaStore.Images.Media#insertImage(ContentResolver, Bitmap, String,
     * String)
     */
    @Nullable
    public Uri insertImage(@Nullable Bitmap source,
                           String title,
                           String description) {
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;

        try {
            url = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = null;
                try {
                    imageOut = contentResolver.openOutputStream(url);
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    if (imageOut != null) {
                        imageOut.close();
                    }
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(contentResolver, id, Images
                        .Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(contentResolver, miniThumb, id, 50F, 50F, Images.Thumbnails
                        .MICRO_KIND);
            } else {
                contentResolver.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (url != null) {
                contentResolver.delete(url, null, null);
                url = null;
            }
        }


        return url;
    }

    @Nullable
    public Uri saveToLocalStorage(@NonNull Bitmap bitmap, String name) {
        File cache = new File(context.getExternalCacheDir(), "receipts");

        if (!cache.exists() && !cache.mkdirs()) return null;

        File tempshot = new File(context.getExternalCacheDir(), name + ".jpeg");
        if (!tempshot.exists()) {
            try {
                if (!tempshot.createNewFile()) throw new IOException();
            } catch (IOException ignored) {
            }
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempshot);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Uri.fromFile(tempshot);
    }


    public int dpToPx(int sizeInDP) {
        return dpToPx(sizeInDP, resources);
    }
}
