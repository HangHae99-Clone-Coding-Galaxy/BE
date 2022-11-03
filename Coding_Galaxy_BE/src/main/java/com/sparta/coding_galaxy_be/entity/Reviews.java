package com.sparta.coding_galaxy_be.entity;

import com.sparta.coding_galaxy_be.dto.requestDto.ReviewRequestDto;
import com.sparta.coding_galaxy_be.util.TimeStamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Reviews extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Courses course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    public void updateReview(ReviewRequestDto reviewRequestDto) {
        this.comment = reviewRequestDto.getComment();
    }
}
