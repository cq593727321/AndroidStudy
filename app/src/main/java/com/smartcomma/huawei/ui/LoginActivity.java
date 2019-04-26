package com.smartcomma.huawei.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartcomma.huawei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    @BindView(R.id.login_ll_container)
    View root;
    @BindView(R.id.login_et_user)
    EditText metLoginUser;
    @BindView(R.id.login_et_password)
    EditText metLoginPassword;
    @BindView(R.id.login_bt_login)
    Button mbtLogin;

    @OnClick(R.id.login_bt_login)
    void doLogin() {
        Snackbar.make(root, "登陆中...", Snackbar.LENGTH_SHORT).show();
        startActivity(new Intent(this, FunctionMenuActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        metLoginUser.setOnFocusChangeListener(this);
        metLoginPassword.setOnFocusChangeListener(this);
        metLoginUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setLoginButtonEnable();
            }
        });
        metLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setLoginButtonEnable();
            }
        });
    }

    private void setLoginButtonEnable() {
        if (metLoginUser.getText().toString().trim().length() > 2
                && metLoginPassword.getText().toString().trim().length() > 6) {
            mbtLogin.setBackground(getResources().getDrawable(R.drawable.selector_button_bg));
            mbtLogin.setEnabled(true);
            mbtLogin.setTextColor(ContextCompat.getColor(this, R.color.color_white));
        } else {
            mbtLogin.setBackground(getResources().getDrawable(R.drawable.shape_button_disable_bg));
            mbtLogin.setEnabled(false);
            mbtLogin.setTextColor(ContextCompat.getColor(this, R.color.color_gary));
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.login_et_user:
                if (b) {
                    metLoginUser.setBackgroundResource(R.drawable.et_underline_selected);
                } else {
                    metLoginUser.setBackgroundResource(R.drawable.et_underline_unselected);
                }
                break;
            case R.id.login_et_password:
                if (b) {
                    metLoginPassword.setBackgroundResource(R.drawable.et_underline_selected);
                } else {
                    metLoginPassword.setBackgroundResource(R.drawable.et_underline_unselected);
                }
                break;
        }
    }
}
