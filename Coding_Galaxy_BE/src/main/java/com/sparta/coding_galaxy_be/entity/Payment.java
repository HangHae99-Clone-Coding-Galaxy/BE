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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private String paymentId;

    @Column
    private String item_name;

    @Column
    private Long item_code;

    @Column
    private String created_at;

    @Column
    private String approved_at;

    @Column
    private int amount;

    @Column
    private String payment_method_type;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private KakaoMembers kakaoMember;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Courses course;

    public void updatePayment(KakaoPayApprovalResponseDto kakaoPayApprovalResponseDto){
        this.created_at = kakaoPayApprovalResponseDto.getCreated_at();
        this.approved_at = kakaoPayApprovalResponseDto.getApproved_at();
        this.payment_method_type = kakaoPayApprovalResponseDto.getPayment_method_type();
    }
}
