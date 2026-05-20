# POC_SSS

setup

# Unzip, then:

# Terminal 1 — Backend
cd shopwave/backend
mvn package -DskipTests
java -jar target/shopwave-backend-1.0.0.jar
# → http://localhost:8080

# Terminal 2 — Frontend
cd shopwave/frontend
npm install
npm start
# → http://localhost:3000
