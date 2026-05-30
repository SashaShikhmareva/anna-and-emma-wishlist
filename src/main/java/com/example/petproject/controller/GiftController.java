package com.example.petproject.controller;

import com.example.petproject.GiftRepository;
import com.example.petproject.model.Gift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GiftController {

    @Autowired
    private GiftRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items", repository.findAll());
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("gift", new Gift());
        model.addAttribute("items", repository.findAll());
        return "admin";
    }

    @PostMapping("/admin/gifts")
    public String createGift(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String imageUrl
    ) {
        Gift gift = new Gift();
        gift.setTitle(title);
        gift.setDescription(description);
        gift.setImageUrl(imageUrl);

        repository.save(gift);

        return "redirect:/admin";
    }

    @PostMapping("/admin/gifts/{id}/delete")
    public String deleteGift(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/reserve/{id}")
    public String reserveItem(@PathVariable Long id, @RequestParam String name) {
        repository.findById(id).ifPresent(item -> {
            item.setReserved(true);
            item.setReservedBy(name);
            repository.save(item);
        });
        return "redirect:/";
    }

    @PostMapping("/unreserve/{id}")
    public String unreserveItem(@PathVariable Long id) {
        repository.findById(id).ifPresent(item -> {
            item.setReserved(false);
            item.setReservedBy(null);
            repository.save(item);
        });
        return "redirect:/";
    }

}
