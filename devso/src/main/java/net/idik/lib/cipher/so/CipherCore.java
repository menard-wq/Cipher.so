package net.idik.lib.cipher.so;

import android.annotation.SuppressLint;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import androidx.annotation.NonNull;

final class CipherCore {
    static {
        System.loadLibrary("cipher-lib");
        init();
    }

    private CipherCore() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    static String get(String key) {
        return getString(key);
    }

    private static Signature[] getSignatures(@NonNull PackageManager pm, @NonNull String packageName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES);
                if (packageInfo == null
                        || packageInfo.signingInfo == null) {
                    return null;
                }
                if (packageInfo.signingInfo.hasMultipleSigners()) {
                    return packageInfo.signingInfo.getApkContentsSigners();
                } else {
                    return packageInfo.signingInfo.getSigningCertificateHistory();
                }
            } else {
                @SuppressLint("PackageManagerGetSignatures")
                PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
                if (packageInfo == null
                        || packageInfo.signatures == null
                        || packageInfo.signatures.length == 0
                        || packageInfo.signatures[0] == null) {
                    return null;
                }
                return packageInfo.signatures;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static native void init();

    private static native String getString(String key);
}
