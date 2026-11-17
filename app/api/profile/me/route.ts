import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  const backendUrl = process.env.SPRING_BOOT_API_URL;
  if (!backendUrl) {
    return NextResponse.json(
      { error: "Spring Boot API is not configured. Set SPRING_BOOT_API_URL." },
      { status: 503 }
    );
  }

  const authorization = request.headers.get("authorization");
  if (!authorization?.startsWith("Bearer ")) {
    return NextResponse.json({ error: "A bearer token is required." }, { status: 401 });
  }

  try {
    const response = await fetch(new URL("/api/profile/me", backendUrl), {
      headers: { Authorization: authorization },
      cache: "no-store",
    });
    const payload = await response.json();
    return NextResponse.json(payload, { status: response.status });
  } catch (error) {
    console.error("Profile proxy failed:", error);
    return NextResponse.json({ error: "Unable to reach the profile service." }, { status: 502 });
  }
}

export async function PUT(request: NextRequest) {
  const backendUrl = process.env.SPRING_BOOT_API_URL;
  if (!backendUrl) {
    return NextResponse.json(
      { error: "Spring Boot API is not configured. Set SPRING_BOOT_API_URL." },
      { status: 503 }
    );
  }

  const authorization = request.headers.get("authorization");
  if (!authorization?.startsWith("Bearer ")) {
    return NextResponse.json({ error: "A bearer token is required." }, { status: 401 });
  }

  try {
    const body = await request.json();
    const response = await fetch(new URL("/api/profile/me", backendUrl), {
      method: "PUT",
      headers: {
        Authorization: authorization,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
      cache: "no-store",
    });
    const payload = await response.json();
    return NextResponse.json(payload, { status: response.status });
  } catch (error) {
    console.error("Profile update proxy failed:", error);
    return NextResponse.json({ error: "Unable to reach the profile service." }, { status: 502 });
  }
}