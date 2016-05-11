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

import com.hotmart.dragonfly.authenticator.model.AccessToken;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OAuth2Service {

    @FormUrlEncoded
    @POST("oauth/token?grant_type=password") Call<AccessToken> getAccessToken(
            @Field("username") String email,
            @Field("password") String password,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("oauth/token?grant_type=refresh_token") Call<AccessToken> refreshAccessToken(
            @Field("refresh_token") String refreshToken,
            @Header("Authorization") String authorization);
}
