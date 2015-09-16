package com.example.harley.basicmenu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import com.example.harley.canvasview.CustomDrawableView;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


class Light{
    Random rand = new Random();
    private int radius=20;
    protected Point center;
    protected boolean KillMe;
    protected int[] color_argb = {255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)};

    Light(Point supplied){
        this.KillMe=false;
        this.center=supplied;
    }
    protected void Animate(){}
    protected void Draw(CustomDrawableView canvas){
        canvas.add_shape(center.x-radius,center.y-radius,radius*2,radius*2, new OvalShape(),0xffff0000);
    }
}
class FadeLight extends Light{
    private int opacity = 255;
    private int radius=150;
    FadeLight(Point supplied){
        super(supplied);
    }
    @Override
    protected void Animate(){
        opacity-=10;
        if(opacity<10) KillMe=true;
    }
    @Override
    protected void Draw(CustomDrawableView canvas){
        canvas.add_shape(center.x-radius,center.y-radius,radius*2,radius*2,new OvalShape(),Color.argb(opacity,color_argb[1],color_argb[2],color_argb[3]));
        super.Draw(canvas);
    }
}
class ShrinkLight extends Light{
    private int radius=150;
    ShrinkLight(Point supplied){
        super(supplied);
    }
    @Override
    protected void Animate(){
        radius*=0.95;
        if(radius<2)KillMe=true;
    }
    protected void Draw(CustomDrawableView canvas){
        canvas.add_shape(center.x-radius,center.y-radius,radius*2,radius*2,new OvalShape(),Color.argb(color_argb[0],color_argb[1],color_argb[2],color_argb[3]));
        super.Draw(canvas);
    }
}
class SpinLight extends Light{
    private double rotation = 2*Math.PI;
    private int size = 200;
    SpinLight(Point supplied){
        super(supplied);
    }
    @Override
    protected void Animate(){
        rotation*=.95;
        if (rotation<.1)KillMe=true;
    }
    @Override
    protected void Draw(CustomDrawableView canvas){
        Path path = new Path();
        path.moveTo((float)(center.x +size* Math.sin(rotation)),(float)(center.y-size*Math.cos(rotation)));
//        System.out.println("Top: " + center.x + ", " + (center.y - size));
        path.lineTo((float) (center.x + Math.cos(rotation+Math.PI / 6 ) * size), (float) (center.y + Math.sin(rotation+Math.PI / 6) * size));
//        System.out.println("Right: " + (center.x + Math.cos(Math.PI / 12) * size) + ", " + (center.y + Math.sin(Math.PI / 12) * size));
        path.lineTo((float) (center.x - Math.cos(-rotation+Math.PI / 6) * size), (float) (center.y + Math.sin(-rotation+Math.PI / 6) * size));
//        System.out.println("Left: " + (center.x - Math.cos(Math.PI / 12) * size) + ", " + (center.y + Math.sin(Math.PI / 12) * size));
        path.lineTo((float)(center.x +size* Math.sin(rotation)),(float)(center.y-size*Math.cos(rotation)));
        canvas.add_custom_shape(new PathShape(path, canvas.getWidth(), canvas.getHeight()), Color.argb(color_argb[0], color_argb[1], color_argb[2], color_argb[3]));

        super.Draw(canvas);
    }

}
public class opt3Activity extends ActionBarActivity {

    private CustomDrawableView canvas;
    private ConcurrentLinkedQueue<Light> LightList = new ConcurrentLinkedQueue<Light>();

    Handler animate_handler= new Handler();
    public Runnable run_animate = new Runnable() {
        @Override
        public void run() {
            canvas.drawset.clear();
            canvas.invalidate();
            for(Light LightObject : LightList){
                LightObject.Animate();

                LightObject.Draw(canvas);
                if(LightObject.KillMe){
                    LightList.remove(LightObject);
                }

            }
            animate_handler.postDelayed(this,100);

        }



    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt3);
        Intent intent = getIntent();
        String color = intent.getStringExtra(menuActivity.EXTRA_COLOR);
        View view = findViewById(android.R.id.content);
        System.out.println(color);
        try {
            view.setBackgroundColor(Color.parseColor(color));
        } catch (Exception e) {
            System.out.println("Bad Color");
        }
        canvas = (CustomDrawableView) findViewById(R.id.LightsCanvasView);

        animate_handler.post(run_animate);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP) {
            if(event.getPressure()<.7)LightList.add(new FadeLight(convert_for_view(event.getRawX(), event.getRawY(), canvas)));
            else if(event.getPressure()>=.7)LightList.add(new SpinLight(convert_for_view(event.getRawX(), event.getRawY(), canvas)));



        }
        return true;
    }
    public Point convert_for_view(float raw_x,float raw_y,View view){
        int[] view_coords =new int[2];
        view.getLocationOnScreen(view_coords);
        float x = raw_x-view_coords[0];
        float y = raw_y-view_coords[1];

        Point point = new Point((int)x,(int)y);
        return point;
    }
    @Override
    protected void onPause(){
        animate_handler.removeCallbacks(run_animate);
        opt3Activity.super.onPause();
    }

}
