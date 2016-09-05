package ru.bolshakova.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.bolshakova.shoppinglist.R;
import ru.bolshakova.shoppinglist.data.models.Buy;
import ru.bolshakova.shoppinglist.utils.CustomOnClickListener;

// адаптер для вывода списка покупок
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Buy> mBuyList;
    private CustomOnClickListener mCustomClickListener;

    public RecyclerViewAdapter(List<Buy> records, CustomOnClickListener customOnClickListener) {
        this.mBuyList = records;
        mCustomClickListener = customOnClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(v, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Buy buy = mBuyList.get(i);
        viewHolder.itemBuy.setText(buy.getItemBuy());
        viewHolder.amountBuy.setText(buy.getAmountBuy() + " рублей");
        if (buy.getStatusBuy()) viewHolder.mCardBuyImage.setImageResource(R.drawable.ic_shopping_basket_red_24dp);
    }

    @Override
    public int getItemCount() {
        return mBuyList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView itemBuy;
        private TextView amountBuy;
        private ImageView toBuyCard;
        private ImageView mCardBuyImage;
        private CustomOnClickListener mCustomOnClickListener;

        public ViewHolder(View itemView, CustomOnClickListener customOnClickListener) {
            super(itemView);
            mCustomOnClickListener = customOnClickListener;
            itemBuy = (TextView) itemView.findViewById(R.id.item_buy);
            amountBuy = (TextView) itemView.findViewById(R.id.amount_buy);
            toBuyCard = (ImageView) itemView.findViewById(R.id.to_buy_card);
            mCardBuyImage = (ImageView) itemView.findViewById(R.id.card_buy_image);

            toBuyCard.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mCustomOnClickListener !=null)
                mCustomOnClickListener.onBuyItemClickListener(getAdapterPosition());

        }
    }
}
