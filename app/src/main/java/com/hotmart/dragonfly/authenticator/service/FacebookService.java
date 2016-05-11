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

import android.util.Log;

import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookService {
    private UserService mUserService;
    private SimpleFacebook mSimpleFacebook;
    private OnProfileListener onProfileListener;

    private Permission[] permissions = new Permission[] {
            Permission.USER_PHOTOS,
            Permission.EMAIL,
            Permission.PUBLISH_ACTION
    };


    public FacebookService(AuthenticatorActivity activity){
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId("990958650958298")
                .setNamespace("com.hotmart.hackathon.dragonfly")
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);
        mSimpleFacebook = SimpleFacebook.getInstance(activity);
        activity.defineSimpleFacebook(mSimpleFacebook);

       mUserService = OAuth2ServiceFactory.createAnonymousService(UserService.class);

        onProfileListener = new OnProfileListener() {
            @Override
            public void onComplete(Profile profile) {
                mUserService.connectWithFacebook("password", profile.getId()).enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        switch (response.code()) {
                            case HttpURLConnection.HTTP_OK:

                                break;
                            case HttpURLConnection.HTTP_BAD_REQUEST:
                                //Snackbar.make(, R.string.status_400, Snackbar.LENGTH_LONG).show();
                            default:
                                //Snackbar.make(email, R.string.status_500, Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //Snackbar.make(email, R.string.status_500, Snackbar.LENGTH_LONG).show();
                        Log.e("dragonfly", t.getMessage(), t);
                    }
                });
            }
        };
        mSimpleFacebook.getProfile(onProfileListener);

    }



        final OnLoginListener onLoginListener = new OnLoginListener() {
            @Override
            public void onLogin(String accessToken, List<Permission> acceptedPermissions,
                    List<Permission> declinedPermissions) {

            }

            @Override
            public void onCancel() {
                // user canceled the dialog
            }

            @Override
            public void onFail(String reason) {
                //mSimpleFacebook.requestNewPermissions();
            }

            @Override
            public void onException(Throwable throwable) {
                // exception from facebook
            }
        };

        public void login() {
            mSimpleFacebook.login(onLoginListener);
        }
    }