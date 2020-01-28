package com.example.g2048;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.g2048.Direction;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private View mContentView;
    private Simulation simulation;

    private void setSimulation(GameType type){
        this.simulation = new Simulation(this, type);
    }

    private GameType[] availableTypes = {new GameType(4,4), new GameType(3,3)};
    private int typeIndex = 0;
    private TextView typeView;

    private GameType getCurrentType(){
        while(this.typeIndex < 0)
            this.typeIndex+=this.availableTypes.length;
        this.typeIndex %= this.availableTypes.length;
        return this.availableTypes[this.typeIndex];
    }


    private void drawBoard(){
        int width = this.getCurrentType().getWidth();
        int height = this.getCurrentType().getHeight();

        LinearLayout boardLayout = findViewById(R.id.boardLayout);
        for(int k = 0; k<height; k++){
            RelativeLayout rel = new RelativeLayout(this, null, R.style.rel_row);
            LinearLayout row = new LinearLayout(this, null, R.style.row);
            for(int i = 0; i<width; i++){
                //id k*width+1+i
                TextView cell = new TextView(this, null, R.style.cell);
                int id = k*width+1+i;
                cell.setId(id);

                row.addView(cell);
                Log.d("app", "Created Cell "+id);
            }

//            rel.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//            boardLayout.addView(rel, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            rel.addView(row);
            rel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            boardLayout.addView(rel);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.fullscreen_content);
        this.typeView = findViewById(R.id.type);
        this.typeView.setText(this.getCurrentType().toString());

        this.drawBoard();
        this.setSimulation(this.getCurrentType());


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

        Button reset_button = (Button)findViewById(R.id.reset_button);
        reset_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setSimulation(getCurrentType());
            }
        });

        Button undo_button = (Button)findViewById(R.id.undo_button);
        undo_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                simulation.undo();
            }
        });

        Button type_left = (Button)findViewById(R.id.type_left);
        type_left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                typeIndex--;
                typeView.setText(getCurrentType().toString());
            }
        });

        Button type_right = (Button)findViewById(R.id.type_right);
        type_right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                typeIndex++;
                typeView.setText(getCurrentType().toString());
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();

    }
}
