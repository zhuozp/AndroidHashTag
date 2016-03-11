package com.ffc.flowlayout;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText editText;
	private RelativeLayout target_rl;
	private LinearLayout edit_ll;
	private FlowLayout bottons_ll;
	private FlowLayout targets_ll;
	private Drawable buttonclear;
	private Button addBt;
	private TextView mTempBtn;
	private String[] tars={
			"零食","usb","健身器材","盆栽","午休神器","养身"
	};
	public static final String TAG_ADD_HASHTAG = "hashtag_target";
	
	private String mInputStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		findViewById(R.id.container).setOnClickListener(mOnClickListener);
		buttonclear = getResources().getDrawable(R.drawable.button_clear);
		edit_ll = (LinearLayout) findViewById(R.id.edits_ll);
		target_rl = (RelativeLayout) findViewById(R.id.target_rl);
		bottons_ll = (FlowLayout) edit_ll.findViewById(R.id.buttons_ll);
		targets_ll = (FlowLayout) target_rl.findViewById(R.id.targets_ll);
		addEditText();
		addTarget();
	}
	
	private void addTarget() {
		// TODO Auto-generated method stub
		for(int i = 0;i<tars.length;i++){
			TextView button = new TextView(this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			button.setLayoutParams(params);
			button.setSingleLine();
			button.setGravity(Gravity.CENTER);
			button.setText(tars[i]);
			button.setTag("target");
			button.setBackgroundResource(R.drawable.normal_hashtag_tv_bg);
//			button.setCompoundDrawablesWithIntrinsicBounds(null, null, buttonclear, null);
			button.setOnClickListener(this);
			targets_ll.addView(button);
		}
	}


	private void addEditText() {
		// TODO Auto-generated method stub
		
		editText = new EditText(this);
		editText.setTextSize(14);
		editText.setTextColor(0xff000000);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		editText.setLayoutParams(params);
		editText.setSingleLine();
		editText.setGravity(Gravity.CENTER_VERTICAL);
		editText.setBackgroundDrawable(null);
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);   
		editText.requestFocus();  
		bottons_ll.addView(editText);
		editText.addTextChangedListener(mTextWatcher);
		editText.setOnFocusChangeListener(mOnFocusChangeListener);
		editText.setOnEditorActionListener(mOnEditorActionListener);
		editText.setOnKeyListener(mOKeyListener);
		openIme();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {   
			// 屏蔽非空白
			touchEditText(ev);
	        if (!isShouldHideInput(edit_ll, ev) || !isShouldHideInput(targets_ll, ev)) {  
	        	openIme();
	        } else {
	        	if (!TextUtils.isEmpty(mInputStr) && !TextUtils.isEmpty(mInputStr.trim())) {
					hideIme();
					addButton(mInputStr.trim());
					mInputStr = "";
					mTempBtn = null;
				}
	        	hideIme(); 
	        } 
	        return super.dispatchTouchEvent(ev);  
	    }  
	    // 必不可少，否则所有的组件都不会有TouchEvent了  
	    if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev); 
	}
	
	public  boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null) {  
	        //获取输入框当前的location位置  
	        int[] leftTop = { 0, 0 };  
	        v.getLocationInWindow(leftTop);  
	        
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX() > left && event.getX() < right  
	                && event.getY() > top && event.getY() < bottom) {  
	            // 点击的是输入框区域，保留点击EditText的事件  
	            return false;  
	        } else {  
	            return true;  
	        }  
	    }  
	    return false;  
	}  
	
	private void touchEditText(MotionEvent event) {
		int[] editLeftTop = {0, 0};
        editText.getLocationInWindow(editLeftTop);
        int editLeft = editLeftTop[0];  
        int editTop = editLeftTop[1];  
        int editBottom = editTop + editText.getHeight();  
        int editRight = editLeft + editText.getWidth(); 
        
        if (event.getX() > editLeft && event.getX() < editRight  
                && event.getY() > editTop && event.getY() < editBottom) {  
//        	editText.setEnabled(true);
			openIme();
			int count = bottons_ll.getChildCount();
			for (int i = 0; i < count - 1; i++) {
				bottons_ll.getChildAt(i).setSelected(false);
			}
        }
	}
	
	private void openIme() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		inputMethodManager.showSoftInput(editText, 0);
	}
	
	private void hideIme() {
		InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}
	
	View.OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.container:
				if (!TextUtils.isEmpty(mInputStr) && !TextUtils.isEmpty(mInputStr.trim())) {
					hideIme();
					addButton(mInputStr.trim());
					mInputStr = "";
					mTempBtn = null;
				}
				break;

			default:
				break;
			}
		}
	};
	
	View.OnKeyListener mOKeyListener = new View.OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_DEL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				Toast.makeText(MainActivity.this, "删除键", Toast.LENGTH_LONG).show();
				if (!TextUtils.isEmpty(mInputStr) && !TextUtils.isEmpty(mInputStr.trim()))  {
					return false;
				}
				if (mTempBtn != null) {
					bottons_ll.removeView(mTempBtn);
					mTempBtn = null;
					openIme();
				} else {
					int count = bottons_ll.getChildCount();
					if (count >= 2) {
						mTempBtn = (TextView) bottons_ll.getChildAt(count - 2);
						mTempBtn.setSelected(true);
					}
				}
            }
			return false;
		}
	};
	
	OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// TODO Auto-generated method stub
			if (actionId <= 7 && actionId >= 2) {
				Toast.makeText(v.getContext(), "按下键", Toast.LENGTH_LONG).show();
				if (!TextUtils.isEmpty(mInputStr) && !TextUtils.isEmpty(mInputStr.trim())) {
					addButton(mInputStr.trim());
					mInputStr = "";
					mTempBtn = null;
				}
			}
			return false;
		}
	};
	
	OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
//			if (!hasFocus) {
//				if (!TextUtils.isEmpty(mInputStr)) {
//					addButton(mInputStr);
//					mInputStr = "";
//				}
			
//			}
//			Toast.makeText(MainActivity.this, "onFocusChange" +hasFocus, Toast.LENGTH_LONG).show();
		}
	};
	
	TextWatcher mTextWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			mInputStr = s.toString();
			mTempBtn = null;
			int c = bottons_ll.getChildCount();
			for (int i = 0; i < c - 1; i++) {
				bottons_ll.getChildAt(i).setSelected(false);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
//			Toast.makeText(MainActivity.this, "afterTextChanged", Toast.LENGTH_LONG).show();
		}
	};
	
	public void addButton(String name) {
		bottons_ll.removeView(editText);
		TextView button = new TextView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		button.setLayoutParams(params);
		button.setSingleLine();
		button.setGravity(Gravity.CENTER_VERTICAL);
		button.setTag(TAG_ADD_HASHTAG);
		button.setTextColor(0xff763b3e);
		button.setText(name);
		button.setBackgroundResource(R.drawable.hash_tag_tv_bg);
		button.setSelected(false);
//		button.setCompoundDrawablesWithIntrinsicBounds(null, null, buttonclear, null);
		button.setOnClickListener(this);
		bottons_ll.addView(button);
		addEditText();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getTag()!=null&&"target".equals(v.getTag().toString())) {
			TextView button = (TextView) v;
			addButton(button.getText().toString());
		} else if (TAG_ADD_HASHTAG.equals((String)v.getTag())){
//			editText.setEnabled(false);
			openIme();
			mTempBtn = (TextView) v;
			int count = bottons_ll.getChildCount();
			for (int i = 0; i < count - 1; i++) {
				bottons_ll.getChildAt(i).setSelected(false);
			}
			v.setSelected(true);
		} else if (v instanceof EditText) {
//			editText.setEnabled(true);
			openIme();
			int count = bottons_ll.getChildCount();
			for (int i = 0; i < count - 1; i++) {
				bottons_ll.getChildAt(i).setSelected(false);
			}
		}
	}

}
