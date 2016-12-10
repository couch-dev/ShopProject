package net.couchdev.android.layoutsandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Tim on 19.11.2016.
 */

public class ShopFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_shop, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.salesList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_sale, R.id.itemTitle,
                new String[]{"Shop Item 1", "Shop Item 2", "Shop Item 3", "Shop Item 4", "Shop Item 5"});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SaleItemActivity.class);
                startActivity(intent);
            }
        });

        final SearchView shopSearch = (SearchView) rootView.findViewById(R.id.shopSearch);
        shopSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopSearch.setIconified(false);
            }
        });

        return rootView;
    }
}
