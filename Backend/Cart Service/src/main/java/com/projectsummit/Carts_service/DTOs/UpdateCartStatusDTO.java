package com.projectsummit.Carts_service.DTOs;

import java.util.List;

public record UpdateCartStatusDTO(
        int CartId,
        String Status,
        List<Integer> ProductIdsInOrder
) {}