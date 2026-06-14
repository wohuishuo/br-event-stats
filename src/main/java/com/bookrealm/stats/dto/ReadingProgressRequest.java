package com.bookrealm.stats.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReadingProgressRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long bookId;
    @NotNull
    private Long chapterId;
    @Min(0)
    private int paragraphIndex;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public Long getChapterId() { return chapterId; }
    public void setChapterId(Long chapterId) { this.chapterId = chapterId; }
    public int getParagraphIndex() { return paragraphIndex; }
    public void setParagraphIndex(int paragraphIndex) { this.paragraphIndex = paragraphIndex; }
}
