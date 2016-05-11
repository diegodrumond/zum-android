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
package com.hotmart.dragonfly.history.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.VerificationResponseVO;
import com.hotmart.dragonfly.tools.DateUtils;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolderHistory> {

    private Context mContext;
    private List<VerificationResponseVO> mList;

    public HistoryAdapter(List<VerificationResponseVO> list) {
        mList = list;
    }

    @Override
    public ViewHolderHistory onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_list, null, false);
        return new ViewHolderHistory(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderHistory holder, int position) {
        VerificationResponseVO verification = mList.get(position);
        holder.mLabel.setText(verification.getAddress().getLabel());
        holder.mVerificationDate.setText(DateUtils.formatDate(verification.getVerificationDate()));
        holder.mAddress.setText(String.format(mContext.getString(R.string.history_address),verification.getAddress().getAddress(),
                                              verification.getAddress().getNumber(),
                                              verification.getAddress().getNeighborhood(),
                                              verification.getAddress().getZipCode()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addItems(List<VerificationResponseVO> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }

    static class ViewHolderHistory extends RecyclerView.ViewHolder{

        TextView mLabel;
        TextView mVerificationDate;
        TextView mAddress;

        public ViewHolderHistory(View itemView) {
            super(itemView);
            mLabel = (TextView) itemView.findViewById(R.id.history_label);
            mVerificationDate = (TextView) itemView.findViewById(R.id.history_date);
            mAddress = (TextView) itemView.findViewById(R.id.history_address);
        }
    }
}
