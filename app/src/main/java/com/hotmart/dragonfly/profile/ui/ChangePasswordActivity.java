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
package com.hotmart.dragonfly.profile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.authenticator.ui.AuthenticatorActivity;
import com.hotmart.dragonfly.rest.model.request.ForgotPasswordRequestVO;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private UserService mUserService;
    @BindView(R.id.toolbar)
    protected Toolbar actionTB;

    private AppCompatActivity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password_activity);
        ButterKnife.bind(this);
        if (actionTB != null) {
            setSupportActionBar(actionTB);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        mUserService = OAuth2ServiceFactory.createAnonymousService(UserService.class);
        thisActivity = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.redefinir_senha_enviar)
    public  void redefinirSenha(View view){
        final EditText email = (EditText)findViewById(R.id.accountName);
        String textoEmail = email.getText().toString();

        ForgotPasswordRequestVO forgotPasswordRequestVO = new ForgotPasswordRequestVO();
        forgotPasswordRequestVO.setEmail(textoEmail);

        mUserService.forgotPassword(forgotPasswordRequestVO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK:
                        Bundle bundle = new Bundle();
                        bundle.putString(AuthenticatorActivity.PARAM_USER_NAME, email.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                      Intent redefinirSenhaSucesso = new Intent(thisActivity, ResetPasswordSuccessActivity.class);
                       thisActivity.startActivity(redefinirSenhaSucesso);
                        finish();
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Snackbar.make(email, R.string.status_400, Snackbar.LENGTH_LONG).show();
                    default:
                        Snackbar.make(email, R.string.status_500, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(email, R.string.status_500, Snackbar.LENGTH_LONG).show();
                Log.e("dragonfly", t.getMessage(), t);
            }
        });

    }
}
