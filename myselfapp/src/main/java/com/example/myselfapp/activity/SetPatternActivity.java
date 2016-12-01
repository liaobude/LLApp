/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.example.myselfapp.activity;

import android.view.View;
import android.widget.Button;

import com.android.core.StaticValue;
import com.android.core.base.AbsBaseActivity;
import com.example.myselfapp.R;
import com.example.myselfapp.custom.LockView;
import com.heaton.liulei.utils.utils.SPUtils;
import com.heaton.liulei.utils.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class SetPatternActivity extends AbsBaseActivity {
    @Bind(R.id.LockView)
    LockView lockView;
    @Bind(R.id.btn_reset)
    Button reset;
    @Bind(R.id.btn_save)
    Button save;
    private int flag = 0;

    private List<Integer> passList;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setpattern;
    }

    @Override
    protected void onInitView() {
        setTitle("锁屏界面");
        toolbar.setNavigationIcon(R.mipmap.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String password = SPUtils.get(getBaseContext(), "password", "");
        flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            reset.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
        }
        lockView.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(List<Integer> passList) {
                if (flag == 1) {
                    StringBuilder sb = new StringBuilder();
                    for (Integer i : passList) {
                        sb.append(i);
                    }
                    if (sb.toString().equals(password)) {
                        startActivity(LoginActivity.class);
                        finish();
                        return true;
                    } else {
                        ToastUtil.showToast("密码验证错误");
                        return false;
                    }
                } else {
                    if (passList.size() < 3) {
                        ToastUtil.showToast("密码不能少于3个点");
                        return false;
                    } else {
                        SetPatternActivity.this.passList = passList;
                        return true;
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_reset)
    void reset() {
        lockView.resetPoints();
    }

    @OnClick(R.id.btn_save)
    void save() {
        if (passList != null) {
            StringBuilder sb = new StringBuilder();
            for (Integer i : passList) {
                sb.append(i);
            }
            SPUtils.put(getBaseContext(), "password", sb.toString());
            SPUtils.put(getBaseContext(), StaticValue.IS_PSW_OPEN, true);
            ToastUtil.showToast("保存完成");
            finish();
        }
    }

}
