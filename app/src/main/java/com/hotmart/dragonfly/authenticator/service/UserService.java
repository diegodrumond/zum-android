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
package com.hotmart.dragonfly.authenticator.service;

import android.graphics.Bitmap;

import com.hotmart.dragonfly.rest.model.request.UserPasswordRequestVO;

import com.hotmart.dragonfly.rest.model.request.ForgotPasswordRequestVO;
import com.hotmart.dragonfly.rest.model.request.ResetPasswordRequestVO;
import com.hotmart.dragonfly.rest.model.request.UserSignupRequestVO;
import com.hotmart.dragonfly.rest.model.response.UserResponseVO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {

    @GET("rest/v1/user/me")
    Call<UserResponseVO> getUserInfo();

    @GET("rest/v1/user/me")
    Call<UserResponseVO> getUserInfo(@Header("Authorization") String authorization);

    @Headers({ "Content-Type: application/json" })
    @POST("rest/v1/user/signup")
    Call<Void> signUp(@Body UserSignupRequestVO user);

    @Headers({ "Content-Type: application/json" })
    @PUT("rest/v1/user")
    Call<Void> updateUser(@Body UserSignupRequestVO user);

    @GET("rest/v1/user/check_email")
    Call<Void> checkEmailAvailability(@Query("email") String email);

    @Headers({ "Content-Type: application/json" })
    @POST("rest/v1/user/password/change")
    Call<Void> changePassword(@Body UserPasswordRequestVO user);

    @Multipart
    @POST("rest/v1/user/picture")
    Call<Void> changePicture(@Part("data") Bitmap photo);

    @Headers({ "Content-Type: application/json" })
    @POST("rest/v1/user/password/forgot")
    Call<Void> forgotPassword(@Body ForgotPasswordRequestVO email);

    @Headers({ "Content-Type: application/json" })
    @POST("rest/v1/user/password/reset")
    Call<Void> resetPassword(@Body ResetPasswordRequestVO resetPasswordRequestVO, @Query("token") String token);

    @Headers({ "Content-Type: application/json" })
    @POST("rest/v1/security/oauth/token")
    Call<Void> connectWithFacebook(@Query("grant_type") String grantType, @Query("facebook") String token);

}
