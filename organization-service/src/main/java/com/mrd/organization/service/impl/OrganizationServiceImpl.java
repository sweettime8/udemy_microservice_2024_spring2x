package com.mrd.organization.service.impl;

import com.mrd.organization.dto.OrganizationDto;
import com.mrd.organization.entity.Organization;
import com.mrd.organization.repository.OrganizationRepository;
import com.mrd.organization.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private OrganizationRepository organizationRepository;

    private ModelMapper modelMapper;
    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {

        Organization organization = modelMapper.map(organizationDto, Organization.class);
        Organization savedOrganization = organizationRepository.save(organization);

        return modelMapper.map(savedOrganization, OrganizationDto.class);
    }

    @Override
    public OrganizationDto getOrganizationByCode(String code) {
        Organization organization = organizationRepository.findByOrganizationCode(code);

        return modelMapper.map(organization, OrganizationDto.class);
    }
}
