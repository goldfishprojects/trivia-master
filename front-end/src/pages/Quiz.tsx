import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import type { AnswerDto, QuestionDto } from "../types/trivia";
import { submitAnswers } from "../services/triviaApi";
import "../styles/quiz.css";
import ErrorMessage from "../components/ErrorMessage";
import PageLayout from "../components/PageLayout";

type LocationState = {
  questions: QuestionDto[];
};

export default function Quiz() {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as LocationState | null;

  const questions = state?.questions ?? [];

  const [index, setIndex] = useState(0);
  const [answers, setAnswers] = useState<Record<number, string>>({});
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  // Select Answer
  function choose(option: string) {
    setAnswers((prev) => ({ ...prev, [q.id]: option }));
  }

  // Next question
  async function next() {
    if (!selected) return;
    setError("");

    const isLast = index === questions.length - 1;
    if (!isLast) {
      setIndex((i) => i + 1);
      return;
    }

    // Submit after final question
    setSubmitting(true);
    try {
      const payload: AnswerDto[] = questions.map((question) => ({
        questionId: question.id,
        answer: answers[question.id],
      }));

      const results = await submitAnswers(payload);
      navigate("/results", { state: { results: results } });
    } catch (e) {
      setError(e instanceof Error ? e.message : "Submit failed");
    } finally {
      setSubmitting(false);
    }
  }

  // Previous question
  function back() {
    if (index > 0) setIndex((i) => i - 1);
  }

  if (questions.length === 0) {
    console.log(questions);
    return <ErrorMessage />;
  }

  const q = questions[index];
  const options = q.answers ?? [];
  const selected = answers[q.id] ?? "";
  const progress = `${index + 1} / ${questions.length}`;

  return (
    <PageLayout className="quiz-wrapper">
      <div className="content-card">
        <h2>Question {progress}</h2>
        <span className="txt-question">{q.question}</span>

        <div className="question-details">
          <div>
            <h3>Difficulty</h3>
            <span>{q.difficulty}</span>
          </div>
          <div>
            <h3>Category</h3>
            <span>{q.category}</span>
          </div>
        </div>

        <div className="quiz-answers">
          {options.map((opt) => {
            const isSelected = opt === selected;
            return (
              <button
                key={opt}
                onClick={() => choose(opt)}
                className={isSelected ? "btn-selected" : ""}
              >
                {opt}
              </button>
            );
          })}
        </div>

        {error && <p className="txt-error">{error}</p>}
      </div>

      <div className="quiz-controls">
        <button
          className="btn-back"
          onClick={back}
          disabled={index === 0 || submitting}
        >
          Back
        </button>

        <button
          onClick={next}
          disabled={!selected || submitting}
          className={index === questions.length - 1 ? "btn-green" : "btn-next"}
        >
          {submitting
            ? "Submitting..."
            : index === questions.length - 1
              ? "Finish"
              : "Next"}
        </button>
      </div>
    </PageLayout>
  );
}
