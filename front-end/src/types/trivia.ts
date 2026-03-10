export type QuestionFilterDto = {
  amount: number;
  category?: number;
  difficulty?: string;
  type?: string;
};

export type QuestionDto = {
  id: number;
  question: string;
  category: string;
  difficulty: string;
  type: string;
  answers: string[];
};

export type AnswerDto = {
  questionId: number;
  answer: string;
};

export type SubmitAnswersRequest = {
  answers: AnswerDto[];
};

export type ResultDto = {
    question: QuestionDto,
    submittedAnswer: string,
    correctAnswer: string,
    result: boolean
}