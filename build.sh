mvn clean package
docker build -t bank-transaction-system .
docker run -p 8080:8080 bank-transaction-system