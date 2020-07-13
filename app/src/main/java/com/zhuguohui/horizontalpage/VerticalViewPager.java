package com.zhuguohui.horizontalpage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.List;

public class VerticalViewPager extends ViewGroup {
    // 用于滑动的类
    private Scroller scroller;
    // 用来跟踪触摸速度的类
    private VelocityTracker velocityTracker;
    // 当前的屏幕视图
    private int curScreen;
    // 默认的显示视图
    private int defaultScreen = 0;
    // 无事件的状态
    private static final int TOUCH_STATE_REST = 0;
    // 处于拖动的状态
    private static final int TOUCH_STATE_SCROLLING = 1;
    // 可以切换页面的最小滑动的速度
    private static final int SNAP_VELOCITY = 500;
    // 所处的状态
    private int touchState = TOUCH_STATE_REST;
    private int touchSlop;
    private float lastMotionY;
    private OnVerticalPageChangeListener verticalPageChangeListener;
    public VerticalViewPager(Context context) {
        this(context, null);
    }
    public VerticalViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    /**
     * 在构造器中进行一些初始化
     */
    public VerticalViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scroller = new Scroller(context);
        curScreen = defaultScreen;
        // ViewConfiguration.get(getContext()).getScaledTouchSlop()可以得到一个距离，ViewPager就是用这个距离来判断用户是否翻页
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop()/4;
    }
    public interface OnVerticalPageChangeListener {
        public void onVerticalPageSelected(int position);
    }
    /**
     * 提供一个页面改变的监听器
     */
    public void setOnVerticalPageChangeListener(OnVerticalPageChangeListener onVerticalPageChangeListener) {
        this.verticalPageChangeListener = onVerticalPageChangeListener;
    }
    public void setViewList(List<View> viewList) {
        for (int i = 0; i < viewList.size(); i++) {
            this.addView(viewList.get(i));
        }
        invalidate();
    }
    /**
     * 重写此方法为子View进行布局
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childheiht = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE) {
                int childWidth = childView.getMeasuredWidth();
                Log.e("==","===onLayout:"+childheiht+"==="+childView.getMeasuredHeight());
                childView.layout(0, childheiht, childWidth, childView.getMeasuredHeight() + childheiht);
                childheiht += childView.getMeasuredHeight();
            }
        }
    }
    /**
     * 重写此方法用来计算高度和宽度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 得到多少页(子View)并设置他们的宽和高
        for (int i = 0; i < getChildCount(); i++) {
            int height = measureHeight(heightMeasureSpec,  getChildAt(i));
            Log.e("==","==="+widthMeasureSpec+"==="+heightMeasureSpec+"==="+height);
            getChildAt(i).measure(widthMeasureSpec,height );
        }


//        View view = getChildAt(getCurrentItem());//注意:这里是有bug的,看文末的解决方式
//        if (view != null) {
//            // measure the current child view with the specified measure spec
//            view.measure(widthMeasureSpec, heightMeasureSpec);
//        }
//
//        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));


    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.max(result, specSize);
            }
        }
        return result;
    }
    /**
     * 根据目前的位置滚动到下一个视图位置
     * todo 只根据速度判断上滑还是下滑不准 有可能是下滑 并且fling<0 即在下滑松手时，可能用户突然手指上滑，导致fling<0
     *
     */
    public void snapToDestination(int fling) {
        int screenHeight =  getChildAt(getCurScreen()).getMeasuredHeight();;
        Log.e("===","===getCurScreen:"+getCurScreen()+"==screenHeight"+screenHeight+"==getChildAt(getCurScreen()).getY()："
                + getChildAt(getCurScreen()).getY()+"===getHeight()"+getHeight()+"==getScrollY()"+getScrollY()+"======fling"+fling);
        // 根据View的高度以及滑动的值来判断是哪个View
//        int destScreen = (getScrollY() + screenHeight/2) / screenHeight;

        Log.e("===","===getCurScreen:"+dy);
        if(dy>0){
            //往下滑，即可能往上一条
            if(getScrollY()-fling<screenHeight*(getCurScreen())-500){
//            snapToScreen(curScreen);
                Log.e("===","===回滚上一条:"+getScrollY()+"==="+( -screenHeight*(getCurScreen()+1)+ getScrollY()));
                curScreen--;
                scroller.startScroll(0, getScrollY(), 0,  screenHeight*(getCurScreen())-getScrollY());
            }else if(getScrollY()-fling<screenHeight*(getCurScreen())||fling<0){
                Log.e("===","===回滚:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
                scroller.startScroll(0, getScrollY(), 0,  screenHeight*(getCurScreen())-getScrollY());
            }else{
                Log.e("===","===正常滚动回:"+getScrollY()+"==="+(getScrollY()-fling));
//                scroller.startScroll(0, getScrollY(), 0,  getScrollY()-fling);
            }
            invalidate();
        }else  if(dy<0) {
            //往上滑，即可能往下一条
          if(getScrollY()+getHeight()-fling>screenHeight*(getCurScreen()+1)+500){
              Log.e("===","===下页:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
                curScreen++;
              snapToScreen(curScreen);
//              scroller.startScroll(0, getScrollY(), 0,  screenHeight*(getCurScreen()+1)-getScrollY());
            } else if(getScrollY()+getHeight()-fling  >screenHeight*(getCurScreen()+1)||fling>0){
              Log.e("===","===到底部:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
              scroller.startScroll(0, getScrollY(), 0, screenHeight*(getCurScreen()+1)-getScrollY()-getHeight());
              invalidate();
            } else{
              Log.e("===","===正常上滑:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
//                scroller.startScroll(0, getScrollY(), 0,  getScrollY()-fling);
//                invalidate();
            }
        }else{
            if(getScrollY()-fling<screenHeight*(getCurScreen())){
                Log.e("===","===回滚11:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
                scroller.startScroll(0, getScrollY(), 0,  screenHeight*(getCurScreen())-getScrollY());
                invalidate();
            }else if(getScrollY()+getHeight()-fling  >screenHeight*(getCurScreen()+1)){
                Log.e("===","===next11:"+getScrollY()+"==="+(-screenHeight*(getCurScreen())+ getScrollY()));
                scroller.startScroll(0, getScrollY(), 0, screenHeight*(getCurScreen()+1)-getScrollY()-getHeight());
                invalidate();
            }

        }



    }
    public void snapToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        int height = getChildAt(getCurScreen()).getMeasuredHeight();
        if (getScrollY() != (whichScreen * height)) {
            final int delta = whichScreen * height - getScrollY();
            if(getScrollY()<0){
                scroller.startScroll(0,0, 0, 0, Math.abs(delta)/4);
            }else{
                scroller.startScroll(0, getScrollY(), 0, delta, Math.abs(delta)/4);
            }


            curScreen = whichScreen;
            invalidate();
            // 重新布局
            if (verticalPageChangeListener != null)
                verticalPageChangeListener.onVerticalPageSelected(whichScreen);
        }
    }
//    public void setToScreen(int whichScreen) {
//        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
//        curScreen = whichScreen;
//        scrollTo(0, whichScreen * getHeight());
//        if (verticalPageChangeListener != null)
//            verticalPageChangeListener.onVerticalPageSelected(whichScreen);
//    }
    /**
     * 获取当前页面
     * @return 当前页面值
     */
    public int getCurScreen() {
        return curScreen;
    }
    /**
     * 获取当前视图
     * @return 当前视图
     */
    public View getCurrentView() {
        return getChildAt(getCurScreen());
    }
    /**
     * 根据位置获取指定页面的视图
     * @param position 页面位置
     * @return 指定页面的视图
     */
    public View getView(int position) {
        return getChildAt(position);
    }
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }


    private  float startX;
    private  float startY;
    private float dx;
    private float dy;

    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            // 使用obtain方法得到VelocityTracker的一个对象
            velocityTracker = VelocityTracker.obtain();
        }
        // 将当前的触摸事件传递给VelocityTracker对象
        velocityTracker.addMovement(event);
        // 得到触摸事件的类型
        final int action = event.getAction();
        final float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastMotionY = y;
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltay = (int) (lastMotionY - y);
                lastMotionY = y;
                scrollBy(0, deltay);
                dx= event.getX()-startX;
                dy = event.getY()-startY;
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = this.velocityTracker;
                // 计算当前的速度
                velocityTracker.computeCurrentVelocity(1000);
                // 获得Y轴方向当前的速度
                int velocityY = (int) velocityTracker.getYVelocity();
//                if (velocityy > snap_velocity && curscreen > 0) {
//                    snaptoscreen(curscreen - 1);
//                } else if (velocityy < -snap_velocity && curscreen < getchildcount() - 1) {
//                    snaptoscreen(curscreen + 1);
//                } else {
//
//                }
                Log.e("--=","================="+velocityY);
                snapToDestination(velocityY/4);

                if (this.velocityTracker != null) {
                    this.velocityTracker.recycle();
                    this.velocityTracker = null;
                }
                touchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                touchState = TOUCH_STATE_REST;
                break;
        }
        return true;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (touchState != TOUCH_STATE_REST)) {
            return true;
        }
        final float y = ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(lastMotionY - y);
                if (xDiff > touchSlop) {
                    touchState = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                lastMotionY = y;
                touchState = scroller.isFinished() ? TOUCH_STATE_REST                  : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                touchState = TOUCH_STATE_REST;
                break;
        }
        return touchState != TOUCH_STATE_REST;
    }
}