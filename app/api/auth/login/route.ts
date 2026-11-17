import { NextRequest, NextResponse } from "next/server";

export async function POST(request: NextRequest) {
  const backendUrl = process.env.SPRING_BOOT_API_URL;
  if (!backendUrl) {
    return NextResponse.json(
      { error: "Spring Boot API is not configured. Set SPRING_BOOT_API_URL." },
      { status: 503 }
    );
  }

  try {
    const body = await request.json();
    const response = await fetch(new URL("/api/auth/login", backendUrl), {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
      cache: "no-store",
    });
    const payload = await response.json();
    return NextResponse.json(payload, { status: response.status });
  } catch (error) {
    console.error("Login proxy failed:", error);
    return NextResponse.json({ error: "Unable to reach the authentication service." }, { status: 502 });
  }
}