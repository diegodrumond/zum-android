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
import com.hotmart.dragonfly.rest.model.request.ResetPasswordRequestVO;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    protected Toolbar actionTB;

    private UserService mUserService;
    private AppCompatActivity thisActivity;
    @BindView(R.id.redefinir_senha_codigo_label)
    protected EditText codeEditText;
    @BindView(R.id.novaSenha)
    protected EditText passwordEditText;
    @BindView(R.id.repetirNovaSenha)
    protected EditText repeatPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reset_password_activity);


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

    @OnClick(R.id.redefinirSalvar)
    public void salvarNovaSenha(View view){

        ResetPasswordRequestVO resetPasswordRequestVO = new ResetPasswordRequestVO();
        final String code = codeEditText.getText().toString();
        resetPasswordRequestVO.setPassword(passwordEditText.getText().toString() );
        resetPasswordRequestVO.setRepeatPassword(repeatPasswordEditText.getText().toString() );

        mUserService.resetPassword(resetPasswordRequestVO, code).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK:

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);

                        Intent homeIntent = new Intent(thisActivity, AuthenticatorActivity.class);
                        thisActivity.startActivity(homeIntent);

                        finish();
                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Snackbar.make(codeEditText, response.message(), Snackbar.LENGTH_LONG).show();
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Snackbar.make(codeEditText, response.message(), Snackbar.LENGTH_LONG).show();
                    default:
                        Snackbar.make(codeEditText, response.message(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(codeEditText, R.string.status_500, Snackbar.LENGTH_LONG).show();
                Log.e("dragonfly", t.getMessage(), t);
            }
        });



    }


}
