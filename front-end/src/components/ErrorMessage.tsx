import { useNavigate } from "react-router-dom";
import PageLayout from "./PageLayout";
import "../styles/errorMessage.css";
import Tools from "../assets/tools.svg";

type ErrorProps = {
  title?: string;
  buttonText?: string;
  redirectTo?: string;
};

export default function ErrorMessage({
  title = "Oops, something went wrong...",
  buttonText = "Back to setup",
  redirectTo = "/",
}: ErrorProps) {
  const navigate = useNavigate();

  return (
    <PageLayout className="error-wrapper">
      <div className="content-card">
        <img src={Tools} alt="Stop"/>
        <h2>{title}</h2>
        <button className="btn-next" onClick={() => navigate(redirectTo)}>
          {buttonText}
        </button>
      </div>
    </PageLayout>
  );
}