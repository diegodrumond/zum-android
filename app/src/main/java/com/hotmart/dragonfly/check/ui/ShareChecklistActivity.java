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
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.rest.model.response.AddressDetailResponseVO;
import com.hotmart.dragonfly.rest.model.response.NearbyVerificationResponseVO;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.hotmart.dragonfly.ui.NearByLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareChecklistActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<List<NearbyVerificationResponseVO>>,
                   OnMapReadyCallback {

    @BindView(R.id.label)
    TextView mLabel;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.share)
    Button mShare;
    @BindView(R.id.footer)
    LinearLayout mFooter;
    @BindView(R.id.avloadingIndicatorView)
    AVLoadingIndicatorView mAvloadingIndicatorView;

    private AddressDetailResponseVO address;
    private GoogleMap googleMap;

    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    public static Intent createIntent(Context context, AddressDetailResponseVO data) {
        Intent intent = new Intent(context, ShareChecklistActivity.class);
        intent.putExtra("address", data);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_share_checklist);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle(getString(R.string.shareTitle));

        address = getIntent().getParcelableExtra("address");
        mLabel.setText(address.getLabel());
        mAddress.setText(address.getFormattedAddress());

        SupportMapFragment fragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps_address);

        fragment.getMapAsync(this);
    }

    @Override
    public Loader<List<NearbyVerificationResponseVO>> onCreateLoader(int id, Bundle args) {
        return new NearByLoader(this, 10, address.getLatitude(), address.getLongitude(), 5.0);
    }

    @Override
    public void onLoadFinished(Loader<List<NearbyVerificationResponseVO>> loader,
            List<NearbyVerificationResponseVO> data) {
        mAvloadingIndicatorView.setVisibility(View.GONE);
        if (data != null) {
            for (NearbyVerificationResponseVO item : data) {
                addMarker(item);
            }
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
        googleMap.moveCamera(cameraUpdate);
    }

    private void addMarker(NearbyVerificationResponseVO item) {
        if (item.getAddress().getLatitude() != address.getLatitude()
                && item.getAddress().getLongitude() != address.getLongitude()) {
            LatLng latLng = new LatLng(item.getAddress().getLatitude(), item.getAddress().getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_green));
            markerOptions.position(latLng);
            builder.include(latLng);
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NearbyVerificationResponseVO>> loader) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin));
        builder.include(latLng);
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f));
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @OnClick(R.id.share)
    void onClickShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_share));
        intent.setType("*/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
