package reflection;

import java.util.Date;
import java.util.Objects;

public class Question {

    private long questionId;

    private String writer;

    private String title;

    private String contents;

    private Date createdDate;

    private int countOfComment;

    public Question(String writer, String title, String contents) {
        this(0, writer, title, contents, new Date(), 0);
    }

    public Question(long questionId, String writer, String title, String contents, Date createdDate,
                    int countOfComment) {
        this.questionId = questionId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.countOfComment = countOfComment;
    }

    public long getQuestionId() {
        return questionId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public long getTimeFromCreateDate() {
        return this.createdDate.getTime();
    }

    public int getCountOfComment() {
        return countOfComment;
    }

    public void update(Question newQuestion) {
        this.title = newQuestion.title;
        this.contents = newQuestion.contents;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdDate=" + createdDate +
                ", countOfComment=" + countOfComment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return questionId == question.questionId && countOfComment == question.countOfComment && Objects.equals(writer, question.writer) && Objects.equals(title, question.title) && Objects.equals(contents, question.contents) && Objects.equals(createdDate, question.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, writer, title, contents, createdDate, countOfComment);
    }
}
