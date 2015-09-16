package com.example.harley.basicmenu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.harley.canvasview.CustomDrawableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class Package{
    public static Comparator<Package> AreaComparator = new Comparator<Package>() {
        @Override
        public int compare(Package lhs, Package rhs) {
            return rhs.getArea()-lhs.getArea();
        }
    };
    public static Comparator<Package> WidthComparator = new Comparator<Package>() {
        @Override
        public int compare(Package lhs, Package rhs) {
            return rhs.getWidth()-lhs.getWidth();
        }
    };
    public static Comparator<Package> HeightComparator=new Comparator<Package>() {
        @Override
        public int compare(Package lhs, Package rhs) {
            return rhs.getHeight()-lhs.getHeight();
        }
    };
    private int[] _location = new int[4];
    private int color;
    private static int counter=0;
    public int id = counter;
    private static Random rand = new Random();
    private static int min=30;
    private static int max=100;
    Package(){
        this._location[0]=0;
        this._location[1]=0;
        this._location[2]=rand.nextInt(max-min+1)+min;
        this._location[3]=rand.nextInt(max-min+1)+min;
        this.color = Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
        counter++;
    }
    public void setMinMax(int user_min, int user_max){
        if(user_max>0){
            max=user_max;
        }
        if(user_min>0){
            min=user_min;
        }
    }
    public void placePackage(int x,int y){
        if(x>=0){
            _location[0]=x;
        }
        if (y >= 0) {
            _location[1]=y;
        }
    }

    public boolean Equals(Package otherBox){
        int x1 = this._location[0];
        int y1=this._location[1];
        int width1 = this._location[2];
        int height1 = this._location[3];

        int x2= otherBox._location[0];
        int y2=otherBox._location[1];
        int width2 = otherBox._location[2];
        int height2= otherBox._location[3];

        if(y1+height1<y2 || y2+height2<y1){
            return false;
        }

        return !(x1 + width1 < x2 || x2 + width2 < x1);

    }
    public void renderPackage(CustomDrawableView canvas){
        canvas.add_shape(_location[0], _location[1], _location[2], _location[3], new RectShape(), color);
    }
    public int getWidth(){
        return _location[2];
    }
    public int getHeight(){
        return _location[3];
    }
    public int getArea(){
        return _location[2]*_location[3];
    }
}

class PackageManager{
    private static Random rand=new Random();
    private List<Package> _load = new ArrayList<Package>();
    private List<Package> _loaded = new ArrayList<Package>();
    private CustomDrawableView canvas;
    PackageManager(CustomDrawableView surface){
        this.canvas=surface;
    }
    public int LoadCount(){
        return _load.size();
    }
    public int LoadedCount(){
        return _loaded.size();
    }
    public void CompareWidth(){
        Collections.sort(_load,Package.WidthComparator);
    }
    public void CompareHeight(){
        Collections.sort(_load,Package.HeightComparator);
    }
    public void CompareArea(){
        Collections.sort(_load,Package.AreaComparator);
    }
    public void Shuffle(){
        Collections.shuffle(_load);
    }
    public void Reload(int num){
        _load.clear();
        for(int i=0;i<num;++i){
            Package box = new Package();
            _load.add(box);
        }
    }
    public void ShowLoad(){
        for(int i =0;i<_loaded.size();++i){
            _loaded.get(i).renderPackage(canvas);
        }

    }
    public int RoadieLoad(int allowed_tries){
        _loaded.clear();
        List<Package> _load_copy = new ArrayList<Package>();
        _load_copy.addAll(_load);
        int attempts =0;
        int cur_index = 0;

        while(attempts<=allowed_tries && _load_copy.size()>0){
            cur_index = cur_index%_load_copy.size();
            boolean taken = false;
            Package cur_package = _load_copy.get(cur_index);
            cur_package.placePackage(rand.nextInt(canvas.getWidth()-cur_package.getWidth()),rand.nextInt(canvas.getHeight()-cur_package.getHeight()));
            for(Package p:_loaded){
                if(cur_package.Equals(p)){
                    cur_index++;
                    attempts++;
                    taken=true;
                    break;
                }

            }
            if(!taken){
                _loaded.add(cur_package);
                _load_copy.remove(cur_index);
                attempts=0;

            }
        }
        return _loaded.size();
    }
    public int sortedLoad(){
        int cur_index=0;
        _loaded.clear();
        List<Package> _load_copy = new ArrayList<Package>();
        _load_copy.addAll(_load);
        int cur_x =0;
        int cur_y=0;
        int highest=0;
        while(_load_copy.size()>0){
            if(cur_index==_load_copy.size()){
                break;
            }
            Package cur_package = _load_copy.get(cur_index);
            if(cur_package.getHeight()>highest){
                highest=cur_package.getHeight();
            }
            if(cur_x+cur_package.getWidth()>canvas.getWidth()) {
                cur_y+=highest;
                cur_x=0;
                highest=0;
            }
            if(cur_y+cur_package.getHeight()>canvas.getHeight()){
                cur_index++;
                continue;
            }

            cur_package.placePackage(cur_x,cur_y);
            _loaded.add(cur_package);
            _load_copy.remove(cur_index);
            cur_x+=cur_package.getWidth();
        }


        return  _loaded.size();
    }
}

public class opt2Activity extends ActionBarActivity {
    private Button menuPrompt;
    private CustomDrawableView _cdv;
    private String sorttype="Height";
    private PackageManager cur_manager=null;

    public void loadDifferently(View view){
        load_call(view, cur_manager);
    }
    public void newLoad(View view){
        cur_manager = new PackageManager(_cdv);
        cur_manager.Reload(150);
        load_call(view, cur_manager);
    }
    public void load_call(View view, PackageManager manager){
        if(manager==null)return;
        _cdv.drawset.clear();
        switch (sorttype){
            case "Area":
                manager.CompareArea();
                Toast.makeText(opt2Activity.this, "Packages loaded: "+manager.sortedLoad(), Toast.LENGTH_SHORT).show();
                break;

            case "Width":
                manager.CompareWidth();
                Toast.makeText(opt2Activity.this,"Packages loaded: "+ manager.sortedLoad(), Toast.LENGTH_SHORT).show();
                break;

            case "Height":
                manager.CompareHeight();
                Toast.makeText(opt2Activity.this,"Packages loaded: "+ manager.sortedLoad(), Toast.LENGTH_SHORT).show();
                break;
            case "Roadie":
                manager.Shuffle();
                Toast.makeText(opt2Activity.this,"Packages loaded: "+manager.RoadieLoad(100), Toast.LENGTH_SHORT).show();
                break;
        }

        manager.ShowLoad();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_opt2);
        Intent intent = getIntent();
        String color = intent.getStringExtra(menuActivity.EXTRA_COLOR);
        View view = findViewById(android.R.id.content);
        System.out.println(color);
        try {
            view.setBackgroundColor(Color.parseColor(color));
        } catch (Exception e) {
            System.out.println("Bad Color");
        }
        _cdv = (CustomDrawableView)findViewById(R.id.BoxCanvasView);

        menuPrompt= (Button)findViewById(R.id.buildMenu);
        menuPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup= new PopupMenu(opt2Activity.this, menuPrompt);
                popup.getMenuInflater().inflate(R.menu.sorttype, popup.getMenu());
                for(int i=0;i<popup.getMenu().size();++i){
                    if(popup.getMenu().getItem(i).getTitle() == sorttype){
                        popup.getMenu().getItem(i).setChecked(true);
                    }
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        sorttype=(String)item.getTitle();
                        item.setChecked(true);
                        return false;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

}



