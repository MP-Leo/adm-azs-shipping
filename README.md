# Spring Boot Shipping Management API

This project is a Spring Boot application that provides a RESTful API for managing shipping entities with dynamic attributes. The application is dockerized, allowing for easy setup and execution in a Docker environment, and it uses MongoDB for data persistence.

## Prerequisites

Before starting, ensure you have Docker and Docker Compose installed on your system:

- [Install Docker](https://docs.docker.com/get-docker/)
- [Install Docker Compose](https://docs.docker.com/compose/install/)

## Quick Start

Follow these steps to get the application up and running:

1. **Clone the Repository**

    ```bash
    git clone https://github.com/MP-Leo/adm-azs-shipping.git
    cd adm-azs-shipping
    ```

2. **Start the Application**

    Use Docker Compose to start the application and its dependencies:

    ```bash
    docker compose up -d
    ```

    This command builds the Docker images and starts the containers in detached mode.

## API Endpoints

The application supports the following endpoints for managing Shippings:

### Create a Shipping

- **POST** `/api/shipping`
  
  **Body:**

    ```json
    {
      "attributes": {
        "weight": "20",
        "size": "medium"
      }
    }
    ```

### Get All Shipping

- **GET** `/api/shipping`

### Get Shipping By Any Attribute Value

- **GET** `/api/shipping/{attributeValue}`

### Update Shipping Attributes

- **PUT** `/api/shipping/{id}`

  **Body:**

    ```json
    {
      "attributes": {
        "weight": "35"
      }
    }
    ```

### Delete a Shipping

- **DELETE** `/dogs/{id}`
