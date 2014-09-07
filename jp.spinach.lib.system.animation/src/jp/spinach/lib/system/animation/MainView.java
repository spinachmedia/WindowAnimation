package jp.spinach.lib.system.animation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("HandlerLeak")
public class MainView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    
    Activity activity;
    SurfaceHolder holder;
    Thread thread;
    Resources res;
    Canvas canvas;
    
    Paint paint;
    
    WindowAnimation windowAnimation;
    
    int scWidth;
    int scHeight;
    
    public MainView(Activity activity) {
        super(activity);
        holder = getHolder();
        holder.addCallback(this);
        this.activity = activity;
        res = getContext().getResources();
        paint = new Paint();
        windowAnimation = new WindowAnimation(res,R.drawable.s_window_sample);
    }
    
    @Override
    public void run() {
    	
    	
    	scWidth = getWidth();
    	scHeight = getHeight();
        windowAnimation.initializeSprite(0, 0, windowAnimation.window.getWidth(), windowAnimation.window.getHeight());
    	windowAnimation.setViewRect(0, 0, scWidth, (int)(scHeight/3));
    	windowAnimation.startOpenAnimation();
    	
        while(thread != null){
            try{
            	canvas = holder.lockCanvas();
            	canvas.drawColor(Color.WHITE);
            	canvas.drawBitmap(windowAnimation.window, windowAnimation.drawRect, windowAnimation.viewRect, paint);
            }catch(Exception e){
                e.printStackTrace();
                thread = null;
                activity.finish();
            }finally{
                try {
                    if(canvas!=null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    thread = null;
                    activity.finish();
                }
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            	if(windowAnimation.isOpen){
            		windowAnimation.startCloseAnimation();
            	}else{
            		windowAnimation.startOpenAnimation();
            	}
                break;
            default:
                break;
        }
        return true;
    }
    
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new Thread(this);
        thread.start();
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    
}
