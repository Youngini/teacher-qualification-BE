package com.teacher.qualification.controller.auth;

import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.auth.FindEmailDto;
import com.teacher.qualification.dto.auth.LoginDto;
import com.teacher.qualification.dto.auth.ResetPasswordDto;
import com.teacher.qualification.dto.auth.SignupRequestDto;
import com.teacher.qualification.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary = "사용자 회원가입")
    public ResponseEntity<User> signup(@RequestBody SignupRequestDto signupRequest) {
        User savedUser = authService.signup(signupRequest);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto.getEmail(), loginDto.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 이메일 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/findEmail")
    @Operation(summary = "사용자 아이디 찾기")
    public ResponseEntity<String> findUserId(@RequestBody FindEmailDto request) {
        String email = authService.findUserEmailByNameAndPhoneNumber(request.getName(), request.getPhoneNumber());
        if (email != null) {
            return ResponseEntity.ok(email);
        } else {
            return ResponseEntity.status(401).body("사용자 정보가 일치하지 않습니다.");
        }
    }

    @PostMapping("/resetPassword")
    @Operation(summary = "사용자 비밀번호 재설정")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto request) {
        String password = authService.resetUserPasswordByEmailAndName(request.getEmail(), request.getName());
        if (password != null) {
            return ResponseEntity.ok(password);
        } else {
            return ResponseEntity.status(401).body("사용자 정보가 일치하지 않습니다.");
        }
    }
}

