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

import com.google.gson.annotations.SerializedName;

public class VerificationResponseVO {

	@SerializedName("id")
	private Long mId;

	@SerializedName("verificationDate")
	private Long mVerificationDate;

	@SerializedName("address")
	private AddressResponseVO mAddress;

	public Long getId() {
		return mId;
	}

	public void setId(Long id) {
		this.mId = id;
	}

	public Long getVerificationDate() {
		return mVerificationDate;
	}

	public void setVerificationDate(Long verificationDate) {
		this.mVerificationDate = verificationDate;
	}

	public AddressResponseVO getAddress() {
		return mAddress;
	}

	public void setAddress(AddressResponseVO address) {
		this.mAddress = address;
	}
}
