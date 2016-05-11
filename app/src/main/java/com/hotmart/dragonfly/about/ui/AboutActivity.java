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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.ContributorResponseVO;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.ContributorService;
import com.hotmart.dragonfly.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.about_list)
    RecyclerView mAboutList;
    @BindView(R.id.progress_loading)
    LinearLayout mProgressLoading;
    @BindView(R.id.error_request)
    LinearLayout mErrorRequest;
    private ContributorService mContributorService;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContributorsAdapter mContributorsAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected int getSelfNavigationMenuItem() {
        return R.id.nav_about;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ButterKnife.bind(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAboutList.setLayoutManager(mLayoutManager);
        mAboutList.setHasFixedSize(true);
        mContributorsAdapter = new ContributorsAdapter(new ArrayList<ContributorResponseVO>());
        mAboutList.setAdapter(mContributorsAdapter);
        getContributors();
    }

    private void getContributors() {
        mContributorService = ApiServiceFactory.createAnonymousService(ContributorService.class);

        mContributorService.get().enqueue(new Callback<List<ContributorResponseVO>>() {
            @Override
            public void onResponse(Call<List<ContributorResponseVO>> call,
                    Response<List<ContributorResponseVO>> response) {
                if (response.isSuccessful()) {
                    mProgressLoading.setVisibility(View.GONE);
                    mAboutList.setVisibility(View.VISIBLE);
                    mContributorsAdapter.addItems(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<List<ContributorResponseVO>> call, Throwable t) {
                showError();
            }
        });
    }

    private void showError() {
        mProgressLoading.setVisibility(View.GONE);
        mAboutList.setVisibility(View.GONE);
        mErrorRequest.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.try_again)
    public void onClickTryAgain(View v) {
        mProgressLoading.setVisibility(View.VISIBLE);
        mAboutList.setVisibility(View.GONE);
        mErrorRequest.setVisibility(View.GONE);
        getContributors();
    }
}
