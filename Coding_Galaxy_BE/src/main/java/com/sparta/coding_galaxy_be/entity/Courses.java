package com.sparta.coding_galaxy_be.entity;

import com.sparta.coding_galaxy_be.dto.requestDto.CourseRequestDto;
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
public class Courses extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "thumbNail")
    private String thumbNail;

    @Column(name = "video")
    private String video;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    public void editCourseDetail(CourseRequestDto courseRequestDto) {
        this.title = courseRequestDto.getTitle();
        this.content = courseRequestDto.getContent();
        this.price = courseRequestDto.getPrice();
    }

    public void editCourseMedia(String thumbnailUrl, String videoUrl) {
        this.thumbNail = thumbnailUrl;
        this.video = videoUrl;
    }
}
