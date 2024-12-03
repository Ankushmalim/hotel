package com.hms.controller;

import com.hms.entities.AppUser;
import com.hms.payloads.LoginDto;
import com.hms.payloads.LoginWithOtpDto;
import com.hms.payloads.TokenDto;
import com.hms.repository.AppUserRepository;
import com.hms.service.OtpStore;
import com.hms.service.SmsService;
import com.hms.service.UserService;
import com.hms.util.OtpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private AppUserRepository appUserRepository;
    private UserService userService;
    private SmsService smsService;

    public UserController(AppUserRepository appUserRepository, UserService userService, SmsService smsService) {
        this.appUserRepository = appUserRepository;
        this.userService = userService;
        this.smsService = smsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(
            @RequestBody AppUser user
    ){
        Optional<AppUser> opUsername = appUserRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("userName already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Optional<AppUser> opEmail= appUserRepository.findByEmail(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("Email already Used", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //this is for encrypt password
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(encryptedPassword);
        user.setRole("ROLE_USER");
        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>("Save user", HttpStatus.CREATED);
    }


    @PostMapping("/signup-property-owner")
    public ResponseEntity<?> createPropertyOwnerUser(
            @RequestBody AppUser user
    ){
        Optional<AppUser> opUsername = appUserRepository.findByUsername(user.getUsername());
        if(opUsername.isPresent()){
            return new ResponseEntity<>("userName already taken", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Optional<AppUser> opEmail= appUserRepository.findByEmail(user.getEmail());
        if(opEmail.isPresent()){
            return new ResponseEntity<>("Email already Used", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //this is for encrypt password
        String encryptedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(5));
        user.setPassword(encryptedPassword);
        user.setRole("ROLE_OWNER");
        AppUser savedUser = appUserRepository.save(user);
        return new ResponseEntity<>("Save Owner", HttpStatus.CREATED);
    }

  @PostMapping("/login")
  public ResponseEntity<?> login(
        @RequestBody LoginDto dto
  ){
      String token = userService.verifyLogin(dto);
      if(token!=null){
          TokenDto tokenDto = new TokenDto();
          tokenDto.setToken(token);
          tokenDto.setType("JWT");
          return new ResponseEntity<>(tokenDto, HttpStatus.OK);
      }else{
          return new ResponseEntity<>("Invalid userName/password", HttpStatus.FORBIDDEN);
      }
  }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String phoneNumber) {
        // Generate OTP
        String newPhone = phoneNumber.substring(3);
        if(!userService.verifyPhoneNumber(newPhone)) {
            return new ResponseEntity<>("Phone Number not found", HttpStatus.NOT_FOUND);
        }
            String otp = OtpUtil.generateOtp();
            // Store OTP for 5 minutes before it expires (in milliseconds)
            OtpStore.storeOtp(phoneNumber, otp, TimeUnit.MINUTES.toMillis(5));
            // Send OTP via SMS
            String message = "Your OTP is: " + otp;
            String response = smsService.sendSms(phoneNumber, message);

            if (response.startsWith("SMS sent successfully")) {
                return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP: " + response);
            }

    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String phoneNumber, @RequestParam String otp) {
        // Verify OTP
        boolean isValid = OtpStore.isOtpValid(phoneNumber, otp);
        if (isValid) {
            OtpStore.removeOtp(phoneNumber); // Remove OTP after successful validation

            //phoneNumber = +919874587120
            // Extract the last 3 digits of the phone number
            String newPhone = phoneNumber.substring(3);
            //newPhone = 9874587120

            //for create token
            String token = userService.verifyLoginUsingOtp(newPhone);
            if (token != null) {
                TokenDto tokenDto = new TokenDto();
                tokenDto.setToken(token);
                tokenDto.setType("JWT");
                return new ResponseEntity<>(tokenDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        }
    }


}
