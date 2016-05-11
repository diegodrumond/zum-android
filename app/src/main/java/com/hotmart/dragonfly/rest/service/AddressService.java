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

import com.hotmart.dragonfly.rest.model.request.AddressRequestVO;
import com.hotmart.dragonfly.rest.model.request.AddressUpdateRequestVO;
import com.hotmart.dragonfly.rest.model.response.AddressDetailResponseVO;
import com.hotmart.dragonfly.rest.model.response.AddressResponseVO;
import com.hotmart.dragonfly.rest.model.response.PageableList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AddressService {

    String PATH = "v1/address";
    String PATH_ID = PATH + "/{id}";

    @GET(PATH)
    Call<PageableList<AddressResponseVO>> get(@Query("page") Integer page, @Query("rows") Integer rows);

    @GET(PATH_ID)
    Call<AddressDetailResponseVO> get(@Path("id") Long id);

    @POST(PATH)
    @Headers({ "Content-Type: application/json" })
    Call<Void> create(@Body AddressRequestVO body);

    @POST(PATH_ID)
    @Headers({ "Content-Type: application/json" })
    Call<AddressResponseVO> put(@Body AddressUpdateRequestVO body);

    @DELETE(PATH_ID)
    Call<Void> delete(@Path("id") Long id);
}
