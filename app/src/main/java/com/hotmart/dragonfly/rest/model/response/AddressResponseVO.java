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
package com.hotmart.dragonfly.rest.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AddressResponseVO implements Parcelable {

	@SerializedName("id")
	private Long mId;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AddressResponseVO)) return false;

		AddressResponseVO that = (AddressResponseVO) o;

		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.mId);
		dest.writeString(this.mLabel);
		dest.writeValue(this.mLatitude);
		dest.writeValue(this.mLongitude);
		dest.writeString(this.mCity);
		dest.writeString(this.mZipCode);
		dest.writeString(this.mState);
		dest.writeString(this.mComplement);
		dest.writeString(this.mAddress);
		dest.writeString(this.mNeighborhood);
		dest.writeString(this.mNumber);
		dest.writeString(this.mCountry);
	}

	public AddressResponseVO() {}

    public AddressResponseVO(Long id, String label, String address) {
        mId = id;
        mLabel = label;
        mAddress = address;
    }

	protected AddressResponseVO(Parcel in) {
		this.mId = (Long) in.readValue(Long.class.getClassLoader());
		this.mLabel = in.readString();
		this.mLatitude = (Double) in.readValue(Double.class.getClassLoader());
		this.mLongitude = (Double) in.readValue(Double.class.getClassLoader());
		this.mCity = in.readString();
		this.mZipCode = in.readString();
		this.mState = in.readString();
		this.mComplement = in.readString();
		this.mAddress = in.readString();
		this.mNeighborhood = in.readString();
		this.mNumber = in.readString();
		this.mCountry = in.readString();
	}

	public Long getId() {
		return mId;
	}

	public void setId(Long id) {
		this.mId = id;
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

	public void setComplement(String complement) {
		this.mComplement = complement;
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

	public String getFormattedAddress() {
		StringBuilder addr = new StringBuilder();
		if (mAddress != null) {
			addr.append(mAddress);
		}

		if (mNumber != null) {
            addr.append(addr.length() > 0 ? ", " : "").append(mNumber);
		}

		if (mNeighborhood != null) {
            addr.append(addr.length() > 0 ? " - " : "").append(mNeighborhood);
		}

		return addr.toString();
	}
}
