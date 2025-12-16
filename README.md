# Culinary Archive

A Spring Boot REST API for managing culinary knowledge including recipes, cooking techniques, useful links, and inspirational quotes from renowned chefs.

## Prerequisites

- **JDK 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)

## Getting Started

### Running the Application

```bash
# Using Maven wrapper (recommended - Windows)
mvnw.cmd spring-boot:run

# Using Maven wrapper (Linux/Mac)
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### Running Tests

```bash
# Run all tests (Windows)
mvnw.cmd test

# Run all tests (Linux/Mac)
./mvnw test

# Run with verbose output
mvn test -Dtest=*Test
```

### Loading Initial Sample Data

The application uses an **H2 in-memory database** that initializes on startup. To load sample data:

1. **Prepare your JSON file**: Use the provided `recipe_collection_data.json` format with your culinary data
2. **API Import**: Use the POST endpoint to bulk import entries (see cURL examples below)
3. **Manual via H2 Console**: Access the H2 console and execute INSERT statements directly

#### H2 Database Console

Access the H2 console at: **http://localhost:8080/h2-console**

**Connection Settings:**
- **JDBC URL:** `jdbc:h2:mem:culinary-database`
- **Username:** `admin`
- **Password:** `admin`

## Authentication & Users

The API uses **HTTP Basic Authentication** with in-memory users:

| Username | Password   | Roles           | Permissions                          |
|----------|------------|-----------------|--------------------------------------|
| `chef`   | `chef123`  | USER, CREATOR   | Create new knowledge entries         |
| `admin`  | `admin123` | USER, ADMIN     | Publish/unPublish and delete entries |

> **Note:** GET endpoints (`/api/knowledge` and `/api/knowledge/search`) are publicly accessible without authentication.

## API Endpoints

| Method | Endpoint                    | Auth Required | Description                              |
|--------|-----------------------------|---------------|------------------------------------------|
| GET    | `/api/knowledge`            | No            | List all published knowledge (paginated) |
| GET    | `/api/knowledge/search`     | No            | Search knowledge by title                |
| POST   | `/api/knowledge`            | CREATOR       | Create new knowledge entry               |
| PATCH  | `/api/knowledge/{id}`       | ADMIN         | Update publish status                    |
| DELETE | `/api/knowledge/{id}`       | ADMIN         | Delete a knowledge entry                 |

## Sample cURL Commands

### 1. Get All Published Recipes (Public - No Authentication)

```bash
curl -X GET "http://localhost:8080/api/knowledge?type=RECIPE&page=0&size=10"
```

Get with custom sorting:
```bash
curl -X GET "http://localhost:8080/api/knowledge?type=RECIPE&page=0&size=5&sortDirection=asc"
```

### 2. Search Knowledge by Title (Public - No Authentication)

```bash
curl -X GET "http://localhost:8080/api/knowledge/search?title=Carbonara"
```

### 3. Create a Recipe (Requires CREATOR Role - Authenticated)

Based on the sample data structure, create a complete recipe:

```bash
curl -X POST "http://localhost:8080/api/knowledge" \
  -u chef:chef123 \
  -H "Content-Type: application/json" \
  -d '{
    "type": "RECIPE",
    "title": "Classic Spaghetti Carbonara",
    "description": "Authentic Roman pasta with eggs, cheese, and guanciale",
    "cuisine": "Italian",
    "servings": 4,
    "prepTimeMinutes": 25,
    "difficultyLevel": "INTERMEDIATE",
    "ingredients": [
      {
        "name": "Spaghetti",
        "quantity": 400,
        "unit": "grams"
      },
      {
        "name": "Guanciale",
        "quantity": 150,
        "unit": "grams"
      },
      {
        "name": "Egg yolks",
        "quantity": 4,
        "unit": "pieces"
      },
      {
        "name": "Pecorino Romano cheese",
        "quantity": 100,
        "unit": "grams"
      },
      {
        "name": "Black pepper",
        "quantity": 1,
        "unit": "teaspoon"
      }
    ],
    "cookingSteps": [
      {
        "stepNumber": 1,
        "instruction": "Bring large pot of salted water to boil for pasta."
      },
      {
        "stepNumber": 2,
        "instruction": "Cut guanciale into small strips and cook in dry pan until crispy, about 8 minutes."
      },
      {
        "stepNumber": 3,
        "instruction": "In bowl, whisk egg yolks with grated Pecorino and black pepper."
      },
      {
        "stepNumber": 4,
        "instruction": "Cook spaghetti until al dente, reserve 1 cup pasta water."
      },
      {
        "stepNumber": 5,
        "instruction": "Add drained pasta to pan with guanciale, remove from heat."
      },
      {
        "stepNumber": 6,
        "instruction": "Add egg mixture, toss quickly, adding pasta water to create creamy sauce. Serve immediately."
      }
    ]
  }'
```

### 4. Create a Cooking Technique (TEXT Knowledge)

```bash
curl -X POST "http://localhost:8080/api/knowledge" \
  -u chef:chef123 \
  -H "Content-Type: application/json" \
  -d '{
    "type": "TEXT",
    "title": "Perfect Knife Skills for Dicing Onions",
    "description": "Master the technique of dicing onions without tears",
    "content": "To dice an onion perfectly: Cut in half from root to tip, peel outer layers, make horizontal cuts toward the root (do not cut through), make vertical cuts following the onion natural lines, then slice crosswise. The root end holds everything together.",
    "category": "TECHNIQUE"
  }'
```

### 5. Create a Useful Link (LINK Knowledge)

```bash
curl -X POST "http://localhost:8080/api/knowledge" \
  -u chef:chef123 \
  -H "Content-Type: application/json" \
  -d '{
    "type": "LINK",
    "title": "Serious Eats Chicken Tikka Masala Guide",
    "description": "Comprehensive article on making restaurant-quality tikka masala",
    "url": "https://seriouseats.com/chicken-tikka-masala-recipe",
    "format": "ARTICLE"
  }'
```

### 6. Create an Inspirational Quote (QUOTE Knowledge)

```bash
curl -X POST "http://localhost:8080/api/knowledge" \
  -u chef:chef123 \
  -H "Content-Type: application/json" \
  -d '{
    "type": "QUOTE",
    "title": "Julia Child on Cooking Fearlessly",
    "description": "Encouragement to embrace mistakes in the kitchen",
    "quoteText": "The only time to eat diet food is while you are waiting for the steak to cook.",
    "speaker": "Julia Child",
    "source": "My Life in France"
  }'
```

### 7. Update Publish Status (Requires ADMIN Role - Authenticated)

Publish a previously created entry:

```bash
curl -X PATCH "http://localhost:8080/api/knowledge/{id}?published=true" \
  -u admin:admin123
```

Unpublish an entry:

```bash
curl -X PATCH "http://localhost:8080/api/knowledge/{id}?published=false" \
  -u admin:admin123
```

### 8. Delete a Knowledge Entry (Requires ADMIN Role - Authenticated)

```bash
curl -X DELETE "http://localhost:8080/api/knowledge/{id}" \
  -u admin:admin123
```

## Project Structure

```
src/main/java/com/cookbook/culinary_archive/
├── config/
│   ├── AppProperties.java          # Pagination configuration
│   └── SecurityConfig.java         # Security & authentication setup
├── controller/
│   └── KnowledgeController.java    # REST API endpoints
├── dto/
│   ├── request/                    # Polymorphic request DTOs
│   │   ├── KnowledgeRequestDTO.java
│   │   ├── RecipeRequestDTO.java
│   │   ├── TextInfoRequestDTO.java
│   │   ├── LinkInfoRequestDTO.java
│   │   └── QuoteInfoRequestDTO.java
│   └── response/                   # Response DTOs
├── exception/
│   └── GlobalExceptionHandler.java # Centralized error handling
├── mapper/
│   └── KnowledgeMapper.java        # Entity <-> DTO mapping
├── model/
│   ├── Knowledge.java              # Abstract base entity
│   ├── Recipe.java                 # Recipe entity with ingredients & steps
│   ├── TextInfo.java               # Cooking tips and techniques
│   ├── LinkInfo.java               # External resource links
│   ├── QuoteInfo.java              # Chef quotes and wisdom
│   ├── Ingredient.java
│   ├── CookingStep.java
│   └── enums/
│       ├── KnowledgeType.java
│       └── DifficultyLevel.java
├── repository/
│   └── KnowledgeRepository.java    # JPA repository
└── service/
    ├── KnowledgeService.java
    └── Impl/
        └── KnowledgeServiceImpl.java
```

## Engineering Notes

### Class Diagram

![Class Diagram](path/to/your/class-diagram.png)


### Key Design Choices

1. **JPA Inheritance Strategy (JOINED)**: Chose JOINED table inheritance for the Knowledge hierarchy to maintain normalized data structure while supporting polymorphic queries. Each knowledge type (Recipe, TextInfo, LinkInfo, QuoteInfo) has its own table joined to the base Knowledge table, enabling efficient type-specific queries without data duplication.

2. **Polymorphic JSON Handling**: Implemented Jackson's `@JsonTypeInfo` and `@JsonSubTypes` for type-safe polymorphic deserialization based on a `type` discriminator field. This allows a single POST endpoint to handle all knowledge types with compile-time type safety and automatic routing to the correct DTO subclass.

3. **DTO Pattern with Separation of Concerns**: Separated request and response DTOs from JPA entities to decouple the API contract from the persistence model. This enables independent evolution of the database schema and API interface, prevents accidental exposure of internal entity details, and allows precise validation rules per knowledge type.

4. **Role-Based Access Control (RBAC)**: Implemented Spring Security with method-level authorization separating content creation (CREATOR role) from moderation (ADMIN role). Public GET endpoints allow unauthenticated access while write operations require authentication, supporting a content moderation workflow.

5. **Configuration Externalization**: Pagination defaults (page size, sort direction) are externalized to `application.yaml` and bound via `@ConfigurationProperties`, allowing environment-specific tuning without code changes and promoting twelve-factor app principles.

6. **Embedded Relationships**: Modeled Recipe ingredients and cooking steps as `@ElementCollection` with `@Embeddable` classes rather than separate entities, since they have no independent lifecycle and are always queried with their parent recipe, simplifying the model and improving query performance.

7. **Centralized Exception Handling**: Used `@RestControllerAdvice` with `@ExceptionHandler` methods to provide consistent error responses across all endpoints with appropriate HTTP status codes, reducing duplication and ensuring uniform API behavior.

### What I Would Improve With 2 More Hours

1. **Data Seeding Mechanism**: Create a `CommandLineRunner` or SQL init script to automatically load the provided JSON sample data on application startup, eliminating manual import steps and providing immediate demo capability.

2. **Enhanced Search Functionality**: Implement full-text search across multiple fields (title, description, content, ingredients) using JPA Specifications or native database features, with support for partial matches and relevance ranking.

3. **API Documentation with OpenAPI/Swagger**: Integrate SpringDoc OpenAPI to auto-generate interactive API documentation, making it easier for developers to explore endpoints, understand request/response schemas, and test the API directly from a browser.

4. **Comprehensive Integration Tests**: Expand test coverage with `@SpringBootTest` integration tests that verify the full request/response cycle including security, JSON serialization, and database persistence for all knowledge types.

5. **Reference Knowledge Linking**: Fully implement and test the `referencedKnowledge` relationship in Recipe entities, allowing recipes to reference related techniques, quotes, or links, creating a knowledge graph within the application.

6. **Input Validation Enhancement**: Add more sophisticated validation rules (e.g., URL format validation for LinkInfo, reasonable bounds on serving sizes and prep times, step number sequencing) and custom validators for business logic constraints.

7. **Pagination Response Metadata**: Enhance paginated responses to include metadata like total pages, total elements, and HATEOAS links (next/previous page), improving client-side pagination implementation.


