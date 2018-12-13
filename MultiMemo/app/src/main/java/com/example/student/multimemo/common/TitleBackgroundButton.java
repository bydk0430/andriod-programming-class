package com.example.student.multimemo.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.student.multimemo.R;

public class TitleBackgroundButton extends AppCompatButton {

	// 베이스 컨텍스트
	Context context;

	// 페인트 요소
	Paint paint;

	// 기본색
	int defaultColor = 0xff333333;

	// 기본 크기
	float defaultSize = 20F;

	// 기본 ScaleX
	float defaultScaleX = 1.0F;

	// 기본 글자체
	Typeface defaultTypeface = Typeface.DEFAULT_BOLD;

	// 타이틀 텍스트
	String titleText = "";

	// 페인트 변화에 대한 플레그
	boolean paintChanged = false;

	public TitleBackgroundButton(Context context) {
		super(context);

		this.context = context;
		init();
	}

	public TitleBackgroundButton(Context context, AttributeSet atts) {
		super(context, atts);

		this.context = context;
		init();
	}

	// 초기 설정
	public void init() {
		setBackgroundResource(R.drawable.title_background);

		paint = new Paint();
		paint.setColor(defaultColor);
		paint.setAntiAlias(true);
		paint.setTextScaleX(defaultScaleX);
		paint.setTextSize(defaultSize);
		paint.setTypeface(defaultTypeface);

	}


	// 핸들 터치 이벤트, 메인 스크린으로 이동
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:

				break;

			case MotionEvent.ACTION_DOWN:
				Toast.makeText(context, titleText, Toast.LENGTH_LONG).show();
				break;

		}

		// 스크린 리페인트
		invalidate();

		return true;
	}

	//	 드로우 텍스트
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
		}

		// 한계 계산
		Rect bounds = new Rect();
		paint.getTextBounds(titleText, 0, titleText.length(), bounds);
		float textWidth = ((float)curWidth - bounds.width())/2.0F;
		float textHeight = ((float)(curHeight) - bounds.height())/2.0F + bounds.height();

		//  드로우 타이틀 텍스트
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


}
