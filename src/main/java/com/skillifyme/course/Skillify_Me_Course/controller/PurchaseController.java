package com.skillifyme.course.Skillify_Me_Course.controller;

import com.skillifyme.course.Skillify_Me_Course.model.dto.PaymentResponse;
import com.skillifyme.course.Skillify_Me_Course.service.PurchaseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("{courseId}")
    public ResponseEntity<?> purchaseCourse(
            @PathVariable ObjectId courseId,
            @RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                PaymentResponse response = purchaseService.purchaseCourse(courseId, jwt);
                return ResponseEntity.ok(response + "Course purchased successfully");
            } else {
                return new ResponseEntity<>("invalid token", HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
