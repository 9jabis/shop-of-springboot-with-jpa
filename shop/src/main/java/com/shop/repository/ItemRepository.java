package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long>{  //jparepository를 상속받는 itemrepository 작성, japrepository는 2개의 제네릭 타입을 사용 <엔티티 타입 클래스, 기본키 타입>
    List<Item> findByItemNm(String itemNm); //itemNm으로 테이터를 조회하기 위해서 By뒤에 ItemNm을 입력, entity는 생략 가능 finditembyitemnm -> findbyitemnm

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);  //상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드

    List<Item> findByPriceLessThan(Integer price);  //파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
