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

import com.hotmart.dragonfly.rest.model.request.VerificationRequestVO;
import com.hotmart.dragonfly.rest.model.response.CheckLastVO;
import com.hotmart.dragonfly.rest.model.response.NearbyVerificationResponseVO;
import com.hotmart.dragonfly.rest.model.response.PageableList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VerificationService {

    String PATH = "v1/verification";
    String PATH_LAST = PATH + "/last";
    String PATH_NEARBY = PATH + "/nearby";

    @GET(PATH_LAST)
    @Headers({ "Content-Type: application/json" })
    Call<CheckLastVO> get();

    @POST(PATH)
    Call<CheckLastVO> post(@Body VerificationRequestVO data);

    @GET(PATH_NEARBY)
    Call<PageableList<NearbyVerificationResponseVO>> get(@Query("page") int page, @Query("rows") int rows,
            @Query("lat") double lat, @Query("lng") double lng,
            @Query("radius") double radius);
}
