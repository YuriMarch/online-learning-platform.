package com.distancelearning.authuser.clients;

import com.distancelearning.authuser.dtos.CourseDto;
import com.distancelearning.authuser.dtos.ResponsePageDto;
import com.distancelearning.authuser.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
@Log4j2
public class CourseClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${distancelearning.api.url.course}")
    String REQUEST_URL_COURSE;

    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDto> searchResult = null;
        String url = REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUser(userId, pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);
        ResponseEntity<ResponsePageDto<CourseDto>> result = null;
        try {
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("Response number of elements: {}", searchResult.size());
        } catch (HttpStatusCodeException e) {

            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /courses userId {}", userId);
        return result.getBody();
    }

    public void deleteUserInCourse(UUID userId) {
        String url = REQUEST_URL_COURSE + "/courses/users/" + userId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
