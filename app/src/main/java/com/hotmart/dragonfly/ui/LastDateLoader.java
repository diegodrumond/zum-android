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

import com.hotmart.dragonfly.rest.model.response.CheckLastVO;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.VerificationService;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit2.Response;

import static com.hotmart.dragonfly.tools.LogUtils.LOGE;

public class LastDateLoader extends AsyncTaskLoader<CheckLastVO> {

    private boolean mIsLoading;
    private boolean mHasError;
    private static final String TAG = LastDateLoader.class.getSimpleName();

    public LastDateLoader(Context context) {
        super(context);
    }

    @Override
    public CheckLastVO loadInBackground() {

        CheckLastVO checkLastVO = null;

        VerificationService service = ApiServiceFactory.createService(VerificationService.class, getContext());
        try {
            Response<CheckLastVO> response = service.get().execute();
            if(response.code() == HttpURLConnection.HTTP_OK){
                mHasError = false;
                checkLastVO = response.body();
            }else{
                mHasError = true;
            }
        } catch (IOException e) {
            LOGE(TAG, e.getMessage(), e);
            mHasError = true;
        }

        return checkLastVO;
    }

    public boolean hasError() {
        return mHasError;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    public void deliverResult(CheckLastVO data) {
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
