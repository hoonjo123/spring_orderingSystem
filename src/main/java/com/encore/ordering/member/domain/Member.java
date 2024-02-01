package com.encore.ordering.member.domain;

import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.member.dto.MemberResponseDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.tomcat.jni.Buffer.address;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Address address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Ordering> orderings;


    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;

    public static Member toEntity(MemberCreateReqDto memberCreateReqDto) {
        Address address = new Address(memberCreateReqDto.getCity(),
                memberCreateReqDto.getStreet(),
                memberCreateReqDto.getZipcode());

        Member member = Member.builder()
                .name(memberCreateReqDto.getName())
                .email(memberCreateReqDto.getEmail())
                .password(memberCreateReqDto.getPassword())// 여기에 괄호를 닫아야 함
                .address(address)
                .role(Role.USER)
                .build();

        return member;
    }

}
