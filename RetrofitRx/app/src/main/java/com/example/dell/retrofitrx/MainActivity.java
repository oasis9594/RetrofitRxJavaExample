package com.example.dell.retrofitrx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button csvButton, popButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        csvButton=(Button)findViewById(R.id.button_csv);
        popButton=(Button)findViewById(R.id.button_pop);
        csvButton.setOnClickListener(this);
        popButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.button_csv)
        {
            Toast.makeText(this, "csv", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CSVActivity.class));
        }
        else if(id==R.id.button_pop)
        {
            Toast.makeText(this, "pop", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, PopulationActivity.class));
        }
    }
}
