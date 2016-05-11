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
package com.hotmart.dragonfly.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.hotmart.dragonfly.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.hotmart.dragonfly.tools.LogUtils.LOGE;
import static com.hotmart.dragonfly.tools.LogUtils.makeLogTag;

public abstract class CollectionRecyclerViewAdapter<VH extends RecyclerView.ViewHolder, T>
        extends RecyclerView.Adapter<VH> {

    private static final String TAG = makeLogTag(CollectionRecyclerViewAdapter.class);

    private Collection<T> mDataset;

    protected int mBackground;

    public CollectionRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public CollectionRecyclerViewAdapter(Context context, Collection<T> dataset) {
        initDataset(dataset);
        TypedValue mTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
    }

    @SuppressWarnings("unchecked")
    private void initDataset(Collection<T> dataset) {
        try {
            if (dataset != null) {
                mDataset = dataset.getClass().newInstance();
                mDataset.addAll(dataset);
            } else {
                mDataset = Collections.synchronizedList(new ArrayList<T>());
            }
        } catch (Exception e) {
            String className = dataset != null
                               ? dataset.getClass().getCanonicalName()
                               : null;
            LOGE(TAG, String.format("fail on create new instance of dataset (%s)", className), e);
        }
    }

    protected abstract void onBindViewHolder(VH holder, int position, T object);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position, getObject(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @SuppressWarnings("unchecked")
    public T getObject(int position) {
        if (mDataset instanceof List) {
            return ((List<T>) mDataset).get(position);
        }

        return (T) mDataset.toArray()[position];
    }

    private int getPositionInDataset(T object) {
        if (mDataset instanceof List) {
            return ((List<T>) mDataset).indexOf(object);
        }

        return getDatasetAsArrayList().indexOf(object);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<T> getDatasetAsArrayList() {
        if (mDataset instanceof ArrayList) return (ArrayList<T>) mDataset;

        return new ArrayList<>((Collection<? extends T>) Arrays.asList(mDataset.toArray()));
    }

    public boolean add(T object) {
        boolean result = mDataset.add(object);
        if (result) notifyItemInserted(getPositionInDataset(object));
        return result;
    }

    public boolean addAll(@NonNull Collection<T> collection) {
        boolean result = mDataset.addAll(collection);
        notifyDataSetChanged();
        return result;
    }

    public boolean remove(T object) {
        int position = getPositionInDataset(object);
        notifyItemRemoved(position);
        return mDataset.remove(object);
    }

    public boolean removeAll(@NonNull Collection<T> collection) {
        boolean result = mDataset.removeAll(collection);
        notifyDataSetChanged();
        return result;
    }

    public void clear() {
        int size = mDataset.size();
        mDataset.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return mDataset.isEmpty();
    }
}
