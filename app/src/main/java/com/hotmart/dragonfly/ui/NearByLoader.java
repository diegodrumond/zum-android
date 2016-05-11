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

import com.hotmart.dragonfly.rest.model.response.NearbyVerificationResponseVO;
import com.hotmart.dragonfly.rest.model.response.PageableList;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.VerificationService;
import com.hotmart.dragonfly.tools.LogUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static com.hotmart.dragonfly.tools.LogUtils.LOGE;

/**
 * An {@link AsyncTaskLoader} that loads activities from the public Google+ stream for
 * a given search query. The loader maintains a page state with the Google+ API and thus
 * supports pagination.
 */
public class NearByLoader extends AsyncTaskLoader<List<NearbyVerificationResponseVO>> {

    private static final String TAG = LogUtils.makeLogTag(NearByLoader.class);

    private List<NearbyVerificationResponseVO> mDataset;

    private Integer mNextPageToken;
    private Integer mMaxResultsPerRequest;

    private boolean mIsLoading;
    private boolean mHasError;

    private double mLat;
    private double mLng;
    private double mRadius;

    public NearByLoader(Context context, int maxResultsPerRequest, double lat, double lng, double radius) {
        super(context);
        mMaxResultsPerRequest = maxResultsPerRequest;
        mLat = lat;
        mLng = lng;
        mRadius = radius;
        init();
    }

    private void init() {
        mHasError = false;
        mNextPageToken = 0;
        mIsLoading = true;
        mDataset = null;
    }

    @Override
    public List<NearbyVerificationResponseVO> loadInBackground() {
        mIsLoading = true;

        PageableList<NearbyVerificationResponseVO> page = null;
        try {
            VerificationService service = ApiServiceFactory.createService(VerificationService.class, getContext());

            Response<PageableList<NearbyVerificationResponseVO>> response = service.get(mNextPageToken, mMaxResultsPerRequest, mLat, mLng, mRadius).execute();
            if (response.code() == HttpURLConnection.HTTP_OK) {
                page = response.body();
                mHasError = false;
                mNextPageToken = page.getPage() + 1;
            } else {
                mHasError = true;
                mNextPageToken = null;
            }
        } catch (IOException e) {
            LOGE(TAG, e.getMessage(), e);
            mHasError = true;
            mNextPageToken = null;
        }

        return (page != null)
               ? page.getData()
               : null;
    }

    @Override
    public void deliverResult(List<NearbyVerificationResponseVO> data) {
        mIsLoading = false;
        if (data != null) {
            if (mDataset == null) {
                mDataset = data;
            } else {
                mDataset.addAll(data);
            }
        }
        if (isStarted()) {
            // Need to return new ArrayList or onLoadFinished() is not called
            super.deliverResult(mDataset == null ? null : new ArrayList<>(mDataset));
        }
    }

    @Override
    protected void onStartLoading() {
        if (mDataset != null) {
            // If we already have results and are starting up, deliver what we already have.
            deliverResult(null);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        mIsLoading = false;
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mDataset = null;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public boolean hasMoreResults() {
        return mNextPageToken != null;
    }

    public boolean hasError() {
        return mHasError;
    }

    public void refresh() {
        reset();
        startLoading();
    }
}
