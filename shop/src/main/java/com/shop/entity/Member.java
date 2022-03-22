package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;
import  javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString

public class Member {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)  //회원은 이메일을 통해 유일하게 구분해야 하기 떄문에, 동일한 값이 데이터베이스에 들어올 수 없도록 unique 속성을 지정한다.
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)    //자바의 enum 타입을 엔티티의 속성으로 지정할 수 있다. Enum을 사용할 때 기본적으로 순서가 저장되는데, enum의 순서가 바뀔 경우 문제가 발생할수 있어
                                    //EnumType.STRING 옵션을 사용해서 String으로 저장하는게 좋다.

    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){    //Member 엔티티를 생성하는 메소드이다. Menber 엔티티에 회원을 생성하는 메소드를
                                                                                                        //만들어서 관리를 한다면 코드가 변경되더라도 한 군데만 수정하면 된다.
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());  //스프링 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean을 파라미터로 넘겨서 비밀번호를 암호화 한다.
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}
