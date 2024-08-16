# BevMate

**BevMate** is a distributed Android application designed to automate the operations of a business enterprise that sells drinks of various brands. The app enables customers to place orders from different branches, while administrators can manage inventory, sales, and customer information. This project demonstrates the integration of Firebase Firestore, user authentication, and a well-structured user interface to deliver a seamless experience for both customers and administrators.

## Project Overview

The **BevMate** app is built to streamline the process of managing a beverage business with multiple branches. The application consists of two main components: the Customer Side and the Admin Side.

### **Customer Side**
- **Home Page:** Displays available drinks with images, descriptions, and prices.
- **Order Management:** Allows customers to select products, add them to the cart, and proceed to checkout.
- **Profile Management:** Customers can view and update their profile details.
- **Branch Selection:** Customers select the branch they want to order from, ensuring inventory is updated accordingly.
- **Payment Process:** Simulates a payment process where customers can finalize their orders.

### **Admin Side**
- **Dashboard:** Provides a summary of key metrics, including total sales, inventory levels, and customer orders.
- **Order Management:** Admins can view, manage, and update customer orders, ensuring smooth processing.
- **Inventory Management:** Allows admins to monitor and update product quantities and manage low stock alerts.
- **User Management:** Admins can view and manage customer profiles, ensuring data integrity and customer satisfaction.
- **Reports:** Generates detailed reports on sales, customers, and branch performance, helping admins make informed decisions.

## Technical Implementation

**BevMate** leverages several key technologies and features to deliver a robust and scalable application:

- **Firebase Firestore:** Used for real-time database management, storing user information, product details, and orders.
- **User Authentication:** Firebase Authentication is implemented to securely manage user login and registration processes.
- **RecyclerView:** Employed for efficient display of product listings and customer orders.
- **Fragment Navigation:** Utilized to manage transitions between different screens, such as Home, Orders, and Profile.
- **Data Binding:** Ensures a responsive and dynamic user interface by linking UI components directly to data sources.

## Brand Identity

**BevMate** maintains a consistent brand identity across the application:
- **Brand Colors:** 
  - Primary: #004D40 (Green)
  - Accent: #FFD600 (Yellow)
  - Additional: Black, White, Gray
- **Fonts:** Poppins Bold and Poppins Regular are used throughout the app for a modern and clean look.
- **Minimalist Design:** The UI is designed to be clean and user-friendly, with a focus on usability and aesthetics.

## Key Features

- **Dynamic Product Filtering:** Customers can filter products by categories such as Soda, Juice, Water, and Energy Drinks.
- **Cart and Checkout:** Users can add items to their cart, view the total cost, and simulate payment.
- **Admin Notifications:** Admins receive alerts for low stock levels and can manage notifications sent to users.
- **Order History:** Both customers and admins can view past orders, ensuring transparency and tracking.

## How to Run the Project

1. **Clone the Repository:** Download or clone the repository to your local machine.
2. **Set Up Firebase:** Configure Firebase Firestore and Authentication in your Firebase Console, and replace the `google-services.json` file with your own.
3. **Open in Android Studio:** Open the project in Android Studio, and sync with Gradle.
4. **Build and Run:** Build the project and run it on an Android device or emulator.

## Future Enhancements

- **Payment Gateway Integration:** Integrate with real payment gateways like M-Pesa or PayPal.
- **Push Notifications:** Implement push notifications to alert customers of new promotions or order updates.
- **Analytics Dashboard:** Expand the Admin Dashboard to include more detailed analytics and insights.
- **Multi-language Support:** Add support for multiple languages to cater to a broader audience.


