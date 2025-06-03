package com.example.SentinelBE.controller.utils;

import com.example.SentinelBE.utils.converter.Converter;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelperMethods {
    public static <E, D> Map<String, Object> makeResponse(Page<E> page, Converter<E, D> converter) {
        List<E> objects = page.getContent();
        Map<String, Object> response = new HashMap<>();
        if (converter != null) {
            response.put("items", objects.stream()
                    .map(converter::createFromEntity)
                    .toList());
        } else {
            response.put("items", objects);
        }
        response.put("currentPage", page.getNumber());
        response.put("totalItems", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());

        return response;
    }

}
