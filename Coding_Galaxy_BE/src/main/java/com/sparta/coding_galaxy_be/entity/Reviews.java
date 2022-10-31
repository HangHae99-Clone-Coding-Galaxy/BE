package com.sparta.coding_galaxy_be.entity;

import com.sparta.coding_galaxy_be.util.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity(name = "review")
@NoArgsConstructor
@AllArgsConstructor
public class Reviews extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long reviewId;

    @Column(name = "star")
    private Long star;

    @Column(name = "comment")
    private String comment;

    // 강의 조인

    // 작성자 조인

    public void updateReview(Long star, String comment) {
        this.star = star;
        this.comment = comment;
    }

}
