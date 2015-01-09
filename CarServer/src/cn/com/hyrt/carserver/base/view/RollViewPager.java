package cn.com.hyrt.carserver.base.view;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import cn.com.hyrt.carserver.R;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class RollViewPager extends ViewPager {
	private List<String> titleList;
	private TextView top_news_title;
	private List<String> imgUrlList;
	private List<View> dotList;
	private Context context;
	private RunnableTask runnableTask;
	private int currentPosition = 0;
			
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			RollViewPager.this.setCurrentItem(currentPosition);
			//保持一直去滚动
			startRoll();
		};
	};
	
	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//点击下去的时候，不允许父控件去拦截相应的事件
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			if(Math.abs(moveX-downX)>Math.abs(moveY-downY)){
				//x轴比Y轴移动的多,事件不可以被拦击，传递到当前viewpager上
				getParent().requestDisallowInterceptTouchEvent(true);
			}else{
				//y轴比x轴移动的多,事件需要被拦截，父控件响应事件(刷新操作)
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	};
	
	//xutils提供的异步下载图片的工具类(缓存图片(下载图片---->文件--(压缩)-->缓存（内存）))
	//（内存---->文件----->下载）
	//java((具有LRU算法，最近最少使用算法)Map<String,Bitmap>最前端，三级缓存)
	private BitmapUtils bitmapUtils;
	private MyAdapter adapter;
	private int downX;
	private int downY;
	private OnPageClick pageClick;
	private List<String> initUrls;
	//必须实现接口中方法，对应就是外层业务逻辑
	public interface OnPageClick{
		public abstract void click(String i);
	}
	
	//传递一个集合封装了多个view对象（view对象就是对应的一个个点）
	public RollViewPager(Context context,final List<View> dotList,OnPageClick pageClick) {
		super(context);
		this.pageClick = pageClick;
		this.context = context;
		this.dotList = dotList;
		runnableTask = new RunnableTask();
		bitmapUtils = new BitmapUtils(context);
		setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				top_news_title.setText(titleList.get(arg0));
				for(int i=0;i<imgUrlList.size();i++){
					if(i == arg0){
						dotList.get(arg0).setBackgroundResource(R.drawable.dot_focus);
					}else{
						dotList.get(i).setBackgroundResource(R.drawable.dot_normal);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	//从界面中移出去后调用的方法
	@Override
	protected void onDetachedFromWindow() {
		//移除当前handler中所有维护的任务
		handler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}

	public void initTitle(List<String> titleList, TextView top_news_title) {
		if(null!=top_news_title && null!=titleList && titleList.size()>0){
			top_news_title.setText(titleList.get(0));
		}
		
		this.titleList = titleList;
		this.top_news_title = top_news_title;
	}

	public void initImgUrl(List<String> imgUrlList) {
		this.imgUrlList = imgUrlList;
	}
	public void initUrls(List<String> initUrls) {
		this.initUrls = initUrls;
	}

	public void startRoll() {
		//0,数据填充viewpager
		if(adapter == null){
			adapter = new MyAdapter();
			this.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
		//延时3秒开启任务
		handler.postDelayed(runnableTask, 3000);
	}
	
	class RunnableTask implements Runnable{
		@Override
		public void run() {
			//维护一个算法，时时刻刻指向后一页
			currentPosition  = (currentPosition+1)%imgUrlList.size();	
			//发空消息  Handler消息机制()
			handler.obtainMessage().sendToTarget();
		}
	}
	class MyAdapter extends PagerAdapter{
		@Override
		public int getCount() {
			return imgUrlList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = View.inflate(context,R.layout.viewpager_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			bitmapUtils.display(imageView, imgUrlList.get(position));
			((RollViewPager)container).addView(view);
			//在view上响应按下事件，如果点中则不要去滑动
			view.setOnTouchListener(new OnTouchListener() {
				private long downTime;
				private int downX;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						handler.removeCallbacksAndMessages(null);
						downTime = System.currentTimeMillis();
						downX = (int) event.getX();
						break;
					case MotionEvent.ACTION_UP:
						startRoll();
						if(System.currentTimeMillis()-downTime<500 && event.getX() == downX){
							//响应点击事件
							pageClick.click(initUrls.get(position));
						}
						break;
					//取消(viewpager中嵌套了一个view)
					//打日志
					case MotionEvent.ACTION_CANCEL:
						startRoll();
						break;
					}
					return true;
				}
			});
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((RollViewPager)container).removeView((View)object);
		}
	}
}
