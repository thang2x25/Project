package com.example.a21029381_nguyenmaiducthang;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPage extends AppCompatActivity {
    ListView lvProduct;
    TextView tvQuantity;
    Button btnAddProduct;
    private Context context;
    CustomListviewProduct adapter;
    AutoCompleteTextView autoCompleteSearch;
    List<Product> productList = new ArrayList<>();
    List<String> productNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_page);
        autoCompleteSearch = findViewById(R.id.autoCompleteSearch);
        lvProduct = findViewById(R.id.lvProduct);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        context = this;

        fetchProductList();
        sendPost();
    }

    private void fetchProductList() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("Tên");
        Customer currentCustomer = new Customer(username, "");
        ApiClient.getApiService().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();  // Xoá danh sách cũ nếu có
                    productList.addAll(response.body());
                    adapter = new CustomListviewProduct(ProductPage.this, R.layout.custom_listview_product, productList, currentCustomer);
                    lvProduct.setAdapter(adapter);

                    productNameList.clear();
                    for (Product product : productList) {
                        productNameList.add(product.getName());
                    }

                    ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(
                            ProductPage.this,
                            android.R.layout.simple_dropdown_item_1line,
                            productNameList
                    );
                    autoCompleteSearch.setAdapter(searchAdapter);
                    autoCompleteSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedProductName = (String) parent.getItemAtPosition(position);

                            List<Product> filteredList = new ArrayList<>();
                            for (Product product : productList) {
                                if (product.getName().equalsIgnoreCase(selectedProductName)) {
                                    filteredList.add(product);
                                }
                            }

                            adapter = new CustomListviewProduct(ProductPage.this, R.layout.custom_listview_product, filteredList, currentCustomer);
                            lvProduct.setAdapter(adapter);
                        }
                    });
                    autoCompleteSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.toString().isEmpty()) {
                                adapter = new CustomListviewProduct(ProductPage.this, R.layout.custom_listview_product, productList, currentCustomer);
                                lvProduct.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                } else {
                    Toast.makeText(ProductPage.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: ", t);
                Toast.makeText(ProductPage.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void sendPost()
    {
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LinearLayout layout = new LinearLayout(context);
                layout.setBackgroundColor(Color.BLACK);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(50, 40, 50, 10);

                TextView tvTitle = new TextView(context);
                tvTitle.setText("Thêm sản phẩm");
                tvTitle.setTextColor(Color.WHITE);
                layout.addView(tvTitle);

                LinearLayout row1 = new LinearLayout(context);
                row1.setOrientation(LinearLayout.HORIZONTAL);

                TextView tvProductName = new TextView(context);
                tvProductName.setText("Tên sản phẩm:");
                tvProductName.setTextColor(Color.WHITE);
                tvProductName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText inputName = new EditText(context);
                inputName.setBackgroundColor(Color.WHITE);
                inputName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

                row1.addView(tvProductName);
                row1.addView(inputName);
                layout.addView(row1);

                LinearLayout row2 = new LinearLayout(context);
                row2.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                row2Params.setMargins(0, 20, 0, 0);
                row2.setLayoutParams(row2Params);

                TextView tvProductPrice = new TextView(context);
                tvProductPrice.setText("Giá tiền");
                tvProductPrice.setTextColor(Color.WHITE);
                tvProductPrice.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText inputPrice = new EditText(context);
                inputPrice.setBackgroundColor(Color.WHITE);
                inputPrice.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

                row2.addView(tvProductPrice);
                row2.addView(inputPrice);
                layout.addView(row2);

                LinearLayout row3 = new LinearLayout(context);
                row3.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams row3Params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                row3Params.setMargins(0, 20, 0, 0);
                row3.setLayoutParams(row3Params);

                TextView tvProducDesc = new TextView(context);
                tvProducDesc.setText("Mô tả sản phẩm");
                tvProducDesc.setTextColor(Color.WHITE);
                tvProducDesc.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText inputDesc = new EditText(context);
                inputDesc.setBackgroundColor(Color.WHITE);
                inputDesc.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

                row3.addView(tvProducDesc);
                row3.addView(inputDesc);
                layout.addView(row3);

                LinearLayout row4 = new LinearLayout(context);
                row4.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams row4Params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                row4Params.setMargins(0, 20, 0, 0);
                row4.setLayoutParams(row3Params);

                TextView tvProductImage = new TextView(context);
                tvProductImage.setText("Ảnh sản phẩm");
                tvProductImage.setTextColor(Color.WHITE);
                tvProductImage.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                EditText imgSource = new EditText(context);
                imgSource.setBackgroundColor(Color.WHITE);
                imgSource.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

                row4.addView(tvProductImage);
                row4.addView(imgSource);
                layout.addView(row4);

                builder.setView(layout);

                builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String productName = inputName.getText().toString();
                        String productPrice = inputPrice.getText().toString();
                        String productDesc = inputDesc.getText().toString();
                        String productImage = imgSource.getText().toString();

                        if (!productName.isEmpty() && !productPrice.isEmpty() && !productDesc.isEmpty() && !productImage.isEmpty()) {
                            int price = Integer.parseInt(productPrice);
                            String imageName = productImage;
                            if (productImage.contains(".")) {
                                String[] parts = productImage.split("\\.");
                                imageName = parts[parts.length - 1];
                            }

                            int imgResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
                            if (imgResId == 0) {
                                Toast.makeText(context, "Không tìm thấy ảnh trong drawable", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Product product = new Product(productName, price, productDesc, imgResId);

                            ApiClient.getApiService().sendPost(product).enqueue(new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    if (response.isSuccessful()) {
                                        // Sau khi thêm sản phẩm, gọi lại API để lấy danh sách sản phẩm mới
                                        fetchProductList();
                                        Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            String errorMessage = response.errorBody().string();
                                            Log.e("API_ERROR", "Lỗi response: " + response.code() + " - " + errorMessage);
                                            Toast.makeText(context, "Thêm sản phẩm thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.e("API_ERROR", "Lỗi khi đọc error body: " + e.getMessage());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(context, "Gọi Api thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, "Không được để trống", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Xoá trắng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputName.setText("");
                        inputPrice.setText("");
                        inputDesc.setText("");
                        imgSource.setText("");
                        inputName.requestFocus();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
