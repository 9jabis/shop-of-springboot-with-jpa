package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import java.time.LocalDateTime;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.shop.entity.QItem;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

@SpringBootTest //통합 테스트를 위해 스프링 부트에서 제공하는 어노테이션, 실세 애플리케이션을 구동할 때처럼 모든 Bean을 IoC 컨테이너에 등록
@TestPropertySource(locations = "classpath:application-test.properties")    //테스트 코드 실행 시 application.propertiy보다 application-test.properties에 같은 값은 설정이 있다면
class ItemRepositoryTest {                                                  //더 높은 우선순위를 부여한다. 기존은 mysql, 테스트는 h2

    @PersistenceContext
    EntityManager em;   //영속성 컨텍스트를 사용하기 위해 @PersistenceContext 어노테이션을 이용해 EntityManager 빈을 주입한다.

    @Autowired
    ItemRepository itemRepository;  //ItemRepository를 사용하기 위해서 @autowired 어노테이션을 이용하여 Bean을 주입한다.

    @Test   //테스트할 메소드 위에 선언하여 해당 메소드를 테스트 대상으로 지정
    @DisplayName("상품 저장 테스트")   //Junit5에 추가된 어노테이션으로 테스트 코드 실행 시 @displayname에 지정한 테스트명이 노출된다.
    public void createItemTest(){   //
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }
    // 위의 코드로 insert 쿼리문을 입력하지 않아도, ItemRepository 인터페이스를 작성한 것만으로 상품테이블에 데이터를 insert하는 쿼리문이 만들어진다.
    //sprin data jpa는 인터페이스만 작성하면 런타임 시점에 자바의 dynamic proxy를 이용해서 객체를 동적으로 생성해준다.

    public void createItemList(){   //테스트 코드 실행 시 데이터베이스에 상품 데이터가 없어, 테스트 데이터 생성을 위해 10개읠 상품을 저장하는 메소드, findbyitemnmtest에서 실행
        for(int i=1;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100); item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");   //ItemRepository 인터페이스에 작성했던 findByItemNm 메소드를 호출
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();  //기존에 만들었던 테스트 상품을 만드는 메소드를 실행하여 조회할 대상을 만듬
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상숨 상세 설명5");  //상품명이 "테스트 상품1" 또는 상세 설명이 "테스트 상품 상세 설명5"이면
                                                                                                                             //해당 상품을 itemList에 할당한다.
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);    //현재 데이터베이스에 저장된 가격은 10001~10010이다. 10개의 상품 중 10001~10004가 출력된다.
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessOrderByPriceDesc(){     //OrderBy + 속성명 + Desc : 내림차순, OrderBy + 속성명 + Asc : 오름차순
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em); //JPAQueryFacotry를 이용하여 쿼리를 동적으로 생성한다. 생성자의 파라미터로는 EntityManager 객체를 넣어준다.
        QItem qItem = QItem.item;   //Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem 객체를 이용한다.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)   //자바 소스코드지만 sql문과 비슷하게 소스를 작성할 수 있다.
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();    //JPAQuery 메소드 중 하나인 fetch를 이용해서 커리 결과를 리스트로 반환
                                                //fetch() 메소드 실생 시점에 쿼리문이 실행된다. fetch() -> 조회 결과 리스트 반환

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

        @Test
        @DisplayName("상품 Querdsl 조회 테스트 2")
        public void queryDslTest2(){    //상품 데이터를 만드는 메소드, 0~5는 SELL, 6~10 SOLD_OUT
            this.createItemList();

            BooleanBuilder booleanBuilder = new BooleanBuilder();
            QItem item = QItem.item;
            String itemDeteil = "테스트 상품 상세 설명";
            int price = 10003;
            String itemSellStat = "SELL";

            booleanBuilder.and(item.itemDetail.like("%" + itemDeteil + "%"));   //필요한 상품을 조회하는데 필요한 "and" 조건을 추가
            booleanBuilder.and(item.price.gt(price));

            if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){              //sell 상태일때만 판매상태 조건을 동적으로 추가
                booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
            }

            Pageable pageable = PageRequest.of(0, 5);   //첫 번째 인자는 조회할 페이지의 번호, 두 번째 인자는 한 페이지당 조회할 데이터의 개수, PageRequest.of(a, b) 데이터를 페이징해 조회한다.
            Page<Item> itemPageResult =
                    itemRepository.findAll(booleanBuilder, pageable);   //findAll로 조건에 맞는 데이터를 Page 객체로 받아온다.
            System.out.println("total elements : " + itemPageResult.getTotalElements());

            List<Item> resultItemList = itemPageResult.getContent();
            for(Item resultItem : resultItemList){
                System.out.println(resultItem.toString());
            }

        }
    }