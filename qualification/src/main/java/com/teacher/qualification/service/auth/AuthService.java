package com.teacher.qualification.service.auth;

import com.teacher.qualification.domain.user.User;
import com.teacher.qualification.dto.auth.SignupRequestDto;
import com.teacher.qualification.repository.user.UserRepository;
import com.teacher.qualification.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JavaMailSender emailSender;

    private static final int TEMP_PASSWORD_LENGTH = 8;
    //private final PasswordEncoder passwordEncoder;

    // 회원가입
    public User signup(SignupRequestDto signupRequest) {
        // 중복 닉네임 및 이메일 체크
        if (userRepository.existsByNickname(signupRequest.getNickname())) {
            throw new IllegalArgumentException("Nickname already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())){
            throw new IllegalArgumentException("Phone number already exists");
        }

        // 비밀번호 암호화
        //String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .nickname(signupRequest.getNickname())
                //.password(encodedPassword)
                .password(signupRequest.getPassword())
                .phoneNumber(signupRequest.getPhoneNumber())
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .build();
        return userRepository.save(user);
    }

    // 로그인
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return jwtService.generateToken(email);
        } else {
            return null; // 사용자가 존재하지 않거나 비밀번호가 일치하지 않는 경우 로그인 실패
        }
    }

    public String findUserEmailByNameAndPhoneNumber(String name, String phoneNumber) {
        return userRepository.findByNameAndPhoneNumber(name, phoneNumber)
                .map(User::getEmail) // User 엔티티에서 이메일 가져오기
                .orElse(null); // 사용자를 찾지 못한 경우 null 반환
    }

    @Transactional
    public String createNewPassword(String email, String name) {
        User user = userRepository.findByEmail(email);
        String tempPassword = generateRandomPassword();
        user.updatePassword(tempPassword);
        sendEmail(user.getEmail(), tempPassword);
        return tempPassword;
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    @Async
    public void sendEmail(String to, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[우리 앱 이름 뭐더라] 비밀번호 재발급 안내");
        message.setText(
                "새로 생성된 비밀번호 입니다: " + temporaryPassword + "\n\n해당 비밀번호로 로그인 후 반드시 비밀번호를 변경해 주시기 바랍니다.");
        emailSender.send(message);
    }
}

