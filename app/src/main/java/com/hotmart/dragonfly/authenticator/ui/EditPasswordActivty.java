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
package com.hotmart.dragonfly.authenticator.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.rest.model.request.UserPasswordRequestVO;
import com.hotmart.dragonfly.ui.BaseActivity;

import java.io.IOException;
import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPasswordActivty extends BaseActivity {

    @BindView(R.id.currentPassword)
    public EditText mCurrentPass;

    @BindView(R.id.newPassword)
    public EditText mNewPass;

    @BindView(R.id.accountConfirmPassword)
    public EditText mConfNewPass;

    @BindView(R.id.btnSave)
    public Button btnSave;

    @BindView(R.id.icUser)
    public ImageView mUserImage;

    UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password_activty);
        userService = OAuth2ServiceFactory.createService(UserService.class, getBaseContext());

    }


    @OnClick(R.id.btnSave)
    public void onClickAlterarSenha(View v){
        alterarSenha();
    }

    private void alterarSenha() {


        if (mCurrentPass.getText().toString() == null || mCurrentPass.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "Senha Atual Obrigatória!", Toast.LENGTH_LONG).show();

        } else  if (mNewPass.getText().toString() == null || mNewPass.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "Nova Senha Obrigatória!", Toast.LENGTH_LONG).show();

        } else  if (mConfNewPass.getText().toString() == null || mConfNewPass.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "Confirmação de Senha Obrigatória!", Toast.LENGTH_LONG).show();

        } else if(!mConfNewPass.getText().toString().equals(mNewPass.getText().toString())){
            Toast.makeText(getBaseContext(), "Nova senha e senha de confirmação distintas!", Toast.LENGTH_LONG).show();

        } else {

            userService.changePassword(new UserPasswordRequestVO(mCurrentPass.getText().toString(), mNewPass.getText().toString(),
                                                             mConfNewPass.getText().toString())).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    switch (response.code()) {
                        case HttpURLConnection.HTTP_OK:
                            Toast.makeText(getBaseContext(), "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                            finish();
                            break;
                        case HttpURLConnection.HTTP_BAD_REQUEST:

                            try {
                               String x =  response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getBaseContext(), "Erro, senha incorreta!", Toast.LENGTH_LONG).show();
                        default:
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("dragonfly", t.getMessage(), t);
                }
            });
        }
    }
}
