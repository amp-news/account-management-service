package com.amp.accountmanagement.model.dto;

import java.util.List;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

@Data
public class PageTO<T> extends ResourceSupport {

  private static final String PAGE_PARAM = "page";
  private static final String SIZE_PARAM = "size";

  private List<T> content;

  private Integer page;
  private Integer pageSize;
  private Integer pageTotalElements;
  private Long totalElements;

  private Boolean firstPage;
  private Boolean lastPage;
  private Integer totalPages;

  public PageTO(Page<T> page) {
    this.content = page.getContent();

    this.page = page.getNumber();
    this.pageSize = page.getSize();
    this.pageTotalElements = page.getNumberOfElements();
    this.totalElements = page.getTotalElements();

    this.firstPage = page.isFirst();
    this.lastPage = page.isLast();
    this.totalPages = page.getTotalPages();


    this.add(buildPageLink(page.getPageable(), Link.REL_SELF));
    if (page.hasPrevious()) {
      this.add(buildPageLink(page.previousPageable(), Link.REL_PREVIOUS));
    }
    if (page.hasNext()) {
      this.add(buildPageLink(page.nextPageable(), Link.REL_NEXT));
    }
  }

  private Link buildPageLink(Pageable pageable, String rel) {
    final String ref = fromCurrentRequestUri()
        .queryParam(PAGE_PARAM, pageable.getPageNumber())
        .queryParam(SIZE_PARAM, pageable.getPageSize())
        .build()
        .toUriString();

    return new Link(ref, rel);
  }
}
