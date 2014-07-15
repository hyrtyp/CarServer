package cn.com.hyrt.carserver.base.view;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class LimitLengthEditText extends EditText{

	private int textLength = -1;
	
	public LimitLengthEditText(Context context) {
		super(context);
		init();
	}

	public LimitLengthEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray t = getContext().obtainStyledAttributes(
				attrs,R.styleable.LimitLengthText);  
        textLength = t.getInt(R.styleable.LimitLengthText_textLength, -1);  
        init();
	}

	public LimitLengthEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray t = getContext().obtainStyledAttributes(
				attrs,R.styleable.LimitLengthText);  
        textLength = t.getInt(R.styleable.LimitLengthText_textLength, -1);  
        init();
	}
	
	private void setTextLength(int length){
		this.textLength = length;
		init();
	}
	
	private void init(){
		addTextChangedListener(mTextWatcher);
	}
	
	private String beforeText;
	private TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence text, int start,
				int lengthBefore, int lengthAfter) {
			if(textLength < 0){
				return;
			}
			String content = getText().toString();
			if (content.length() > textLength) {
				AlertHelper.getInstance(getContext())
						.showCenterToast(R.string.text_count_beyond);
				if (beforeText != null) {
					setText(beforeText);
					setSelection(start);
				}
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence text, int start,
				int lengthBefore, int lengthAfter) {
			if (beforeText == null) {
				beforeText = text.toString();
			}
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
			beforeText = null;
		}
	};
	
}
