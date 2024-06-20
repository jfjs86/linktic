# Prerequisites

- Docker installed.
- Git client installed.

# Application Deployment

In the command console, run: `git clone https://github.com/jfjs86/linktic.git`

## a. Network Creation for Containers

1. Run in the operating system console: `docker network create ecommerce_net`

## b. Database Creation and Execution

1. Navigate to the path `4.Backend/Data_Base`
2. Run in the operating system console: `docker build -t postgres-custom .`
3. Run in the operating system console: `docker run --name postgres-custom --network ecommerce_net -d -p 5432:5432 postgres-custom`

## c. Product Management Service Creation and Execution

1. Navigate to the path `4.Backend/ProductService`
2. Run in the operating system console: `docker build -t product-service-dev -f Dockerfile-dev .`
3. Run in the operating system console: `docker run -d --name product-service-dev --network ecommerce_net -p 8081:8081 product-service-dev`

## d. Order Management Service Creation and Execution

1. Navigate to the path `4.Backend/OrderManagementService`
2. Run in the operating system console: `docker build -t order-service-dev -f Dockerfile-dev .`
3. Run in the operating system console: `docker run -d --name order-service-dev --network ecommerce_net -p 8082:8082 order-service-dev`

## e. Frontend Application Creation and Execution

1. Navigate to the path `5.Frontend/ecommerce-front`
2. Run in the operating system console: `docker build -t ecommerce-front -f Dockerfile-dev .`
3. Run in the operating system console: `docker run -d -p 8080:80 ecommerce-front`
4. In the web browser, go to the following URL: `http://localhost:8080`

## Note: You can see the project in a production environment at the following url: https://d3167jmngty0pr.cloudfront.net/