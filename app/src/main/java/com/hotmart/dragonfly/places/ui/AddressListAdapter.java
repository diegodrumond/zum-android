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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.check.ui.CheckListActivity;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.ui.CollectionRecyclerViewAdapter;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressListAdapter extends CollectionRecyclerViewAdapter<AddressListAdapter.ViewHolder, AddressResponseVO> {

    public AddressListAdapter(MyAddressActivity context) {
        super(context);
    }

    public AddressListAdapter(MyAddressActivity context, Collection<AddressResponseVO> dataset) {
        super(context, dataset);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.address_list_item, parent, false);

        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, AddressResponseVO object) {
        holder.setModel(object);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        @BindView(R.id.place_name)
        TextView mName;

        @BindView(R.id.place_address)
        TextView mAddress;

        @BindView(R.id.place_zip_code)
        TextView mZipCode;

        private long id;

        ViewHolder(View view) {
            super(view);
            mContext = view.getContext();
            ButterKnife.bind(this, view);
        }

        void setModel(AddressResponseVO model) {
            resetViews();
            mName.setText(model.getLabel());
            mAddress.setText(model.getFormattedAddress());
            id = model.getId();
            if (model.getZipCode() != null) {
                mZipCode.setText(String.format(mContext.getString(R.string.zip_code), model.getZipCode()));
            }
        }

        private void resetViews() {
            mName.setText(null);
            mAddress.setText(null);
            mZipCode.setText(null);
        }

        @OnClick(R.id.place_item)
        public void onClickItem() {

            mContext.startActivity(CheckListActivity.createIntent(mContext, id));

        }

    }
}
