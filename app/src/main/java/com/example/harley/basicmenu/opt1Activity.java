package com.example.harley.basicmenu;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.media.Image;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


class node{
    public static int width = 10;
    private int x;
    private int y;
    private node left;
    private node right;

    node(int x,int y){
        this.x= x;
        this.y = y;
    }
    node(int x,int y,node left,node right){
        this.x= x;
        this.y = y;
        this.left = left;
        this.right=right;
    }
    public node getRight(){
        return right;
    }

    public node getLeft(){
        return left;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void linkr(node right){
        this.right = right;
    }
    public void linkl(node left){
        this.left=left;
    }
    @Override
    public boolean equals(Object comp){
        if(comp==null){return false;}
        if(getClass() != comp.getClass()){return false;}
        final node other = (node)comp;
        if(this.x == other.x && this.y == other.y){return true;}
        return false;
    }

}

public class opt1Activity extends ActionBarActivity {
    private CustomDrawableView _cdv;
    private TextView divisor_view;
    private int divisor_val;
    private static Random rand = new Random();
    private ArrayList<node> nodeArray = new ArrayList<node>();


    private node getRandNode(CustomDrawableView canvas, int division){
        int x = rand.nextInt(canvas.getWidth()/division)*division;
        int y = rand.nextInt(canvas.getHeight()/division)*division;
        node returnNode = new node(x,y);

        return new node(x,y);
    }
    public void makeListConnect(View view){
        makeList();
        connectTheDotsList();
    }
    public void makeLinkConnect(View view){
        makeList();
        connectTheDotsLinked();
    }
    public void makeList(){
        nodeArray.clear();
        _cdv.drawset.clear();
        int expectedPoints = (_cdv.getWidth()/divisor_val)*(_cdv.getHeight()/divisor_val);
        System.out.println("Canvas width: "+_cdv.getWidth());
        System.out.println("Canvas height: "+_cdv.getHeight());
        System.out.println("Divisions: "+divisor_val);
        System.out.println("Expected points: "+expectedPoints);
        System.out.println("Size: " + nodeArray.size());
        while(nodeArray.size()<expectedPoints){
            node newNode = getRandNode(_cdv,divisor_val);
            if(!nodeArray.contains(newNode)){
                nodeArray.add(newNode);
            }
        }
        ((android.widget.Button)findViewById(R.id.listInfo)).setText("Made " + nodeArray.size() + " Points");
        System.out.println("Done. Printing lines now...");

    }

    public void connectTheDotsList(){
        node prev_node =null;
        for(node cur_node:nodeArray){
            if(prev_node != null){
                _cdv.add_line(prev_node.getX(),prev_node.getY(),cur_node.getX(),cur_node.getY(),cur_node.width,cur_node.width,Color.argb(255,177,79,209));
            }
            prev_node = cur_node;
        }


    }
    public void connectTheDotsLinked(){
        int end_bound = ((int)(_cdv.getWidth()/divisor_val))*divisor_val;
        //Clear out linked list
        for(node nodes:nodeArray){
            nodes.linkl(null);
            nodes.linkr(null);
        }
        node head = nodeArray.get(0);
        node tail = nodeArray.get(0);
        System.out.println("Node Array size: "+nodeArray.size());
        for(node cur_node:nodeArray) {
            node focus = head;
            while (focus != null) {
                if ((focus.getX()<cur_node.getX() && cur_node.getY()==focus.getY())||(focus.getY()<cur_node.getY())) {
                    if (focus.getRight() == null) {
                        //System.out.println("Linked ("+cur_node.getX()+","+cur_node.getY()+") to right of ("+focus.getX()+","+focus.getY()+") and set as new tail " );
                        focus.linkr(cur_node);
                        cur_node.linkl(focus);
                        tail = cur_node;
                    }else if((focus.getRight().getX()>cur_node.getX() && focus.getRight().getY()==cur_node.getY())||(focus.getRight().getY()>cur_node.getY())){
                        //System.out.println("Linked ("+cur_node.getX()+","+cur_node.getY()+") to right of ("+focus.getX()+","+focus.getY()+")" );
                        focus.getRight().linkl(cur_node);
                        cur_node.linkr(focus.getRight());
                        focus.linkr(cur_node);
                        cur_node.linkl(focus);
                    }
                }
                else if ((focus.getX()>cur_node.getX() && focus.getY() == cur_node.getY())||(focus.getY()>cur_node.getY())) {
                    if (focus.getLeft() == null) {
                        //System.out.println("Linked ("+cur_node.getX()+","+cur_node.getY()+") to left of ("+focus.getX()+","+focus.getY()+") and set as new head " );
                        focus.linkl(cur_node);
                        cur_node.linkr(focus);
                        head = cur_node;
                    } else if((focus.getLeft().getX()<cur_node.getX() && focus.getLeft().getY()==cur_node.getY())||(focus.getLeft().getY()<cur_node.getY())){
                        //System.out.println("Linked (" + cur_node.getX() + "," + cur_node.getY() + ") to left of (" + focus.getX() + "," + focus.getY() + ")");
                        focus.getLeft().linkr(cur_node);
                        cur_node.linkl(focus.getLeft());
                        focus.linkl(cur_node);
                        cur_node.linkr(focus);

                    }

                }
                focus = focus.getRight();
            }
        }


                //Draw all nodes
                while (head.getRight() != null) {
                    //System.out.println("X: "+head.getX()+ " Y: "+head.getY());

                    _cdv.add_line(head.getX(), head.getY(), head.getRight().getX(), head.getRight().getY(), head.width, head.width, Color.argb(255, 177, 79, 209));
                    head = head.getRight();
                }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GestureDetector gest_scan = new GestureDetector(new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(velocityY<0){
                    divisor_val +=10;
                    divisor_view.setText(""+divisor_val);
                }else if(divisor_val>0){
                    divisor_val -=10;
                    divisor_view.setText(""+divisor_val);
                }

                return false;
            }
        });
        setContentView(R.layout.activity_opt1);
        Intent intent = getIntent();
        String color = intent.getStringExtra(menuActivity.EXTRA_COLOR);
        View view = findViewById(android.R.id.content);
        System.out.println(color);
        try{
        view.setBackgroundColor(Color.parseColor(color));}
        catch(Exception e){
            System.out.println("Bad Color");
        }

        _cdv = (CustomDrawableView)findViewById(R.id.nodeCanvas);
        divisor_view = (TextView) findViewById(R.id.divisor);
        divisor_val = Integer.parseInt(divisor_view.getText().toString());

        divisor_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gest_scan.onTouchEvent(event);
                return true;
            }
        });
    }

}
