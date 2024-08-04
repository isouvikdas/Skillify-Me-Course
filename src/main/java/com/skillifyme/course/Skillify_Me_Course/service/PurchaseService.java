package com.skillifyme.course.Skillify_Me_Course.service;

import com.skillifyme.course.Skillify_Me_Course.exception.PaymentException;
import com.skillifyme.course.Skillify_Me_Course.exception.ResourceNotFoundException;
import com.skillifyme.course.Skillify_Me_Course.exception.UnauthorizedException;
import com.skillifyme.course.Skillify_Me_Course.model.dto.PaymentRequest;
import com.skillifyme.course.Skillify_Me_Course.model.dto.PaymentResponse;
import com.skillifyme.course.Skillify_Me_Course.model.dto.ValidateTokenResponse;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Course;
import com.skillifyme.course.Skillify_Me_Course.model.entity.UserCourse;
import com.skillifyme.course.Skillify_Me_Course.repository.CourseRepository;
import com.skillifyme.course.Skillify_Me_Course.repository.UserCourseRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PurchaseService {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PaymentServiceClient paymentServiceClient;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Transactional
    public PaymentResponse purchaseCourse(ObjectId courseId, String token) throws RuntimeException {
        ValidateTokenResponse response = courseService.validateUser(token);

        if (!response.getValid()) {
            throw new UnauthorizedException("Invalid or unauthorized token");
        }

        String userEmail = response.getEmail();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id" + courseId));

        PaymentRequest paymentRequest = getPaymentRequest(userEmail, course);

        PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest);
        if (paymentResponse.isSuccess()) {
            UserCourse userCourse = userCourseRepository.findByUserEmail(userEmail).orElseGet(() -> {
                UserCourse newUserCourse = new UserCourse();
                newUserCourse.setUserEmail(userEmail);
                return newUserCourse;
            });

            userCourse.getCourseId().add(courseId);
            userCourseRepository.save(userCourse);

            return paymentResponse;
        } else {
            throw new PaymentException("Payment failed: " + paymentResponse.getMessage());
        }
    }

    private static PaymentRequest getPaymentRequest(String userEmail, Course course) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserEmail(userEmail);
        paymentRequest.setAmount(BigDecimal.valueOf(Long.parseLong(course.getPrice())));
        paymentRequest.setCurrency("USD");
        paymentRequest.setMethod("paypal");
        paymentRequest.setIntent("sale");
        paymentRequest.setDescription("Course purchase: " + course.getTitle());
        paymentRequest.setCancelUrl("http://yourapp.com/cancel");
        paymentRequest.setSuccessUrl("http://yourapp.com/success");
        return paymentRequest;
    }
}
