package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByEmail(String email);   //회원 가입 시 중복된 회원이 있는지 검사하기 위해서 이메일로 화면을 검사할 수 있도록 쿼리 메소드 생성
}
