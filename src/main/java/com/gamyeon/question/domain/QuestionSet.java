package com.gamyeon.question.domain;

import com.gamyeon.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class QuestionSet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long intvId;

    private String content;

    protected QuestionSet() {}

    private QuestionSet(Long intvId, String content) {
        this.intvId = intvId;
        this.content = content;
    }

    public static QuestionSet save(Long intvId, String content){
        return new QuestionSet(intvId, content);
    }
}
