package com.tp.pry20220169.controller;

import com.tp.pry20220169.domain.model.Bibliometric;
import com.tp.pry20220169.domain.service.BibliometricService;
import com.tp.pry20220169.resource.BibliometricResource;
import com.tp.pry20220169.resource.SaveBibliometricResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Bibliometrics", description = "Bibliometrics API")
@RestController
@RequestMapping("/api/bibliometrics")
@CrossOrigin
public class BibliometricController {

    @Autowired
    private BibliometricService bibliometricService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("")
    public Page<BibliometricResource> getAllBibliometrics(Pageable pageable){
        Page<Bibliometric> bibliometricPage = bibliometricService.getAllBibliometrics(pageable);
        List<BibliometricResource> resources = bibliometricPage.getContent()
                .stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources, pageable, resources.size());
    }

    @GetMapping("/{bibliometricId}")
    public BibliometricResource getBibliometricById(@PathVariable(name = "bibliometricId") Long bibliometricId){
        return convertToResource(bibliometricService.getBibliometricById(bibliometricId));
    }

    @PostMapping("")
    public BibliometricResource createBibliometric(@Valid @RequestBody SaveBibliometricResource resource){
        Bibliometric bibliometric = convertToEntity(resource);
        return convertToResource(bibliometricService.createBibliometric(bibliometric));
    }

    @PutMapping("/{bibliometricId}")
    public BibliometricResource updateBibliometric(@PathVariable(name = "bibliometricId") Long bibliometricId,
                                                   @Valid @RequestBody SaveBibliometricResource resource){
        Bibliometric bibliometric = convertToEntity(resource);
        return convertToResource(bibliometricService.updateBibliometric(bibliometricId, bibliometric));
    }

    @DeleteMapping("/{bibliometricId}")
    public ResponseEntity<?> deleteBibliometric(@PathVariable(name = "bibliometricId") Long bibliometricId){
        return bibliometricService.deleteBibliometric(bibliometricId);
    }

    private Bibliometric convertToEntity(SaveBibliometricResource resource) { return mapper.map(resource, Bibliometric.class); }
    private BibliometricResource convertToResource(Bibliometric entity) { return mapper.map(entity, BibliometricResource.class); }
}
