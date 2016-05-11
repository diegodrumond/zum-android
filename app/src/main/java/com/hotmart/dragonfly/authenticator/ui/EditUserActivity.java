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

import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hotmart.dragonfly.R;
import com.hotmart.dragonfly.authenticator.AuthConstants;
import com.hotmart.dragonfly.authenticator.service.OAuth2ServiceFactory;
import com.hotmart.dragonfly.authenticator.service.UserService;
import com.hotmart.dragonfly.home.ui.HomeActivity;
import com.hotmart.dragonfly.rest.model.request.UserSignupRequestVO;
import com.hotmart.dragonfly.rest.model.response.UserResponseVO;
import com.hotmart.dragonfly.ui.BaseActivity;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends BaseActivity{

    @BindView(R.id.accountName)
    public EditText accountName;

    @BindView(R.id.accountEmail)
    public EditText acountEmail;

    @BindView(R.id.btnSalvar)
    public Button btnSalvar;

    @BindView(R.id.btnSair)
    public Button btnSair;

    @BindView(R.id.btnAlterarSenha)
    public Button btnAlterarSenha;

    @BindView(R.id.icUser)
    public ImageView mUserImage;

    @BindView(R.id.editPhoto)
    public FloatingActionButton fabPhoto;

    UserResponseVO mUser;

    UserService userService;


    private ProgressDialog mProgressBar;

    public static final int ACTION_REQUEST_GALLERY = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


         userService = OAuth2ServiceFactory.createService(UserService.class, getBaseContext());

        fabPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // GET IMAGE FROM THE GALLERY

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, ACTION_REQUEST_GALLERY);

                /*
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                Bundle opt = new Bundle();
                Intent chooser = Intent.createChooser(intent, "Selecionar Imagem");
                startActivityForResult(chooser, ACTION_REQUEST_GALLERY, opt);
*/
            }
        });
        getInfo();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == ACTION_REQUEST_GALLERY && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                                                       filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor



            mUserImage.setImageBitmap(StringToBitMap(picturePath.toString()));

           // changePhoto(StringToBitMap( picturePath.toString()));

          //  bitmap = decodeFilePath(picturePath.toString());


        }

        /*
        if (requestCode == ACTION_REQUEST_GALLERY && resultCode == Activity.RESULT_OK)
        {
            if(data.getData()!=null)
            {
                try
                {
                    if (bitmap != null)
                    {
                        bitmap.recycle();
                    }

                    InputStream stream = getContentResolver().openInputStream(data.getData());


                    bitmap = BitmapFactory.decodeStream(stream);
                    stream.close();
                    mUserImage.setImageBitmap(bitmap);

                    //changePhoto(bitmap);


                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            else
            {
                bitmap =(Bitmap) data.getExtras().get("data");
                mUserImage.setImageBitmap(bitmap);

            }

            super.onActivityResult(requestCode, resultCode, data);
            changePhoto(bitmap);
        }*/
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @OnClick(R.id.btnSalvar)
    public void onClickSalvar(View v) {
        signUp();

    }

    @OnClick(R.id.btnSair)
    public void onClickSair(View v){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AccountManager.get(getBaseContext()).removeAccountExplicitly(AccountManager.get(getBaseContext()).getAccountsByType(AuthConstants.ACCOUNT_TYPE)[0]);
        } else{
            AccountManager.get(getBaseContext()).removeAccount(AccountManager.get(getBaseContext()).getAccountsByType(AuthConstants.ACCOUNT_TYPE)[0], null, null);
        }
        AccountManager.get(getBaseContext()).invalidateAuthToken(AuthConstants.AUTHTOKEN_TYPE, null);

        Intent intent = new Intent(this, AuthenticatorActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.btnAlterarSenha)
    public void onClickAlterarSenha(View v) {
        Intent intent = new Intent(this, EditPasswordActivty.class);
        startActivity(intent);
    }

    private void getInfo(){

        userService.getUserInfo().enqueue(new Callback<UserResponseVO>() {

            @Override
            public void onResponse(Call<UserResponseVO> call, Response<UserResponseVO> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK:
                        //showProgressBar();
                        mUser = response.body();

                        accountName.setText(mUser.getName());
                        acountEmail.setText(mUser.getEmail());

                        Picasso
                                .with(getBaseContext())
                                .load(mUser.getPhoto())
                                .placeholder(R.drawable.ic_login)
                                .resize(350, 350)
                                .onlyScaleDown()
                                .into(mUserImage);
                       // mProgressBar.dismiss();

                        break;
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        // Snackbar.make(mName, R.string.status_400, Snackbar.LENGTH_LONG).show();
                    default:
                        //  Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponseVO> call, Throwable t) {
                //Snackbar.make(mName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                Log.e("dragonfly", t.getMessage(), t);
            }
        });
    }

    private void signUp() {

        if (accountName.getText() == null || accountName.getText().equals("")) {

            Toast.makeText(getBaseContext(), "Nome Obrigatório!", Toast.LENGTH_LONG).show();

        } else {

            userService.updateUser(new UserSignupRequestVO(accountName.getText().toString())).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    switch (response.code()) {
                        case HttpURLConnection.HTTP_OK:
                            Toast.makeText(getBaseContext(), "Nome de usuário alterado com sucesso!", Toast.LENGTH_LONG).show();

                            AccountManager accountManager = AccountManager.get(getBaseContext());
                            accountManager.setUserData(AccountManager.get(getBaseContext()).getAccountsByType(AuthConstants.ACCOUNT_TYPE)[0], "name", accountName.getText().toString());

                            Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                            startActivity(intent);
                            break;
                        case HttpURLConnection.HTTP_BAD_REQUEST:
                            Toast.makeText(getBaseContext(), "Erro!", Toast.LENGTH_LONG).show();
                        default:
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Snackbar.make(accountName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                    Log.e("dragonfly", t.getMessage(), t);
                }
            });
        }
    }

    private void changePhoto(Bitmap bitmap) {



            userService.changePicture(bitmap).enqueue(new Callback<Void>() {

                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    switch (response.code()) {
                        case HttpURLConnection.HTTP_OK:
                            Toast.makeText(getBaseContext(), "Nome de usuário alterado com sucesso!", Toast.LENGTH_LONG).show();


                            break;
                        case HttpURLConnection.HTTP_BAD_REQUEST:
                            Toast.makeText(getBaseContext(), "Erro!", Toast.LENGTH_LONG).show();
                        default:
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Snackbar.make(accountName, R.string.status_500, Snackbar.LENGTH_LONG).show();
                    Log.e("dragonfly", t.getMessage(), t);
                }
            });
    }

    //Showing progress bar
    private void showProgressBar(){
        mProgressBar = new ProgressDialog(getBaseContext());
        mProgressBar.setMessage("Loading images...");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
    }
}
