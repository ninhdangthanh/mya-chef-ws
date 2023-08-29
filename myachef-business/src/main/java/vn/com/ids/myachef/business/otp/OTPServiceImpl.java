package vn.com.ids.myachef.business.otp;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class OTPServiceImpl implements OTPService {

    // @Autowired
    // private EmployeeService employeeService;
    //
    // @Override
    // public String generateOTPCode() {
    // log.info("------- generateOTPCode - START");
    // boolean found = false;
    // String otpCode = "";
    // do {
    // otpCode = String.valueOf(generateOTP());
    // found = employeeService.existsByOtpCode(otpCode);
    // } while (found);
    // log.info("------- generateOTPCode - END");
    // return otpCode;
    // }
    //
    // private static int generateOTP() {
    // log.info("------- generateOTP - START");
    // int otp = 100000;
    // try {
    // Random rand = SecureRandom.getInstance("SHA1PRNG");
    // otp = rand.nextInt(900000) + 100000;
    // } catch (NoSuchAlgorithmException ex) {
    // log.error(ex.getMessage(), ex);
    // }
    // log.info("------- generateOTP - END");
    // return otp;
    // }

}
