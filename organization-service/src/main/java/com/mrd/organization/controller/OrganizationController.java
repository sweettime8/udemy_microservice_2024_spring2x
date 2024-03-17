package com.mrd.organization.controller;

import com.mrd.organization.dto.OrganizationDto;
import com.mrd.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/organizations")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto){
        OrganizationDto savedOrganization = organizationService.saveOrganization(organizationDto);
        return new ResponseEntity<>(savedOrganization, HttpStatus.CREATED);
    }

    @GetMapping("{code}")
    public ResponseEntity<OrganizationDto> saveOrganization(@PathVariable String code){
        OrganizationDto organizationDto = organizationService.getOrganizationByCode(code);
        return new ResponseEntity<>(organizationDto, HttpStatus.OK);
    }
}
