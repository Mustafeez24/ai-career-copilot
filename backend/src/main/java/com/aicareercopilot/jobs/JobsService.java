package com.aicareercopilot.jobs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

@Service
public class JobsService {
    private final RestClient restClient;
    private final String apiKey;

    public JobsService(RestClient.Builder builder, @Value("${integrations.serpapi.key}") String apiKey) {
        this.restClient = builder.build();
        this.apiKey = apiKey;
    }

    public Map<String, Object> search(String role, String location, String experience, String rawSkills) {
        String cleanRole = role == null ? "" : role.trim();
        if (cleanRole.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job role (q) is required");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "SerpApi key is not configured");
        }

        List<String> skills = rawSkills == null ? List.of() : java.util.Arrays.stream(rawSkills.split(","))
            .map(String::trim).filter(value -> !value.isBlank()).distinct().limit(8).toList();
        String query = cleanRole + (skills.isEmpty() ? "" : " " + String.join(" ", skills.subList(0, Math.min(4, skills.size()))));

        try {
            Map<?, ?> data = restClient.get()
                .uri(uriBuilder -> buildUri(uriBuilder, query, location))
                .retrieve()
                .body(Map.class);

            List<Map<String, Object>> jobs = new ArrayList<>();
            Object rawJobs = data == null ? null : data.get("jobs_results");
            if (rawJobs instanceof List<?> list) {
                for (Object item : list) {
                    if (item instanceof Map<?, ?> job) jobs.add(formatJob(job));
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("success", true);
            result.put("query", query);
            result.put("count", jobs.size());
            result.put("jobs", jobs);
            return result;
        } catch (ResponseStatusException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to retrieve jobs from SerpApi");
        }
    }

    private java.net.URI buildUri(UriBuilder builder, String query, String location) {
        var uri = builder.scheme("https").host("serpapi.com").path("/search.json")
            .queryParam("engine", "google_jobs")
            .queryParam("q", query)
            .queryParam("api_key", apiKey)
            .queryParam("hl", "en")
            .queryParam("gl", "in");
        if (location != null && !location.isBlank()) uri.queryParam("location", location.trim());
        return uri.build();
    }

    private Map<String, Object> formatJob(Map<?, ?> job) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", job.get("job_id"));
        result.put("title", job.get("title"));
        result.put("company", job.get("company_name"));
        result.put("location", job.get("location"));
        result.put("via", job.get("via"));
        result.put("description", job.get("description"));
        result.put("highlights", job.get("job_highlights"));
        result.put("salary", extension(job, "salary"));
        result.put("experience", extension(job, "experience"));
        result.put("jobType", extension(job, "type"));
        result.put("workFromHome", nested(job, "detected_extensions", "work_from_home"));
        result.put("scheduleType", nested(job, "detected_extensions", "schedule_type"));
        result.put("applyLink", job.get("apply_link"));
        result.put("postedAt", job.get("posted_at"));
        return result;
    }

    private Object extension(Map<?, ?> job, String keyword) {
        Object value = job.get("extensions");
        if (value instanceof List<?> list) {
            for (Object item : list) {
                if (item instanceof Map<?, ?> ext) {
                    Object name = ext.get("name");
                    if (name != null && name.toString().toLowerCase().contains(keyword)) return ext.get("value");
                }
            }
        }
        return null;
    }

    private Object nested(Map<?, ?> map, String parent, String key) {
        Object value = map.get(parent);
        return value instanceof Map<?, ?> nested ? nested.get(key) : null;
    }
}