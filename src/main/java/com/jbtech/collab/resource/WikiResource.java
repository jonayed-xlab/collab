package com.jbtech.collab.resource;

import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.Wiki;
import com.jbtech.collab.service.IWikiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/wikis")
@RequiredArgsConstructor
public class WikiResource extends BaseResource {

    private final IWikiService wikiService;

    @PostMapping
    public ApiResponse<Wiki> create(@RequestBody Wiki request) {
        return ApiResponse.success(
                wikiService.create(request)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Wiki> update(@PathVariable Long id, @RequestBody Wiki request) {
        return ApiResponse.success(
                wikiService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        wikiService.delete(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<Wiki> getById(@PathVariable Long id) {
        return ApiResponse.success(
                wikiService.getById(id)
        );
    }

    @GetMapping
    public ApiResponse<List<Wiki>> getAll() {
        return ApiResponse.success(
                wikiService.getAll()
        );
    }
}