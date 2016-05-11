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
package com.hotmart.dragonfly.rest.model.request;

import android.location.Address;

import com.google.gson.annotations.SerializedName;

public class AddressRequestVO {

	@SerializedName("label")
	private String mLabel;

	@SerializedName("latitude")
	private Double mLatitude;

	@SerializedName("longitude")
	private Double mLongitude;

	@SerializedName("city")
	private String mCity;

	@SerializedName("zipCode")
	private String mZipCode;

	@SerializedName("state")
	private String mState;

	@SerializedName("complement")
	private String mComplement;

	@SerializedName("address")
	private String mAddress;

	@SerializedName("neighborhood")
	private String mNeighborhood;

	@SerializedName("number")
	private String mNumber;

	@SerializedName("country")
	private String mCountry;

	public AddressRequestVO() {
	}

	public AddressRequestVO(Address address) {
        this.mAddress = address.getThoroughfare();
        this.mCountry = address.getCountryName();
        this.mLatitude = address.getLatitude();
        this.mLongitude = address.getLongitude();
        this.mState = address.getAdminArea();
        this.mNumber = address.getFeatureName();
        this.mCity = address.getLocality();
        this.mZipCode = address.getPostalCode();
        this.mNeighborhood = address.getSubLocality();
        // TODO complement
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String label) {
		this.mLabel = label;
	}

	public Double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(Double latitude) {
		this.mLatitude = latitude;
	}

	public Double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(Double longitude) {
		this.mLongitude = longitude;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public String getZipCode() {
		return mZipCode;
	}

	public void setZipCode(String zipCode) {
		this.mZipCode = zipCode;
	}

	public String getState() {
		return mState;
	}

	public void setState(String state) {
		this.mState = state;
	}

	public String getComplement() {
		return mComplement;
	}

	public void setComplement(String mComplement) {
		this.mComplement = mComplement;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		this.mAddress = address;
	}

	public String getNeighborhood() {
		return mNeighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.mNeighborhood = neighborhood;
	}

	public String getNumber() {
		return mNumber;
	}

	public void setNumber(String number) {
		this.mNumber = number;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		this.mCountry = country;
	}

}
