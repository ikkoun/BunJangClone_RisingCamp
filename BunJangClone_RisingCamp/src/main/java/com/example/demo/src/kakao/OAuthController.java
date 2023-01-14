package com.example.demo.src.kakao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtService jwtService;
    private final OAuthService oAuthService;

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     * @return BaseResponse<>
     */
    @ResponseBody
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
        oAuthService.getKakaoAccessToken(code);

    }

    /**
     * 카카오 로그인 API
     * [GET] /oauth/kakao
     * @return BaseResponse<>
     */
    @ResponseBody
    @PostMapping("")
    public void kakao(@RequestBody Map<String, String> postKakaoLoginReq) {
        postKakaoLoginReq.get("accessToken");

    }

}
