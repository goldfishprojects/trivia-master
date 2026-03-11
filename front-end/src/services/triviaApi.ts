import type { AnswerDto, QuestionDto, QuestionFilterDto, ResultDto } from "../types/trivia";

const API_BASE = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:9090/api";

export async function fetchQuestions(filter: QuestionFilterDto): Promise<QuestionDto[]> {
  const params = new URLSearchParams();

  params.set("amount", String(filter.amount));
  if (filter.category) params.set("category", String(filter.category));
  if (filter.difficulty) params.set("difficulty", filter.difficulty);
  if (filter.type) params.set("type", filter.type);

  const res = await fetch(`${API_BASE}/questions?${params.toString()}`, {
    method: "GET",
    headers: { "Accept": "application/json" },
  });

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`GET /questions failed (${res.status}) ${text}`);
  }

  return res.json();
}


export async function submitAnswers(answers: AnswerDto[]): Promise<ResultDto[]> {
  const res = await fetch(`${API_BASE}/answers/validate`, {
    method: "POST",
    headers: {
      "Accept": "application/json",
      "Content-Type": "application/json",
    },
    body: JSON.stringify(answers),
  });

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`POST /answers/validate failed (${res.status}) ${text}`);
  }

  return res.json();
}