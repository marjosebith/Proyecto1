//package org.jala.university.application.mapper;
//
//import org.jala.university.application.dto.UserServiceDto;
//import org.jala.university.domain.entity.UserService;
//
///**
// * Mapper para convertir entre UserService entity y UserServiceDto.
// */
//public class UserServiceMapper {
//
//    /**
//     * Convierte una entidad UserService a DTO.
//     */
//    public UserServiceDto toDto(UserService userService) {
//        return UserServiceDto.builder()
//                .userServiceId(userService.getUserServiceId())
//                .userId(userService.getUserId())
//                .serviceId(userService.getServiceId())
//                .accountNumber(userService.getAccountNumber())
//                .alias(userService.getAlias())
//                .notes(userService.getNotes())
//                .isActive(userService.getIsActive())
//                .createdAt(userService.getCreatedAt())
//                .updatedAt(userService.getUpdatedAt())
//                .serviceName(userService.getServiceName())
//                .serviceType(userService.getServiceType())
//                .providerName(userService.getProviderName())
//                .build();
//    }
//
//    /**
//     * Convierte un DTO a entidad UserService.
//     */
//    public UserService toEntity(UserServiceDto dto) {
//        return UserService.builder()
//                .userServiceId(dto.getUserServiceId())
//                .userId(dto.getUserId())
//                .serviceId(dto.getServiceId())
//                .accountNumber(dto.getAccountNumber())
//                .alias(dto.getAlias())
//                .notes(dto.getNotes())
//                .isActive(dto.getIsActive())
//                .createdAt(dto.getCreatedAt())
//                .updatedAt(dto.getUpdatedAt())
//                .serviceName(dto.getServiceName())
//                .serviceType(dto.getServiceType())
//                .providerName(dto.getProviderName())
//                .build();
//    }
//}

