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
package com.hotmart.dragonfly.about.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.ContributorResponseVO;

import java.util.List;

public class ContributorsAdapter extends RecyclerView.Adapter<ContributorsAdapter.ViewHolderContributor> {

    private Context mContext;
    private List<ContributorResponseVO> mList;

    public ContributorsAdapter(List<ContributorResponseVO> list) {
        mList = list;
    }


    @Override
    public ViewHolderContributor onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_list, null, false);
        mContext = parent.getContext();
        return new ViewHolderContributor(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderContributor holder, int position) {
        ContributorResponseVO contributor = mList.get(position);
        holder.mName.setText(String.format(mContext.getString(R.string.dev_name),contributor.getName()));
        holder.mRole.setText(contributor.getRole());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addItems(List<ContributorResponseVO> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }

    static class ViewHolderContributor extends RecyclerView.ViewHolder{

        TextView mName;
        TextView mRole;

        public ViewHolderContributor(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.about_list_name);
            mRole = (TextView) itemView.findViewById(R.id.about_list_role);
        }
    }
}
