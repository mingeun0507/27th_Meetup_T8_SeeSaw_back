package com._8attery.seesaw.controller.user;

import com._8attery.seesaw.domain.user.account.UserAccount;
import com._8attery.seesaw.dto.api.request.NicknameRequestDto;
import com._8attery.seesaw.dto.api.response.UserHistoryResponseDto;
import com._8attery.seesaw.exception.BaseException;
import com._8attery.seesaw.exception.BaseResponse;
import com._8attery.seesaw.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

//    @GetMapping
//    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal UserAccount userAccount) {
//        return ResponseEntity.ok(userService.resolveUserById(userAccount.getUserId()));
//    }

    // 닉네임 조회
    @GetMapping
    public BaseResponse<String> getUser (@AuthenticationPrincipal UserAccount userAccount) throws BaseException {
        Long userId = userService.resolveUserById(userAccount.getUserId()).getId();

        try {
            String nickName = userService.getUserNickname(userId);

            return new BaseResponse<>(nickName);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/email")
    public BaseResponse<String> getUserEmail(@AuthenticationPrincipal UserAccount userAccount) {
        return new BaseResponse<>(userService.getUserEmail(userAccount.getUserId()));
    }

    // 닉네임 수정
    @PutMapping
    public BaseResponse<String> updateUser(@Valid @RequestBody NicknameRequestDto req, @AuthenticationPrincipal UserAccount userAccount) throws BaseException {
        Long userId = userService.resolveUserById(userAccount.getUserId()).getId();

        try {
            userService.updateNickname(userId, req);

            String nickName = userService.getUserNickname(userId);

            return new BaseResponse<>(nickName);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // 회원 탈퇴 화면 - 함께 한 일수, 가치 목록 조회 API
    @GetMapping("/history")
    public BaseResponse<UserHistoryResponseDto> getHistory(@AuthenticationPrincipal UserAccount userAccount) throws BaseException {
        Long userId = userService.resolveUserById(userAccount.getUserId()).getId();

        UserHistoryResponseDto res = userService.getUserHistory(userId);

        return new BaseResponse<>(res);
    }

    @DeleteMapping()
    public ResponseEntity<Long> deleteUser(@AuthenticationPrincipal UserAccount userAccount) {
        userService.deleteUser(userAccount.getUserId());
        return ResponseEntity.ok(userAccount.getUserId());
    }
}
