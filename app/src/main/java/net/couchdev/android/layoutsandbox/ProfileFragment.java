package net.couchdev.android.layoutsandbox;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Tim on 19.11.2016.
 */

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_profile, container, false);

        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.myItemsRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, parent, false);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
                // viewHolder.field.setText(mItems.get(i));
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler.setAdapter(adapter);

        RecyclerView recycler2 = (RecyclerView) rootView.findViewById(R.id.favoRecycler);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler2.setLayoutManager(layoutManager2);
        RecyclerView.Adapter adapter2 = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, parent, false);
                TextView owner = (TextView) view.findViewById(R.id.itemOwner);
                owner.setVisibility(View.VISIBLE);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler2.setAdapter(adapter2);

        final RecyclerView recycler3 = (RecyclerView) rootView.findViewById(R.id.shopRecycler);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler3.setLayoutManager(layoutManager3);
        RecyclerView.Adapter adapter3 = new RecyclerView.Adapter<CustomViewHolder>(){
            @Override
            public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, parent, false);
                TextView owner = (TextView) view.findViewById(R.id.itemOwner);
                owner.setVisibility(View.VISIBLE);
                return new CustomViewHolder(view);
            }
            @Override
            public void onBindViewHolder(CustomViewHolder holder, int position) {
            }
            @Override
            public int getItemCount() {
                return 5;
            }
        };
        recycler3.setAdapter(adapter3);

        final TextView shopHeader = (TextView) rootView.findViewById(R.id.shopHeader);
        shopHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recycler3.getVisibility() == View.INVISIBLE){
                    recycler3.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shopHeader.getLayoutParams();
                    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    shopHeader.setLayoutParams(params);
                } else{
                    recycler3.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) shopHeader.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    shopHeader.setLayoutParams(params);
                }
            }
        });

        ImageView shopImage = (ImageView) rootView.findViewById(R.id.shopImage);
        shopImage.getDrawable().setColorFilter(getContext().getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);

        return rootView;
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        public CustomViewHolder(View itemView) {
            super(itemView);
        }
    }
}
