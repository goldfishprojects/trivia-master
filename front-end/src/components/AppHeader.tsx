import logo from "../assets/logo.webp";

export default function AppHeader() {
  return (
    <div className="wrapper-header">
      <img src={logo} alt={"TM"} />
      <h1>Trivia Master</h1>
    </div>
  );
}