# Livora Backend

This is the backend of **Livora**, a Spring Boot-based RESTful API for managing an e-commerce furniture platform. It handles product management, user authentication, orders, and more. You can find the frontend [here](https://github.com/lingchen2333/Livora-frontend).

## Tech Stack

- Java 17+
- Spring Boot
- Spring AI (including image-based product search)
- Spring Security (JWT)
- Spring Data JPA (MySQL)
- Maven
- Docker (optional for deployment)
- ChromaDB (optional for AI features)
- Stripe payment processing
- Render (deployment)

## 🌐 Live Demo


🔗 [https://shop-livora.netlify.app](https://shop-livora.netlify.app)

> ⚠️ **Note**: The backend is hosted on [Render](https://livora-backend-latest.onrender.com/) using the free tier, which means it may take **5 min to spin up** after a period of inactivity. You may notice a short delay on the first request — this is expected behavior on free-tier Render services.

## API Endpoints (Sample)

### Auth

- `POST /api/v1/auth/register` – Register new users
- `POST /api/v1/auth/login` – Authenticate and receive a JWT

### Products

- `GET /api/v1/products` – List all products
- `POST /api/v1/products` – Add a new product
- `GET /api/v1/products/{id}` – Get product by ID
- `DELETE /api/v1/products/{id}` – Delete a product
- `POST /api/v1/products/search-by-image` – Search products using image similarity

### Orders

- `POST /api/orders` – Place a new order
- `GET /api/orders/user` – Get orders for the logged-in user

## 🛠️ Development Notes

- Ensure MySQL is running and the `livora_db` schema is created.
- Use tools like Postman or Insomnia to test endpoints.
- If using Chroma DB, download Docker Desktop and run docker-compose.yml to set it up.

## 📂 Project Structure

```
src/
 └── main/
     ├── java/
     │    └── com/lingchen/livora/
     │         ├── controller/
     │         ├── model/
     │         ├── repository/
     │         ├── service/
     │         └── LivoraApplication.java
     └── resources/
         └── application.properties
```

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
