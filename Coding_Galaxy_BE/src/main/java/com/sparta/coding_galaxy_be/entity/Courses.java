package com.sparta.coding_galaxy_be.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@Entity(name = "course")
@NoArgsConstructor
@AllArgsConstructor
public class Courses {

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
    @Column(name = "image")
    @ElementCollection
    private List<String> image;

    @Column(name = "video")
    private String video;
    
    // 작성자 mapping 추가
    
    // review 양방향 처리 질문 필요
    // 우선 단방향 처리 예정

    public void updateCourse(String title, String content, List<String> image, String video) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.video = video;
    }
}
