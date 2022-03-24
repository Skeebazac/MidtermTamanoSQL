package com.example.midtermtamanosql;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ProductInformation extends AppCompatActivity {
    private ArrayList<Product> productArrayList;
    private RecyclerView productRecyclerView;
    private ImageButton backImageButton, addStudentButton, searchButton;
    private TextInputEditText txtSearch;
    MyDatabaseHelper db = new MyDatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        productArrayList = new ArrayList<>();
        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(productArrayList);
        initialize();
        setAdapter();
        addProductItems();

        backImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductInformation.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductInformation.this, AddProduct.class);
                startActivity(intent);
                finish();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = false;
                productArrayList.clear();
                if(txtSearch.getText().toString().equals("")) {
                    Toast.makeText(ProductInformation.this, "No Result Found!", Toast.LENGTH_SHORT).show();
                    addProductItems();
                    setAdapter();
                }
                else {
                    Cursor data = db.getdata();
                    if(data.getCount()==0){
                        Toast.makeText(ProductInformation.this, "No Result Found!", Toast.LENGTH_SHORT).show();
                        productArrayList.clear();
                        addProductItems();
                        setAdapter();
                    }
                    while(data.moveToNext()){
                        if(txtSearch.getText().toString().equals(data.getString(0))) {
                            productArrayList.add(new Product(data.getString(0),data.getString(1),data.getString(2),Double.parseDouble(data.getString(3)),Integer.parseInt(data.getString(4))));
                            setAdapter();
                            result = true;
                        }
                    }
                    if(result) {
                        Toast.makeText(ProductInformation.this, "Result Found!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ProductInformation.this, "No Result Found!", Toast.LENGTH_SHORT).show();
                        productArrayList.clear();
                        setAdapter();
                    }
                }
            }
        });
    }

    public void setAdapter() {
        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(productArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productRecyclerView.setAdapter(adapter);
        productRecyclerView.setHasFixedSize(true);
    }

    public void addProductItems() {
        Cursor data = db.getdata();
        if(data.getCount()==0){
            Toast.makeText(ProductInformation.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        while(data.moveToNext()){
            productArrayList.add(new Product(data.getString(0),data.getString(1),data.getString(2),Double.parseDouble(data.getString(3)),Integer.parseInt(data.getString(4))));
        }
    }

    public void initialize() {
        backImageButton = findViewById(R.id.backImageButton);
        addStudentButton = findViewById(R.id.addStudentButton);
        productRecyclerView = findViewById(R.id.studentRecyclerView);
        searchButton = findViewById(R.id.searchInfoButton);
        txtSearch = findViewById(R.id.txtInfoSearch);
    }
}