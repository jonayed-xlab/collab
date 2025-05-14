package com.jbtech.collab.service.impl;

import com.jbtech.collab.model.Wiki;
import com.jbtech.collab.repository.WikiRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.IWikiService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WikiService extends BaseService implements IWikiService {

    private final WikiRepository wikiRepo;

    public WikiService(JwtUtil jwtUtil, WikiRepository wikiRepo) {
        super(jwtUtil);
        this.wikiRepo = wikiRepo;
    }

    @Override
    public Wiki create(Wiki request) {
        return wikiRepo.save(request);
    }

    @Override
    public Wiki update(Long id, Wiki request) {
        Wiki wiki = wikiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wiki not found"));

        wiki.setTitle(request.getTitle());
        wiki.setDescription(request.getDescription());

        return wikiRepo.save(wiki);
    }

    @Override
    public void delete(Long id) {
        wikiRepo.deleteById(id);
    }

    @Override
    public Wiki getById(Long id) {
        return wikiRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Wiki not found"));
    }

    @Override
    public List<Wiki> getAll() {
        return wikiRepo.findAll();
    }

}
