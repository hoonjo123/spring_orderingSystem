package com.encore.ordering.member.domain;

import com.encore.ordering.member.dto.MemberCreateReqDto;
import com.encore.ordering.order.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member{
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
    //city, street, zipcode 개별 컬럼으로 삽입, 조립 시에는 세 개를 세트로 한 번에 가져옴
    //null 처리 시 장점
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

    public static Member toEntity(MemberCreateReqDto memberCreateReqDto){
        // Address 조립, 필수 입력이 아니라 null이 들어있을 수도 있음
        Address address = new Address(memberCreateReqDto.getCity(),
                memberCreateReqDto.getStreet(), memberCreateReqDto.getZipcode());

        return Member.builder()
                .name(memberCreateReqDto.getName())
                .email(memberCreateReqDto.getEmail())
                .password(memberCreateReqDto.getPassword())
                .address(address)
                .role(Role.USER)
                .build();
    }
}