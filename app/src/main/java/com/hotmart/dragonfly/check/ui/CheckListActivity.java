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
package com.hotmart.dragonfly.check.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.request.VerificationRequestVO;
import com.hotmart.dragonfly.rest.model.response.AddressChecklistItemResponseVO;
import com.hotmart.dragonfly.rest.model.response.AddressDetailResponseVO;
import com.hotmart.dragonfly.rest.model.response.CheckLastVO;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.VerificationService;
import com.hotmart.dragonfly.ui.AddressDetailLoader;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<AddressDetailResponseVO> {

    @BindView(R.id.mRecycleView)
    RecyclerView mRecyclerView;
    @BindView(R.id.pin_image)
    ImageView mPinImage;
    @BindView(R.id.place_name)
    TextView mPlaceName;
    @BindView(R.id.place_address)
    TextView mPlaceAddress;
    @BindView(R.id.place_zip_code)
    TextView mPlaceZipCode;
    @BindView(R.id.sign_up_next)
    Button mSignUpNext;
    @BindView(R.id.buttonLayout)
    FrameLayout mButtonLayout;
    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView mAvloadingIndicatorView;

    private long idAddress;
    private CheckListAdapter checkListAdapter;

    private Call<CheckLastVO> call;

    private AddressDetailResponseVO data;

    public static Intent createIntent(Context context, long id) {
        Intent intent = new Intent(context, CheckListActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        idAddress = getIntent().getLongExtra("id", -1);
        List<AddressChecklistItemResponseVO> list = new ArrayList<>();

        checkListAdapter = new CheckListAdapter(this, list);
        setTitle("");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(checkListAdapter);

        mPinImage.setVisibility(View.GONE);
        mPlaceName.setTextColor(ContextCompat.getColor(this, R.color.sea));

        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    private void setContent(AddressDetailResponseVO data) {
        setTitle(data.getLabel());

        mPlaceName.setText(data.getLabel());
        mPlaceAddress.setText(data.getFormattedAddress());
        if (data.getZipCode() != null) {
            mPlaceZipCode.setText(String.format(getString(R.string.zip_code), data.getZipCode()));
        }
    }

    @Override
    public Loader<AddressDetailResponseVO> onCreateLoader(int id, Bundle args) {
        return new AddressDetailLoader(this, idAddress);
    }

    @Override
    public void onLoadFinished(Loader<AddressDetailResponseVO> loader, AddressDetailResponseVO data) {
        mAvloadingIndicatorView.setVisibility(View.GONE);
        if (data != null) {
            List<AddressChecklistItemResponseVO> list = data.getChecklistItems();
            List<AddressChecklistItemResponseVO> listTemp = new ArrayList<>();

            for (AddressChecklistItemResponseVO item : list) {
                if (item.isAvailable()) {
                    listTemp.add(item);
                }
            }

            checkListAdapter.addAll(listTemp);
            setContent(data);

            this.data = data;
        }
    }

    @Override
    public void onLoaderReset(Loader<AddressDetailResponseVO> loader) {

    }

    @OnClick(R.id.sign_up_next)
    void onClickButton(View view) {
        List<Long> ids = checkListAdapter.getCheckedItems();

        if (ids.isEmpty()) {
            Snackbar.make(view, "Selecione ao menos um item para concluir a verifacação", Snackbar.LENGTH_SHORT).show();
        } else {
            this.saveCheckList(ids);
        }
    }

    private void saveCheckList(List<Long> ids) {
        VerificationRequestVO request = new VerificationRequestVO(idAddress, ids);
        VerificationService service = ApiServiceFactory.createService(VerificationService.class, this);
        call = service.post(request);
        call.enqueue(new Callback<CheckLastVO>() {
            @Override
            public void onResponse(Call<CheckLastVO> call, Response<CheckLastVO> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED) {
                    startActivity(ShareChecklistActivity.createIntent(CheckListActivity.this, data));
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.sign_up_next),
                                  "Detectamos um erro, tente novamente mais tarde.",
                                  Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckLastVO> call, Throwable t) {
                Snackbar.make(findViewById(R.id.sign_up_next),
                              "Detectamos um erro, tente novamente mais tarde.",
                              Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
        super.onBackPressed();
    }
}
