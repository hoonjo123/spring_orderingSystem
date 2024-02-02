package com.encore.ordering.member.controller;



import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.securities.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class MemberController {
    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponse> memberCreate(@Valid @RequestBody MemberCreateReqDto memberCreateReqDto){
        Member member = memberService.create(memberCreateReqDto);
        //ResponseDto 객체에 담긴 값은 header로 나가고, body에 담긴 Map은 json 형태로 나감.
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED, "member is successfully created", member.getId()), HttpStatus.CREATED);
    }

//    @GetMapping("/member/{id}/orders")
//    //id별로 order를 구하겠다 -> 관리자모드
//
//
//    @GetMapping("/member/myorders")




    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> memberLogin(@Valid @RequestBody LoginReqDto loginReqDto){
        Member member = memberService.login(loginReqDto);
        //tokken생성 로직
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwtToken);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK, "member is successfully logined", member_info), HttpStatus.OK);
    }

    @GetMapping("/members")
    public List<MemberResponseDto> members(){
        return memberService.findAll();
    }
// ----- ** 테스트 시나리오 -----//
    //토큰없이 members호출 -> (filter에서걸림)
    //정상토큰을 넣어서 ok
    //잘못된 토큰 보내보기 -> 파싱이 안됨.
    @GetMapping("/member/myinfo")
    public MemberResponseDto findMyInfo(){
        return memberService.findMyInfo();
    }




}