package vn.tapbi.photoeditor.feature.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class DrawableImageView extends AppCompatImageView implements View.OnTouchListener {
    float downX = 0;
    float downY = 0;
    float upX = 0;
    float upY = 0;

    int color;
    int size;

    Canvas canvas;
    Paint paint;
    Matrix matrix;

    public DrawableImageView(@NonNull Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public DrawableImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public DrawableImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public int getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        paint.setStrokeWidth(size);
    }

    public void setNewImage(Bitmap alteredBitmap, Bitmap bitmap, int color, int width) {
        canvas = new Canvas(alteredBitmap);
        paint = new Paint();
        matrix = new Matrix();

        setColor(color);
        setSize(width);
//        paint.setStrokeWidth(width);
        paint.setAntiAlias(true);

        canvas.drawBitmap(bitmap, matrix, paint);

        setImageBitmap(alteredBitmap);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = getPointerCoordinates(motionEvent)[0];
                downY = getPointerCoordinates(motionEvent)[1];
                break;
            case MotionEvent.ACTION_MOVE:
                upX = getPointerCoordinates(motionEvent)[0];
                upY = getPointerCoordinates(motionEvent)[1];
                canvas.drawLine(downX, downY, upX, upY, paint);
                invalidate();
                downX = upX;
                downY = upY;
                break;
            case MotionEvent.ACTION_UP:
                upX = getPointerCoordinates(motionEvent)[0];
                upY = getPointerCoordinates(motionEvent)[1];
                canvas.drawLine(downX, downY, upX, upY, paint);
                invalidate();
                break;
            default:
                break;
        }

        return true;
    }

    final float[] getPointerCoordinates(MotionEvent motionEvent) {
        final int index = motionEvent.getActionIndex();
        final float[] coordinates = new float[] {
                motionEvent.getX(index),
                motionEvent.getY(index)
        };
        Matrix matrix = new Matrix();
        getImageMatrix().invert(matrix);
        matrix.postTranslate(getScrollX(), getScrollY());
        matrix.mapPoints(coordinates);

        return coordinates;
    }
}
