package com.web.puppylink.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.puppylink.dao.user.UserDao;
import com.web.puppylink.model.BasicResponse;
import com.web.puppylink.model.user.SignupRequest;
import com.web.puppylink.model.user.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "http://localhost:8081" })
@RestController
public class UserController {

    @Autowired
    UserDao userDao;

    @GetMapping("/account/login")
    @ApiOperation(value = "로그인")
    public Object login(@RequestParam(required = true) final String email,
            @RequestParam(required = true) final String password) {
    	System.out.println("Login..");
        Optional<User> userOpt = userDao.findUserByEmailAndPassword(email, password);
        ResponseEntity response = null;

        if (userOpt.isPresent()) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @PostMapping("/account/signup")
    @ApiOperation(value = "회원가입")
    public Object signup(@Valid @RequestBody SignupRequest request) {
        // 이메일, 닉네임 중복처리 필수
    	String uid = request.getUid();
    	String email = request.getEmail();
    	String password = request.getPassword();
    	
    	Optional<User> userOpt = userDao.findUserByEmailAndUid(email, uid);
    
        // 회원가입단을 생성해 보세요.
        if (userOpt.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        } else {
            User user = new User();
            LocalDateTime localDateTime = LocalDateTime.now();
            user.setUid(uid);
            user.setEmail(email);
            user.setPassword(password);
            user.setCreateDate(localDateTime);
            userDao.save(user);
        	
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }
    
    @PutMapping("/account/pwdchange")
    @ApiOperation(value = "비밀번호 변경")
    public Object pwdchange(@Valid @RequestBody User user) {
		userDao.save(user);
		
    	final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/account/uid")
    @ApiOperation(value = "UID 중복 조회")
    public Object uidcheck(@RequestParam(required = true) final String uid) {
    	System.out.println("UID..");
        Optional<User> userOpt = userDao.findUserByUid(uid);
        ResponseEntity response = null;

        if (userOpt.isPresent()) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return response;
    }
}