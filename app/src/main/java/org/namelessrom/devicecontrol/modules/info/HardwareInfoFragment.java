/*
 * Copyright (C) 2013 - 2016 Alexander Martinz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.namelessrom.devicecontrol.modules.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.namelessrom.devicecontrol.DeviceConstants;
import org.namelessrom.devicecontrol.R;
import org.namelessrom.devicecontrol.modules.info.hardware.FingerprintView;
import org.namelessrom.devicecontrol.ui.views.AttachFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class HardwareInfoFragment extends AttachFragment {

    @Bind(R.id.hardware_fingerprint) FingerprintView hardwareFingerprint;

    @Override protected int getFragmentId() { return DeviceConstants.ID_INFO_HARDWARE; }

    public HardwareInfoFragment() { }

    @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstState) {
        final View v = inflater.inflate(R.layout.fragment_info_hardware, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public void onResume() {
        super.onResume();
        final RxPermissions rxPermissions = RxPermissions.getInstance(getContext());

        // use hardcoded string to support older apis as well
        final boolean fingerprintGranted = rxPermissions.isGranted("android.permission.USE_FINGERPRINT");
        if (fingerprintGranted) {
            hardwareFingerprint.setSupported(true);
            hardwareFingerprint.onResume();
        } else {
            hardwareFingerprint.setSupported(false);
            rxPermissions.request("android.permission.USE_FINGERPRINT").subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean isGranted) {
                    if (isGranted) {
                        hardwareFingerprint.setSupported(true);
                        hardwareFingerprint.onResume();
                    }
                }
            });
        }
    }

    @Override public void onPause() {
        super.onPause();
        hardwareFingerprint.onPause();
    }

}
