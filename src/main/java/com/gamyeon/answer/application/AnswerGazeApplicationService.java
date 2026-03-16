package com.gamyeon.answer.application;

import com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentCommand;
import com.gamyeon.answer.application.port.in.SendAnswerGazeSegmentUseCase;
import com.gamyeon.answer.application.port.out.AnswerGazeSegmentPayload;
import com.gamyeon.answer.application.port.out.LoadQuestionSetPort;
import com.gamyeon.answer.application.port.out.SendAnswerGazeToAiPort;
import com.gamyeon.answer.domain.AnswerErrorCode;
import com.gamyeon.answer.domain.AnswerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnswerGazeApplicationService implements SendAnswerGazeSegmentUseCase {

  private final LoadQuestionSetPort loadQuestionSetPort;
  private final SendAnswerGazeToAiPort sendAnswerGazeToAiPort;

  public AnswerGazeApplicationService(
      LoadQuestionSetPort loadQuestionSetPort, SendAnswerGazeToAiPort sendAnswerGazeToAiPort) {
    this.loadQuestionSetPort = loadQuestionSetPort;
    this.sendAnswerGazeToAiPort = sendAnswerGazeToAiPort;
  }

  @Override
  public void send(SendAnswerGazeSegmentCommand command) {
    if (!loadQuestionSetPort.existsById(command.questionSetId())) {
      throw new AnswerException(AnswerErrorCode.QUESTION_SET_NOT_FOUND);
    }

    sendAnswerGazeToAiPort.send(
        new AnswerGazeSegmentPayload(command.questionSetId(), command.body()));
  }
}
