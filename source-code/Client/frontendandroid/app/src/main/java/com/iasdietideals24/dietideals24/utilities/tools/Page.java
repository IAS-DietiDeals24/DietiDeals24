package com.iasdietideals24.dietideals24.utilities.tools;

import java.util.List;

public class Page<T> {

    private List<T> content = List.of();
    private Pageable pageable = new Pageable();
    private int totalElements = 0;
    private int totalPages = 0;
    private boolean last = true;
    private int numberOfElements = 0;
    private boolean first = true;
    private int size = 20;
    private int number = 0;
    private Sort sort = new Sort();
    private boolean empty = true;

    public Page() {
    }

    public Page(List<T> content, Pageable pageable, int totalElements, int totalPages, boolean last,
                int numberOfElements, boolean first, int size, int number, Sort sort, boolean empty) {
        this.content = content;
        this.pageable = pageable;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.numberOfElements = numberOfElements;
        this.first = first;
        this.size = size;
        this.number = number;
        this.sort = sort;
        this.empty = empty;
    }

    public List<T> getContent() {
        return content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public Sort getSort() {
        return sort;
    }

    public boolean isEmpty() {
        return empty;
    }
}