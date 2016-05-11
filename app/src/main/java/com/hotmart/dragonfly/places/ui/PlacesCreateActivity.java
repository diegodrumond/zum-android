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
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.request.AddressRequestVO;
import com.hotmart.dragonfly.rest.service.AddressService;
import com.hotmart.dragonfly.rest.service.ApiServiceFactory;
import com.hotmart.dragonfly.rest.service.GeocoderService;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesCreateActivity extends BaseActivity
        implements OnMapReadyCallback, Validator.ValidationListener {

    private GoogleMap mMap;

    private Validator mValidator;

    private AddressService mAddressService;

    private AddressRequestVO mAddressRequest;

    @NotEmpty(messageResId = R.string.required_field)
    @BindView(R.id.places_create_label)
    protected EditText mLabel;

    @NotEmpty(messageResId = R.string.required_field)
    @BindView(R.id.places_create_address)
    protected EditText mAddress;

    @BindView(R.id.places_create_start_message)
    protected TextView mStartMessage;

    @BindView(R.id.places_create_switcher)
    protected ViewSwitcher mViewSwitcher;

    @BindView(R.id.places_create_not_found)
    protected View mNotFound;

    @BindView(R.id.places_create_map_container)
    protected View mMapContainer;

    @BindView(R.id.places_create_find)
    protected Button mFindButton;

    @BindView(R.id.places_create_save)
    protected Button mSaveButton;

    public static Intent createIntent(Context context) {
        return new Intent(context, PlacesCreateActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_create_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.places_create_map);
        mapFragment.getMapAsync(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mAddressService = ApiServiceFactory.createService(AddressService.class, this);

        mAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mFindButton.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFindButton.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mFindButton.setVisibility(View.VISIBLE);
                mSaveButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onValidationSucceeded() {
        searchAddress();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof TextView) {
                ((TextInputLayout) view.getParent()).setError(message);
            } else {
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.places_create_find)
    public void onClickFind() {
        mValidator.validate();
    }

    @OnClick(R.id.places_create_save)
    public void onClickSave() {
        mAddressRequest.setLabel(mLabel.getText().toString());
        mAddressService.create(mAddressRequest).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_CREATED:
                        setResult(RESULT_OK, new Intent());
                        finish();
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Snackbar.make(mViewSwitcher, R.string.status_400, Snackbar.LENGTH_LONG).show();
                        break;
                    default:
                        Snackbar.make(mViewSwitcher, R.string.status_500, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("dragonfly", t.getMessage(), t);
                Snackbar.make(mViewSwitcher, R.string.status_500, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void searchAddress() {
        mViewSwitcher.setVisibility(View.VISIBLE);
        mStartMessage.setVisibility(View.GONE);
        mMap.clear();

        GeocoderService.GeocoderListener listener = new GeocoderService.GeocoderListener() {
            @Override
            public void onGeocoderError(IOException e) {
                Log.e("dragonfly", e.getMessage(), e);
                Snackbar.make(mViewSwitcher, R.string.places_unknown_error, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onAddressFound(Address address) {
                mFindButton.setVisibility(View.GONE);
                mSaveButton.setVisibility(View.VISIBLE);

                if (mViewSwitcher.getCurrentView() != mMapContainer) {
                    mViewSwitcher.showPrevious();
                }

                LatLng addressLocation = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 16.5f));
                mMap.addMarker(new MarkerOptions()
                        .position(addressLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin)));
                mAddressRequest = new AddressRequestVO(address);
            }

            @Override
            public void onAddressNotFound() {
                if (mViewSwitcher.getCurrentView() != mNotFound) {
                    mViewSwitcher.showNext();
                }
            }
        };

        new GeocoderService(mViewSwitcher)
                .setGeocoderListener(listener)
                .execute(mAddress.getText().toString());
    }
}
