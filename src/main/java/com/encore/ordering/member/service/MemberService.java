package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import com.encore.ordering.member.dto.LoginReqDto;
import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.encore.ordering.member.dto.MemberResponseDto.toMemberResponseDto;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Member create(MemberCreateReqDto memberCreateReqDto) {
        // Address 조립, 필수 입력이 아니라 null이 들어있을 수도 있음
       memberCreateReqDto.setPassword(passwordEncoder.encode(memberCreateReqDto.getPassword()));
       Member member = Member.toEntity(memberCreateReqDto);
       return memberRepository.save(member);


    }


        public MemberResponseDto findMyInfo() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Member member = memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
            return toMemberResponseDto(member);
    }

        public List<MemberResponseDto> findAll(){
            List<Member> members = memberRepository.findAll();
            return members.stream().map(m-> toMemberResponseDto(m)).collect(Collectors.toList());
        }

    public Member login(LoginReqDto loginReqDto) throws IllegalArgumentException {
        //회원가입 여부(email존재여부, password여부)
        Member member = memberRepository.findByEmail(loginReqDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email is not existed"));
        //password 일치여부
        if (!passwordEncoder.matches(loginReqDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("Password mismatch");
        }
        return member;
    }


    //dto 공통화작업 시작


}