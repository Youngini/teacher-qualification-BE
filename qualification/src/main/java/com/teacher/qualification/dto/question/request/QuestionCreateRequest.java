package com.teacher.qualification.dto.question.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public record QuestionCreateRequest(
        QuestionRequestDto questionDto,
        List<OptionRequestDto> optionDtos,
        AnswerRequestDto answerDto
) {
}