/*
 * This file is part of Zum.
 * 
 * Zum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Zum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Zum. If not, see <http://www.gnu.org/licenses/>.
 */
package com.hotmart.dragonfly.places.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.rest.service.AddressService;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.ui.CollectionRecyclerViewAdapter;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesListAdapter extends CollectionRecyclerViewAdapter<PlacesListAdapter.ViewHolder, AddressResponseVO> {

    public PlacesListAdapter(PlacesActivity context) {
        super(context);
    }

    public PlacesListAdapter(PlacesActivity context, Collection<AddressResponseVO> dataset) {
        super(context, dataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.places_list_item, parent, false);

        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, AddressResponseVO object) {
        holder.setModel(object);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        private Context mContext;

        @BindView(R.id.place_name)
        TextView mName;

        @BindView(R.id.place_address)
        TextView mAddress;

        @BindView(R.id.place_zip_code)
        TextView mZipCode;

        ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
        }

        void setModel(AddressResponseVO model) {
            resetViews();
            mName.setText(model.getLabel());
            mAddress.setText(model.getFormattedAddress());
            if (model.getZipCode() != null) {
                mZipCode.setText(String.format(mContext.getString(R.string.zip_code), model.getZipCode()));
            }
        }

        private void resetViews() {
            mName.setText(null);
            mAddress.setText(null);
            mZipCode.setText(null);
        }

        @OnClick(R.id.btn_menu)
        public void onClickContextMenu(View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.place_item_menu);
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @OnClick(R.id.place_item)
        public void onClickItem() {
            Snackbar.make(itemView, R.string.not_implemented, Snackbar.LENGTH_LONG).show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
                case R.id.place_edit:
                    Snackbar.make(itemView, R.string.not_implemented, Snackbar.LENGTH_LONG).show();
                    break;
                case R.id.place_delete:
                        new AlertDialog.Builder(itemView.getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle(R.string.dialog_title_warning)
                                .setMessage(R.string.really_delete_place)
                                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final AddressResponseVO addressSelected =  getObject(getAdapterPosition());
                                        AddressService service = ApiServiceFactory.createService(AddressService.class, itemView.getContext());

                                        service.delete(addressSelected.getId()).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                remove(addressSelected);
                                            }
                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Snackbar.make(itemView, R.string.status_500, Snackbar.LENGTH_LONG).show();
                                            }
                                        });

                                    }

                                })
                                .setNegativeButton(R.string.dialog_no, null)
                                .show();
                        break;
            }
            return true;
        }
    }
}
