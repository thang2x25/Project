package com.example.a21029381_nguyenmaiducthang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomListviewProduct extends ArrayAdapter<Product> {
    private Context context;
    private int resource;
    private List<Product> objects;
    private LayoutInflater inflater;
    private Customer currentCustomer;


    public CustomListviewProduct(Context context, int resource, List<Product> objects, Customer currentCustomer) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentCustomer = currentCustomer;
    }


    public void updateList(List<Product> newProductList)
    {
        this.objects.clear();
        this.objects.addAll(newProductList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_listview_product, null);
            holder.imgProduct = (ImageView) convertView.findViewById(R.id.imgProduct);
            holder.tvProductName = (TextView) convertView.findViewById(R.id.tvProductName);
            holder.tvProductPrice = (TextView) convertView.findViewById(R.id.tvProductPrice);
            holder.tvProductDesc = (TextView) convertView.findViewById(R.id.tvProductDesc);
            holder.tvProductQuantity = (TextView) convertView.findViewById(R.id.tvQuantity);
            holder.btnMinus = (Button) convertView.findViewById(R.id.btnDecrease);
            holder.btnPlus = (Button) convertView.findViewById(R.id.btnIncrease);
            holder.btnBuy = (Button) convertView.findViewById(R.id.btnBuy);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = objects.get(position);

        int imgResId = product.getImage();
        if (imgResId != 0) {
            holder.imgProduct.setImageResource(imgResId);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.tvProductName.setText(String.valueOf(product.getName()));
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()));
        holder.tvProductDesc.setText(product.getDescription());

        final int[] quantity = {1};
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0]++;
                holder.tvProductQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                holder.tvProductQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderQuantity = quantity[0];
                int productPrice = product.getPrice();
                int total = orderQuantity * productPrice;
                String productName = product.getName() + " - " + currentCustomer.getUsername();
                Order order = new Order();
                order.setName(productName);
                order.setQuantity(orderQuantity);
                order.setTotal(total);
                ApiClient.getApiService().sendOrder(order).enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Lỗi khi đặt hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Toast.makeText(context, "Gọi API thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return convertView;
    }

    public class ViewHolder{
        TextView tvProductName, tvProductPrice, tvProductQuantity, tvProductDesc;
        ImageView imgProduct;
        Button btnBuy, btnMinus, btnPlus;
    }
}
