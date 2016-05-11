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

public class UserResponseVO {

	@SerializedName("id")
    private Long mId;

	@SerializedName("signupDate")
    private Long mSignupDate;

	@SerializedName("lastAccess")
    private Long mLastAccess;

	@SerializedName("name")
    private String mName;

	@SerializedName("email")
    private String mEmail;

	@SerializedName("urlPhoto")
	private String mPhoto;

	public UserResponseVO(){

	}

	public UserResponseVO(String name){
		this.mName = name;
	}

	public String getPhoto() {
		return mPhoto;
	}

	public void setPhoto(String photo) {
		mPhoto = photo;
	}

	public Long getId() {
		return mId;
	}

	public void setId(Long id) {
		this.mId = id;
	}

	public Long getSignupDate() {
		return mSignupDate;
	}

	public void setSignupDate(Long signupDate) {
		this.mSignupDate = signupDate;
	}

	public Long getLastAccess() {
		return mLastAccess;
	}

	public void setLastAccess(Long lastAccess) {
		this.mLastAccess = lastAccess;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		this.mEmail = email;
	}



}
