package com.aicareercopilot.analysis;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analyze")
public class AnalysisController {
    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public record AnalysisRequest(@NotNull Map<String, Object> profile, @NotNull Map<String, Object> job) {}

    @PostMapping
    public Map<String, Object> analyze(@RequestBody AnalysisRequest request) {
        return analysisService.analyze(request.profile(), request.job());
    }
}