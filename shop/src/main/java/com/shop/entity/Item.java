package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item {

    @Id //테이블의 기본키에 사용할 속성을 지정
    @Column(name="item_id") //필드와 컬럼 매핑, name - 필드와 매핑할 컬럼의 이름 설정(기본값 : 객체의 필드 이름)
    @GeneratedValue(strategy = GenerationType.AUTO) //키 값을 생성하는 전략 명시, generationtype.auto - jpa 구현체가 자동으로 생성 전략 결정
    private Long id;                        //상품 코드

    @Column(nullable = false, length = 50)  //nullable - null값의 허용 여부 설정, false설정 시, ddl 생성 시에 not null 제약조건 추가
    private String itemNm;                  //상품명

    @Column(name="price", nullable = false)
    private int price;                      //가격

    @Column(nullable = false)
    private int stockNumber;                //재고수량

    @Lob    //BLOCK, CLOB 타입 매핑
    private String itemDetail;              //상품 상세 설명

    @Enumerated(EnumType.STRING)    //enum 타입 매핑
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    private LocalDateTime regTime;          //등록 시간

    private LocalDateTime updateTime;       //수정 시간

}
