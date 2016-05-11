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

public class AddressChecklistItemResponseVO extends ChecklistItemResponseVO implements Parcelable {

	@SerializedName("available")
	private boolean mAvailable;

	private transient boolean check;

	public boolean isAvailable() {
		return mAvailable;
	}

	public void setAvailable(boolean available) {
		this.mAvailable = available;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte(mAvailable
					   ? (byte) 1
					   : (byte) 0);
		dest.writeByte(check
					   ? (byte) 1
					   : (byte) 0);
	}

	public AddressChecklistItemResponseVO() {}

	protected AddressChecklistItemResponseVO(Parcel in) {
		this.mAvailable = in.readByte() != 0;
		this.check = in.readByte() != 0;
	}

	public static final Parcelable.Creator<AddressChecklistItemResponseVO> CREATOR =
			new Parcelable.Creator<AddressChecklistItemResponseVO>() {
				@Override
				public AddressChecklistItemResponseVO createFromParcel(
						Parcel source) {return new AddressChecklistItemResponseVO(source);}

				@Override
				public AddressChecklistItemResponseVO[] newArray(
						int size) {return new AddressChecklistItemResponseVO[size];}
			};
}
