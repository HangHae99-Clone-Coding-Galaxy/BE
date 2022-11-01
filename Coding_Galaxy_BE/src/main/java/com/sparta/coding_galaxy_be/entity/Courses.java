package com.sparta.coding_galaxy_be.entity;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
import com.sparta.coding_galaxy_be.util.TimeStamped;
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
public class Courses extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    // imageList??
    // 썸네일??
    @Column(name = "thumbNail")
    private String thumbNail;

    @Column(name = "video")
    private String video;

    @Column(name = "price")
    private int price;
    
    // 작성자 mapping 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kakaomember_id")
    private KakaoMembers kakaoMember;
    
    // review 양방향 처리 질문 필요
    // 우선 단방향 처리 예정

    public void editCourseDetail(CourseRequestDto courseRequestDto) {
        this.title = courseRequestDto.getTitle();
        this.content = courseRequestDto.getContent();
    }

    public void editCourseMedia(String thumbnailUrl, String videoUrl) {
        this.thumbNail = thumbnailUrl;
        this.video = videoUrl;
    }
}
