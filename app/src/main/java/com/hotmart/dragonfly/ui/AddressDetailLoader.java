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
import android.support.v4.content.AsyncTaskLoader;

import com.hotmart.dragonfly.rest.model.response.AddressDetailResponseVO;
import com.hotmart.dragonfly.rest.service.AddressService;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Response;

import static com.hotmart.dragonfly.tools.LogUtils.LOGE;

public class AddressDetailLoader extends AsyncTaskLoader<AddressDetailResponseVO> {

    private boolean mIsLoading;
    private boolean mHasError;
    private static final String TAG = AddressDetailLoader.class.getSimpleName();
    private long id;

    public AddressDetailLoader(Context context, long id) {
        super(context);
        this.id = id;
    }

    @Override
    public AddressDetailResponseVO loadInBackground() {

        AddressDetailResponseVO addressDetailResponseVO = null;
        AddressService service = ApiServiceFactory.createService(AddressService.class, getContext());

        try {
            Response<AddressDetailResponseVO> response = service.get(this.id).execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                mHasError = false;
                addressDetailResponseVO = response.body();
            }else{
                mHasError = true;
            }
        } catch (IOException e) {
            LOGE(TAG, e.getMessage(), e);
            mHasError = true;
        }

        return addressDetailResponseVO;
    }

    public boolean hasError() {
        return mHasError;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void deliverResult(AddressDetailResponseVO data) {
        super.deliverResult(data);

        if (isStarted()) {
            // Need to return new ArrayList or onLoadFinished() is not called
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        mIsLoading = false;
        cancelLoad();
    }

    public void refresh() {
        reset();
        startLoading();
    }
}
