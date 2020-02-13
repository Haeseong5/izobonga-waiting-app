package com.example.izobonga_waiting_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.izobonga_waiting_app.R;
import com.example.izobonga_waiting_app.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.MyViewHolder>   {
    private ArrayList<Customer> customers;
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;
    public ManagerAdapter(ArrayList<Customer> customers){
        this.customers = customers;
    }
    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate item_layout
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, null);

        MyViewHolder vh = new MyViewHolder(itemLayoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTicket.setText(customers.get(position).getWaitingNumber()+"");
        holder.tvPhone.setText(customers.get(position).getPhone());
        holder.tvPersonnel.setText(customers.get(position).getPersonnel()+"");
    }

    @Override
    public int getItemCount() {
        if(customers != null)
            return customers.size();
        else
            return 0;
    }
    public void addItem(Customer customer){
        customers.add(customer);
    }
    public void removeItem(int position ){
        customers.remove(position);
    }
    // inner static class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTicket;
        TextView tvPhone;
        TextView tvPersonnel;
        Button btCall;
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvTicket = itemLayoutView.findViewById(R.id.item_customer_ticket);
            tvPhone = itemLayoutView.findViewById(R.id.item_customer_phone);
            tvPersonnel = itemLayoutView.findViewById(R.id.item_customer_personnel);
            btCall = itemLayoutView.findViewById(R.id.item_customer_call_button);

            btCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, pos) ;
                        }
                    }
                }
            });
            // 아이템 클릭 이벤트 처리.
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        // 리스너 객체의 메서드 호출.
//                        if (mListener != null) {
//                            mListener.onItemClick(v, pos) ;
//                        }
//                    }
//                }
//            });
        }
    }
}


