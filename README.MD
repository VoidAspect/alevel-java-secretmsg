# Secret Message Service

Provides RESTful API for creating and retrieving self-destructing messages
(similar to [privnote](https://privnote.com/))

## API

By default, application port is 8080

### Endpoints

**POST /**

create self-destructing message.

Expected request body example:
```json
{
  "text": "shared secret"
}
```

Response body example:

```json
{
  "id": "b7595705-b9b5-44bc-8210-ae7f34a5f568"
}
```

Response status should be 201 CREATED

**GET /{uuid}**

Retrieve and remove self-destructing message.

Expected response body example:
```json
{
  "text": "shared secret"
}
```

**DELETE /{uuid}**

Remove (drop) self-destructing message. Response body should be empty
```
