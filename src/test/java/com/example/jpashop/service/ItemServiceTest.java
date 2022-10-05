package com.example.jpashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.jpashop.domain.entity.item.Album;
import com.example.jpashop.domain.entity.item.Book;
import com.example.jpashop.domain.entity.item.Movie;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    EntityManager em;
    @Autowired ItemService itemService;

    @DisplayName("상품 등록")
    @Test
    void newItem() {
        //given
        Book book = new Book();
        book.setName("스프링책1");
        book.setPrice(2000);
        book.setStockQuantity(100);
        book.setAuthor("저자1");
        book.setIsbn("1234");

        Movie movie = new Movie();
        movie.setName("재밌는영화");
        movie.setPrice(10000);
        movie.setStockQuantity(10);
        movie.setActor("박보검");
        movie.setDirector("감독1");

        Album album = new Album();
        album.setName("좋은노랙");
        album.setPrice(3000);
        album.setStockQuantity(300);
        album.setArtist("가수1");
        album.setEtc("그 외");

        //when
        Long bookId = itemService.save(book);
        Long movieId = itemService.save(movie);
        Long albumId = itemService.save(album);

        em.clear(); // clear 안하면 => 영속성컨텍스트 데이터를 리턴한다(아래 then에서 select 쿼리가 발생하지 않는다)
        System.out.println("=============");

        //then
        assertEquals(book.getAuthor(), ((Book) itemService.findOne(bookId)).getAuthor());
        assertEquals(movie.getActor(), ((Movie)itemService.findOne(movieId)).getActor());
        assertEquals(album.getArtist(), ((Album) itemService.findOne(albumId)).getArtist());

    }
}