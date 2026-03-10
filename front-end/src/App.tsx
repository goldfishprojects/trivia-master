import { Routes, Route, Navigate } from "react-router-dom";
import SetupPage from "./pages/Setup";
import QuizPage from "./pages/Quiz";
import ResultPage from "./pages/Results";
import "./styles/responsive.css";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<SetupPage />} />
      <Route path="/quiz" element={<QuizPage />} />
      <Route path="/results" element={<ResultPage />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}