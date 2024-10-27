package domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post {
    private int id;
    private String title;
    private String body;
    @JsonProperty("userId")
    private int userId;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters and Setterscurl --location 'https://jsonplaceholder.typicode.com/posts' \
--header 'Content-Type: application/json' \
--data '{
    "title": "foo",
    "body": "bar",
    "userId": 1
}'
{
    "title": "foo",
    "body": "bar",
    "userId": 1,
    "id": 101
}




curl --location --request PUT 'https://jsonplaceholder.typicode.com/posts/1' \
--header 'Content-Type: application/json' \
--data '{
    "id": 1,
    "title": "foo",
    "body": "bar",
    "userId": 1
  }'
  
  
  {
    "id": 1,
    "title": "foo",
    "body": "bar",
    "userId": 1
}
  
 curl --location --request PATCH 'https://jsonplaceholder.typicode.com/posts/1' \
--header 'Content-Type: application/json' \
--data '{
    "title": "foo"
}'


{
    "userId": 1,
    "id": 1,
    "title": "foo",
    "body": "quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto"
}

curl --location --request DELETE 'https://jsonplaceholder.typicode.com/posts/1'


{}
    
}
