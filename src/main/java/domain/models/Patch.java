package domain.models;

public class Patch {
    private int id;
    private String title;
    private String body;
    private int userId;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Post{");
        boolean isFirst = true;
        if (id != 0) {
            appendField(sb, "id", id, isFirst);
            isFirst = false;
        }
        if (title != null) {
            appendField(sb, "title", title, isFirst);
            isFirst = false;
        }
        if (body != null) {
            appendField(sb, "body", body, isFirst);
            isFirst = false;
        }
        if (userId != 0) {
            appendField(sb, "userId", userId, isFirst);
            isFirst = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private void appendField(StringBuilder sb, String fieldName, Object fieldValue, boolean isFirst) {
        if (!isFirst) {
            sb.append(", ");
        }
        sb.append(fieldName).append("=").append(fieldValue);
    }

}