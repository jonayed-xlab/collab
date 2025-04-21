
package com.jbtech.collab.service;

import com.jbtech.collab.model.Wiki;

import java.util.List;

public interface IWikiService {
    Wiki create(Wiki request);
    Wiki update(Long id, Wiki request);
    void delete(Long id);
    Wiki getById(Long id);
    List<Wiki> getAll();
}


