package com.encore.ordering.order.repository;

import com.encore.ordering.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Member, Long> {
}
