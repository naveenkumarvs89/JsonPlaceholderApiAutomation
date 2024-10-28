# JsonPlaceholderApiAutomation
## Framework Overview

This framework is a test automation framework for a web application, utilizing Java as the programming language. It is designed using a modular and structured approach, with separate classes and files for different components, such as test listeners, API clients, and data providers.

### SOLID Principles

* Single Responsibility Principle (SRP): Each class in the framework has a single responsibility, ensuring code maintainability and extensibility. For example, the CustomTestListener class is responsible for listening to test events, while the ApiClient class handles API requests. This separation of concerns makes the code more understandable and easier to modify.
* Open/Closed Principle (OCP): The framework is open for extension but closed for modification. The use of interfaces and abstract classes, such as ITestListener and ExtentReportManager, allows for easy extension of the framework without modifying the existing code. For instance, you can create a new test listener by implementing the ITestListener interface without changing the existing code.
* Liskov Substitution Principle (LSP): The subclasses, such as CustomTestListener, can be used in place of their parent classes, like ITestListener, without affecting the correctness of the program. This is evident in the testng.xml file, where the CustomTestListener class is used as a listener.
* Interface Segregation Principle (ISP): The interfaces, such as ITestListener and ExtentReportManager, are client-specific and do not force the implementing classes to implement unnecessary methods. This is evident in the CustomTestListener class, which only implements the necessary methods from the ITestListener interface.
* Dependency Inversion Principle (DIP): The high-level modules, such as the ApiClient class, do not depend on low-level modules, like the ExtentReportManager class. Instead, both modules depend on abstractions, such as interfaces, which makes the code more decoupled and easier to maintain.

### Additional Design Patterns and Principles

The framework also utilizes other design patterns and principles, such as:

* Factory Pattern: The ExtentReportManager class uses a factory method to create ExtentTest objects.
* Data Provider Pattern: The JsonDataProvider class provides data for tests using a data provider pattern.
* Configurable: The framework is designed to be highly configurable, with configuration files, such as config.properties, that allow for easy changes to the framework's behavior.

Overall, this framework demonstrates a solid understanding of the SOLID principles and other design patterns and principles, making it maintainable, scalable, and easy to understand.






