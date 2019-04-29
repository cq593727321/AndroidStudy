package com.smartcomma.huawei.ui.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.smartcomma.huawei.R;
import com.smartcomma.huawei.view.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAndSaveLocationActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.location_sb_default)
    SwitchButton mSwitchButton;
    @BindView(R.id.location_bt_save)
    Button mbtSave;
    @BindView(R.id.location_et_address)
    EditText metAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_save_location);
        ButterKnife.bind(this);
        mSwitchButton.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
        }

    }
}
