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
package com.hotmart.dragonfly.rest.service;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderService extends AsyncTask<String, Void, Intent> {

    private Geocoder mGeocoder;

    private View mView;

    private GeocoderListener mGeocoderListener;

    public GeocoderService(View viewRoot) {
        this.mView = viewRoot;
        this.mGeocoder = new Geocoder(viewRoot.getContext(), Locale.getDefault());
    }

    public GeocoderService setGeocoderListener(GeocoderListener mGeocoderListener) {
        this.mGeocoderListener = mGeocoderListener;
        return this;
    }

    @Override
    protected Intent doInBackground(String... params) {
        Intent intent = new Intent();

        try {
            List<Address> addresses = mGeocoder.getFromLocationName(params[0], 1);
            if (addresses.isEmpty()) {
                intent.putExtra("ADDRESS_FOUND", false);
            } else {
                Address address = addresses.get(0);
                intent.putExtra("ADDRESS_FOUND", true);
                intent.putExtra("ADDRESS_STREET", address.getThoroughfare());
                intent.putExtra("ADDRESS_COUNTRY", address.getCountryName());
                intent.putExtra("ADDRESS_LATITUDE", address.getLatitude());
                intent.putExtra("ADDRESS_LONGITUDE", address.getLongitude());
                intent.putExtra("ADDRESS_STATE", address.getAdminArea());
                intent.putExtra("ADDRESS_NUMBER", address.getFeatureName());
                intent.putExtra("ADDRESS_CITY", address.getLocality());
                intent.putExtra("ADDRESS_ZIP_CODE", address.getPostalCode());
                intent.putExtra("ADDRESS_NEIGHBORHOOD", address.getSubLocality());
            }
        } catch (IOException e) {
            intent.putExtra("GEOCODER_ERROR", e);
        }

        return intent;
    }

    @Override
    protected void onPostExecute(Intent intent) {
        if (mGeocoderListener == null) {
            return;
        }

        if (intent.hasExtra("GEOCODER_ERROR")) {
            mGeocoderListener.onGeocoderError((IOException) intent.getSerializableExtra("GEOCODER_ERROR"));
        } else if (intent.getBooleanExtra("ADDRESS_FOUND", false)) {
            Address address = new Address(Locale.getDefault());
            address.setThoroughfare(intent.getStringExtra("ADDRESS_STREET"));
            address.setCountryName(intent.getStringExtra("ADDRESS_COUNTRY"));
            address.setLatitude(intent.getDoubleExtra("ADDRESS_LATITUDE", 0.0));
            address.setLongitude(intent.getDoubleExtra("ADDRESS_LONGITUDE", 0.0));
            address.setAdminArea(intent.getStringExtra("ADDRESS_STATE"));
            address.setFeatureName(intent.getStringExtra("ADDRESS_NUMBER"));
            address.setLocality(intent.getStringExtra("ADDRESS_CITY"));
            address.setPostalCode(intent.getStringExtra("ADDRESS_ZIP_CODE"));
            address.setSubLocality(intent.getStringExtra("ADDRESS_NEIGHBORHOOD"));

            mGeocoderListener.onAddressFound(address);
        } else {
            mGeocoderListener.onAddressNotFound();
        }
    }

    public interface GeocoderListener {

        void onGeocoderError(IOException e);

        void onAddressFound(Address address);

        void onAddressNotFound();
    }
}
