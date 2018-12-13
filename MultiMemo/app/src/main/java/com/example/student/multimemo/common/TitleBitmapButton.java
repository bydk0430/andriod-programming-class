package com.example.student.multimemo.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.student.multimemo.R;


public class TitleBitmapButton extends AppCompatButton {

	// 베이스 컨텍스트
	Context context;

	// 페인트 요소
	Paint paint;

	// 기본색
	int defaultColor = 0xffffffff;

	// 기본 크기
	float defaultSize = 18F;

	// 기본 ScaleX
	float defaultScaleX = 1.0F;

	//	기본 글자체
	Typeface defaultTypeface = Typeface.DEFAULT;

	// 타이틀 텍스트
	String titleText = "";

	// 아이콘 Status : 0 - Normal, 1 - Clicked
	int iconStatus = 0;

	//	아이콘 평소 Bitmap
	Bitmap iconNormalBitmap;

	//	아이콘 클릭된  Bitmap
	Bitmap iconClickedBitmap;

	public static final int BITMAP_ALIGN_CENTER = 0;

	public static final int BITMAP_ALIGN_LEFT = 1;

	public static final int BITMAP_ALIGN_RIGHT = 2;


	int backgroundBitmapNormal = R.drawable.title_button;
	int backgroundBitmapClicked = R.drawable.title_button_clicked;


	//	정렬
	int bitmapAlign = BITMAP_ALIGN_CENTER;

	// 왼쪽 혹은 오른쪽 패딩
	int bitmapPadding = 10;

	// 페인트 변화의 플레그
	boolean paintChanged = false;

	private boolean selected;

	private int tabId;

	public TitleBitmapButton(Context context) {
		super(context);

		this.context = context;
		init();
	}

	public TitleBitmapButton(Context context, AttributeSet atts) {
		super(context, atts);

		this.context = context;
		init();
	}

	public void setTabId(int id) {
		tabId = id;
	}

	public void setSelected(boolean flag) {
		selected = flag;
		if (selected) {
			setBackgroundResource(backgroundBitmapClicked);
			paintChanged = true;
			defaultColor = Color.BLACK;
		} else {
			setBackgroundResource(backgroundBitmapNormal);
			paintChanged = true;
			defaultColor = Color.WHITE;
		}
	}

	public boolean isSelected() {
		return selected;
	}

	//	초기 설정
	public void init() {
		setBackgroundResource(backgroundBitmapNormal);

		paint = new Paint();
		paint.setColor(defaultColor);
		paint.setAntiAlias(true);
		paint.setTextScaleX(defaultScaleX);
		paint.setTextSize(defaultSize);
		paint.setTypeface(defaultTypeface);

		selected = false;
	}

	//	bitmap 아이콘 set
	public void setIconBitmap(Bitmap iconNormal, Bitmap iconClicked) {
		iconNormalBitmap = iconNormal;
		iconClickedBitmap = iconClicked;

	}

	public void setBackgroundBitmap(int resNormal, int resClicked) {
		backgroundBitmapNormal = resNormal;
		backgroundBitmapClicked = resClicked;

		setBackgroundResource(backgroundBitmapNormal);
	}

	// 핸들 터치 이벤트, 메인 스크린으로 이동
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:

				if (selected) {

				} else {

					setBackgroundResource(backgroundBitmapNormal);

					iconStatus = 0;

					paintChanged = true;
					defaultColor = Color.WHITE;

				}

				break;

			case MotionEvent.ACTION_DOWN:

				if (selected) {

				} else {
					setBackgroundResource(backgroundBitmapClicked);

					iconStatus = 1;

					paintChanged = true;
					defaultColor = Color.BLACK;
				}

				break;

		}
		// 스크린 리페인트
		invalidate();

		return true;
	}

	//  드로우 텍스트
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int curWidth = getWidth();
		int curHeight = getHeight();

		// 페인트 속성 적용
		if (paintChanged) {
			paint.setColor(defaultColor);
			paint.setTextScaleX(defaultScaleX);
			paint.setTextSize(defaultSize);
			paint.setTypeface(defaultTypeface);

			paintChanged = false;
		}

		// bitmap
		Bitmap iconBitmap = iconNormalBitmap;
		if (iconStatus == 1) {
			iconBitmap = iconClickedBitmap;
		}

		if (iconBitmap != null) {
			int iconWidth = iconBitmap.getWidth();
			int iconHeight = iconBitmap.getHeight();
			int bitmapX = 0;
			if (bitmapAlign == BITMAP_ALIGN_CENTER) {
				bitmapX = (curWidth-iconWidth)/2;
			} else if(bitmapAlign == BITMAP_ALIGN_LEFT) {
				bitmapX = bitmapPadding;
			} else if(bitmapAlign == BITMAP_ALIGN_RIGHT) {
				bitmapX = curWidth-bitmapPadding;
			}

			canvas.drawBitmap(iconBitmap, bitmapX, (curHeight-iconHeight)/2, paint);
		}

		Rect bounds = new Rect();
		paint.getTextBounds(titleText, 0, titleText.length(), bounds);
		float textWidth = ((float)curWidth - bounds.width())/2.0F;
		float textHeight = ((float)curHeight + bounds.height())/2.0F+4.0F;

		canvas.drawText(titleText, textWidth, textHeight, paint);

	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public int getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(int defaultColor) {
		this.defaultColor = defaultColor;
		paintChanged = true;
	}

	public float getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(float defaultSize) {
		this.defaultSize = defaultSize;
		paintChanged = true;
	}

	public float getDefaultScaleX() {
		return defaultScaleX;
	}

	public void setDefaultScaleX(float defaultScaleX) {
		this.defaultScaleX = defaultScaleX;
		paintChanged = true;
	}

	public Typeface getDefaultTypeface() {
		return defaultTypeface;
	}

	public void setDefaultTypeface(Typeface defaultTypeface) {
		this.defaultTypeface = defaultTypeface;
		paintChanged = true;
	}

	public int getBitmapAlign() {
		return bitmapAlign;
	}

	public void setBitmapAlign(int bitmapAlign) {
		this.bitmapAlign = bitmapAlign;
	}

	public int getBitmapPadding() {
		return bitmapPadding;
	}

	public void setBitmapPadding(int bitmapPadding) {
		this.bitmapPadding = bitmapPadding;
	}



}
