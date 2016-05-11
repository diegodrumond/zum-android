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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.ui.DividerItemDecoration;
import com.hotmart.dragonfly.ui.PageableLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.LinkedHashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAddressActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<List<AddressResponseVO>> {

    public static final int LOADER_PLACES_ID = 0;

    public static final int PLACE_ADD_REQUEST = 1;

    private static final int ROWS = 20;

    @BindView(R.id.addressRecycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView mAvloadingIndicatorView;

    public static Intent createIntent(Context context) {
        return new Intent(context, MyAddressActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.my_addres_title);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        mRecyclerView.setAdapter(new AddressListAdapter(this, new LinkedHashSet<AddressResponseVO>()));

        getSupportLoaderManager().initLoader(LOADER_PLACES_ID, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_ADD_REQUEST && resultCode == RESULT_OK) {
            getSupportLoaderManager().restartLoader(LOADER_PLACES_ID, null, this);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public Loader<List<AddressResponseVO>> onCreateLoader(int id, Bundle args) {
        return new PageableLoader(this, ROWS);
    }

    @Override
    public void onLoadFinished(Loader<List<AddressResponseVO>> loader, List<AddressResponseVO> data) {
        mAvloadingIndicatorView.setVisibility(View.GONE);
        if (data != null) {
            getAdapter().addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<AddressResponseVO>> loader) {
        getAdapter().clear();
    }

    private AddressListAdapter getAdapter() {
        return (AddressListAdapter) mRecyclerView.getAdapter();
    }
}
