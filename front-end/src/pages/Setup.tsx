import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { QuestionFilterDto } from "../types/trivia";
import { fetchQuestions } from "../services/triviaApi";
import { CATEGORIES } from "../constants/triviaCategories";
import "../styles/setup.css";
import PageLayout from "../components/PageLayout";

export default function Setup() {
  const [filter, setFilter] = useState<QuestionFilterDto>({
    amount: 10,
    category: 0,
    difficulty: "",
    type: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string>("");

  const navigate = useNavigate();

  // Start quiz - fetch data
  async function onStart() {
    setError("");
    setLoading(true);
    try {
      const filterDto: QuestionFilterDto = {
        amount: filter.amount,
        category: filter.category || undefined,
        difficulty: filter.difficulty?.trim() || undefined,
        type: filter.type?.trim() || undefined,
      };

      const questionData = await fetchQuestions(filterDto);

      // Extra check because some filter combinations give back empty results (for example: https://opentdb.com/api.php?amount=10&category=14&difficulty=easy&type=boolean)
      if (questionData.length > 0) {
          navigate("/quiz", { state: { questions: questionData } });
      } else {
        setError("No questions were found for filter, please try a different combination.");
      }
    } catch (e) {
      console.log(e);
      setError("Oops, something went wrong...");
    } finally {
      setLoading(false);
    }
  }

  return (
    <PageLayout className="setup-wrapper">
      <div className="content-card">
        <h2>Quiz setup</h2>
        <br></br>
        <label>
          Amount of questions
          <input
            type="number"
            min={1}
            max={50}
            value={filter.amount}
            onChange={(e) =>
              setFilter((f) => ({ ...f, amount: Number(e.target.value) }))
            }
          />
        </label>

        <label>
          Category
          <select
            value={filter.category ?? "any"}
            onChange={(e) =>
              setFilter((f) => ({
                ...f,
                category:
                  e.target.value === "any" ? undefined : Number(e.target.value),
              }))
            }
          >
            <option value="any">Any Category</option>
            {CATEGORIES.map((cat) => (
              <option key={cat.value} value={cat.value}>
                {cat.label}
              </option>
            ))}
          </select>
        </label>

        <label>
          Difficulty
          <select
            value={filter.difficulty ?? ""}
            onChange={(e) =>
              setFilter((f) => ({ ...f, difficulty: e.target.value }))
            }
          >
            <option value="">(any)</option>
            <option value="easy">easy</option>
            <option value="medium">medium</option>
            <option value="hard">hard</option>
          </select>
        </label>

        <label>
          Type
          <select
            value={filter.type ?? ""}
            onChange={(e) => setFilter((f) => ({ ...f, type: e.target.value }))}
          >
            <option value="">(any)</option>
            <option value="multiple">Multiple-choice</option>
            <option value="boolean">True/False</option>
          </select>
        </label>

        <button
          className="btn-green"
          onClick={onStart}
          disabled={loading || filter.amount < 1}
        >
          {loading ? "Loading..." : "Start"}
        </button>

        {error && <p className="txt-error">{error}</p>}
      </div>
    </PageLayout>
  );
}
