package com.example.student.multimemo;

import java.io.OutputStream;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//	손글씨 뷰
public class HandwritingView extends View {

	public static final String TAG = "HandwritingView";

	// 언두 스텍
	Stack<Bitmap> undos = new Stack<Bitmap>();

	// 언두 최대치
	public static int maxUndos = 10;

	//	바뀐 플레그
	public boolean changed = false;

	// 캔버스 인스턴스
	Canvas mCanvas;

	// Bitmap 더블 버퍼링
	Bitmap mBitmap;

	// 페인트 인스턴스
	final Paint mPaint;

	// X 좌표
	float lastX;

	// Y 좌표
	float lastY;

    private final Path mPath = new Path();

    private float mCurveEndX;
    private float mCurveEndY;

    private int mInvalidateExtraBorder = 10;

    static final float TOUCH_TOLERANCE = 1;

    private static final boolean RENDERING_ANTIALIAS = true;
    private static final boolean DITHER_FLAG = true;

    private int mCertainColor = 0xFF000000;
    private float mStrokeWidth = 8.0f;

	// 페인트 겍채와 좌표 초기 설정
	public HandwritingView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 새로운 페인트 객체 생성
		mPaint = new Paint();
		mPaint.setAntiAlias(RENDERING_ANTIALIAS);
		mPaint.setColor(mCertainColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setDither(DITHER_FLAG);


		lastX = -1;
		lastY = -1;

		Log.i(TAG, "initialized.");

	}

	public HandwritingView(Context context) {
		super(context);

		// 새로운 페인트 객체 생성
		mPaint = new Paint();
		mPaint.setAntiAlias(RENDERING_ANTIALIAS);
		mPaint.setColor(mCertainColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setDither(DITHER_FLAG);


		lastX = -1;
		lastY = -1;

		Log.i(TAG, "initialized.");

	}

	// 언두 클리어
	public void clearUndo()
	{
		while(true) {
			Bitmap prev = undos.pop();
			if (prev == null) return;

			prev.recycle();
		}
	}

	// 언두 저장
	public void saveUndo()
	{
		if (mBitmap == null) return;

		while (undos.size() >= maxUndos){
			Bitmap i = (Bitmap)undos.get(undos.size()-1);
			i.recycle();
			undos.remove(i);
		}

		Bitmap img = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		canvas.drawBitmap(mBitmap, 0, 0, mPaint);

		undos.push(img);

		Log.i(TAG, "saveUndo() called.");
	}

	// 언두
	public void undo()
	{
		Bitmap prev = null;
		try {
			prev = (Bitmap)undos.pop();
		} catch(Exception ex) {
			Log.e(TAG, "Exception : " + ex.getMessage());
		}

		if (prev != null){
			drawBackground(mCanvas);
			mCanvas.drawBitmap(prev, 0, 0, mPaint);
			invalidate();

			prev.recycle();
		}

		Log.i(TAG, "undo() called.");
	}

	// 페인트 배경
	public void drawBackground(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(Color.argb(255, 255, 255, 255));
		}
	}

	// 페인트 특징 업데이트
	public void updatePaintProperty(int color, int size)
	{
		mPaint.setColor(color);
		mPaint.setStrokeWidth(size);
	}

	// 새이미지 생성
	public void newImage(int width, int height)
	{
		Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);

		mBitmap = img;
		mCanvas = canvas;

		drawBackground(mCanvas);

		changed = false;
		invalidate();
	}

	public Bitmap getImage() {
		return mBitmap;
	}

	// 이미지 셋
	public void setImage(Bitmap newImage)
	{
		changed = false;

		setImageSize(newImage.getWidth(),newImage.getHeight(),newImage);
		invalidate();
	}

	// 이미지 크기 셋
	public void setImageSize(int width, int height, Bitmap newImage)
	{
		if (mBitmap != null){
			if (width < mBitmap.getWidth()) width = mBitmap.getWidth();
			if (height < mBitmap.getHeight()) height = mBitmap.getHeight();
		}

		if (width < 1 || height < 1) return;

		Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		drawBackground(canvas);

		if (newImage != null) {
			canvas.setBitmap(newImage);
		}

		if (mBitmap != null) {
			mBitmap.recycle();
			mCanvas.restore();
		}

		mBitmap = img;
		mCanvas = canvas;

		clearUndo();
	}

	// 크기조정
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w > 0 && h > 0) {
			newImage(w, h);
		}
	}

	// bitmap 드로우
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}

	}

	// 핸들 터치 이벤트, 위 아래 이동
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:
				changed = true;

				Rect rect = touchUp(event, false);
				if (rect != null) {
                    invalidate(rect);
                }

		        mPath.rewind();

                return true;

			case MotionEvent.ACTION_DOWN:
				saveUndo();

				rect = touchDown(event);
				if (rect != null) {
                    invalidate(rect);
                }

				return true;

			case MotionEvent.ACTION_MOVE:
				rect = touchMove(event);
                if (rect != null) {
                    invalidate(rect);
                }

                return true;
		}

		return false;
	}

	//	프로세스이벤트 터치다운
    private Rect touchDown(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        lastX = x;
        lastY = y;

        Rect mInvalidRect = new Rect();
        mPath.moveTo(x, y);

        final int border = mInvalidateExtraBorder;
        mInvalidRect.set((int) x - border, (int) y - border, (int) x + border, (int) y + border);

        mCurveEndX = x;
        mCurveEndY = y;

        mCanvas.drawPath(mPath, mPaint);

        return mInvalidRect;
    }


    // 프로세스 이벤트 터치 무브
    private Rect touchMove(MotionEvent event) {
        Rect rect = processMove(event);

        return rect;
    }

    private Rect touchUp(MotionEvent event, boolean cancel) {
    	Rect rect = processMove(event);

        return rect;
    }

    // 프로세스 이동 좌표
    private Rect processMove(MotionEvent event) {

    	final float x = event.getX();
        final float y = event.getY();

        final float dx = Math.abs(x - lastX);
        final float dy = Math.abs(y - lastY);

        Rect mInvalidRect = new Rect();
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            final int border = mInvalidateExtraBorder;
            mInvalidRect.set((int) mCurveEndX - border, (int) mCurveEndY - border,
                    (int) mCurveEndX + border, (int) mCurveEndY + border);

            float cX = mCurveEndX = (x + lastX) / 2;
            float cY = mCurveEndY = (y + lastY) / 2;

            mPath.quadTo(lastX, lastY, cX, cY);

            // 컨트롤 포인트의 새로운 커브 연결
            mInvalidRect.union((int) lastX - border, (int) lastY - border,
                    (int) lastX + border, (int) lastY + border);

            // 마지막 포인트의 새로운 커브 연결
            mInvalidRect.union((int) cX - border, (int) cY - border,
                    (int) cX + border, (int) cY + border);

            lastX = x;
            lastY = y;

            mCanvas.drawPath(mPath, mPaint);
        }

        return mInvalidRect;
    }

	// jpeg 이미지를 이 경로로 저장
	public boolean Save(OutputStream outstream) {
		try {
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
			invalidate();

			return true;
		} catch (Exception e) {
			return false;
		}
	}


}
