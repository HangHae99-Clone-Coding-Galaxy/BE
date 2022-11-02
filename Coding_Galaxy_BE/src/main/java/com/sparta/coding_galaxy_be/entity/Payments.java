package com.sparta.coding_galaxy_be.entity;

import com.sparta.coding_galaxy_be.dto.responseDto.KakaoPayApprovalResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payments {

    @Id
    @Column(name = "payment_id")
    private String paymentId;

    @Column
    private String itemName;

    @Column
    private Long itemCode;

    @Column
    private String createdAt;

    @Column
    private String approvedAt;

    @Column
    private int amount;

    @Column
    private String paymentMethodType;

    @Column
    private boolean paycheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses course;

    public void updatePayment(KakaoPayApprovalResponseDto kakaoPayApprovalResponseDto){
        this.createdAt = kakaoPayApprovalResponseDto.getCreated_at();
        this.approvedAt = kakaoPayApprovalResponseDto.getApproved_at();
        this.paymentMethodType = kakaoPayApprovalResponseDto.getPayment_method_type();
        this.paycheck = true;
    }
}
