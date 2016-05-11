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

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerificationRequestVO {

    @SerializedName("addressId")
    private long addressId;

    @SerializedName("verifiedItems")
    private List<Long> verifiedItems;

    public VerificationRequestVO(long addressId, List<Long> verifiedItems) {
        this.addressId = addressId;
        this.verifiedItems = verifiedItems;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public List<Long> getVerifiedItems() {
        return verifiedItems;
    }

    public void setVerifiedItems(List<Long> verifiedItems) {
        this.verifiedItems = verifiedItems;
    }
}
