package com.example.jpashop.web;

import com.example.jpashop.domain.entity.item.Book;
import com.example.jpashop.domain.entity.item.Item;
import com.example.jpashop.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @RequestMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "item/createItemForm";
    }

    @RequestMapping("/{itemId}/edit")
    public String updateForm(@PathVariable(name = "itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);

        Book form = new Book();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "item/updateItemForm";
    }

    @PostMapping("/new")
    public String create(BookForm bookForm) {
        Book book = new Book();
        book.setName(bookForm.getName());
        book.setPrice(bookForm.getPrice());
        book.setStockQuantity(bookForm.getStockQuantity());
        book.setAuthor(bookForm.getAuthor());
        book.setIsbn(bookForm.getIsbn());

        itemService.save(book);
        return "redirect:/";
    }

    @GetMapping
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "item/itemList";
    }

    @PostMapping("/{itemId}/edit")
    public String updateForm(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
        // todo 실무에서는 파라미터로 들어오는 itemId 조작해서 들어올 수 있다 => 백엔드에서 요청 사용자의 권한을 체크하는 등의 방법으로 체크하는 것이 기능이 필수적이다
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.save(book);
        return "redirect:/items";
    }
}
