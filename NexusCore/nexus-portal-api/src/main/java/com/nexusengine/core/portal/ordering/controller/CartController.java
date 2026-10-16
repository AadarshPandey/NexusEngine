package com.nexusengine.core.portal.ordering.controller;

import com.nexusengine.core.common.exception.Asserts;
import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.CartProduct;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.ordering.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - Cart", description = "Storefront shopping cart management APIs")
@RequestMapping("/portal/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartItemService;
    private final MemberService memberService;

    @Operation(summary = "Add item to cart")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer add(@Valid @RequestBody CartItem cartItem) {
        int count = cartItemService.add(cartItem);
        if (count <= 0) {
            Asserts.fail("Failed to add item to cart");
        }
        return count;
    }

    @Operation(summary = "List cart items")
    @GetMapping("/list")
    public List<CartItem> list() {
        return cartItemService.list(memberService.getCurrentMember().getId());
    }

    @Operation(summary = "List cart items with calculated promotions")
    @GetMapping("/list/promotion")
    public List<CartPromotionItem> listPromotion(
            @RequestParam(required = false) List<Long> cartIds) {
        return cartItemService.listPromotion(memberService.getCurrentMember().getId(), cartIds);
    }

    @Operation(summary = "Update cart item quantity")
    @GetMapping("/update/quantity")
    public Integer updateQuantity(
            @RequestParam("id") Long id, @RequestParam("quantity") Integer quantity) {
        int count =
                cartItemService.updateQuantity(
                        id, memberService.getCurrentMember().getId(), quantity);
        if (count <= 0) {
            Asserts.fail("Failed to update cart item quantity");
        }
        return count;
    }

    @Operation(summary = "Get product SKU options for cart item")
    @GetMapping("/getProduct/{productId}")
    public CartProduct getCartProduct(@PathVariable("productId") Long productId) {
        return cartItemService.getCartProduct(productId);
    }

    @Operation(summary = "Update cart item SKU attributes")
    @PostMapping("/update/attr")
    public Integer updateAttr(@Valid @RequestBody CartItem cartItem) {
        int count = cartItemService.updateAttr(cartItem);
        if (count <= 0) {
            Asserts.fail("Failed to update cart item attributes");
        }
        return count;
    }

    @Operation(summary = "Delete cart items")
    @PostMapping("/delete")
    public Integer delete(@RequestParam("ids") List<Long> ids) {
        int count = cartItemService.delete(memberService.getCurrentMember().getId(), ids);
        if (count <= 0) {
            Asserts.fail("Failed to delete cart items");
        }
        return count;
    }

    @Operation(summary = "Clear cart")
    @PostMapping("/clear")
    public Integer clear() {
        int count = cartItemService.clear(memberService.getCurrentMember().getId());
        if (count <= 0) {
            Asserts.fail("Failed to clear cart");
        }
        return count;
    }
}
