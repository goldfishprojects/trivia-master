import type { ResultDto } from "../types/trivia";
import { useLocation, useNavigate } from "react-router-dom";
import "../styles/results.css";
import ErrorMessage from "../components/ErrorMessage";
import PageLayout from "../components/PageLayout";

type ResultState = {
  results: ResultDto[];
};

export default function Results() {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state as ResultState | null;

  const results = state?.results ?? [];

  // Calculate scores
  const total = results.length;
  const correctCount = results.filter(
    (r) => r.submittedAnswer === r.correctAnswer,
  ).length;
  const pct = total === 0 ? 0 : Math.round((correctCount / total) * 100);

  // Decide result button color
  function getAnswerStyle(opt: string, result: ResultDto) {
    const isCorrect = opt === result.correctAnswer;
    const isSubmitted = opt === result.submittedAnswer;

    let background = "#fff";
    let border = "1px solid #ccc";

    if (isCorrect && isSubmitted) {
      background = "#d4edda";
      border = "1px solid #28a745";
    }

    if (isSubmitted && !isCorrect) {
      background = "#e0e0e0";
      border = "1px solid #999";
    }

    if (!isSubmitted && isCorrect) {
      background = "#ffafaf";
      border = "1px solid #f63030";
    }

    return { background, border };
  }

  if (results.length === 0) {
    return <ErrorMessage />;
  }

  return (
    <PageLayout className="results-wrapper">
      <div className="content-card">
        <h2>Results</h2>

        <div className="score-box">
          <strong>
            {correctCount}/{total} correct
          </strong>
          <span>{pct}%</span>
        </div>

        {results.map((result) => {
          return (
            <div key={result.question.id} className="result-card">
              <span>{result.question.question}</span>

              <div>
                {result.question.answers.map((opt) => {
                  const style = getAnswerStyle(opt, result);

                  return (
                    <button
                      key={opt}
                      disabled
                      style={{
                        border: style.border,
                        background: style.background,
                      }}
                    >
                      {opt}
                    </button>
                  );
                })}
              </div>
            </div>
          );
        })}

        <div>
          <button className="btn-next" onClick={() => navigate("/")}>
            Start new Quiz
          </button>
        </div>
      </div>
    </PageLayout>
  );
}
