package com.sundayfactory.testwizet.customView;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.sundayfactory.testwizet.R;
import com.sundayfactory.testwizet.customListiner.indexHolder;
import com.sundayfactory.testwizet.utils.SharedUtils;

public class IndexLayout extends LinearLayout {
	private Paint mPaint;
	private Rect TextBounds;
	private List<indexHolder> indexList;
	private int maxTextSize = 0;

	public IndexLayout(Context context) {
		super(context);
		Init(context);

	}

	public IndexLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init(context);

	}

	public IndexLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Init(context);
	}

	private void Init(Context context) {
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setTextAlign(Paint.Align.CENTER);
		TextBounds = new Rect();
		try {
			maxTextSize = (int) context.getResources().getDimension(R.dimen.indexTextSize);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void setAppList(List<indexHolder> mList) {
		indexList = mList;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (indexList != null && indexList.size() != 0) {
			int TextHeight = getHeight() / indexList.size();
			mPaint.setTextSize(TextHeight);
			mPaint.getTextBounds("A", 0, 1, TextBounds);
			mPaint.setTextAlign(Align.CENTER);
			if (maxTextSize < TextBounds.width()) {
				mPaint.setTextSize(maxTextSize);
			}
			int maxHeight  = getHeight() / indexList.size();
			
			for (int i = 0; i < indexList.size(); i++) {
				canvas.drawText(indexList.get(i).index, getWidth() / 2,
						maxHeight * (i+1)   , mPaint);
			}

		}

	}
}
