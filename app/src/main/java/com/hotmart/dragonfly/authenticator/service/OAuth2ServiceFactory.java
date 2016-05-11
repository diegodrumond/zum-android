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

import android.accounts.AccountManager;
import android.content.Context;

import com.hotmart.dragonfly.authenticator.AuthConstants;
import com.hotmart.dragonfly.authenticator.HttpAuthenticator;
import com.hotmart.dragonfly.authenticator.HttpInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class OAuth2ServiceFactory {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AuthConstants.OAUTH2_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

    /**
     * Create a service without authorization header
     *
     * @param serviceClass
     * @param <S>
     * @return
     */
    public static <S> S createAnonymousService(Class<S> serviceClass) {
        return builder.build()
                .create(serviceClass);
    }

    /**
     * Create a service with authorization header
     *
     * @param serviceClass
     * @param context
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, Context context) {
        AccountManager mAccountManager = AccountManager.get(context);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .authenticator(new HttpAuthenticator(mAccountManager))
                .addInterceptor(new HttpInterceptor(context))
                .build();

        return builder.client(client).build()
                .create(serviceClass);
    }
}
