package com.hms.service;

import com.hms.entities.AppUser;
import com.hms.payloads.LoginDto;
import com.hms.payloads.LoginWithOtpDto;
import com.hms.repository.AppUserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private AppUserRepository appUserRepository;
    private JWTService jwtService;

    public UserService(AppUserRepository appUserRepository, JWTService jwtService) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }
//Generate Token using Login by Username and Password
    public String verifyLogin(LoginDto dto){
    Optional<AppUser> opUser = appUserRepository.findByUsername(dto.getUsername());
    if(opUser.isPresent()){
        AppUser appUser = opUser.get();
        if( BCrypt.checkpw(dto.getPassword(), appUser.getPassword())){
            //generate token
            String token = jwtService.generateToken(appUser.getUsername());
            return token;
        }

    }else{
        return null;
    }
    return null;
}

    //Generate Token using Login by OTP
    public String verifyLoginUsingOtp(String phoneNumber) {
        Optional<AppUser> opUser = appUserRepository.findByPhoneNumber(phoneNumber);
        if (opUser.isPresent()) {
            AppUser appUser = opUser.get();
            // Generate token
            return jwtService.generateToken(appUser.getUsername());
        }
        return null; // User not found
    }

    //Check phoneNumber available or not in database
    public boolean verifyPhoneNumber(String phoneNumber) {
        Optional<AppUser> opUser = appUserRepository.findByPhoneNumber(phoneNumber);
        if (opUser.isPresent()) {
            AppUser appUser = opUser.get();
            appUser.getPhoneNumber().equals(phoneNumber);
            return true;
        }
        return false; // User not found
    }


}
