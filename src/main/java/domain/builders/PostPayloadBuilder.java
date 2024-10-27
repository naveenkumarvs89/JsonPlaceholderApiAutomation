package domain.builders;

import domain.models.Post;

public class PostPayloadBuilder {
    private String title;
    private String body;
    private int userId;

    public PostPayloadBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostPayloadBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public PostPayloadBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Post build() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setBody(this.body);
        post.setUserId(this.userId);
        return post;
    }
}
