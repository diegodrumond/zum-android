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

public class CheckLastVO {
    @SerializedName("id")
    private long id;

    @SerializedName("verificationDate")
    private long verificationDate;

    @SerializedName("address")
    private AddressResponseVO mAddressResponseVO;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(long verificationDate) {
        this.verificationDate = verificationDate;
    }

    public AddressResponseVO getAddressResponseVO() {
        return mAddressResponseVO;
    }

    public void setAddressResponseVO(AddressResponseVO addressResponseVO) {
        mAddressResponseVO = addressResponseVO;
    }
}
