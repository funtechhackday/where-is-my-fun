package fun.hackathon.whereismyfun.Adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fun.hackathon.whereismyfun.R;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {
    private String[] company;

    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_card_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SaleAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(company[position]);
    }

    @Override
    public int getItemCount() {
        return company.length;
    }

    public SaleAdapter(String[] company){
        this.company = company;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView)v.findViewById(R.id.info_text);
        }
    }
}
