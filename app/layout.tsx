import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: {
    default: "AI Career Copilot",
    template: "%s | AI Career Copilot",
  },
  applicationName: "AI Career Copilot",
  description:
    "AI-powered career guidance and job recommendation system for discovering opportunities, analysing job fit, and identifying skill gaps.",
  openGraph: {
    title: "AI Career Copilot",
    description:
      "Discover relevant jobs, analyse your skills, and get AI-powered career guidance.",
    type: "website",
  },
  twitter: {
    card: "summary",
    title: "AI Career Copilot",
    description:
      "AI-powered career guidance and job recommendation system.",
  },
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html
      lang="en"
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col">{children}</body>
    </html>
  );
}
