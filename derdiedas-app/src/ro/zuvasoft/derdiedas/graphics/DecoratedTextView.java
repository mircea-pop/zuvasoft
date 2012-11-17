package ro.zuvasoft.derdiedas.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import ro.zuvasoft.derdiedas.R;

public class DecoratedTextView extends TextView
{

	private Drawable thumbsUp;
	private Paint textPainter;
	private Rect textBounds;
	private int thumbUpWidth;
	private int thumbUpHeight;
	private int thumbAlpha;

	public DecoratedTextView(Context context)
	{
		super(context);
		init();
	}

	public DecoratedTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	public DecoratedTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		Resources res = getResources();
		thumbsUp = res.getDrawable(R.drawable.thumbs_up);
		thumbUpWidth = (int) res.getDimension(R.dimen.thumb_up_width);
		thumbUpHeight = (int) res.getDimension(R.dimen.thumb_up_height);
		textPainter = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPainter.setTextSize(getTextSize());
		textBounds = new Rect();
		thumbAlpha = 0;
	}

	public int getThumbAlpha()
	{
		return this.thumbAlpha;
	}

	public void setThumbAlpha(int thumbAlpha)
	{
		this.thumbAlpha = thumbAlpha;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		View parent = (View) getParent();
		int textWidth = (int) textPainter.measureText((String) getText());

		textPainter.getTextBounds((String) getText(), 0, getText().length(),
				textBounds);
		int textLeftRightOffset = (parent.getWidth() - textWidth) / 2;
		int imageYPos = (getHeight() - thumbUpHeight) / 2;
		int space = (int) getResources().getDimension(R.dimen.thumb_text_space);

		thumbsUp.setBounds(getLeft() + textLeftRightOffset - thumbUpWidth
				- space, imageYPos, getRight() - textWidth - space
				- textLeftRightOffset + thumbUpWidth, imageYPos + thumbUpHeight);
		thumbsUp.setAlpha(getThumbAlpha());
		thumbsUp.draw(canvas);

		canvas.save();
		canvas.translate(thumbUpWidth + space, 0);
		super.onDraw(canvas);
		canvas.restore();
	}
}
