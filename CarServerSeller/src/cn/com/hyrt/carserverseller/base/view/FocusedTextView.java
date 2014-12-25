package cn.com.hyrt.carserverseller.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * @author awen
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedTextView(Context context) {
		super(context);
	}
	//当前控件并没有真正获得焦点，我们只是欺骗Android系统，让其以我获得焦点的方式去处理渲染
	@Override
	public boolean isFocused() {
		return true;
	}
}
