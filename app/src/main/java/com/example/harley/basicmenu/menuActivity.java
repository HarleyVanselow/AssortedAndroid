package com.example.harley.basicmenu;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;


public class menuActivity extends ActionBarActivity {
    public final static String EXTRA_COLOR = "com.example.harley.basicmenu.COLOR";
    String background = "#ffe36700";

    public void onRadioClick(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()){
            case R.id.radioButton0:
                if (checked)
                    background="#ffe36700";
                break;

            case R.id.radioButton1:
                if (checked)
                    background="#ffe3200e";
                break;
            case R.id.radioButton2:
                if (checked)
                    background="#ff5be32d";
                break;
            case R.id.radioButton3:
                if (checked)
                    background="#ff484be3";
                break;
        }
        System.out.println(view.getId());
        System.out.println(background);
    }

    public void opt1(View view){
        Intent intent = new Intent(this,opt1Activity.class);
        System.out.println(background);
        intent.putExtra(EXTRA_COLOR,background);
        startActivity(intent);
    }
    public void opt2(View view){
        Intent intent = new Intent(this,opt2Activity.class);
        intent.putExtra(EXTRA_COLOR,background);
        startActivity(intent);
    }
    public void opt3(View view){
        Intent intent = new Intent(this,opt3Activity.class);
        intent.putExtra(EXTRA_COLOR,background);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
