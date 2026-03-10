import type { ReactNode } from "react";
import AppHeader from "./AppHeader";

type PageLayoutProps = {
  children: ReactNode;
  className?: string;
};

export default function PageLayout({ children, className = "" }: PageLayoutProps) {
  return (
    <div className={`wrapper ${className}`.trim()}>
      <AppHeader />
        {children}
    </div>
  );
}