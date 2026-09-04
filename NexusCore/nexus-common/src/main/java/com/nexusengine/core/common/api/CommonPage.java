package com.nexusengine.core.common.api;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Common pagination data wrapper
 */
public class CommonPage<T> {
    /**
     * Current page number
     */
    private Integer pageNum;
    /**
     * Page size
     */
    private Integer pageSize;
    /**
     * Total pages
     */
    private Integer totalPage;
    /**
     * Total records
     */
    private Long total;
    /**
     * Page data
     */
    private List<T> list;

    /**
     * Convert Spring Data Page to CommonPage
     */
    public static <T> CommonPage<T> restPage(Page<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getTotalPages());
        result.setPageNum(pageInfo.getNumber());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotalElements());
        result.setList(pageInfo.getContent());
        return result;
    }

    /**
     * Create a CommonPage from a plain list (no pagination metadata)
     */
    public static <T> CommonPage<T> restPage(List<T> list, long total, int pageNum, int pageSize) {
        CommonPage<T> result = new CommonPage<T>();
        result.setList(list);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalPage((int) Math.ceil((double) total / pageSize));
        return result;
    }

    /**
     * Convenience: wrap a plain list as a single-page result.
     * Useful when pagination was already applied before the call.
     */
    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        result.setList(list);
        result.setTotal((long) list.size());
        result.setPageNum(1);
        result.setPageSize(list.size());
        result.setTotalPage(1);
        return result;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
