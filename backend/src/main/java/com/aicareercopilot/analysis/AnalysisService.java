package com.aicareercopilot.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AnalysisService {
    private static final String NVIDIA_URL = "https://integrate.api.nvidia.com/v1/chat/completions";
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public AnalysisService(
        RestClient.Builder builder,
        ObjectMapper objectMapper,
        @Value("${integrations.nvidia.key}") String apiKey,
        @Value("${integrations.nvidia.model}") String model
    ) {
        this.restClient = builder.build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
    }

    public Map<String, Object> analyze(Map<String, Object> profile, Map<String, Object> job) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "NVIDIA API key is not configured");
        }
        if (profile.get("role") == null || job.get("title") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile role and job title are required");
        }

        String prompt = """
            You are an expert career coach and technical recruiter. Analyse the candidate against the job.
            Candidate profile: %s
            Job details: %s
            Return only valid JSON with exactly these fields:
            matchScore (integer 0-100), recommendation (Highly Recommended, Recommended, Consider, or Not Recommended),
            matchingSkills (string array), missingSkills (string array), whyApply (string), whySkip (string),
            interviewQuestions (5 strings), prepPlan (4 strings).
            Be specific and constructive. Do not invent candidate qualifications.
            """.formatted(profile, job);

        Map<String, Object> payload = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", "You are an expert career coach. Return structured JSON only."),
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.3,
            "max_tokens", 2000,
            "top_p", 0.9
        );

        try {
            Map<?, ?> response = restClient.post()
                .uri(NVIDIA_URL)
                .header("Authorization", "Bearer " + apiKey)
                .body(payload)
                .retrieve()
                .body(Map.class);

            Object choicesValue = response == null ? null : response.get("choices");
            if (!(choicesValue instanceof List<?> choices) || choices.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Empty response from AI service");
            }
            Object messageValue = ((Map<?, ?>) choices.get(0)).get("message");
            Object contentValue = messageValue instanceof Map<?, ?> message ? message.get("content") : null;
            if (contentValue == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Empty response from AI service");
            }

            String content = contentValue.toString().trim();
            int start = content.indexOf('{');
            int end = content.lastIndexOf('}');
            if (start < 0 || end <= start) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI response did not contain JSON");
            }
            Map<String, Object> analysis = objectMapper.readValue(
                content.substring(start, end + 1),
                objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, Object.class)
            );

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("analysis", analysis);
            return result;
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI analysis is temporarily unavailable");
        }
    }
}