package mobi.tarantino.stub.auto.helper;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

/**

 */

public class ValidatorHelper {

    private static final String PLATE_A_000_AA_00 =
            "^[АВЕКМНОРСТУХ]{1}[0-9]{3}[АВЕКМНОРСТУХ]{2}[0-9]{2}$";
    private static final String PLATE_A_000_AA_000 =
            "^[АВЕКМНОРСТУХ]{1}[0-9]{3}[АВЕКМНОРСТУХ]{2}[0-9]{3}$";
    private static final String PLATE_AA_000_00 = "^[АВЕКМНОРСТУХ]{2}[0-9]{5}$";
    private static final String PLATE_AA_000_000 = "^[АВЕКМНОРСТУХ]{2}[0-9]{6}$";
    private static final String CAR_CERTIFICATE_00AA000000 = "^[0-9]{2}[АВЕКМНОРСТУХ]{2}[0-9]{6}$";
    private static final String CAR_CERTIFICATE_0000000000 = "^[0-9]{10}$";
    private static final String DRIVER_LICENSE_0000000000 = "^[0-9]{10}$";
    private static final String DRIVER_LICENSE_00AA000000 = "^[0-9]{2}[АВЕКМНОРСТУХ]{2}[0-9]{6}$";

    public boolean driverLicenseNumber(@NonNull String licenseNumber) {
        return Pattern.compile(DRIVER_LICENSE_0000000000).matcher(licenseNumber).matches() ||
                Pattern.compile(DRIVER_LICENSE_00AA000000).matcher(licenseNumber).matches();
    }

    public boolean licensePlate(@NonNull String licensePlate) {
        return Pattern.compile(PLATE_A_000_AA_00).matcher(licensePlate).matches() ||
                Pattern.compile(PLATE_A_000_AA_000).matcher(licensePlate).matches() ||
                Pattern.compile(PLATE_AA_000_00).matcher(licensePlate).matches() ||
                Pattern.compile(PLATE_AA_000_000).matcher(licensePlate).matches();
    }

    public boolean carCertificateNumber(@NonNull String certificateNumber) {

        return Pattern.compile(CAR_CERTIFICATE_00AA000000).matcher(certificateNumber).matches() ||
                Pattern.compile(CAR_CERTIFICATE_0000000000).matcher(certificateNumber).matches();
    }
}
