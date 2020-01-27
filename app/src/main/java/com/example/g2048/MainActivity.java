package com.example.g2048;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import com.example.g2048.Direction;

public class MainActivity extends AppCompatActivity {

    private View mContentView;
    private Simulation simulation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.fullscreen_content);
        this.simulation = new Simulation(this);

        mContentView.setOnTouchListener(new com.example.g2048.OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                simulation.registerMove(Direction.UP);
            }
            public void onSwipeRight() {
                simulation.registerMove(Direction.RIGHT);
            }
            public void onSwipeLeft() {
                simulation.registerMove(Direction.LEFT);
            }
            public void onSwipeBottom() {
                simulation.registerMove(Direction.DOWN);
            }
        });

    }
}
