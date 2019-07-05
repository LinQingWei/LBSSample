package cn.way.lbs.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationManager;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import cn.way.lbs.R;

public class DirectionView extends View {
    private static final float OFFSET = 0f;
    private static final String[] DIRECTIONS = new String[]{
            "北", "南", "西", "东"
    };
    private float[] mTextCenterLengths;

    private Handler mHandler;

    private Paint mPaint;
    private Paint mPen;
    private double mDir;
    private Bitmap mBmp;

    private int mBmpW;
    private int mBmpH;

    public DirectionView(Context context) {
        this(context, null);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        final int centerX = w / 2;
        final int centerY = h / 2;
        int r = w < h ? centerX : centerY;
        r -= 30;
        canvas.drawCircle(centerX, centerY, r, mPaint);

        canvas.drawText(DIRECTIONS[0], centerX - mTextCenterLengths[0], centerY - r + mTextCenterLengths[0] * 2 + OFFSET, mPen);
        canvas.drawText(DIRECTIONS[1], centerX - mTextCenterLengths[1], centerY + r - mTextCenterLengths[1] - OFFSET, mPen);
        canvas.drawText(DIRECTIONS[2], centerX - r + OFFSET, centerY + mTextCenterLengths[2], mPen);
        canvas.drawText(DIRECTIONS[3], centerX + r - mTextCenterLengths[3] * 2, centerY + mTextCenterLengths[3], mPen);

        canvas.save();
        // 由于方向图标箭头向右, 有必要调整到向北
        canvas.rotate(-90, centerX, centerY);

        // 根据定位SDK获得的方向旋转箭头
        canvas.rotate((float) mDir, centerX, centerY);

        canvas.translate(centerX - mBmpW, centerY - mBmpH);
        canvas.drawBitmap(mBmp, 0, 0, null);
        canvas.restore();

        mHandler.sendEmptyMessageDelayed(0, 50);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public void updateDirection(double direction) {
        if (!Double.isNaN(direction)) {
            mDir = direction;
            invalidate();
        }
    }

    private void init(final Context context) {
        mHandler = new MyHandler(this);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);
        mPaint.setColor(Color.RED);

        mPen = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPen.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP, 20));
        mPen.setColor(Color.BLACK);

        mBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_pointer);
        mBmpW = mBmp.getWidth() / 2;
        mBmpH = mBmp.getHeight() / 2;

        final int size = DIRECTIONS.length;
        mTextCenterLengths = new float[size];
        for (int i = 0; i < size; i++) {
            mTextCenterLengths[i] = mPen.measureText(DIRECTIONS[i]) / 2;
        }
    }

    private float getRawSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    private void handleMessage(Message msg) {
        TencentLocation location = TencentLocationManager.getInstance(getContext()).getLastKnownLocation();
        if (location != null) {
            double direction = location.getExtra().getDouble(TencentLocation.EXTRA_DIRECTION);
            updateDirection(direction);
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<DirectionView> viewWeakReference;

        public MyHandler(DirectionView directionView) {
            viewWeakReference = new WeakReference<>(directionView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DirectionView view = viewWeakReference.get();
            if (view != null) {
                view.handleMessage(msg);
            }
        }
    }
}
