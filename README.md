## 💈 TrimTime – Online Barber Shop Management System  
**Tech Stack:** Java, Spring Boot  
**Cloud Services:** [AWS DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/), [AWS Cognito](https://docs.aws.amazon.com/cognito/latest/developerguide/), [AWS SNS](https://docs.aws.amazon.com/sns/latest/dg/)

### 🔧 Key Features & Contributions

- 🔐 Implemented **OTP-based authentication** using [AWS SNS](https://docs.aws.amazon.com/sns/latest/dg/), and **JWT-based authentication/authorization** using [AWS Cognito](https://docs.aws.amazon.com/cognito/latest/developerguide/)
- 🧾 Designed **role-based signup** flow with password decryption logic
- 🔄 Developed secure, RESTful APIs for managing Admin, Barber, and Customer operations

### 👥 Role-Based Functionalities

- **Admin**  
  - Create, update, and delete profiles  
  - View all appointments and barbers

- **Barber**  
  - Manage personal profile  
  - View appointments  
  - Access customer reviews

- **Customer**  
  - Manage personal profile  
  - Search barbers by location  
  - Book, reschedule, or cancel appointments

### 🗄️ [AWS DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/)
Used as a scalable NoSQL database to store user profiles, appointments, and reviews

---

Feel free to reach out if you’d like to see how I implemented secure user flows with Cognito and OTP-based verification using SNS!
